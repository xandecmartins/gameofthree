package com.takeaway.gameofthree.domain;

public class CustomSuccessType {

	private String successMessage;

	public CustomSuccessType(String successMessage) {
		this.successMessage = successMessage;
	}

	public String getErrorMessage() {
		return successMessage;
	}

}