package com.servicenow.demoservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;

public class Custom extends HttpClientErrorException{
	public Custom(HttpStatus statusCode, String statusText) {		
		super(statusCode, statusText);
		
	}

}
