package com.httpclient.example.commons.exception;

import java.util.List;

import com.httpclient.example.commons.httpclient.models.ServiceErrorModel;

public interface IExceptionService {

	void checkErrors(List<ServiceErrorModel> errors) throws SubChannelException;

	SubChannelException handle(String exceptionMessage);

	SubChannelException createException(ErrorCodes error);

	SubChannelException createException(ErrorCodes error, String... args);

	SubChannelException handleFinalException(Exception exception);

}
