package com.servicenow.demoservice.clintreq;

import java.util.ArrayList;

import org.springframework.core.env.Environment;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.LdapContextSource;
import org.springframework.ldap.filter.EqualsFilter;
import org.springframework.ldap.filter.Filter;
import org.springframework.ldap.support.LdapUtils;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

public class LdapAuthenticationProvider  implements AuthenticationProvider{
	
	    private LdapContextSource contextSource;
	    private LdapTemplate ldapTemplate;
	    private Environment environment;
	    
	    public LdapAuthenticationProvider(Environment environment) {    	
	        this.environment = environment;	        
	    }
	    
	    
	 private void initContext()
	    {  
	        contextSource = new LdapContextSource();
	        contextSource.setUrl(environment.getProperty("ldap.server.url"));
	        contextSource.setAnonymousReadOnly(true);contextSource.setBase("ou=People,dc=maxcrc,dc=com");
	        contextSource.setUserDn("uid={}");
	        contextSource.afterPropertiesSet();

	        ldapTemplate = new LdapTemplate(contextSource);
	    }

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		// TODO Auto-generated method stub
		
         initContext();
		
		//Filter fil = new AndFilter().and("ou","People").;
		
        Filter filter = new EqualsFilter("uid", authentication.getName());
        
        
        System.out.println("This is authenticate method"+authentication.getName()+" and passwod "+authentication.getCredentials().toString()+authentication.getPrincipal().toString());
        
        System.out.println("boolen value"+"ldaputils =="+LdapUtils.emptyLdapName()+ "  filter is =="+filter.encode()+"cred is =="+authentication.getCredentials().toString());

        Boolean authenticate = ldapTemplate.authenticate(LdapUtils.emptyLdapName(), filter.encode(), authentication.getCredentials().toString());
        System.out.println("boolen value"+authenticate);
        if (authenticate)
        {
            UserDetails userDetails = new User(authentication.getName(), authentication.getCredentials().toString()
                    , new ArrayList<>());
            Authentication auth = new UsernamePasswordAuthenticationToken(userDetails,
                    authentication.getCredentials().toString(), new ArrayList<>());
            System.out.println("Exiting from manager");
            return auth;
            
        }
        else
        {
            return authentication;
        }
	}
		

	@Override
	public boolean supports(Class<?> authentication) {
		// TODO Auto-generated method stub
		return authentication.equals(UsernamePasswordAuthenticationToken.class);
	}

}
