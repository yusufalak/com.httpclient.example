package com.httpclient.example.commons.exception;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.httpclient.example.commons.httpclient.models.ServiceErrorModel;

@Service
public class ExceptionService implements IExceptionService {

	private final Logger LOG = LoggerFactory.getLogger(this.getClass());

	@Override
	public void checkErrors(List<ServiceErrorModel> errors) throws SubChannelException {

		if (errors == null || errors.isEmpty())
			return;

		ServiceErrorModel firstError = errors.get(0);

		String errorMessage = firstError.getCode() + "." + firstError.getMessage();

		throw handle(errorMessage);
	}

	@Override
	public SubChannelException handle(String exceptionMessage) {

		if (exceptionMessage.contains("No such host is known")) {
			return new SubChannelException(ErrorCodes.URL_CONNECTION_ERROR);
		}

		return new SubChannelException(ErrorCodes.UNKNOWN_ERROR, exceptionMessage);
	}

	@Override
	public SubChannelException createException(ErrorCodes error) {
		return new SubChannelException(error);
	}

	@Override
	public SubChannelException createException(ErrorCodes error, String... args) {
		if (args == null || args.length <= 0)
			return new SubChannelException(error);

		return new SubChannelException(error, args);
	}

	@Override
	public SubChannelException handleFinalException(Exception exception) {
		LOG.error("::handleFinalException ex:{}", exception);
		return handleInternal(exception);
	}

	private SubChannelException handleInternal(Exception exception) {
		if (exception instanceof SubChannelException) {
			SubChannelException sce = (SubChannelException) exception;

			SubChannelException copy = new SubChannelException(sce.getError());

			if (sce.getArguments() != null && !sce.getArguments().isEmpty()) {
				for (SingleMessageArgument arg : sce.getArguments()) {
					copy.addArgumentLine(arg.getInnerArguments());
				}
			}
			return copy;
		} else {
			return handle(exception.getMessage());
		}
	}
}
