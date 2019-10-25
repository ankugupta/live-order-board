package com.silverbars.application.exception;

/**
 * Model for error details
 * 
 * @author Ankit
 *
 */
public class ErrorBean {

	String code;
	String message;

	public ErrorBean() {

	}

	public ErrorBean(String code, String message) {
		super();
		this.code = code;
		this.message = message;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public String toString() {
		return "ErrorBean [code=" + code + ", message=" + message + "]";
	}

}
