package com.dc.storage.message.client;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import com.dc.storage.message.EndpointConfig;
import com.dc.storage.message.EndpointConfigHelper;
import com.dc.storage.message.IEndpoint;
import com.dc.storage.util.AssertUtil;
import com.dc.storage.util.IOUtils;

public class ShortClientEndpoint implements IEndpoint {
	private Socket socket = null;

	public ShortClientEndpoint(EndpointConfig config) {
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
	public int read(byte[] b) {
		AssertUtil.checkNull(socket, "socket can not be null!");
		try {
			InputStream is = socket.getInputStream();
			return is.read(b);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return -1;
	}

	@Override
	public void write(byte[] b, int off, int len) {
		AssertUtil.checkNull(socket, "socket can not be null!");
		try {
			OutputStream os = socket.getOutputStream();
			os.write(b, off, len);
			os.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void close() {
		IOUtils.closeQuietly(socket);
	}

}
