package com.dc.storage.console.ws;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;

public class MyExceptionMapper implements ExceptionMapper<Throwable> {

	@Override
	public Response toResponse(Throwable t) {
		t.printStackTrace();
		if (t instanceof WebApplicationException) {
			return ((WebApplicationException) t).getResponse();
		}
		StringBuilder sb = new StringBuilder();
		StackTraceElement[] elements = t.getStackTrace();
		if (elements != null && elements.length > 0) {
			for (StackTraceElement element : elements) {
				sb.append("<p>at " + element.getClassName() + "."
						+ element.getMethodName() + "(" + element.getFileName()
						+ ":" + element.getLineNumber() + ")" + "</p>");
			}
		}
		return Response.status(Status.INTERNAL_SERVER_ERROR).entity(
				"<p>后台异常：" + t.getMessage() + "</p>" + sb.toString()).type(
				MediaType.TEXT_HTML + ";charset=UTF-8").build();
	}
}
