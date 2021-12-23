package com.httpclient.example.commons.httpclient.models;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.apache.http.message.BasicHeader;

public class HttpRequestModel implements Serializable {

	private static final long serialVersionUID = 9118781920705588667L;

	private String url;
	private String requestBody;
	private String charset;
	private String contentType;
	private boolean retryOnError;
	private int retryCount;
	private String method;
	private boolean UseGzip;
	private boolean getBodyElement;
	private String authorizationToken;
	private String soapAction;
	private Map<String, String> requestParameters;
	private Map<String, String> formData;
	private List<BasicHeader> headers;
	private String serviceName;

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getRequestBody() {
		return requestBody;
	}

	public void setRequestBody(String requestBody) {
		this.requestBody = requestBody;
	}

	public String getCharset() {
		return charset;
	}

	public void setCharset(String charset) {
		this.charset = charset;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public boolean isRetryOnError() {
		return retryOnError;
	}

	public void setRetryOnError(boolean retryOnError) {
		this.retryOnError = retryOnError;
	}

	public int getRetryCount() {
		return retryCount;
	}

	public void setRetryCount(int retryCount) {
		this.retryCount = retryCount;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public boolean isUseGzip() {
		return UseGzip;
	}

	public void setUseGzip(boolean useGzip) {
		UseGzip = useGzip;
	}

	public boolean isGetBodyElement() {
		return getBodyElement;
	}

	public void setGetBodyElement(boolean getBodyElement) {
		this.getBodyElement = getBodyElement;
	}

	public String getAuthorizationToken() {
		return authorizationToken;
	}

	public void setAuthorizationToken(String authorizationToken) {
		this.authorizationToken = authorizationToken;
	}

	public String getSoapAction() {
		return soapAction;
	}

	public void setSoapAction(String soapAction) {
		this.soapAction = soapAction;
	}

	public Map<String, String> getRequestParameters() {
		return requestParameters;
	}

	public void setRequestParameters(Map<String, String> requestParameters) {
		this.requestParameters = requestParameters;
	}

	public Map<String, String> getFormData() {
		return formData;
	}

	public void setFormData(Map<String, String> formData) {
		this.formData = formData;
	}

	public List<BasicHeader> getHeaders() {
		return headers;
	}

	public void setHeaders(List<BasicHeader> headers) {
		this.headers = headers;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

}
