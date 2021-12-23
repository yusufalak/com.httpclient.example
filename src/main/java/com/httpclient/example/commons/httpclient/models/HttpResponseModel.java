package com.httpclient.example.commons.httpclient.models;

import java.io.Serializable;
import java.util.List;

import org.apache.http.message.BasicHeader;

public class HttpResponseModel implements Serializable {

	private static final long serialVersionUID = -5894369188203152925L;

	private String response;
	private List<BasicHeader> headers;
	private int statusCode;
	private ServiceErrorModel serviceError;

	public String getResponse() {
		return response;
	}

	public void setResponse(String response) {
		this.response = response;
	}

	public List<BasicHeader> getHeaders() {
		return headers;
	}

	public void setHeaders(List<BasicHeader> headers) {
		this.headers = headers;
	}

	public int getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}

	public ServiceErrorModel getServiceError() {
		return serviceError;
	}

	public void setServiceError(ServiceErrorModel serviceError) {
		this.serviceError = serviceError;
	}

}
