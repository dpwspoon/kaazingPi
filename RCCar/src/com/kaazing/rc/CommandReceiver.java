package com.kaazing.rc;

import java.net.URI;

import javax.jms.Connection;
import javax.jms.ExceptionListener;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Session;
import javax.jms.TextMessage;

import com.kaazing.gateway.jms.client.stomp.StompConnectionFactory;
import com.kaazing.rc.Car.LightCommand;
import com.kaazing.rc.Car.SteeringCommand;
import com.kaazing.rc.Car.ThrustCommand;

public class CommandReceiver {

	private final Car car = new Car();
	private final Connection connection;
	private String currentUserKey = "no-one";
	private final string urlToConnectTo = "URL TO CONNECT TO GOES HERE";

	public CommandReceiver() throws Exception {
		// Get JMS Stomp Factory
		StompConnectionFactory factory = new StompConnectionFactory(
				new URI(urlToConnectTo));
		
		// Create a connection from factory
		connection = factory.createConnection();
		connection.setExceptionListener(new ExceptionListener() {
			@Override
			public void onException(JMSException arg0) {
				arg0.printStackTrace();
			}
		});
	}
	
	public void startReceivingCommands() throws Exception {
		//start the connection
		connection.start();
		
		// Create Session from Connection
		Session session = connection.createSession(false,
				Session.AUTO_ACKNOWLEDGE);

		// Subscribe to the admin topic and register 
		// AdminCommandListener as message listener 
		MessageConsumer adminSubscriber = session.createConsumer(session
				.createTopic("/topic/rcadmin"));
		adminSubscriber.setMessageListener(new AdminCommandListener());
		
		// Subscribe to the rc topic and register 
		// ControlCommandListner as message listener 
		MessageConsumer controlSubscriber = session.createConsumer(session
				.createTopic("/topic/rc"));
		controlSubscriber.setMessageListener(new ControlCommandListener());
	}
	
	private class AdminCommandListener implements MessageListener{
		// On a message from the rcadmin topic, AdminCommandListener processes the
		// message into known commands to control access to the car 
		@Override
		public void onMessage(Message arg0) {
			try {
				TextMessage textMessage = (TextMessage) arg0;
				String textFromMessage = textMessage.getText();
				System.out.println(textFromMessage);
				String[] parsedText = textFromMessage.split(";");
				if (parsedText.length != 2) {
					System.out.println("Invalid Command");
					return;
				}
				String key = parsedText[0];
				String value = parsedText[1];
				if (key.equalsIgnoreCase("key")) {
					currentUserKey = value;
				}
				car.steering(SteeringCommand.OFF);
				car.thrust(ThrustCommand.OFF);
			} catch (JMSException e) {
				e.printStackTrace();
			}
		}
	}
	
	private class ControlCommandListener implements MessageListener{
		// On a message from the rc topic, ControlCommandListener processes the message
		// into commands that control the car
		@Override
		public void onMessage(Message message) {
			try {
				String secretKey = message.getStringProperty("key");
				if (secretKey == null || !secretKey.equals(currentUserKey)) {
					System.out.println("Invalid User");
					return;
				}
				TextMessage textMessage = (TextMessage) message;
				String textFromMessage = textMessage.getText();
				System.out.println(textFromMessage);

				String[] commandSets = textFromMessage.split(":");
				for (int i = 0; i < commandSets.length; i++) {
					parseAndPassCommandToCar(commandSets[i].trim());
				}

			} catch (JMSException e) {
				e.printStackTrace();
			}
		}
		
		private void parseAndPassCommandToCar(String textFromMessage) {
			String[] parsedText = textFromMessage.split(";");
			if (parsedText.length != 2) {
				System.out.println("Invalid Steering Command" + textFromMessage);
				return;
			}
			String key = parsedText[0];
			String value = parsedText[1];
			if (key.equalsIgnoreCase("thrust")) {
				if (value.equalsIgnoreCase("forward")) {
					car.thrust(ThrustCommand.FORWARD);
				} else if (value.equalsIgnoreCase("backward")) {
					car.thrust(ThrustCommand.BACKWARD);
				} else if (value.equalsIgnoreCase("off")) {
					car.thrust(ThrustCommand.OFF);
				}
			} else if (key.equalsIgnoreCase("steering")) {
				if (value.equalsIgnoreCase("off")) {
					car.steering(SteeringCommand.OFF);
				} else if (value.equalsIgnoreCase("left")) {
					car.steering(SteeringCommand.LEFT);
				} else if (value.equalsIgnoreCase("right")) {
					car.steering(SteeringCommand.RIGHT);
				}
			} else if (key.equalsIgnoreCase("frontLight")) {
				if (value.equalsIgnoreCase("off")) {
					car.frontLight(LightCommand.OFF);
				} else if (value.equalsIgnoreCase("on")) {
					car.frontLight(LightCommand.ON);
				}
			} else if (key.equalsIgnoreCase("backLight")) {
				if (value.equalsIgnoreCase("off")) {
					car.backLight(LightCommand.OFF);
				} else if (value.equalsIgnoreCase("on")) {
					car.backLight(LightCommand.ON);
				}
			}
		}
	}	
}
