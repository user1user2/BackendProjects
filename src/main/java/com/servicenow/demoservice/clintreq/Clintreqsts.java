package com.servicenow.demoservice.clintreq;

import java.io.IOException;
import java.lang.reflect.Array;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;


import javax.websocket.server.PathParam;

import org.apache.http.protocol.HttpContext;
import org.apache.tomcat.util.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.web.servlet.server.Encoding;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.servicenow.demoservice.conf.AuthReq;
import com.servicenow.demoservice.conf.AuthRes;
import com.servicenow.demoservice.conf.Jwtutil;
import com.servicenow.demoservice.conf.MyCustomUserDetails;
import com.servicenow.demoservice.exception.Attrib;
import com.servicenow.demoservice.exception.Custom;
import com.servicenow.demoservice.exception.Praseexception;
import com.servicenow.demoservice.model.Result;

import lombok.SneakyThrows;


@RestController
@RequestMapping("/sn")
public class Clintreqsts  { 
	

	@Autowired
	public Jwtutil jwt;
	
	@Autowired
	public AuthenticationManager authManager;
	
	@Autowired
	public LdapAuthenticationProvider ldapAuthenticationProvider;
	
	@Autowired
	public MyCustomUserDetails myUser;
	 public static Logger log = LoggerFactory.getLogger(Clintreqsts.class);
    @Autowired
    Businesslogic bus;
	//GET-D
    @SneakyThrows
	@GetMapping("/incidents")
	public  ResponseEntity<?> getList( @RequestParam MultiValueMap<String,String> map) {
		//List<Userreq>  out =    Businesslogic.getList();
    	
	   List<Result> response = bus.getList(map);
	   
	   JsonNode list = bus.getListQuery(map);

		return new ResponseEntity<>(list,HttpStatus.OK);
	}
	//GET-D
	@GetMapping("/incident/{number}")
	@SneakyThrows
	public  ResponseEntity<?> getListById(@PathVariable String number) {
		Result response=null;
		Map out = null;
		try {
			response = bus.getListById(number);
			return new ResponseEntity<>(response,HttpStatus.OK);
			}catch(HttpClientErrorException e) {
				if(e.getRawStatusCode() == 404) {
					throw new Custom(HttpStatus.NOT_FOUND,"Record Not Found");
				}
				if(e.getRawStatusCode() == 401) {
					throw new Custom(HttpStatus.UNAUTHORIZED, "Check creentials");
				}
			}
		catch(HttpServerErrorException s) {
				throw new Custom(HttpStatus.INTERNAL_SERVER_ERROR, "Server Exception");
			}
		
			return new ResponseEntity<>(out,HttpStatus.BAD_REQUEST);
		
	}	
	/*throws JSONException, IOException*/
	//POST -D
	@PostMapping("/incident")
	public ResponseEntity<?> res(@RequestBody Result s){
		
		
		if(s.getCaller_id()==null || s.getShort_description() == null) {
			//throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "check Attributes", null);
			throw new Attrib("attributes");
		}
		else {
			try {
		String result1 = bus.createDetatils(s);
		return new ResponseEntity<>(result1,HttpStatus.CREATED);
			}
		catch(HttpClientErrorException e) {
			
			if(e.getRawStatusCode() == 401) {
				//throw new Custom(HttpStatus.UNAUTHORIZED, "Check creentials");
				throw new Custom(HttpStatus.UNAUTHORIZED, "Bad Credentials");
			}
		}
			return null;
		}
	}
	
	//PUT-D
	@PutMapping("/{id}")
	public ResponseEntity<?> uodate(@RequestBody Result re ,@PathVariable String id){
		String response = null;
		try {
		    response = bus.updateDetatils(re,id);
			return new ResponseEntity<>(response,HttpStatus.OK);
			}catch(HttpClientErrorException e) {
				if(e.getRawStatusCode() == 404) {
					throw new Custom(HttpStatus.NOT_FOUND ,"Record Not Found");
				}
				//@SneakyThrows 
				
				if(e.getRawStatusCode() == 401) {
					throw new Custom(HttpStatus.UNAUTHORIZED, "Check creentials");
				}
				
			}catch(HttpServerErrorException s) {
				throw new Custom(HttpStatus.BAD_GATEWAY,"Server Exception");
			}catch (HttpMessageNotReadableException e) {
				throw new Praseexception("Check the JSON Syntax");
				// TODO: handle exception
			}
		
			return new ResponseEntity<>(response,HttpStatus.BAD_GATEWAY);
		
	
	}
	
