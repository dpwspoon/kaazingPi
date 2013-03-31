package com.kaazing.rc;

import java.util.concurrent.atomic.AtomicBoolean;

public class App {
	
	//Entry point into the application
	public static void main(String[] args) {
		AtomicBoolean exit = new AtomicBoolean(false);
		try {
			CommandReceiver cmdRec = new CommandReceiver();
			cmdRec.startReceivingCommands();
		} catch (Exception e) {
			e.printStackTrace();
			exit.set(true);
		}
		while(!exit.get()){
		}

	}

}
