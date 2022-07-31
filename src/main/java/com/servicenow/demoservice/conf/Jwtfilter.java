package com.servicenow.demoservice.conf;

import java.io.IOException;
import java.nio.charset.MalformedInputException;

import javax.security.sasl.AuthenticationException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tomcat.util.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.WebProperties.Resources.Chain;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.server.ResponseStatusException;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;






public class Jwtfilter extends OncePerRequestFilter {

	@Autowired
	public MyCustomUserDetails myUser;
	
	@Autowired
	public Jwtutil jwt;
	
	
	Logger log = LoggerFactory.getLogger(Jwtfilter.class);

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		
		HttpServletResponse res = response;
		
		final String authoHeader = request.getHeader("Authorization");  // jwt token
		
		log.info("Header is : {} ",authoHeader);

		String username = null;
		String password = null;
		
		String jt=null;
		
		

        
		if(authoHeader == null ) {
			response.setStatus(401);
			response.getWriter().println("Un authorised access");

		}
		
		
		else if(authoHeader != null && authoHeader.startsWith("Bearer ")) {
			 
				
				
				jt=authoHeader.substring(7);  //collection of jwt 

				try {
					
					
				     username = jwt.extractUsername(jt);
				     log.info("The username from JWT : {}",username);
				     if(SecurityContextHolder.getContext().getAuthentication().isAuthenticated() == true) {
				    	 filterChain.doFilter(request, response);
				     }
				
				      if(username != null  && SecurityContextHolder.getContext().getAuthentication() == null) {  //empy authenticatin and username
					 
				
					   
		                   if(jwt.validateToken(jt, username)) {
		            	   
		            	         UsernamePasswordAuthenticationToken user = new UsernamePasswordAuthenticationToken(username, null);
		            	         user.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
		            	         SecurityContextHolder.getContext().setAuthentication(user);

	                         
						        filterChain.doFilter(request, response);
				
				    }
		              
			      }
				
				}
				catch (SignatureException e) {
					//throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid Token");
					// TODO: handle exception
					response.setStatus(400);
					response.getWriter().print("Token Invalid");
				}catch (MalformedJwtException e) {
					response.setStatus(400);
					response.getWriter().print("Token Format is Invalid");
 				}catch(ExpiredJwtException e) {
 					response.setStatus(401);
 					response.getWriter().print("Token Format is Expired");
 				}
			}
		else {
			
			if(authoHeader != null && authoHeader.startsWith("Basic ") ) {
				 
				 if(request.getServletPath().toString().equalsIgnoreCase("/sn/auth")) {
					 
					 filterChain.doFilter(request, response);
				 }else {
				 response.setStatus(400);
		         response.getWriter().print("Reqired Token");
				 //
				 }
				 
				 }

			
			}
			
			
		}
		
			
		
			
		}
	

		
		
		
		
		
	
	

		
	
	



//if(username != null && SecurityContextHolder.getContext().getAuthentication() == null) {  //empy authenticatin and username
//
//try {
//if(jwt.validateToken1(jt, username)) {
//
//
//UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = 
//
//new UsernamePasswordAuthenticationToken(username, null);
//
//usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
//
//filterChain.doFilter(request, response);
//}
//
//}catch(MalformedInputException e1) {
//throw new MalformedJwtException("Invalid token");
//}catch (SignatureException e) {
//throw new SignatureException("Invalid Token");
//}



//}catch (SignatureException e) {
////throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid Token");
//// TODO: handle exception
//response.setStatus(400);
//response.getWriter().print("Token Invalid");
//}catch (MalformedJwtException e) {
//response.setStatus(400);
//response.getWriter().print("Token Format is Invalid");
//}
//




