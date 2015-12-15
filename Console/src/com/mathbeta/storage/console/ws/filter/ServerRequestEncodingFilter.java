package com.mathbeta.storage.console.ws.filter;

import java.io.IOException;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;

public class ServerRequestEncodingFilter implements ContainerRequestFilter {

	@Override
	public void filter(ContainerRequestContext request) throws IOException {
		request.setProperty("CharacterEncoding", "UTF-8");
	}

}
