package com.httpclient.example.commons.service;

import com.httpclient.example.commons.exception.SubChannelException;
import com.httpclient.example.commons.httpclient.models.HttpResponseModel;

public interface IApiServiceResponse {

	ServiceResponseModel parse(HttpResponseModel httpResponse) throws SubChannelException;

}
