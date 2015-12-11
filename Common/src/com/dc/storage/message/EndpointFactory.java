package com.dc.storage.message;

import com.dc.storage.message.client.LongClientEndpoint;
import com.dc.storage.message.client.ShortClientEndpoint;
import com.dc.storage.message.server.LongServerEndpoint;
import com.dc.storage.message.server.ShortServerEndpoint;

public class EndpointFactory {
	private static EndpointFactory factory = null;

	private EndpointFactory() {
	}

	public static EndpointFactory getFactory() {
		if (factory == null) {
			synchronized (EndpointFactory.class) {
				if (factory == null) {
					factory = new EndpointFactory();
				}
			}
		}
		return factory;
	}

	public IEndpoint getEndpoint(EndpointConfig config) {
		if (config != null) {
			if (config.getType() == EndpointType.CLIENT) {
				if (config.getMode() == Mode.SHORT) {
					return new ShortClientEndpoint(config);
				} else if (config.getMode() == Mode.LONG) {
					return new LongClientEndpoint(config);
				}
			} else if (config.getType() == EndpointType.SERVER) {
				if (config.getMode() == Mode.SHORT) {
					return new ShortServerEndpoint(config);
				} else if (config.getMode() == Mode.LONG) {
					return new LongServerEndpoint(config);
				}
			}
		}
		return null;
	}
}
