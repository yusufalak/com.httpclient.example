package com.httpclient.example.commons.httpclient;

import com.httpclient.example.commons.exception.SubChannelException;
import com.httpclient.example.commons.httpclient.models.HttpRequestModel;
import com.httpclient.example.commons.httpclient.models.HttpResponseModel;

public interface IHttpClientService {

	HttpResponseModel execute(HttpRequestModel params) throws SubChannelException;

}
