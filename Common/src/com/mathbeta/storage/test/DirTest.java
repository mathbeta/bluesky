package com.mathbeta.storage.test;

import java.util.ArrayList;
import java.util.List;

public class DirTest {
	public static void main(String[] args) {
		List<String> dirs = new ArrayList<String>();
		dirs.add("a.txt");
		dirs.add("a");
		dirs.add("b");
		dirs.add("/");
		StringBuilder sb = new StringBuilder();
		for (int i = dirs.size() - 1; i >= 0; i--) {
			sb.append(dirs.get(i));
			if (!"/".equals(dirs.get(i)) && i > 0) {
				sb.append("/");
			}
		}
		System.out.println(sb.toString());
	}
}
