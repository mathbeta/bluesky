package com.dc.storage.util;

public class AssertUtil {
	public static void checkNull(Object o, String message)
			throws NullPointerException {
		if (o == null) {
			throw new NullPointerException(message);
		}
	}
}
