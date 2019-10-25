package com.silverbars.application.exception;

import java.util.Arrays;
import java.util.List;

import org.springframework.http.HttpStatus;

/**
 * Custom exception class for error scenarios
 * 
 * @author Ankit
 *
 */
public class WebException extends Exception {

	private static final long serialVersionUID = -5728746275936785278L;

	private List<ErrorBean> errors;
	private HttpStatus httpStatus;

	public WebException(List<ErrorBean> errors, HttpStatus status) {
		this.errors = errors;
		this.httpStatus = status;
	}

	public WebException(ErrorBean error, HttpStatus status) {

		this.errors = Arrays.asList(error);
		this.httpStatus = status;
	}

	public List<ErrorBean> getErrors() {
		return errors;
	}

	public void setErrors(List<ErrorBean> errors) {
		this.errors = errors;
	}

	public HttpStatus getHttpStatus() {
		return httpStatus;
	}

	public void setHttpStatus(HttpStatus httpStatus) {
		this.httpStatus = httpStatus;
	}

	@Override
	public String toString() {
		return "WebException [errors=" + errors + ", httpStatus=" + httpStatus + "]";
	}

}
