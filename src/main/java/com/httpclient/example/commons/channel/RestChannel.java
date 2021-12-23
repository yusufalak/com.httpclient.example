package com.httpclient.example.commons.channel;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.httpclient.example.commons.exception.SubChannelException;
import com.httpclient.example.commons.httpclient.IHttpClientService;
import com.httpclient.example.commons.httpclient.models.HttpRequestModel;
import com.httpclient.example.commons.httpclient.models.HttpResponseModel;

@RestController
@RequestMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
public class RestChannel {

	@Autowired
	@Qualifier("commons.blockingHttpClientService1")
	private IHttpClientService blockingHttpClientService;
	
	@Autowired
	@Qualifier("commons.httpClientService1")
	private IHttpClientService httpClientService;

	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	@GetMapping(value = "/stat", consumes = MediaType.ALL_VALUE, produces = MediaType.TEXT_PLAIN_VALUE)
	public String stat() {
		return "OK";
	}

	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	@PostMapping(value = "/httpClient")
	public HttpResponseModel searchFlights(@RequestBody HttpRequestModel httpRequest) throws SubChannelException {
		return httpClientService.execute(httpRequest);
	}

	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	@PostMapping(value = "/blockingHttpClient")
	public HttpResponseModel blockingHttpClient(@RequestBody HttpRequestModel httpRequest) throws SubChannelException {
		return blockingHttpClientService.execute(httpRequest);
	}
}
