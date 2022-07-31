package com.servicenow.demoservice.clintreq;


import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.SignedObject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.catalina.connector.Response;
import org.apache.tomcat.util.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.TreeTraversingParser;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.google.gson.Gson;
import com.servicenow.demoservice.model.Attributes;
import com.servicenow.demoservice.model.Proper;
import com.servicenow.demoservice.model.Result;


@Service
public class Businesslogic {
	  
	  

	static RestTemplate restT;
	
	@Autowired
	public Businesslogic(RestTemplate restT) {
		this.restT=restT;
		
		// TODO Auto-generated constructor stub
	}
	
	@Autowired
	private  Proper Pro;
	
	@Value("${api.username}")
	 public  String Username;
	
	
	@Value("${api.password}")
	 public  String Password;
	
	
	@Value("${api.url}")
	public String URL;
	
	
	
	
	
	public static Logger log = LoggerFactory.getLogger(Businesslogic.class);

	//GET
	public  List<Result> getList(MultiValueMap<String, String> map) throws JSONException, IOException {
		String plainCreds = Username+":"+Password;
		String auth = Base64.encodeBase64String(plainCreds.getBytes());
		
		HttpHeaders headers = new HttpHeaders();		
		
		headers.add("Authorization", "Basic " + auth);
		
		HttpEntity<String> httpEntity = new HttpEntity<>(headers);	
		
		log.info("We are Entering to the rest template");
	    RestTemplate restTe = new RestTemplate();
	    
	    
	    UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(URL).queryParams(map);
		log.info("Url is : "+builder);

		
		
		ResponseEntity<JsonNode> response = restTe.exchange(builder.build().toUriString(), HttpMethod.GET, httpEntity, JsonNode.class);
		log.info("The JSON node : "+response.getBody());
		
		
		
		
		JsonNode result = response.getBody().get("result");
		
		 
		List<Result> list = new ArrayList<Result>();
		
		
		for(JsonNode i : result) {
			
			ObjectMapper objMapper = new ObjectMapper();
			
			Result object =  objMapper.readValue(new TreeTraversingParser(i), Result.class);
			
			
			list.add(object);
		}
			
		return list;											
	}
	
