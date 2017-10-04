package com.takeaway.gameofthree.domain;

import org.json.JSONObject;

public class CustomErrorType {

	private String errorMessage;

	public CustomErrorType(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	
	public CustomErrorType(JSONObject jsonObject) {
		this.errorMessage = jsonObject.get("errorMessage") + "";
	}

	public String getErrorMessage() {
		return errorMessage;
	}

}