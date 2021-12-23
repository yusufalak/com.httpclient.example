package com.httpclient.example.commons.exception;

import java.io.Serializable;

public class ErrorCodes implements Serializable {

	private static final long serialVersionUID = -8779570337856076422L;

	private final String code;
	private final String message;
	private final int argumentCount;

	private ErrorCodes(String code, String message) {
		this.code = code;
		this.message = message;
		this.argumentCount = 0;
	}

	private ErrorCodes(String code, String message, int argumentCount) {
		this.code = code;
		this.message = message;
		this.argumentCount = argumentCount;
	}

	public String getCode() {
		return this.code;
	}

	/**
	 * Error explanation message in English. <br/>
	 */
	public String getMessage() {
		return this.message;
	}

	/**
	 * Argument size of error explanation message. <br/>
	 */
	public int getArgumentCount() {
		return this.argumentCount;
	}

	/**
	 * Only timeout problems will be resulted with this code. <br/>
	 */
	public static final ErrorCodes TIMEOUT_EXPIRED = new ErrorCodes("1000",
			"The request was not completed within the expected time interval. Please try again");

	/**
	 * Connection reset, no route to host etc. errors will be resulted with this code. <br/>
	 * 
	 * @arguments <b>error message</b> Free text error message from http connection exception. <br/>
	 */
	public static final ErrorCodes URL_CONNECTION_ERROR = new ErrorCodes("1001", "We are not able to access provider web service endpoint. Error message:{0}",
			1);

	/**
	 * <br/>
	 * 
	 * @arguments <b>missing field</b>. <br/>
	 */
	public static final ErrorCodes MISSING_MANDATORY_FIELD = new ErrorCodes("4000", "Missing mandatory field : {0}.", 1);

	/**
	 * Explains a web service response payload root value is null. <br/>
	 * <b>cause</b> Free text error reason from subchannel implementation. <br/>
	 */
	public static final ErrorCodes SERVICE_RESPONSE_NULL = new ErrorCodes("5000",
			"Null or empty web service response received from provider web service. Cause:{0}", 1);

	/**
	 * Explains a web service response payload contains invalid or null (empty) data. <br/>
	 * <b>cause</b> Free text error reason from subchannel implementation. <br/>
	 */
	public static final ErrorCodes EMPTY_OR_INVALID_DATA_RECEIVED = new ErrorCodes("5001", "Null or empty data received from provider web service. Cause:{0}",
			1);

	/**
	 * Used for unidentified errors (or unmapped) from provider. <br/>
	 * <b>Error message</b> Free text error reason from subchannel implementation. <br/>
	 */
	public static final ErrorCodes UNKNOWN_ERROR = new ErrorCodes("9999", "Unmapped error has been occurred. Error message:{0} ", 1);

}
