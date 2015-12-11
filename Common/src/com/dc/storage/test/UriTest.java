package com.dc.storage.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;

public class UriTest {
	public static void main(String[] args) throws IOException {
		URI uri = URI.create("tcp://abc:def@10.1.2.3:4444/abc/def/ghi");
		System.out.println(uri.getHost() + ", " + uri.getPort() + ", "
				+ uri.getUserInfo() + ", " + uri.getPath());

		File f = new File(UriTest.class.getClassLoader().getResource(
				"jms-configs.xml").getPath());
		BufferedReader br = new BufferedReader(new FileReader(f));
		String s = null;
		while ((s = br.readLine()) != null) {
			System.out.println(s);
		}
	}
}
