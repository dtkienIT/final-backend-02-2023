package com.example.demo.constant;

public enum ResponseCode {
	SUCCESS(200, "OK"), 
	NOT_FOUND(404, "Not found"), 
	NO_PARAM(6001, "No param"), 
	NO_CONTENT(2004, "No content"),
	INTERNAL_SERVER_ERROR(5000, "Internal server error"),
	DATA_ALREADY_EXISTS(2023, "Data already exists"),
	Fail(4006, "Login Fail"),
	USER_NOT_FOUND(4005, "User not found"),
	CART_LINE_ITEMS_NOT_FOUND(4007,"CART LINE ITEMS NOT FOUND"), 
	INVALID_DATE_FORMAT(4008,"INVALID_DATE_FORMAT"), 
	ADDRESS_NOT_FOUND(4009,"ADDRESS_NOT_FOUND"), 
	CART_NOT_FOUND(4010,"CART_NOT_FOUND");

	private int code;
	private String message;

	public int getCode() {
		return code;
	}

	public String getMessage() {
		return message;
	}

	private ResponseCode(int code, String message) {
		this.code = code;
		this.message = message;
	}
}
