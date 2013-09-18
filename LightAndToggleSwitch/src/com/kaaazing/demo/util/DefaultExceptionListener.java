package com.kaaazing.demo.util;

import javax.jms.ExceptionListener;
import javax.jms.JMSException;

public class DefaultExceptionListener implements ExceptionListener {

	@Override
	public void onException(JMSException jmse) {
		System.out.println(jmse.getMessage());
	}

}
