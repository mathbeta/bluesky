package com.dc.storage.util;

import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;

/**
 * 消息包装类
 * 
 * @author xuxy
 * 
 */
public class MessageHelper {
	public static String wrap(boolean result, StatusCode statusCode,
			String message) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("result", result);
		map.put("statusCode", statusCode);
		map.put("message", message);
		return JSON.toJSONString(map);
	}

	public static Map<String, Object> extract(String json) {
		return JSON.parseObject(json, new TypeReference<Map<String, Object>>() {
		});
	}
}
