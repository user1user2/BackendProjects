
 package com.servicenow.demoservice.exception;

public class Errorinfo {
	
	
	private String message;
	private String code;
	private String req;
	
	
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getReq() {
		return req;
	}
	public void setReq(String req) {
		this.req = req;
	}
	public Errorinfo(String message, String code, String req) {
		super();
		this.message = message;
		this.code = code;
		this.req = req;
	}
	
	

}
