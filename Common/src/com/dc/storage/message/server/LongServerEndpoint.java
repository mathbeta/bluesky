package com.dc.storage.message.server;

import java.io.IOException;
import java.net.ServerSocket;

import com.dc.storage.message.EndpointConfig;
import com.dc.storage.message.EndpointConfigHelper;
import com.dc.storage.message.IEndpoint;

public class LongServerEndpoint implements IEndpoint {
	private ServerSocket server = null;

	public LongServerEndpoint(EndpointConfig config) {
		try {
			int port = Integer.parseInt(config.getConnectionString());
			server = new ServerSocket(port);
			EndpointConfigHelper.config(server, config);
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void close() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int read(byte[] b) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void write(byte[] b, int off, int len) {
		// TODO Auto-generated method stub
		
	}

}
