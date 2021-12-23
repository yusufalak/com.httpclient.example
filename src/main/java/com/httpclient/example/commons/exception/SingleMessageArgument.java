package com.httpclient.example.commons.exception;

import java.io.Serializable;

public class SingleMessageArgument implements Serializable {

	private static final long serialVersionUID = -4074798741923296551L;

	private String[] innerArguments;

	public SingleMessageArgument() {

	}

	public SingleMessageArgument(String[] innerArguments) {
		this.innerArguments = innerArguments;
	}

	public String[] getInnerArguments() {
		return innerArguments;
	}

	public void setInnerArguments(String[] innerArguments) {
		this.innerArguments = innerArguments;
	}
}
