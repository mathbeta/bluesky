package com.mathbeta.storage.util;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class IOUtils extends org.apache.commons.io.IOUtils {
	public static void closeQuietly(ServerSocket server) {
		if (server != null) {
			try {
				server.close();
			} catch (IOException e) {
			}
		}
	}

	public static void closeQuietly(Socket socket) {
		if (socket != null) {
			try {
				socket.close();
			} catch (IOException e) {
			}
		}
	}
}
