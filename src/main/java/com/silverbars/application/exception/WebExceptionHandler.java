package com.silverbars.application.exception;

import static com.silverbars.application.constants.Constants.ERROR_CODE_INTERNAL_SERVER_ERROR;
import static com.silverbars.application.constants.Constants.ERROR_MSG_INTERNAL_SERVER_ERROR;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Handler for all exceptions generated in the application
 * 
 * @author Ankit
 *
 */
@RestControllerAdvice
public class WebExceptionHandler {

	@ExceptionHandler(value = WebException.class)
	public ResponseEntity<List<ErrorBean>> handleRestException(WebException exception, HttpServletRequest request) {

		return new ResponseEntity<>(exception.getErrors(), exception.getHttpStatus());
	}

	@ExceptionHandler
	public ResponseEntity<List<ErrorBean>> handleException(Exception exception, HttpServletRequest request) {
		exception.printStackTrace();
		List<ErrorBean> errors = new ArrayList<>();
		errors.add(new ErrorBean(ERROR_CODE_INTERNAL_SERVER_ERROR, ERROR_MSG_INTERNAL_SERVER_ERROR));
		return new ResponseEntity<>(errors, HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
