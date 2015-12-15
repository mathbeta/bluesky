package com.mathbeta.storage.util;

import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DataTransferTask implements Runnable {
	private static Logger log = LoggerFactory.getLogger(DataTransferTask.class);

	private Socket socket;

	public DataTransferTask(Socket socket) {
		this.socket = socket;
	}

	public void run() {
		try {
			byte[] buf = TransferUtil.read(socket);
			while (buf != null) {
				Message message = TransferUtil.readObject(buf);
				Map<String, Object> content = message.getContent();
				String path = (String) content.get(StorageProperties.FILE_PATH);
				if (Op.UPLOAD_DATA == message.getOp()) {
					log.info("receive file data: " + path);
					boolean firstBlock = (Boolean) content
							.get(StorageProperties.IS_FIRST_BLOCK);
					File file = FileUtil.createFile(ConfigHelper
							.getValue(StorageProperties.NODE_STORAGE_DIR)
							+ "/" + path);
					if (firstBlock) {
						if (!file.getParentFile().exists()) {
							file.getParentFile().mkdirs();
						}
						file.createNewFile();
					}

					byte[] data = (byte[]) content
							.get(StorageProperties.FILE_DATA);
					FileUtil.writeData(file, data, !firstBlock);
				} else if (Op.DOWNLOAD_DATA == message.getOp()) {
					log.info("send file data: " + path);
					File file = new File(ConfigHelper
							.getValue(StorageProperties.NODE_STORAGE_DIR)
							+ "/" + path);
					if (file == null || !file.exists()) {
						Message ret = new Message();
						ret.setOp(Op.DOWNLOAD_DATA);
						Map<String, Object> retContent = Message.buildContent(
								StorageProperties.IS_LAST_BLOCK, true,
								StorageProperties.FILE_DATA, null);
						ret.setContent(retContent);
						TransferUtil.writeMessage(socket, ret);
					} else {
						int blockSize = Integer
								.parseInt(ConfigHelper
										.getValue(StorageProperties.DATA_TRANSFER_BLOCK_SIZE));
						TransferUtil.writeFile(socket, path, file, blockSize,
								Op.DOWNLOAD_DATA);
					}
				}
				buf = TransferUtil.read(socket);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
}
