package com.kce.shortener.controller;

import java.util.Date;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import com.kce.shortener.model.ErrorDetails;
import com.kce.shortener.util.RateLimitExceededException;
import com.kce.shortener.util.UrlNotFoundException;

@ControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(Exception.class)
	public ResponseEntity<?> globalExceptionHandler(Exception ex, WebRequest req) {
		ErrorDetails error = new ErrorDetails(new Date(), req.getDescription(false), ex.getMessage());
		return new ResponseEntity<ErrorDetails>(error, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(UrlNotFoundException.class)
	public ResponseEntity<?> urlNotFoundExceptionHandler(Exception ex, WebRequest req) {
		ErrorDetails error = new ErrorDetails(new Date(), req.getDescription(false), ex.getMessage());
		return new ResponseEntity<ErrorDetails>(error, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(RateLimitExceededException.class)
	public ResponseEntity<?> rateLimitExceededExceptionHandler(Exception ex, WebRequest req) {
		ErrorDetails error = new ErrorDetails(new Date(), req.getDescription(false), ex.getMessage());
		return new ResponseEntity<ErrorDetails>(error, HttpStatus.TOO_MANY_REQUESTS);
	}

	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<?> illegalArgumentExceptionHandler(Exception ex, WebRequest req) {
		ErrorDetails error = new ErrorDetails(new Date(), req.getDescription(false), ex.getMessage());
		return new ResponseEntity<ErrorDetails>(error, HttpStatus.BAD_REQUEST);
	}
}
