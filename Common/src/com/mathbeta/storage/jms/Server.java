package com.mathbeta.storage.jms;

import org.apache.activemq.broker.BrokerService;

public class Server {
	public static void main(String[] args) {
		BrokerService brokerService = new BrokerService();
		brokerService.setBrokerName("test");
		try {
			brokerService.addConnector("tcp://127.0.0.1:65535");
			brokerService.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
