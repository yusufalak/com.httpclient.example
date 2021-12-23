package com.httpclient.example.commons.httpclient;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.protocol.RequestAcceptEncoding;
import org.apache.http.client.protocol.RequestClientConnControl;
import org.apache.http.client.protocol.RequestDefaultHeaders;
import org.apache.http.client.protocol.ResponseContentEncoding;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HttpProcessor;
import org.apache.http.protocol.HttpProcessorBuilder;
import org.apache.http.protocol.RequestConnControl;
import org.apache.http.protocol.RequestContent;
import org.apache.http.protocol.RequestExpectContinue;
import org.apache.http.protocol.RequestTargetHost;
import org.apache.http.protocol.RequestUserAgent;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.http.HttpMethod;

import com.httpclient.example.commons.exception.ErrorCodes;
import com.httpclient.example.commons.exception.IExceptionService;
import com.httpclient.example.commons.exception.SubChannelException;
import com.httpclient.example.commons.httpclient.models.GzipEntity;
import com.httpclient.example.commons.httpclient.models.HttpRequestModel;
import com.httpclient.example.commons.httpclient.models.HttpResponseModel;
import com.httpclient.example.commons.utils.LoggingUtil;

public class HttpClientService implements InitializingBean, IHttpClientService {

	private static final int CONNECTION_EVICTION_TIMEOUT = 500;
	private static final int MAX_TTL = 60_000;

	protected final Logger LOG = LoggerFactory.getLogger(this.getClass());

	protected static final int NUMBER_OF_TRIES = 3;
	protected static final String AUTHORIZATION = "Authorization";
	protected static final String HEADER_SOAP_ACTION = "SOAPAction";

	protected HttpClient client;
	private String userAgent;
	private boolean useExpectContinue;
	private List<BasicHeader> defaultHeaders;
	private boolean bypassSSLChecks;
	private int requestTimeout;
	private int connectionTimeout;
	private int socketTimeout;
	private int defaultMaxPerRoute;
	private int maxTotal;
	private IExceptionService exceptionService;

	@Override
	public void afterPropertiesSet() throws Exception {

		HttpProcessor processor = createHttpProcessor();

		SSLConnectionSocketFactory socketFactory = createSocketFactory();

		PoolingHttpClientConnectionManager connectionManager = createConnectionManager(socketFactory);

		RequestConfig.Builder requestBuilder = RequestConfig.custom().setConnectTimeout(connectionTimeout)
				.setSocketTimeout(socketTimeout).setConnectionRequestTimeout(requestTimeout);

		this.client = HttpClientBuilder.create().setDefaultRequestConfig(requestBuilder.build())
				.evictIdleConnections(CONNECTION_EVICTION_TIMEOUT, TimeUnit.MILLISECONDS)
				.setConnectionTimeToLive(MAX_TTL, TimeUnit.MILLISECONDS).setConnectionManager(connectionManager)
				.setHttpProcessor(processor).build();
	}

	private PoolingHttpClientConnectionManager createConnectionManager(SSLConnectionSocketFactory socketFactory) {
		Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
				.register("http", PlainConnectionSocketFactory.getSocketFactory()).register("https", socketFactory)
				.build();
		PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
		cm.setDefaultMaxPerRoute(defaultMaxPerRoute);
		cm.setMaxTotal(maxTotal);
		cm.setValidateAfterInactivity(MAX_TTL);
		cm.closeIdleConnections(CONNECTION_EVICTION_TIMEOUT, TimeUnit.MILLISECONDS);
		return cm;
	}

