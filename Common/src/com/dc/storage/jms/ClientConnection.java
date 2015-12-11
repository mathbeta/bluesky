package com.dc.storage.jms;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.Session;

import org.apache.activemq.ActiveMQConnectionFactory;

public class ClientConnection {
	private Config config;
	private Connection connection;
	private MessageProducer producer;
	private MessageConsumer consumer;

	public Config getConfig() {
		return config;
	}

	public void setConfig(Config config) {
		this.config = config;
	}

	public MessageProducer getProducer() {
		return producer;
	}

	public void setProducer(MessageProducer producer) {
		this.producer = producer;
	}

	public MessageConsumer getConsumer() {
		return consumer;
	}

	public void setConsumer(MessageConsumer consumer) {
		this.consumer = consumer;
	}

	@SuppressWarnings("unchecked")
	public ClientConnection(Config config) {
		this.config = config;
		try {
			ConnectionFactory factory = new ActiveMQConnectionFactory(config
					.getBrokerURL());
			connection = factory.createConnection();
			connection.start();
			Session session = connection.createSession(config.isTransacted(),
					Session.AUTO_ACKNOWLEDGE);
			Destination out = session.createQueue(config.getOutbox());
			Destination in = session.createQueue(config.getInbox());
			producer = session.createProducer(out);
			if (!config.isSelected()) {
				consumer = session.createConsumer(in);
			} else {
				consumer = session.createConsumer(in,
						MessageProperties.SELECTOR_KEY + "='"
								+ config.getSelector() + "'");
			}
			System.out.println(consumer.getMessageSelector());
			if (config.getReceiver() != null) {
				Class<? extends MessageListener> receiverClass = (Class<? extends MessageListener>) Class
						.forName(config.getReceiver());
				if (receiverClass != null) {
					MessageListener receiver = receiverClass.newInstance();
					consumer.setMessageListener(receiver);
				}
			}
		} catch (JMSException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}

	public void close() {
		if (connection != null) {
			try {
				connection.close();
			} catch (JMSException e) {
				e.printStackTrace();
			}
		}
	}
}
