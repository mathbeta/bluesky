package com.mathbeta.storage.util;

import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class ConnectorMaps {
	private static ConnectorMaps instance;

	private ConnectorMaps() {
	}

	public static ConnectorMaps getInstance() {
		if (instance == null) {
			synchronized (ConnectorMaps.class) {
				if (instance == null) {
					instance = new ConnectorMaps();
				}
			}
		}
		return instance;
	}

	// command connectors, key is the node ip
	private Map<String, Socket> commandConnectors = new HashMap<String, Socket>();
	// transfer connectors, key is like node_ip:port
	private Map<String, Socket> transferConnectors = new HashMap<String, Socket>();

	public Socket getCommandConnector(String nodeIp) {
		return commandConnectors.get(nodeIp);
	}

	public Socket getTransferConnector(String nodeIp, String port) {
		return transferConnectors.get(nodeIp + ":" + port);
	}

	public Socket putCommandConnector(String nodeIp, Socket socket) {
		return commandConnectors.put(nodeIp, socket);
	}

	public Socket putTransferConnector(String nodeIp, String port, Socket socket) {
		return transferConnectors.put(nodeIp + ":" + port, socket);
	}

	public Socket removeCommandConnector(String nodeIp) {
		return commandConnectors.remove(nodeIp);
	}

	public Socket removeTransferConnector(String nodeIp, String port) {
		return transferConnectors.remove(nodeIp + ":" + port);
	}
}
