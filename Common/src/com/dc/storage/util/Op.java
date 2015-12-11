package com.dc.storage.util;

public enum Op {
	/**
	 * node初始化
	 */
	INIT,
	/**
	 * node给console发送心跳
	 */
	HEART_BEAT,
	/**
	 * 向node传送数据
	 */
	UPLOAD_DATA,
	/**
	 * 从node读取数据
	 */
	DOWNLOAD_DATA,
	/**
	 * 确认node是否活着
	 */
	ACK,
	/**
	 * 删除node上的文件
	 */
	DELETE
}
