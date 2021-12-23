package com.httpclient.example.commons.components;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.RestClientException;
import org.springframework.web.servlet.NoHandlerFoundException;

import com.httpclient.example.commons.channel.RestChannel;
import com.httpclient.example.commons.exception.SubChannelException;
import com.httpclient.example.commons.httpclient.models.HttpResponseModel;
import com.httpclient.example.commons.httpclient.models.ServiceErrorModel;
import com.httpclient.example.commons.utils.ServiceErrorBuilder;

@ControllerAdvice
@ConditionalOnBean(RestChannel.class)
public class ExceptionHandlerAdvice {

	private final Logger LOG = LoggerFactory.getLogger(this.getClass());

	@ExceptionHandler(SubChannelException.class)
	public ResponseEntity<HttpResponseModel> handleSubChannelException(SubChannelException e,
			HttpServletRequest httpRequest) {

		LOG.error("handleSubChannelException", e);
		HttpResponseModel error = createBaseResponseFromException(e);
		return new ResponseEntity<>(error, HttpStatus.ACCEPTED); // Accepted will be retuned default in order to consume
																	// http response entity without catching
																	// exception on client side.
	}

	@ExceptionHandler({ Exception.class, RestClientException.class })
	public ResponseEntity<HttpResponseModel> handleBaseException(Exception e, HttpServletRequest httpRequest) {
		LOG.error("handleBaseException", e);
		HttpResponseModel baseResponseModel = createBaseResponseFromException(e);
		return new ResponseEntity<>(baseResponseModel, HttpStatus.ACCEPTED);
	}

	// Http Exceptions
	@ExceptionHandler({ HttpMediaTypeNotAcceptableException.class, HttpMessageNotReadableException.class,
			NoHandlerFoundException.class })
	public ResponseEntity<HttpResponseModel> handleNotAcceptableException(Exception e, HttpServletRequest httpRequest) {
		LOG.error("handleNotAcceptableException", e);
		HttpResponseModel baseResponseModel = createBaseResponseFromException(e);
		return new ResponseEntity<>(baseResponseModel, HttpStatus.NOT_ACCEPTABLE);
	}

	@ExceptionHandler(HttpRequestMethodNotSupportedException.class)
	public ResponseEntity<HttpResponseModel> handleHttpMethodNotSupportedException(
			HttpRequestMethodNotSupportedException e, HttpServletRequest httpRequest) {
		LOG.error("handleHttpMethodNotSupportedException", e);
		HttpResponseModel baseResponseModel = createBaseResponseFromException(e);
		return new ResponseEntity<>(baseResponseModel, HttpStatus.METHOD_NOT_ALLOWED);
	}

	private HttpResponseModel createBaseResponseFromException(Exception e) {
		ServiceErrorModel serviceErrorModel = ServiceErrorBuilder.instance().fromException(e);
		return createBaseResponseModel(serviceErrorModel);
	}

	private HttpResponseModel createBaseResponseModel(ServiceErrorModel serviceError) {
		HttpResponseModel baseRes = new HttpResponseModel();
		baseRes.setServiceError(serviceError);
		return baseRes;
	}

}
