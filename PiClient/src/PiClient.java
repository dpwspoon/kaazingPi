import java.net.URI;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.jms.Connection;
import javax.jms.ExceptionListener;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;

import com.kaazing.gateway.jms.client.stomp.StompConnectionFactory;
import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;

public class PiClient {

    /**
     * @param args
     */
    public static void main(String[] args) throws Exception {
        
    	// GpioController is used to connect to the GIOP
        GpioController gpio = GpioFactory.getInstance();
        
        // turn it off by default
        final GpioPinDigitalOutput outputPin = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_01, "MyLED", PinState.LOW);
        
        // The flag is used to exit the application on exception in connection exception listener
        final AtomicBoolean exitApp = new AtomicBoolean(false);
        
        // Stores the captured status of the led connected through GPIO
        final AtomicBoolean ledStatus = new AtomicBoolean(false);
        
        // Connect to the remote server over websocket
        StompConnectionFactory connectionFactory = new StompConnectionFactory(new URI("ws://demo.kaazing.com:80/jms"));
        Connection connection = connectionFactory.createConnection();
        connection.setExceptionListener(new ExceptionListener() {
            
            @Override
            public void onException(JMSException exception) {
                exception.printStackTrace();
                exitApp.set(true);
            }
        });
        final Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        connection.start();
        
        // This topic is used to subscribe to the command (ON and OFF) to control the led
        final Topic commandTopic = session.createTopic("/topic/command");
        
        // This topic is used to publish the status of the led every 
        // five seconds to the remote server
        final Topic statusTopic = session.createTopic("/topic/status");
        
        // The message producer publish the status of the led
        final MessageProducer ledStatusPublisher = session.createProducer(statusTopic);
        
        // The consumer listens for the command to control the led
        MessageConsumer consumer = session.createConsumer(commandTopic);
        consumer.setMessageListener(new MessageListener() {
            
            @Override
            public void onMessage(Message message) {
                if (message instanceof TextMessage) {
                    try {
                    	
                    	// check the command
                        String command = ((TextMessage)message).getText();
                        System.out.println("Command Received: " + command);
                        
                        // if the command is 'on', turn on the led
                        if (command.equalsIgnoreCase("on")) {
                            ledStatus.set(true);
                        }
                        else if (command.equalsIgnoreCase("off")) {
                        	// if the command is 'off', turn off the led
                            ledStatus.set(false);
                        }
                        
                        // control the led as per the command
                        if (ledStatus.get()) {
                            outputPin.high();
                        }
                        else {
                            outputPin.low();
                        }
                    } catch (JMSException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        
        // Send the led status "on or off" in every 5 seconds
        ScheduledExecutorService exec = Executors.newSingleThreadScheduledExecutor();
        exec.scheduleAtFixedRate(new Runnable() {
          @Override
          public synchronized void run() {
              String command = "OFF";
              if (ledStatus.get()) {
                  command = "ON";
              }
              try {
                  System.out.println("Sending command staus: " + command);
                  ledStatusPublisher.send(session.createTextMessage(command));
              } catch (JMSException e) {
                  e.printStackTrace();
              }
          }
        }, 0, 5, TimeUnit.SECONDS);
        
        while (true){
            if (exitApp.get()) {
                // Game Over!!
                // just crap out of the application
                break;
            }
        }
        connection.close();
        
    }

}
