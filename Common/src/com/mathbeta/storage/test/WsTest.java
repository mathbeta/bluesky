package com.mathbeta.storage.test;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

public class WsTest {
	public static void main(String[] args) {
		Client client = ClientBuilder.newClient();
		WebTarget target = client
				.target("http://10.126.253.178:5085/cloudui/matrix/login.jsp");
		Response response = target.request().get();
		System.out.println(response.readEntity(String.class));
	}
}
