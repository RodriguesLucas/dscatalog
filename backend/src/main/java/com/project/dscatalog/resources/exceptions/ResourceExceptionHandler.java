package com.project.dscatalog.resources.exceptions;

import java.time.Instant;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.project.dscatalog.services.exceptions.EntityNotFoundException;

@ControllerAdvice
public class ResourceExceptionHandler {
	
	@ExceptionHandler(EntityNotFoundException.class)
	public ResponseEntity<StandarError> entityNotFound(EntityNotFoundException entityNotFoundException,
			HttpServletRequest httpServletRequestF) {
		StandarError standarError = new StandarError();
		standarError.setTimestamp(Instant.now());
		standarError.setStatus(HttpStatus.NOT_FOUND.value());
		standarError.setError("Resource not found");
		standarError.setMessage(entityNotFoundException.getMessage());
		standarError.setPath(httpServletRequestF.getRequestURI());
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(standarError);
	}
}
