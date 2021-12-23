package com.httpclient.example.commons.httpclient.models;

import java.io.Serializable;
import java.util.List;

import com.httpclient.example.commons.exception.SingleMessageArgument;

public class ServiceErrorModel implements Serializable {

	private static final long serialVersionUID = 1L;

	private String code;
	private String message;
	private List<SingleMessageArgument> arguments;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public List<SingleMessageArgument> getArguments() {
		return arguments;
	}

	public void setArguments(List<SingleMessageArgument> arguments) {
		this.arguments = arguments;
	}
}