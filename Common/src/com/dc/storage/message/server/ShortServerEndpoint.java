package com.dc.storage.message.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import com.dc.storage.message.EndpointConfig;
import com.dc.storage.message.EndpointConfigHelper;
import com.dc.storage.message.IEndpoint;
import com.dc.storage.util.AssertUtil;
import com.dc.storage.util.IOUtils;

public class ShortServerEndpoint implements IEndpoint {
	private ServerSocket server = null;
	private Socket socket = null;

	public ShortServerEndpoint(EndpointConfig config) {
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
	public int read(byte[] b) {
		AssertUtil.checkNull(server, "server can not be null!");
		try {
			if (socket == null) {
				socket = server.accept();
			}
			InputStream is = socket.getInputStream();
			return is.read(b);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return -1;
	}

	@Override
	public void write(byte[] b, int off, int len) {
		AssertUtil.checkNull(server, "server can not be null!");
		try {
			if (socket == null) {
				socket = server.accept();
			}
			OutputStream os = socket.getOutputStream();
			os.write(b, off, len);
			os.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void close() {
		IOUtils.closeQuietly(server);
	}

}
