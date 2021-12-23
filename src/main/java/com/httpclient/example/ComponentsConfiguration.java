package com.httpclient.example;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.http.message.BasicHeader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import com.httpclient.example.commons.exception.IExceptionService;
import com.httpclient.example.commons.httpclient.HttpClientService;
import com.httpclient.example.commons.httpclient.IHttpClientService;

@Configuration
public class ComponentsConfiguration {

	@Autowired
	private IExceptionService exceptionService;

	@Value("${http.user-agent:#{null}}")
	private String userAgent;

	@Value("${http.use-expect-continue:false}")
	private boolean useExpectContinue;

	@Value("${http.default-headers:#{null}}")
	private Map<String, String> defaultHeaders;

	@Value("${http.bypass-ssl-checks:false}")
	private boolean bypassSSLChecks;

	@Value("${http.default-max-per-route:100}")
	private int defaultMaxPerRoute;

	@Value("${http.max-total:200}")
	private int maxTotal;

	@Value("${http.request-timeout:3000}") // 3 secs.
	private int requestTimeout;

	@Value("${http.connection-timeout:10000}") // 10 secs.
	private int connectionTimeout;

	@Value("${http.socket-timeout:20000}") // 20 sec.
	private int socketTimeout;

	@Value("${http.blocking.request-timeout:15000}") // 15 secs.
	private int blockingRequestTimeout;

	@Value("${http.blocking.connection-timeout:300000}") // 5 mins.
	private int blockingConnectionTimeout;

	@Value("${http.blocking.socket-timeout:300000}") // 5 mins.
	private int blockingSocketTimeout;

	@Bean(name = "commons.httpClientService1")
	@Primary
	public IHttpClientService httpClientService() throws Exception {

		var httpClientService = createHttpClient();

		httpClientService.afterPropertiesSet();
		return httpClientService;
	}

	@Bean(name = "commons.blockingHttpClientService1")
	public IHttpClientService blockingHttpClientService() throws Exception {

		var httpClientService = createHttpClient();

		httpClientService.setRequestTimeout(blockingRequestTimeout);
		httpClientService.setConnectionTimeout(blockingConnectionTimeout);
		httpClientService.setSocketTimeout(blockingSocketTimeout);

		httpClientService.afterPropertiesSet();
		return httpClientService;
	}

	private HttpClientService createHttpClient() {
		return createDefaultHttpClient();
	}

	private HttpClientService createDefaultHttpClient() {

		HttpClientService httpClient = new HttpClientService();

		httpClient.setBypassSSLChecks(bypassSSLChecks);
		httpClient.setClient(null);
		httpClient.setConnectionTimeout(connectionTimeout);
		httpClient.setRequestTimeout(requestTimeout);
		httpClient.setSocketTimeout(socketTimeout);
		httpClient.setDefaultMaxPerRoute(defaultMaxPerRoute);
		httpClient.setMaxTotal(maxTotal);
		httpClient.setUseExpectContinue(useExpectContinue);
		httpClient.setUserAgent(userAgent);
		httpClient.setExceptionService(exceptionService);

		if (defaultHeaders != null && !defaultHeaders.isEmpty()) {
			List<BasicHeader> headers = defaultHeaders.entrySet().stream()
					.map(e -> new BasicHeader(e.getKey(), e.getValue())).collect(Collectors.toList());
			httpClient.setDefaultHeaders(headers);
		}

		return httpClient;
	}

}
