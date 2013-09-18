package com.kaaazing.demo.toggle;

import java.net.URI;

import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;

import org.apache.activemq.ActiveMQConnectionFactory;

import com.kaaazing.demo.util.AbstractJmsMessenger;
import com.kaaazing.demo.util.DefaultExceptionListener;

public class ToggleJmsTcpListener extends AbstractJmsMessenger implements
        ToggleListener {

    private final ActiveMQConnectionFactory connectionFactory;
    private final Connection connection;
    private final Session session;
    private final Topic topic;
    private final MessageProducer producer;
    private final Toggle toggle;
    private final MessageConsumer consumer;
    private boolean running = true;

    public ToggleJmsTcpListener() {

        try {
            toggle = new Toggle();
            connectionFactory = new ActiveMQConnectionFactory(URI.create("tcp://"
                    + BROKER_HOSTNAME + ":61616"));
            connection = connectionFactory.createConnection();
            connection.setExceptionListener(new DefaultExceptionListener());
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            topic = session.createTopic(LIGHT_TOPIC);
            producer = session.createProducer(topic);

            // clean shutdown listener
            consumer = session.createConsumer(topic);
            consumer.setMessageListener(new MessageListener() {
                @Override
                public void onMessage(Message message) {
                    try {
                        TextMessage textMessage = (TextMessage) message;
                        String messageData = textMessage.getText();
                        if (messageData.equals(SHUTDOWN)) {
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
            System.out.println("Toggle initialized");
            toggle.registerListener(this);

        } catch (JMSException e) {
            shutdown();
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public void on() {
        try {
            System.out.println("Sending on message");
            producer.send(session.createTextMessage(ON_MESSAGE));
        } catch (JMSException e) {
            e.printStackTrace();
            shutdown();
        }
    }

    @Override
    public void off() {
        try {
            System.out.println("Sending off message");
            producer.send(session.createTextMessage(OFF_MESSAGE));
        } catch (JMSException e) {
            e.printStackTrace();
            shutdown();
        }
    }

    private void shutdown() {
        running = false;
        if (toggle != null) {
            toggle.shutdown();
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

    public static void main(String[] args) throws JMSException,
            InterruptedException {
        ToggleJmsTcpListener toggleJmsTcpListener = new ToggleJmsTcpListener();
        while (toggleJmsTcpListener.isRunning()) {
            Thread.sleep(1000);
        }
        System.out.println("Exiting Application");
    }

    public boolean isRunning() {
        return running;
    }

}
