package com.dc.storage.node.start;

import com.dc.storage.node.init.NodeInit;

public class StartNode {
	public static void main(String[] args) {
		NodeInit.start();
		System.out.println("==================================================================");
		System.out.println("Node has been started.");
		System.out.println("==================================================================");
	}
}
