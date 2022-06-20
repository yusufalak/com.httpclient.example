package com.httpclient.example.commons.service;

import java.io.Serializable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ServiceResponseModel implements Serializable {

	private static final long serialVersionUID = 6208389726314975915L;

	@SerializedName("info")
	@Expose
	private ServiceInfoResponseModel info;

	public ServiceInfoResponseModel getInfo() {
		return info;
	}

	public void setInfo(ServiceInfoResponseModel info) {
		this.info = info;
	}
}
