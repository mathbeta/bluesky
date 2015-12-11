package com.dc.storage.console.service.impl;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.dc.storage.console.bean.DataFile;
import com.dc.storage.console.bean.Node;
import com.dc.storage.console.bean.User;
import com.dc.storage.console.dao.INodeDao;
import com.dc.storage.console.dao.IStorageDao;
import com.dc.storage.console.service.IStorageService;
import com.dc.storage.util.ConfigHelper;
import com.dc.storage.util.ConnectorMaps;
import com.dc.storage.util.FileUtil;
import com.dc.storage.util.Message;
import com.dc.storage.util.MessageHelper;
import com.dc.storage.util.Op;
import com.dc.storage.util.StatusCode;
import com.dc.storage.util.StorageProperties;
import com.dc.storage.util.TransferUtil;

@Service("storageService")
public class StorageService implements IStorageService {
	private static Logger log = LoggerFactory.getLogger(StorageService.class);

	@Autowired
	@Qualifier("storageDao")
	private IStorageDao storageDao;

	@Autowired
	@Qualifier("nodeDao")
	private INodeDao nodeDao;

	@Override
	public String saveUpload(InputStream is,
			FormDataContentDisposition disposition, String description,
			String path, User user) {
		String dir = user.getUserName();
		if (path != null && !path.isEmpty()) {
			dir = dir + path;
		}
		File f = FileUtil.save(is, ConfigHelper
				.getValue(StorageProperties.STORAGE_FS_TMP_DIR)
				+ "/" + dir + "/" + disposition.getFileName());
		if (f != null) {
			long fileId = storageDao.save(disposition.getFileName(),
					f.length(), user.getId(), getPathId(path, user.getId(),
							true), description);
			List<Node> nodes = nodeDao.findAllRunning();
			if (nodes == null) {
				nodes = new ArrayList<Node>();
			}
			List<Node> selectedNodes = new ArrayList<Node>();
			int replCount = Integer.parseInt(ConfigHelper
					.getValue(StorageProperties.STORAGE_REPL_COUNT));
			if (nodes.size() >= replCount) {
				int[] rand = generateRand(nodes.size());
				for (int i = 0; i < replCount; i++) {
					selectedNodes.add(nodes.get(rand[i]));
				}
			} else {
				selectedNodes = nodes;
			}
			storageDao.updateReplCount(fileId, selectedNodes.size());
			int blockSize = Integer.parseInt(ConfigHelper
					.getValue(StorageProperties.DATA_TRANSFER_BLOCK_SIZE));
			for (Node node : selectedNodes) {
				String[] ports = node.getTransferPorts().split(",");
				int i = (int) (Math.random() * ports.length);
				Socket socket = ConnectorMaps.getInstance()
						.getTransferConnector(node.getIp(), ports[i]);
				if (socket == null) {
					try {
						socket = new Socket(node.getIp(), Integer
								.parseInt(ports[i]));
						ConnectorMaps.getInstance().putTransferConnector(
								node.getIp(), ports[i], socket);
					} catch (NumberFormatException e) {
						e.printStackTrace();
					} catch (UnknownHostException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				TransferUtil.writeFile(socket, dir + "/" + f.getName(), f,
						blockSize, Op.UPLOAD_DATA);
				storageDao.saveFileNode(fileId, node.getId());
			}
			// 删除临时文件
			f.delete();

			return MessageHelper.wrap(true, StatusCode.OK, "");
		}
		return MessageHelper.wrap(false, StatusCode.INTERNAL_SERVER_ERROR,
				"can not save file");
	}

	/**
	 * 产生0~size-1构成的乱序数组
	 * 
	 * @param size
	 * @return
	 */
	private int[] generateRand(int size) {
		int[] r = new int[size];
		for (int i = 0; i < size; i++) {
			r[i] = i;
		}
		for (int i = 0; i < size; i++) {
			int k = (int) (Math.random() * size);
			int tmp = r[i];
			r[i] = r[k];
			r[k] = tmp;
		}
		return r;
	}

	@Override
	public List<String> getConsoleUrls() {
		return storageDao.getConsoleUrls();
	}

	@Override
	public Response download(String id, User user) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Response downloadByPath(String path, User user) {
		long fileId = getPathId(path, user.getId(), false);
		List<Node> nodes = nodeDao.findFileNodes(fileId);
		if (nodes != null && !nodes.isEmpty()) {
			// 随机选择一个节点获取文件数据
			int i = (int) (Math.random() * nodes.size());
			Node node = nodes.get(i);

			String[] ports = node.getTransferPorts().split(",");
			i = (int) (Math.random() * ports.length);
			Socket socket = ConnectorMaps.getInstance().getTransferConnector(
					node.getIp(), ports[i]);
			if (socket == null) {
				try {
					socket = new Socket(node.getIp(), Integer
							.parseInt(ports[i]));
					ConnectorMaps.getInstance().putTransferConnector(
							node.getIp(), ports[i], socket);
				} catch (NumberFormatException e) {
					e.printStackTrace();
				} catch (UnknownHostException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			File f = new File(ConfigHelper
					.getValue(StorageProperties.STORAGE_FS_TMP_DIR)
					+ "/"
					+ user.getUserName()
					+ "/"
					+ FileUtil.getFileName(path));
			TransferUtil.readFile(socket, user.getUserName() + "/" + path, f);
			ResponseBuilder rb = Response.ok(f,
					MediaType.APPLICATION_OCTET_STREAM);
			String fileName = f.getName();
			try {
				fileName = URLEncoder.encode(f.getName(), "utf-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			rb
					.header("Content-Disposition", "attachment; filename="
							+ fileName);
			return rb.build();
		}
		return null;
	}

	@Override
	public List<DataFile> list(String path, long userId) {
		long pathId = getPathId(path, userId, true);
		List<DataFile> list = storageDao.list(pathId, userId);
		return list;
	}

	private Long getPathId(String path, long userId, boolean isDir) {
		long pathId = storageDao.getUserRootId(userId);
		String[] dirs = path.substring(1).split("/");
		for (int i = 0; i < dirs.length - 1; i++) {
			pathId = storageDao.getPathId(dirs[i], userId, pathId, true);
		}
		if (dirs.length > 0) {
			pathId = storageDao.getPathId(dirs[dirs.length - 1], userId,
					pathId, isDir);
		}
		return pathId;
	}

	@Override
	public List<DataFile> listDir(long parentId, long userId) {
		List<DataFile> list = storageDao.list(parentId, userId);
		return list;
	}

	@Override
	public String saveDir(String path, String curPath, long parentId, User user) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("fileName", path);
		params.put("userId", user.getId());
		params.put("parentId", parentId);
		int i = storageDao.saveDir(params);
		if (i > 0) {
			return MessageHelper.wrap(true, StatusCode.OK, "");
		}
		return MessageHelper.wrap(false, StatusCode.UNKNOWN_SERVER_FAILURE,
				"保存数据失败！");
	}

	@Override
	public long getUserRootId(long id) {
		return storageDao.getUserRootId(id);
	}

	@Override
	public String delete(String ids) {
		if (ids == null || ids.isEmpty()) {
			return MessageHelper.wrap(false, StatusCode.CLIENT_ERROR,
					"请指定需要删除的文件记录id，用英文逗号分割！");
		}
		String[] idArr = ids.split(",");
		StringBuilder sb = new StringBuilder();
		for (String id : idArr) {
			String path = storageDao.getAbsPath(id);
			Map<String, Object> f = storageDao.getFileInfo(id);
			if (!storageDao.delete(id)) {
				sb.append(f.get("file_name"));
				sb.append(",");
			} else {
				// 删除文件
				List<String> nodeIds = storageDao.getFileNodes(id);
				// send message to node
				if (nodeIds != null && !nodeIds.isEmpty()) {
					for (String nodeId : nodeIds) {
						Node node = nodeDao.findById(nodeId);
						String[] ports = node.getTransferPorts().split(",");
						int i = (int) (Math.random() * ports.length);
						Socket socket = ConnectorMaps.getInstance()
								.getCommandConnector(node.getIp());
						if (socket == null) {
							try {
								socket = new Socket(node.getIp(), Integer
										.parseInt(ports[i]));
								ConnectorMaps.getInstance()
										.putCommandConnector(node.getIp(),
												socket);
							} catch (NumberFormatException e) {
								e.printStackTrace();
							} catch (UnknownHostException e) {
								e.printStackTrace();
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
						Message message = new Message();
						message.setContent(Message.buildContent(
								StorageProperties.FILE_PATH, path));
						message.setOp(Op.DELETE);
						try {
							TransferUtil.writeMessage(socket, message);
						} catch (IOException e) {
							e.printStackTrace();
						}
						try {
							Message ret = TransferUtil.readObject(TransferUtil
									.read(socket));
							if (ret.getOp() == Op.DELETE) {
								if (log.isDebugEnabled()) {
									log
											.debug("delete file ["
													+ path
													+ "] on node ["
													+ node.getName()
													+ "] result: "
													+ ret
															.getContent()
															.get(
																	StorageProperties.FILE_DELETE_RESULT));
								}
							}
						} catch (IOException e) {
							e.printStackTrace();
						} catch (ClassNotFoundException e) {
							e.printStackTrace();
						}

						storageDao.deleteFileNode(id, nodeId);
					}
				}
			}
		}
		return MessageHelper.wrap(true, StatusCode.OK, "[" + sb.toString()
				+ "]未能删除！");
	}

}
