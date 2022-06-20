package com.httpclient.example.commons.service;

import java.io.Serializable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ServiceInfoResponseModel implements Serializable {

	private static final long serialVersionUID = 7002257465837891722L;

	@SerializedName("seed")
	@Expose
	private String seed;

	@SerializedName("results")
	@Expose
	private int results;

	@SerializedName("page")
	@Expose
	private int page;

	@SerializedName("version")
	@Expose
	private String version;

	public String getSeed() {
		return seed;
	}

	public void setSeed(String seed) {
		this.seed = seed;
	}

	public int getResults() {
		return results;
	}

	public void setResults(int results) {
		this.results = results;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

}
