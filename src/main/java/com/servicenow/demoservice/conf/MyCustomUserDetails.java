package com.servicenow.demoservice.conf;

import static org.springframework.ldap.query.LdapQueryBuilder.query;

import java.util.ArrayList;
import java.util.List;

import javax.naming.Name;
import javax.naming.directory.SearchControls;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.ldap.authentication.LdapAuthenticationProvider;
import org.springframework.security.ldap.authentication.LdapAuthenticator;


public class MyCustomUserDetails  implements UserDetailsService{
	@Autowired
    private LdapTemplate ldapTemplate;
	@Value("${user}")
	private String username;
	@Value("${pass}")
	private String password;
	
	//userDeatailService explained in Javabrains 
	Logger log = LoggerFactory.getLogger(MyCustomUserDetails.class);
	
	@Override 
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		log.info("we are entered to the userDetailService  :{}",username);
	
		
    	
		
		
		
		return new User("foo", "foo", new ArrayList<>());
	}
		}
	


