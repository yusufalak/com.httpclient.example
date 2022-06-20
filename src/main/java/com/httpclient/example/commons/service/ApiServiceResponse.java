package com.httpclient.example.commons.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.httpclient.example.commons.exception.SubChannelException;
import com.httpclient.example.commons.httpclient.models.HttpResponseModel;

@Service
public class ApiServiceResponse implements IApiServiceResponse {

	@Autowired
	private Gson gson;

	@Override
	public ServiceResponseModel parse(HttpResponseModel httpResponse) throws SubChannelException {

		String strResponse = httpResponse.getResponse();

		return (ServiceResponseModel) gson.fromJson(strResponse, ServiceResponseModel.class);
	}

}