	//working
	@DeleteMapping("/incidents/{id}")
	public  ResponseEntity<?> removeById(@PathVariable String id){
		String response ="Empty";
		try {
		response = bus.removeById(id);
		return new ResponseEntity<>(response,HttpStatus.OK);
		}catch(HttpClientErrorException e) {
			if(e.getRawStatusCode() == 404) {
				throw new Custom(HttpStatus.NOT_FOUND,"Record Not Found");
			}
			if(e.getRawStatusCode() == 401) {
				throw new Custom(HttpStatus.UNAUTHORIZED, "Check creentials");
			}
		}catch(HttpServerErrorException s) {
			throw new Custom(HttpStatus.INTERNAL_SERVER_ERROR,"Server Exception");
		}
		return new ResponseEntity<>(response,HttpStatus.BAD_GATEWAY);
		//System.out.println(Businesslogic.getList());
		
		
	}
	@GetMapping("/sample")
	public ResponseEntity<?> res(){
		return new ResponseEntity<>("we;l come ", HttpStatus.OK);
	}
	
//	@PostMapping("/token")
//	public ResponseEntity<?> getToken(@RequestBody AuthReq user){
//		String username = null;
//		String password = null;
//		try
//	}
	
	@PostMapping("/auth")
	public ResponseEntity<?> getAuth(@RequestHeader ("Authorization") String s) throws Exception{
		
		String userName= null;
		 String password=null;
		 
		try {
			
					
			log.info("enter auth : "+s.substring("Basic ".length()));
			String cred = s.substring("Basic ".length());
			
			 byte[] encoding = Base64.decodeBase64(cred);
			String det = new String(encoding);
			int seperatorIndex = det.indexOf(':');
			
			userName = det.substring(0,seperatorIndex);
			password = det.substring(seperatorIndex + 1);
			
			

   		 Authentication obj = new UsernamePasswordAuthenticationToken(userName,password);
   		
   		 
   		 Authentication ob =  authManager.authenticate(obj);
   		 SecurityContextHolder.getContext().setAuthentication(ob);
   		
   		
   		 
   		 if(ob.isAuthenticated()) {
   			 
   			String token =  jwt.generateToken1(userName);
   			
   			 return new ResponseEntity<>(new AuthRes(token),HttpStatus.OK);
   		 }
   		 else  {
   			 if(ob.isAuthenticated() == false) {
   				 return new ResponseEntity<>("Check the Credentials ", HttpStatus.UNAUTHORIZED);
   			 }
   			 
   		 }
		
		
		}catch(BadCredentialsException e) {
			throw new Exception("Invalid details",e);
		}

		
	return new ResponseEntity<>("Bad Request", HttpStatus.BAD_REQUEST);
		
	}


    @PostMapping("/ldapauth")
    public ResponseEntity<?> authenticateRequest() throws Exception{
        System.out.println(" " + SecurityContextHolder.getContext().getAuthentication().toString());
        authenticate(SecurityContextHolder.getContext().getAuthentication());

        final String token = jwt.generateToken1(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
        return ResponseEntity.ok(new  AuthRes(token));
    }

    private void authenticate(Authentication auth) throws Exception {
        try {
        	System.out.println(auth.toString());
            ldapAuthenticationProvider.authenticate(auth);
        } catch (DisabledException e) {
            throw new Exception("User disabled", e);
        } catch (BadCredentialsException e) {
            throw new Exception("bad credentials", e);
        }
    }
	
	
	@SneakyThrows(HttpClientErrorException.class)
	@DeleteMapping("/incident/{id}")
	public  ResponseEntity<?> removeById1(@PathVariable String id){
//		String response ="Empty";
		
		
		String response = bus.removeById(id);
		return new ResponseEntity<>(response,HttpStatus.OK);
		
		
		
//		}catch(HttpClientErrorException e) {
//			if(e.getRawStatusCode() == 404) {
//				throw new Custom(HttpStatus.NOT_FOUND,"Record Not Found");
//			}
//			if(e.getRawStatusCode() == 401) {
//				throw new Custom(HttpStatus.UNAUTHORIZED, "Check creentials");
//			}
//		}catch(HttpServerErrorException s) {
//			throw new Unknown(HttpStatus.INTERNAL_SERVER_ERROR,"Server Exception");
//		}
//		return new ResponseEntity<>(response,HttpStatus.BAD_GATEWAY);
//		//System.out.println(Businesslogic.getList());
		
		
	}
	
	
	

    }
	
	
	

    
	
	
	

	
/*
 * @GetMapping("/list1/{number}")
	public  ResponseEntity<?> getListById1(@PathVariable String number){
		//List<Userreq>  out =    Businesslogic.getList();
		String out = null;
		try {
		 out = bus.getListById1(number);
		}catch(Exception e) {
			e.getLocalizedMessage();
		}
		
		return new ResponseEntity<>(out,HttpStatus.OK);
	}*/	
	
	
	



