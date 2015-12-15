package com.mathbeta.storage.message.client;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import com.mathbeta.storage.message.EndpointConfig;
import com.mathbeta.storage.message.EndpointConfigHelper;
import com.mathbeta.storage.message.IEndpoint;
import com.mathbeta.storage.util.AssertUtil;

public class LongClientEndpoint implements IEndpoint {
	private Socket socket = null;

	public LongClientEndpoint(EndpointConfig config) {
		String[] addr = config.getConnectionString().split(":");
		try {
			socket = new Socket(addr[0], Integer.parseInt(addr[1]));
			EndpointConfigHelper.config(socket, config);
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void close() {
	}

	@Override
	public int read(byte[] b) {
		AssertUtil.checkNull(socket, "socket can not be null!");
		return 0;
	}

	@Override
	public void write(byte[] b, int off, int len) {
		AssertUtil.checkNull(socket, "socket can not be null!");
		
	}

}