	//GET-QP
	public  JsonNode getListQuery(MultiValueMap<String, String> map) throws JSONException, IOException {
		String plainCreds = Username+":"+Password;
		
		String auth = Base64.encodeBase64String(plainCreds.getBytes());
		
		HttpHeaders headers = new HttpHeaders();		
		
		headers.add("Authorization", "Basic " + auth);
		
		HttpEntity<String> httpEntity = new HttpEntity<>(headers);	
		
		log.info("We are Entering to the rest template");
	    RestTemplate restTe = new RestTemplate();
	    
	    
	    UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(URL).queryParams(map);
		log.info("Url is : "+builder);

		
		
		ResponseEntity<JsonNode> response = restTe.exchange(builder.build().toUriString(), HttpMethod.GET, httpEntity, JsonNode.class);
		log.info("The JSON node : "+response.getBody());
		
		
		
		
		JsonNode result = response.getBody().get("result");
		
		 
		List<Result> lis = new ArrayList<Result>();
		
		
		for(JsonNode i : result) {
			
			ObjectMapper objMapper = new ObjectMapper();
			
			Result object =  objMapper.readValue(new TreeTraversingParser(i), Result.class);
			
			lis.add(object);
		}
			
		return result;											
	}
	//GET-ID
	public  Result getListById(String number) throws JSONException, IOException {
		String url= URL +"/"+ number;
		
		String plainCreds = Username+":"+Password;
		
		String auth = Base64.encodeBase64String(plainCreds.getBytes());	
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Basic " + auth);
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<?> request = new HttpEntity<>(headers);
		
		log.info("We are Entering to the rest template");
		
		RestTemplate restTe = new RestTemplate();
		
		ResponseEntity<JsonNode> response = restTe.exchange(url, HttpMethod.GET, request, JsonNode.class);
		log.info("Exited from the template response is "+response);
		
		
		JsonNode result = response.getBody().get("result");
		
		ObjectMapper objectMap = new ObjectMapper();		
		Result  object = objectMap.readValue(new TreeTraversingParser(result), Result.class);
	 		
		return object;

	}
	
	
	//DELETE
	public  String removeById(String number) {
		
		String url=URL+"/"+number;
		String plainCreds =  Username+":"+Password;
		String auth = Base64.encodeBase64String(plainCreds.getBytes());		
		HttpHeaders headers = new HttpHeaders();		
		headers.add("Authorization", "Basic " + auth);
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<?> request = new HttpEntity<>(headers);	
		
		log.info("We are Entering to the rest template");
		
		ResponseEntity<String> restTeee = restT.exchange(url, HttpMethod.DELETE, request, String.class);
		
		log.info("Exited from the template response is "+restTeee);
		
		return "Record Delete Succesfully";
	}
	//POST
	public  String createDetatils(Result s) {
		String url=URL;
		String plainCreds =Username+ ":"+ Password;
		String auth = Base64.encodeBase64String(plainCreds.getBytes());
		
		HttpHeaders headers = new HttpHeaders();		
		headers.add("Authorization", "Basic " + auth);
		headers.setContentType(MediaType.APPLICATION_JSON);
		
		HttpEntity<?> httpEntity = new HttpEntity<>(s , headers);		
		ResponseEntity<JsonNode> lut1 = restT.exchange(url, HttpMethod.POST, httpEntity , JsonNode.class);
		 JsonNode response = lut1.getBody();		
		return "Record created";
		
		
	}
	//UPDATE
	public  String updateDetatils(Result s, String number) {
		String url = URL+"/" + number;
		String plainCreds = Username+":"+Password;
		String auth = Base64.encodeBase64String(plainCreds.getBytes());
		HttpHeaders headers = new HttpHeaders();		
		headers.add("Authorization", "Basic " + auth);		
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<?> httpEntity = new HttpEntity<>(s,headers);
		 restT.put(url, httpEntity);
		 return "Record updated sucessfully";
	}

	
	
	
	
	
	
	
	
	
	
	
	/*
	public static void getuser() {
		String url="https://dev124036.service-now.com/api/now/table/incident/1c741bd70b2322007518478d83673af3";
		//List<Object> data = restTemp.getForObject(url, List.class);
		
		
		String plainCreds = "admin:Lpu#4244";
		byte[] plainCredsBytes = plainCreds.getBytes();
		byte[] base64CredsBytes = Base64.encodeBase64(plainCredsBytes);
		String base64Creds = new String(base64CredsBytes);

		HttpHeaders headers = new HttpHeaders();
		//headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		headers.add("Authorization", "Basic " + base64Creds);
		System.out.println(headers);
		HttpEntity<Object> request = new HttpEntity<Object>(headers);
		
		ResponseEntity<String>  us = restT.exchange(url,HttpMethod.GET,request,String.class);
		String da = us.getBody();
		System.out.println(da);
	}
	
	
	
	public static void getListuser() {
		String url="https://dev124036.service-now.com/api/now/table/incident/1c741bd70b2322007518478d83673af3";
		//List<Object> data = restTemp.getForObject(url, List.class);
		
		
		String plainCreds = "admin:Lpu#4244";
		byte[] plainCredsBytes = plainCreds.getBytes();
		byte[] base64CredsBytes = Base64.encodeBase64(plainCredsBytes);
		String base64Creds = new String(base64CredsBytes);

		HttpHeaders headers = new HttpHeaders();
		//headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		headers.add("Authorization", "Basic " + base64Creds);
		System.out.println(headers);
		HttpEntity<Object> request = new HttpEntity<Object>(headers);
		
		ResponseEntity<String>  us = restT.exchange(url,HttpMethod.GET,request,String.class);
		String da = us.getBody();
		System.out.println(da);
	}

	public static void updateById(String number,Result s) {
		String url="https://dev124036.service-now.com/api/now/table/incident"+number;
		log.info("The url i s: ");
		String plainCreds = "admin:Lpu#4244";
		byte[] plainCredsBytes = plainCreds.getBytes();
		byte[] base64CredsBytes = Base64.encodeBase64(plainCredsBytes);
		String base64Creds = new String(base64CredsBytes);
		HttpHeaders headers = new HttpHeaders();
		
		headers.add("Authorization", "Basic " + base64Creds);
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<?> o = new HttpEntity<>(headers);
		Map<String,Object> var = new HashMap<String,Object>();
		
		var.put("short_description", s.getShort_description());
		
		restT.put(url, o, var);
		
		
	}
*/
	
//	public static String createDetatils1(Result s) {
//		String url="https://dev124036.service-now.com/api/now/table/incident";
//		String plainCreds = "admin:Lpu#4244";
//		byte[] plainCredsBytes = plainCreds.getBytes();
//		byte[] base64CredsBytes = Base64.encodeBase64(plainCredsBytes);
//		String base64Creds = new String(base64CredsBytes);
//		HttpHeaders headers = new HttpHeaders();
//		
//		headers.add("Authorization", "Basic " + base64Creds);
//		headers.setContentType(MediaType.APPLICATION_JSON);
//		
//		
//		Result bo = new Result();
//		bo.setCaller_id(s.getCaller_id());
//		bo.setCategory(s.getCategory());
//		bo.setNumber(s.getNumber());
//		bo.setShort_description(s.getShort_description());
//		
//		Gson obj = new Gson();
//		String d = obj.toJson(bo);
//		System.out.print(d);
//		
//		ObjectMapper ob1 = new ObjectMapper();
//		
//	for(int i=0;i<out.length();i=i+5) {
//		
//		log.info(out.get(i).toString());
//		
//		String s = out.get(i).toString();
//		
//		JSONObject  json = new JSONObject(s);
//		
//		    Result person = new Result();
//		    
//		    Attributes k = new Attributes();
//		    
//	        person.setCaller_id(json.getString(k.callerId));
//	        person.setShort_description(json.getString(k.shortDescription));
//	        person.setCategory(json.getString(k.category));
//	        person.setNumber(json.getString(k.number));
//	        person.setSys_id(json.getString(k.sysId));		        
//	        lis.add(person);
//		
//	}
//		
//		
//		HttpEntity<?> ent = null;
//		try {
//			ent = new HttpEntity<>(ob1.writeValueAsString(bo),headers);
//			System.out.println(ent);
//		} catch (JsonProcessingException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	
//		
//		//Result res = new Result(s.getNumber(),s.getShort_description());
//		 ResponseEntity<String> lut1 =     restT.exchange(url, HttpMethod.POST, ent, String.class);
//		 String r = lut1.getBody();
//		 log.info("THe lust1 is :"+lut1+" body is : "+r);
//		 
//		
//		return r;
//		
//		
//	}
	//restTe.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
			//URI ur=null;
/*	
	public  String getListById1(String number) {

		int c=0;
		String url="https://dev124036.service-now.com/api/now/table/incident/1c741bd70b2322007518478d83673af3";
		String plainCreds = "admin:Lpu#4244";
		byte[] plainCredsBytes = plainCreds.getBytes();
		byte[] base64CredsBytes = Base64.encodeBase64(plainCredsBytes);
		String base64Creds = new String(base64CredsBytes);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		
		headers.add("Authorization", "Basic " + base64Creds);
		HttpEntity<String> request = new HttpEntity<>(headers);
		//HttpEntity<?> re = new Ht
		
				
		log.info("We are Entering to the rest template");
		RestTemplate restTe = new RestTemplate();
		
		ResponseEntity<Object> restTe1 = restT.exchange(url, HttpMethod.GET,request, Object.class);
		//ResponseEntity<String> rr = restTe.exchange(ur1, HttpMethod.GET, request, String.class);
		
		log.info("Exited from the template response is "+restTe1);
		String result = restTe1.getBody();
		
		
		log.info("outer data "+result);		
		
		System.out.println(result);
		return result;
		}
		*/
		
		
	
}
	

