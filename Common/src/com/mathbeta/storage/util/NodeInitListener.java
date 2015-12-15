package com.mathbeta.storage.util;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class NodeInitListener extends Thread {

	public static void listen() {
		ExecutorServicePool.getInstance().submit(new NodeInitListener());
	}

	public void run() {
		int port = Integer.parseInt(ConfigHelper
				.getValue(StorageProperties.CONSOLE_COMMAND_PORT));
		ServerSocket server = null;
		Socket socket = null;
		try {
			server = new ServerSocket(port);
			while (true) {
				socket = server.accept();
				ExecutorServicePool.getInstance().submit(
						new NodeInitListenTask(socket));
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			IOUtils.closeQuietly(server);
			IOUtils.closeQuietly(socket);
		}
	}

}
