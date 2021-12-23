package com.httpclient.example.commons.components;

import java.lang.reflect.Type;
import java.time.LocalDateTime;

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdviceAdapter;

import com.httpclient.example.commons.channel.RestChannel;

@ControllerAdvice
@ConditionalOnBean(RestChannel.class)
@Order(1)
public class RequestBodyAdvice extends RequestBodyAdviceAdapter {

	@Override
	public Object afterBodyRead(Object body, HttpInputMessage inputMessage, MethodParameter parameter, Type targetType,
			Class<? extends HttpMessageConverter<?>> converterType) {

		RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
		if (requestAttributes != null) {
			requestAttributes.setAttribute(RequestConstants.REQUEST_BODY_ATTRIBUTE_NAME, body, RequestAttributes.SCOPE_REQUEST);
			requestAttributes.setAttribute(RequestConstants.REQUEST_DATE_ATTR_NAME, LocalDateTime.now(), RequestAttributes.SCOPE_REQUEST);
		}

		return body;
	}

	@Override
	public boolean supports(MethodParameter methodParameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
		return true;
	}
}
