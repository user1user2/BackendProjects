 package com.servicenow.demoservice.exception;

public class Attrib extends RuntimeException {
	String message;
	public Attrib(String message){
		this.message=message;
	}

}
