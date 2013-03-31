package com.kaazing.rc;

import java.util.Timer;
import java.util.TimerTask;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;

public class Car {
	final private GpioController gpio;
	final private GpioPinDigitalOutput thrustPin12;
	final private GpioPinDigitalOutput thrustPin13;
	final private GpioPinDigitalOutput steeringPin0;
	final private GpioPinDigitalOutput steeringPin2;
	final private GpioPinDigitalOutput frontLight;
	final private GpioPinDigitalOutput backLight;
	
	private Timer steeringTimer = null;
	private Timer thrustTimer = null;
	
	private static final int COMMAND_TIMEOUT = 300; // in milliseconds

	// On construction the Car inits the Pi4J GPIO Pins
	public Car() {
		try {
			System.out.println("Initting Car");

			gpio = GpioFactory.getInstance();

			thrustPin12 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_12,
					"ThrustPin0", PinState.LOW);
			thrustPin13 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_13,
					"ThrustPin2", PinState.LOW);
			steeringPin0 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_00,
					"steeringPin12", PinState.LOW);
			steeringPin2 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_02,
					"steeringPin13", PinState.LOW);
			frontLight = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_07,
					"frontLight", PinState.LOW);
			backLight = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_04,
					"backLight", PinState.LOW);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Could not Init Car");
		}
	}

	// Commands to control thrust ie. forward and backward
	public enum ThrustCommand {
		OFF, FORWARD, BACKWARD
	}

	// Command to control steering
	public enum SteeringCommand {
		OFF, LEFT, RIGHT
	}

	// Method to steer the car
	public void steering(SteeringCommand cmd) {
		if (steeringTimer != null) {
			steeringTimer.cancel();
		}
		steeringTimer = new Timer();
		switch (cmd) {
			case OFF:
				System.out.println("Steering OFF");
				steeringPin2.low();
				steeringPin0.low();
				break;
			case RIGHT:
				System.out.println("Steering Right");
				steeringPin2.low();
				steeringPin0.high();
				// Turn off steering right after command times out
				// (this may happen if the admin changes the current
				// user in the middle of a car turning)
				steeringTimer.schedule(new TimerTask() {
					@Override
					public void run() {
						System.out.println("command timed out");
						steeringPin0.low();
					}
				}, COMMAND_TIMEOUT);
				break;
			case LEFT:
				System.out.println("Steering Left");
				steeringPin0.low();
				steeringPin2.high();
				steeringTimer.schedule(new TimerTask() {
					@Override
					public void run() {
						System.out.println("command timed out");
						steeringPin2.low();
					}
				}, COMMAND_TIMEOUT);
				break;
			}
	}

	// Method to move the card forward and backwards
	public void thrust(ThrustCommand cmd) {
		if (thrustTimer != null) {
			thrustTimer.cancel();
		}
		thrustTimer = new Timer();
		switch (cmd) {
		case OFF:
			System.out.println("Thrust OFF");
			thrustPin13.low();
			thrustPin12.low();
			break;
		case FORWARD:
			System.out.println("Thrust FORWARD");
			thrustPin13.low();
			thrustPin12.high();
			// Turn off thrust forward command after command times out
			// (this may happen if the admin changes the current
			// user in the middle of the car going forward)
			thrustTimer.schedule(new TimerTask() {	
				@Override
				public void run() {
					thrustPin12.low();
				}
			}, COMMAND_TIMEOUT);
			break;
		case BACKWARD:
			System.out.println("Thrust BACKWARDS");
			thrustPin12.low();
			thrustPin13.high();
			thrustTimer.schedule(new TimerTask() {
				@Override
				public void run() {
					thrustPin13.low();
				}
			}, COMMAND_TIMEOUT);
			break;
		}
	}

	// Command to the lights
	public enum LightCommand {
		ON, OFF
	}

	// Method to control the front lights
	public void frontLight(LightCommand cmd) {
		switch (cmd) {
		case ON:
			System.out.println("Front light on");
			frontLight.high();
			break;
		case OFF:
			System.out.println("Front light off");
			frontLight.low();
			break;
		}
	}

	// Method to control the back lights
	public void backLight(LightCommand cmd) {
		switch (cmd) {
		case ON:
			System.out.println("Back light on");
			backLight.high();
			break;
		case OFF:
			System.out.println("Back light off");
			backLight.low();
			break;
		}
	}

	// Shutdown the car
	public void shutdown() {
		System.out.println("Car shut down");
		thrustPin12.low();
		thrustPin13.low();
		steeringPin0.low();
		steeringPin2.low();
		gpio.shutdown();
	}
}
