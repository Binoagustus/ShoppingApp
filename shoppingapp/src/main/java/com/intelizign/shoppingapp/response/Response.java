package com.intelizign.shoppingapp.response;

public class Response {

	private String message;
	private Object object;

	public Response(String message) {
		this.message = message;
	}

	public Response(String message, Object object) {
		this.message = message;
		this.object = object;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Object getObject() {
		return object;
	}

	public void setObject(Object object) {
		this.object = object;
	}
}
