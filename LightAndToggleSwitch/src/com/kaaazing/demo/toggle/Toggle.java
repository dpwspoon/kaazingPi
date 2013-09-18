package com.kaaazing.demo.toggle;

import java.util.concurrent.CopyOnWriteArrayList;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.PinPullResistance;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;

public class Toggle {
	private final GpioController gpio;
	private final GpioPinDigitalInput gpioPin;
	private final CopyOnWriteArrayList<ToggleListener> listeners;

	public Toggle() {
		listeners = new CopyOnWriteArrayList<ToggleListener>();

		gpio = GpioFactory.getInstance();
		gpioPin = gpio.provisionDigitalInputPin(RaspiPin.GPIO_00,
				PinPullResistance.PULL_DOWN);
		gpioPin.addListener(new GpioPinListenerDigital() {

			@Override
			public void handleGpioPinDigitalStateChangeEvent(
					GpioPinDigitalStateChangeEvent event) {
				notifyListeners(event.getState());
			}
		});
	}

	private void notifyListeners(PinState state) {
		if (state == PinState.HIGH) {
			System.out.println("Toggle on");
		} else {
			System.out.println("Toggle off");
		}

		for (ToggleListener listener : listeners) {
			if (state == PinState.HIGH) {
				listener.on();
			} else {
				listener.off();
			}
		}
	}

	public void registerListener(ToggleListener listener) {
		listeners.add(listener);
		// send initial state
		if (gpioPin.getState() == PinState.HIGH) {
			listener.on();
		} else {
			listener.off();
		}
	}

	public void shutdown() {
		gpio.shutdown();
	}

	public static void main(String[] args) {
		Toggle toggle = new Toggle();
		System.out.println("Running toggle for 20 seconds");
		try {
			Thread.sleep(20000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			toggle.shutdown();
		}
		System.out.println("Exiting");
	}
}
