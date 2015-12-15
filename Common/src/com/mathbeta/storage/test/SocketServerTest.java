package com.mathbeta.storage.test;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import com.mathbeta.storage.util.IOUtils;

public class SocketServerTest {
	public static void main(String[] args) {
		ServerSocket server = null;
		Socket socket = null;
		try {
			server = new ServerSocket(65535);
			socket = server.accept();
			socket.getInputStream().read();
			System.out.println("socket connected...");
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			IOUtils.closeQuietly(server);
			IOUtils.closeQuietly(socket);
		}
	}
}
