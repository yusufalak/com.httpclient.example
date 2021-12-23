package com.httpclient.example.commons.exception;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

public class SubChannelException extends Exception {

	private static final long serialVersionUID = -6842208960148134223L;

	private final ErrorCodes error;
	private final List<SingleMessageArgument> arguments;
	private String requestXml;
	private String responseXml;

	public SubChannelException(ErrorCodes error) {
		super(error.getMessage());
		this.error = error;
		this.arguments = new ArrayList<>();
	}

	public SubChannelException(ErrorCodes error, String... args) {
		super(error.getMessage());
		this.error = error;
		this.arguments = new ArrayList<>();
		if (args.length != this.getError().getArgumentCount()) {
			throw new IllegalArgumentException("Defined error code argument size can not be different than args size.");
		}

		addArgumentLine(args);
	}

	public void addArgumentLine(String... args) {
		if (args.length != this.getError().getArgumentCount()) {
			throw new IllegalArgumentException("Defined error code argument size can not be different than args size.");
		}
		this.getArguments().add(new SingleMessageArgument(args));
	}

	public String getCode() {
		return getError().getCode();
	}

	public String getMessageFormat() {
		return getError().getMessage();
	}

	@Override
	public String getMessage() {
		StringBuilder builder = new StringBuilder(this.getError().getCode()).append(":");
		if (!this.getArguments().isEmpty()) {
			for (SingleMessageArgument singleMessageArgument : getArguments()) {
				builder.append(MessageFormat.format(getError().getMessage(), (Object[]) singleMessageArgument.getInnerArguments())).append("\n");
			}
		} else {
			builder.append(this.getError().getMessage());
		}
		return builder.toString();
	}

	public String getRequestXml() {
		return requestXml;
	}

	public void setRequestXml(String requestXml) {
		this.requestXml = requestXml;
	}

	public String getResponseXml() {
		return responseXml;
	}

	public void setResponseXml(String responseXml) {
		this.responseXml = responseXml;
	}

	public List<SingleMessageArgument> getArguments() {
		return arguments;
	}

	public ErrorCodes getError() {
		return error;
	}

}
