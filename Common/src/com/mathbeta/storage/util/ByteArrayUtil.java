package com.mathbeta.storage.util;

public class ByteArrayUtil {

	public static byte[] int2byte(int res) {
		byte[] targets = new byte[4];

		targets[0] = (byte) (res & 0xff);// 最低位
		targets[1] = (byte) ((res >> 8) & 0xff);// 次低位
		targets[2] = (byte) ((res >> 16) & 0xff);// 次高位
		targets[3] = (byte) (res >>> 24);// 最高位,无符号右移。
		return targets;
	}

	public static int byte2int(byte[] res) {
		// 一个byte数据左移24位变成0x??000000，再右移8位变成0x00??0000
		// | 表示安位或
		int targets = (res[0] & 0xff) | ((res[1] << 8) & 0xff00)
				| ((res[2] << 24) >>> 8) | (res[3] << 24);
		return targets;
	}

	public static byte[] wrap(byte[] original) {
		// wrap original data with data length as 4 bytes head
		int len = original.length;
		byte[] result = new byte[4 + len];
		byte[] head = int2byte(len);
		System.arraycopy(head, 0, result, 0, 4);
		System.arraycopy(original, 0, result, 4, len);
		return result;
	}
}
