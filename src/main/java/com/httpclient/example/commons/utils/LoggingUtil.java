package com.httpclient.example.commons.utils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.http.message.BasicHeader;
import org.slf4j.Logger;

import com.httpclient.example.commons.httpclient.models.HttpRequestModel;

public class LoggingUtil {

	private LoggingUtil() {
		throw new AssertionError();
	}

	private static String createRestMessage(HttpRequestModel params, String responseBody, Exception exception) {

		StringBuilder buf = new StringBuilder().append("\n");
		
		buf.append("URL: " + params.getUrl()).append("\n");

		if (params.getAuthorizationToken() != null && !params.getAuthorizationToken().isEmpty()) {
			buf.append("AuthorizationToken: " + params.getAuthorizationToken()).append("\n");
		}

		if (params.getSoapAction() != null && !params.getSoapAction().isEmpty()) {
			buf.append("SoapAction: " + params.getSoapAction()).append("\n");
		}

		List<BasicHeader> additionalHeaders = params.getHeaders();
		if (additionalHeaders != null && !additionalHeaders.isEmpty()) {
			buf.append("Headers:\n");
			String headersStr = additionalHeaders.stream()
					.map(header -> header.getName().concat(":").concat(header.getValue()))
					.collect(Collectors.joining("\n"));
			buf.append(headersStr).append("\n");
		}

		Map<String, String> urlParams = params.getRequestParameters();
		if (urlParams != null && !urlParams.isEmpty()) {
			buf.append("Parameters:\n");
			String paramsStr = urlParams.entrySet()
					.stream()
					.map(entry -> entry.getKey().concat(":").concat(entry.getValue()))
					.collect(Collectors.joining("\n"));
			buf.append(paramsStr).append("\n");
		}

		buf.append("RequestBody:\n").append(params.getRequestBody()).append("\n");

		if (responseBody != null) {
			buf.append("ResponseBody:\n").append(responseBody).append("\n");
		}

		if (exception != null) {
			buf.append("Exception:\n").append(exception.getMessage()).append("\n");
		}

		return buf.toString();
	}

	

	public static void logRequestResponseDebug(Logger log, HttpRequestModel params, String responseBody, Exception exception) {
		log.debug(createRestMessage(params, responseBody, exception));
	}

	public static void logRequestResponseDebug(Logger log, HttpRequestModel params, String responseBody) {
		log.debug(createRestMessage(params, responseBody, null));
	}

	public static void logRequestResponseInfo(Logger log, HttpRequestModel params, String responseBody, Exception exception) {
		log.info(createRestMessage(params, responseBody, exception));
	}

	public static void logRequestResponseInfo(Logger log, HttpRequestModel params, String responseBody) {
		log.info(createRestMessage(params, responseBody, null));
	}

	public static void logRequestResponseWarn(Logger log, HttpRequestModel params, String responseBody, Exception exception) {
		log.warn(createRestMessage(params, responseBody, exception));
	}

	public static void logRequestResponseWarn(Logger log, HttpRequestModel params, String responseBody) {
		log.warn(createRestMessage(params, responseBody, null));
	}

	public static void logRequestResponseError(Logger log, HttpRequestModel params, String responseBody, Exception exception) {
		log.error(createRestMessage(params, responseBody, exception));
	}

	public static void logRequestResponseError(Logger log, HttpRequestModel params, String responseBody) {
		log.error(createRestMessage(params, responseBody, null));
	}
}