	private SSLConnectionSocketFactory createSocketFactory() throws NoSuchAlgorithmException, KeyManagementException {
		SSLConnectionSocketFactory socketFactory = null;

		if (bypassSSLChecks) {

			SSLContext sslContext = SSLContext.getInstance("TLS");

			TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {

				public java.security.cert.X509Certificate[] getAcceptedIssuers() {
					return new java.security.cert.X509Certificate[0];
				}

				public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType) {
				}

				public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType) {
				}
			} };

			sslContext.init(null, trustAllCerts, new java.security.SecureRandom());

			socketFactory = new SSLConnectionSocketFactory(sslContext, (HostnameVerifier) null);

		} else {
			socketFactory = SSLConnectionSocketFactory.getSocketFactory();
		}
		return socketFactory;
	}

	private HttpProcessor createHttpProcessor() {
		HttpProcessorBuilder builder = HttpProcessorBuilder.create();

		if (userAgent != null)
			builder.add(new RequestUserAgent(userAgent));
		if (useExpectContinue)
			builder.add(new RequestExpectContinue(true));

		builder.add(new RequestAcceptEncoding());
		builder.add(new RequestConnControl());
		builder.add(new RequestTargetHost());
		builder.add(new ResponseContentEncoding());

		builder.add(new RequestContent());
		builder.add(new RequestClientConnControl());

		if (defaultHeaders != null && !defaultHeaders.isEmpty()) {
			List<Header> headers = new ArrayList<>(defaultHeaders.size());
			for (Header header : defaultHeaders) {
				headers.add(header);
			}
			builder.add(new RequestDefaultHeaders(headers));
		}

		return builder.build();
	}

	@Override
	public HttpResponseModel execute(HttpRequestModel params) throws SubChannelException {

		try {
			HttpResponse response = executeInner(params);
			HttpEntity entity = response.getEntity();

			String entityResponse = parseEntity(entity);

			Header[] allHeaders = response.getAllHeaders();
			var responseHeaders = new ArrayList<>(Arrays.asList(allHeaders));
			HttpResponseModel rsp = new HttpResponseModel();
			rsp.setHeaders(responseHeaders.stream().map(h -> new BasicHeader(h.getName(), h.getValue()))
					.collect(Collectors.toList()));
			rsp.setResponse(entityResponse);
			rsp.setServiceError(null);
			rsp.setStatusCode(response.getStatusLine().getStatusCode());

			LoggingUtil.logRequestResponseDebug(LOG, params, entityResponse);
			return rsp;
		} catch (Exception e) {
			LoggingUtil.logRequestResponseError(LOG, params, e.getMessage(), e);
			throw exceptionService.handleFinalException(e);
		}
	}

	private String parseEntity(HttpEntity entity) throws SubChannelException {
		try {
			return EntityUtils.toString(entity);
		} catch (ParseException | IOException e) {
			handleEntityParseException(e);
		}
		return null;
	}

	protected HttpResponse executeInner(HttpRequestModel params) throws SubChannelException {

		HttpRequestBase request = prepareRequest(params);
		String errorMessage = null;
		for (int i = 1; i <= NUMBER_OF_TRIES; i++) {
			try {
				return client.execute(request);
			} catch (Exception e) {
				errorMessage = e.getMessage();
				LOG.error(":executeInner iteration:{}, postData:{}", i, params.getRequestBody(), e);
				if (!params.isRetryOnError()) {
					break;
				}
			}
		}
		throw new SubChannelException(ErrorCodes.URL_CONNECTION_ERROR, errorMessage);

	}

	protected HttpRequestBase prepareRequest(HttpRequestModel params) throws SubChannelException {

		String postData = params.getRequestBody();
		boolean isUseGzip = params.isUseGzip();
		String authToken = params.getAuthorizationToken();
		String soapAction = params.getSoapAction();
		Map<String, String> urlParams = params.getRequestParameters();

		URI uri = constructURI(params, urlParams);
		HttpRequestBase request = createHttpRequest(params, postData, isUseGzip, soapAction, uri);

		if (authToken != null && !authToken.isEmpty()) {
			BasicHeader header = new BasicHeader(AUTHORIZATION, authToken);
			request.addHeader(header);
		}

		if (params.getHeaders() != null && !params.getHeaders().isEmpty()) {
			for (BasicHeader basicHeader : params.getHeaders()) {
				request.addHeader(basicHeader);
			}
		}

		return request;
	}

	private HttpRequestBase createHttpRequest(HttpRequestModel params, String postData, boolean isUseGzip,
			String soapAction, URI uri) throws SubChannelException {

		HttpMethod httpMethod = findHttpMethod(params.getMethod());

		if (httpMethod == HttpMethod.GET) {
			return new HttpGet(uri);
		}

		HttpRequestBase request = new HttpPost(uri);
		if (soapAction != null)
			request.addHeader(HEADER_SOAP_ACTION, soapAction);

		HttpEntity strent = null;
		if (isUseGzip) {
			strent = new GzipEntity(postData, params.getContentType());
		} else {
			strent = new StringEntity(postData == null ? "" : postData, params.getCharset());
			if (params.getContentType() != null)
				((StringEntity) strent).setContentType(params.getContentType());
		}
		((HttpPost) request).setEntity(strent);
		return request;
	}

	private HttpMethod findHttpMethod(String method) throws SubChannelException {
		if (method == null || method.isEmpty())
			throw new SubChannelException(ErrorCodes.MISSING_MANDATORY_FIELD, "HttpMethod");

		method = method.trim().toLowerCase(Locale.ENGLISH);

		if ("get".equals(method))
			return HttpMethod.GET;

		if ("post".equals(method))
			return HttpMethod.POST;

		if ("delete".equals(method))
			return HttpMethod.DELETE;

		if ("head".equals(method))
			return HttpMethod.HEAD;

		if ("options".equals(method))
			return HttpMethod.OPTIONS;

		if ("patch".equals(method))
			return HttpMethod.PATCH;

		if ("put".equals(method))
			return HttpMethod.PUT;

		return HttpMethod.TRACE;
	}

	private URI constructURI(HttpRequestModel params, Map<String, String> urlParams) throws SubChannelException {
		URI uri = null;

		try {
			URIBuilder builder = new URIBuilder(params.getUrl());
			if (urlParams != null) {
				Iterator<Entry<String, String>> iter = urlParams.entrySet().iterator();
				while (iter.hasNext()) {
					Entry<String, String> next = iter.next();
					builder.addParameter(next.getKey(), next.getValue());
				}
			}
			uri = builder.build();
		} catch (URISyntaxException e) {
			throw new SubChannelException(ErrorCodes.UNKNOWN_ERROR, e.getMessage());
		}
		return uri;
	}

	private void handleEntityParseException(Exception e) throws SubChannelException {
		LOG.error("::handleEntityParseException", e);
		throw new SubChannelException(ErrorCodes.EMPTY_OR_INVALID_DATA_RECEIVED, e.getMessage());
	}

	public HttpClient getClient() {
		return client;
	}

	public void setClient(HttpClient client) {
		this.client = client;
	}

	public String getUserAgent() {
		return userAgent;
	}

	public void setUserAgent(String userAgent) {
		this.userAgent = userAgent;
	}

	public boolean isUseExpectContinue() {
		return useExpectContinue;
	}

	public void setUseExpectContinue(boolean useExpectContinue) {
		this.useExpectContinue = useExpectContinue;
	}

	public List<BasicHeader> getDefaultHeaders() {
		return defaultHeaders;
	}

	public void setDefaultHeaders(List<BasicHeader> defaultHeaders) {
		this.defaultHeaders = defaultHeaders;
	}

	public boolean isBypassSSLChecks() {
		return bypassSSLChecks;
	}

	public void setBypassSSLChecks(boolean bypassSSLChecks) {
		this.bypassSSLChecks = bypassSSLChecks;
	}

	public int getRequestTimeout() {
		return requestTimeout;
	}

	public void setRequestTimeout(int requestTimeout) {
		this.requestTimeout = requestTimeout;
	}

	public int getConnectionTimeout() {
		return connectionTimeout;
	}

	public void setConnectionTimeout(int connectionTimeout) {
		this.connectionTimeout = connectionTimeout;
	}

	public int getSocketTimeout() {
		return socketTimeout;
	}

	public void setSocketTimeout(int socketTimeout) {
		this.socketTimeout = socketTimeout;
	}

	public int getDefaultMaxPerRoute() {
		return defaultMaxPerRoute;
	}

	public void setDefaultMaxPerRoute(int defaultMaxPerRoute) {
		this.defaultMaxPerRoute = defaultMaxPerRoute;
	}

	public int getMaxTotal() {
		return maxTotal;
	}

	public void setMaxTotal(int maxTotal) {
		this.maxTotal = maxTotal;
	}

	public IExceptionService getExceptionService() {
		return exceptionService;
	}

	public void setExceptionService(IExceptionService exceptionService) {
		this.exceptionService = exceptionService;
	}

}