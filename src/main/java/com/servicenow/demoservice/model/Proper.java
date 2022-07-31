package com.servicenow.demoservice.model;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@ConfigurationProperties(prefix = "cust.data.employee")
@Configuration("Pro")
public class Proper {
	


	    private String name;
	    private String pass;
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getPass() {
			return pass;
		}
		public void setPass(String pass) {
			this.pass = pass;
		}
	    

	    //Getters and Setters go here
	}


