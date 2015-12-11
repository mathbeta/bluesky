package com.dc.storage.util;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * console与node交互的消息
 * 
 * @author xuxy
 * 
 */
public class Message implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 操作类型
	 */
	private Op op;
	/**
	 * 消息内容
	 */
	private Map<String, Object> content;

	public Op getOp() {
		return op;
	}

	public void setOp(Op op) {
		this.op = op;
	}

	public Map<String, Object> getContent() {
		return content;
	}

	public void setContent(Map<String, Object> content) {
		this.content = content;
	}

	/**
	 * 快速构造一个map，按顺序传入key1，value1，key2，value2...
	 * 
	 * @param kvs
	 * @return
	 */
	public static Map<String, Object> buildContent(Object... kvs) {
		Map<String, Object> content = null;
		if (kvs != null && kvs.length > 0) {
			int len = kvs.length;
			if (len % 2 == 0) {
				content = new HashMap<String, Object>();
				for (int i = 0; i < len / 2; i++) {
					int j = i * 2;
					if (kvs[j] instanceof String) {
						content.put((String) kvs[j], kvs[j + 1]);
					}
				}
			}
		}
		return content;
	}

	public String toString() {
		return "op=" + op + ", content=" + content;
	}
}
