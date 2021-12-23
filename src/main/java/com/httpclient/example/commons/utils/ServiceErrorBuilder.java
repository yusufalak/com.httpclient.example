package com.httpclient.example.commons.utils;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.ConnectException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.web.client.RestClientException;

import com.httpclient.example.commons.exception.ErrorCodes;
import com.httpclient.example.commons.exception.SingleMessageArgument;
import com.httpclient.example.commons.exception.SubChannelException;
import com.httpclient.example.commons.httpclient.models.ServiceErrorModel;

public class ServiceErrorBuilder {
	private static final ServiceErrorBuilder INSTANCE = new ServiceErrorBuilder();

	public static ServiceErrorBuilder instance() {
		return INSTANCE;
	}

	public ServiceErrorModel fromException(Exception ex) {

		if (ex instanceof ConnectException || ex instanceof RestClientException) {
			ErrorCodes providerConnectionError = ErrorCodes.URL_CONNECTION_ERROR;

			com.httpclient.example.commons.httpclient.models.ServiceErrorModel svc = new ServiceErrorModel();
			svc.setArguments(toArguments(ex.getMessage()));
			svc.setCode(providerConnectionError.getCode());
			svc.setMessage(providerConnectionError.getMessage());
			return svc;
		}

		if (ex instanceof SubChannelException) {
			SubChannelException sce = (SubChannelException) ex;
			com.httpclient.example.commons.httpclient.models.ServiceErrorModel svc = new ServiceErrorModel();
			svc.setArguments(sce.getArguments());
			svc.setCode(sce.getCode());
			svc.setMessage(sce.getMessageFormat());
			return svc;
		}

		String message = getStackTrace(ex);

		com.httpclient.example.commons.httpclient.models.ServiceErrorModel svc = new ServiceErrorModel();
		svc.setArguments(toArguments(message));
		svc.setCode(ErrorCodes.UNKNOWN_ERROR.getCode());
		svc.setMessage(ErrorCodes.UNKNOWN_ERROR.getMessage());
		return svc;
	}

	private List<SingleMessageArgument> toArguments(String... args) {
		if (args == null || args.length == 0) {
			return null;
		}

		return new ArrayList<>(Arrays.asList(new SingleMessageArgument(args)));
	}

	public String getStackTrace(final Throwable throwable) {
		final StringWriter sw = new StringWriter();
		final PrintWriter pw = new PrintWriter(sw, true);
		throwable.printStackTrace(pw);
		return sw.getBuffer().toString();
	}
}
