package com.servicenow.demoservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.servicenow.demoservice.clintreq.Businesslogic;
import com.servicenow.demoservice.conf.Jwtfilter;
import com.servicenow.demoservice.conf.Jwtutil;
import com.servicenow.demoservice.conf.MyCustomUserDetails;

import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
public class DemoserviceApplication {
    
	@Bean
	public PasswordEncoder passwordEncoder() {
		return NoOpPasswordEncoder.getInstance();
	}
	
	@Bean
	public Jwtutil jwt() {
		return new Jwtutil();
	}

	
	
	
	@Bean
	public MyCustomUserDetails myCustomUserDetails() {
		return new MyCustomUserDetails();
	}
	
	@Bean
	public Jwtfilter jwtfil() {
		return new Jwtfilter();
	}
	public static void main(String[] args) {
		SpringApplication.run(DemoserviceApplication.class, args);
	}
//	@Bean
//	public RestTemplate getRest() {
//		return new RestTemplate();
//	}
	

}
