package com.mathbeta.storage.jms;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.TextMessage;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.apache.activemq.command.ActiveMQTextMessage;

public class Client {
	private static Client client;
	private static Configs configs;
	private Map<String, ClientConnection> clientConnections = new HashMap<String, ClientConnection>();

	private Client() {
		String config = Client.class.getClassLoader().getResource(
				"jms-configs.xml").getPath();
		reload(config);
	}

	public void reload(String config) {
		clientConnections.clear();
		try {
			Unmarshaller um = JAXBContext.newInstance(Configs.class)
					.createUnmarshaller();
			configs = (Configs) um.unmarshal(new FileInputStream(new File(
					config)));
			List<Config> configList = configs.getConfigs();
			if (configList != null && !configList.isEmpty()) {
				for (Config conf : configList) {
					conf.setSelector(configs.getSelector());
					ClientConnection clientConnection = new ClientConnection(
							conf);
					clientConnections.put(conf.getName(), clientConnection);
				}
			}
		} catch (JAXBException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public Message createTextMessage(String content) {
		TextMessage message = new ActiveMQTextMessage();
		try {
			message.setText(content);
		} catch (JMSException e) {
			e.printStackTrace();
		}
		return message;
	}

	public static Client getClient() {
		if (client == null) {
			synchronized (Client.class) {
				if (client == null) {
					client = new Client();
				}
			}
		}
		return client;
	}

	public void asyncSend(Message message, String queue) {
		try {
			ClientConnection clientConnection = clientConnections.get(queue);
			MessageProducer producer = clientConnection.getProducer();
			producer.send(message);
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}

	public Message syncSend(Message message, String queue) {
		try {
			ClientConnection clientConnection = clientConnections.get(queue);
			MessageProducer producer = clientConnection.getProducer();
			producer.send(message);
			MessageConsumer consumer = clientConnection.getConsumer();
			return consumer.receive();
		} catch (JMSException e) {
			e.printStackTrace();
		}
		return null;
	}
}
