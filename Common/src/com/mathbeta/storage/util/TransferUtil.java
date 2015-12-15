package com.mathbeta.storage.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.util.Map;

public class TransferUtil {
	/**
	 * 从字节数组中读取一个对象
	 * 
	 * @param <T>
	 * @param b
	 * @return
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	@SuppressWarnings("unchecked")
	public static <T> T readObject(byte[] b) throws IOException,
			ClassNotFoundException {
		if (b == null) {
			throw new IOException("Input byte array can not be null!");
		}
		ByteArrayInputStream bais = new ByteArrayInputStream(b);
		ObjectInputStream ois = new ObjectInputStream(bais);
		return (T) ois.readObject();
	}

	/**
	 * 将对象obj序列化字节流写到输出流os中
	 * 
	 * @param os
	 * @param obj
	 * @throws IOException
	 */
	public static void writeObject(OutputStream os, Serializable obj)
			throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(baos);
		oos.writeObject(obj);
		byte[] original = baos.toByteArray();
		byte[] b = ByteArrayUtil.wrap(original);
		os.write(b);
	}

	/**
	 * 读取一个数据包
	 * 
	 * @param socket
	 * @return
	 * @throws IOException
	 */
	public static byte[] read(Socket socket) throws IOException {
		InputStream is = socket.getInputStream();
		byte[] buf = null;
		final int dataLen = 4;
		byte[] tempBytes = new byte[dataLen];
		int length = 0;
		int i = 0;
		DataInputStream ins = new DataInputStream(is);
		while (i < dataLen) {
			int headpart = ins.read();
			if (headpart == -1) {
				socket.close();
				break;
			}
			tempBytes[i] = (byte) headpart;
			i++;
		}
		if (i < dataLen) {
			return null;
		}
		length = ByteArrayUtil.byte2int(tempBytes);
		buf = new byte[length];
		ins.readFully(buf);

		return buf;
	}

	/**
	 * 往socket的输出流中写一个消息
	 * 
	 * @param socket
	 * @param message
	 * @throws IOException
	 */
	public static void writeMessage(Socket socket, Message message)
			throws IOException {
		writeObject(socket.getOutputStream(), message);
	}

	public static void writeFile(Socket socket, String path, File f,
			int blockSize, Op op) {
		InputStream is = null;
		try {
			is = new FileInputStream(f);
			byte[] buf = new byte[blockSize];
			int len = -1;
			boolean firstBlock = true;
			boolean lastBlock = false;
			long length = f.length();
			long curLength = 0;
			while ((len = is.read(buf)) != -1) {
				Message message = new Message();
				message.setOp(op);
				byte[] data = new byte[len];
				System.arraycopy(buf, 0, data, 0, len);
				curLength += len;
				lastBlock = (curLength == length);
				Map<String, Object> content = Message.buildContent(
						StorageProperties.FILE_PATH, path,
						StorageProperties.FILE_DATA, data,
						StorageProperties.IS_FIRST_BLOCK, firstBlock,
						StorageProperties.IS_LAST_BLOCK, lastBlock);
				message.setContent(content);
				TransferUtil.writeMessage(socket, message);

				firstBlock = false;
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			IOUtils.closeQuietly(is);
		}
	}

	public static void readFile(Socket socket, String path, File f) {
		Message message = new Message();
		message.setOp(Op.DOWNLOAD_DATA);
		Map<String, Object> content = Message.buildContent(
				StorageProperties.FILE_PATH, path);
		message.setContent(content);
		try {
			TransferUtil.writeMessage(socket, message);
		} catch (IOException e) {
			e.printStackTrace();
		}
		OutputStream os = null;
		try {
			os = new FileOutputStream(f, false);
			boolean hasNext = true;
			while (hasNext) {
				message = readObject(read(socket));
				if (message != null && message.getOp() == Op.DOWNLOAD_DATA) {
					byte[] data = (byte[]) message.getContent().get(
							StorageProperties.FILE_DATA);
					if (data != null && data.length > 0) {
						os.write(data);
					}
				}
				hasNext = !(Boolean) message.getContent().get(
						StorageProperties.IS_LAST_BLOCK);
			}
			os.flush();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			IOUtils.closeQuietly(os);
		}
	}
}
