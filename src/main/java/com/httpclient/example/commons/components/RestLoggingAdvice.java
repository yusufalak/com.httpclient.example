package com.httpclient.example.commons.components;

import java.lang.reflect.Type;

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdviceAdapter;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import com.httpclient.example.commons.autoconfigure.ChannelLoggingConfiguration;
import com.httpclient.example.commons.channel.RestChannel;
import com.httpclient.example.commons.httpclient.models.HttpRequestModel;
import com.httpclient.example.commons.httpclient.models.HttpResponseModel;

@ControllerAdvice
@ConditionalOnBean({ RestChannel.class, ChannelLoggingConfiguration.class })
public class RestLoggingAdvice extends RequestBodyAdviceAdapter implements ResponseBodyAdvice<HttpResponseModel> {

	@Override
	public Object afterBodyRead(Object body, HttpInputMessage inputMessage, MethodParameter parameter, Type targetType,
			Class<? extends HttpMessageConverter<?>> converterType) {

		// TODO
		return body;
	}

	@Override
	public HttpResponseModel beforeBodyWrite(HttpResponseModel body, MethodParameter returnType,
			MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType,
			ServerHttpRequest request, ServerHttpResponse response) {
		// TODO
		return body;
	}

	@Override
	public boolean supports(MethodParameter methodParameter, Type targetType,
			Class<? extends HttpMessageConverter<?>> converterType) {
		return targetType.getClass().isAssignableFrom(HttpRequestModel.class);
	}

	@Override
	public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
		return returnType.getClass().isAssignableFrom(HttpResponseModel.class);
	}

}
