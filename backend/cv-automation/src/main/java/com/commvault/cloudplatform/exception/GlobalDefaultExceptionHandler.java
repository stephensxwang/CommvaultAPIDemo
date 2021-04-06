package com.commvault.cloudplatform.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalDefaultExceptionHandler {

	public static final String DEFAULT_ERROR_VIEW = "error";
	
	@ExceptionHandler(ApplicationException.class)
	@ResponseStatus(value= HttpStatus.PRECONDITION_FAILED)
	public @ResponseBody
    Map<Object, Object> handleApplicationException(HttpServletRequest req, Exception ex) {
		Map<Object, Object> result = new HashMap<Object, Object>();
		result.put("error", ex.getMessage());
		result.put("message", ex.getMessage());
	    return result;
	}
}
