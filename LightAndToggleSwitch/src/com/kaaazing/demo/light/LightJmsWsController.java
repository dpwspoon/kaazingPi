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

import com.kaaazing.demo.util.AbstractJmsMessenger;
import com.kaaazing.demo.util.DefaultExceptionListener;
import com.kaazing.gateway.jms.client.stomp.StompConnectionFactory;

public class LightJmsWsController extends AbstractJmsMessenger {

	private final StompConnectionFactory connectionFactory;
	private final Connection connection;
	private final Session session;
	private final Topic topic;
	private final MessageConsumer consumer;
	private final Light light;
	private boolean running = true;

	public LightJmsWsController() {
		try {
			light = new Light();

			connectionFactory = new StompConnectionFactory(URI.create("ws://"
					+ GATEWAY_HOST + "/jms"));
			connection = connectionFactory.createConnection();
			connection.setExceptionListener(new DefaultExceptionListener());
			session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			topic = session.createTopic(LIGHT_TOPIC_WS);
			consumer = session.createConsumer(topic);
			consumer.setMessageListener(new MessageListener() {

				@Override
				public void onMessage(Message message) {
					try {
						TextMessage textMessage = (TextMessage) message;
						String messageData = textMessage.getText();
						System.out.println("websocket message received: " + messageData);
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
			e.printStackTrace();
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

	public static void main(String[] args) throws JMSException,
			InterruptedException {
		LightJmsWsController lightController = new LightJmsWsController();
		while (lightController.isRunning()) {
			Thread.sleep(1000);
		}
		System.out.println("Exiting Application");

	}

	public boolean isRunning() {
		return running;
	}

}
