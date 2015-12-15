package com.mathbeta.storage.node.start;

import com.mathbeta.storage.node.init.NodeInit;

public class StartNode {
	public static void main(String[] args) {
		NodeInit.start();
		System.out.println("==================================================================");
		System.out.println("Node has been started.");
		System.out.println("==================================================================");
	}
}
