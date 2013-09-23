package com.kaaazing.demo.light;

import java.net.URI;

import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;

import org.apache.activemq.ActiveMQConnectionFactory;

import com.kaaazing.demo.util.AbstractJmsMessenger;
import com.kaaazing.demo.util.DefaultExceptionListener;

public class LightJmsTcpController extends AbstractJmsMessenger {

    private final ActiveMQConnectionFactory connectionFactory;
    private final Connection connection;
    private final Session session;
    private final Topic topic;
    private final MessageConsumer consumer;
    private final Light light;
    private boolean running = true;

    public LightJmsTcpController() {
        try {
            light = new Light();

            connectionFactory = new ActiveMQConnectionFactory(URI.create("tcp://" 
                    + BROKER_HOSTNAME + ":61616"));
            connection = connectionFactory.createConnection();
            connection.setExceptionListener(new DefaultExceptionListener());
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            topic = session.createTopic(LIGHT_TOPIC);
            consumer = session.createConsumer(topic);
            consumer.setMessageListener(new MessageListener() {

                @Override
                public void onMessage(Message message) {
                    try {
                        TextMessage textMessage = (TextMessage) message;
                        String messageData = textMessage.getText();
                        System.out.println("message received: " + messageData);
                        if (messageData.equals(ON_MESSAGE)) {
                            light.on();
                        } else if (messageData.equals(OFF_MESSAGE)) {
                            light.off();
                        } else if (messageData.equals(SHUTDOWN)) {
                            System.out.println("shutting down");
                            shutdown();
                        }
                    } catch (JMSException e) {
                        e.printStackTrace();
                        shutdown();
                    }
                }
            });

            connection.start();
            System.out.println("Light initialized");
        } catch (JMSException e) {
            shutdown();
            throw new RuntimeException(e.getMessage());
        }
    }

    private void shutdown() {
        running = false;
        if (light != null) {
            light.shutdown();
        }
        if (connection != null) {
            try {
                System.out.println("Cleaning up resources");
                connection.close();
            } catch (JMSException e) {
                throw new RuntimeException(e.getMessage());
            }
        }
    }

    public static void main(String[] args) throws JMSException, InterruptedException {
        LightJmsTcpController lightController = new LightJmsTcpController();
        while (lightController.isRunning()) {
            Thread.sleep(1000);
        }
        System.out.println("Exiting Application");

    }

    public boolean isRunning() {
        return running;
    }

}
