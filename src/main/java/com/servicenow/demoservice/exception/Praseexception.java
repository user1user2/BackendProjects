package com.servicenow.demoservice.exception;

import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.client.HttpClientErrorException;

public class Praseexception extends HttpMessageNotReadableException {
    
	@SuppressWarnings("deprecation")
	public Praseexception(String msg) {
		super(msg);
		// TODO Auto-generated constructor stub
	}

}
