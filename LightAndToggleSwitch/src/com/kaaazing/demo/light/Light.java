package com.kaaazing.demo.light;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;

public class Light {

	private final GpioController gpio;
	private final GpioPinDigitalOutput gpioPin;

	public Light() {
		gpio = GpioFactory.getInstance();
		gpioPin = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_01, 
				"MyLight", PinState.LOW);
	}

	public void on() {
		gpioPin.high();
		System.out.println("Light on");
	}

	public void off() {
		gpioPin.low();
		System.out.println("Light off");
	}

	public void shutdown() {
		gpio.shutdown();
	}

	public static void main(String[] args) {
		Light lightController = new Light();
		System.out.println("Light controller initialized");
		try {
			Thread.sleep(2000);
			lightController.on();
			Thread.sleep(2000);
			lightController.off();
			Thread.sleep(2000);
			lightController.on();
			Thread.sleep(2000);
			lightController.off();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			lightController.shutdown();
		}
	}

}
