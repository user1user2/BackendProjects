package com.servicenow.demoservice.conf;

import java.security.Key;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.crypto.MacProvider;

public class Jwtutil {
	private static final  String SECRET_KEY="sec";
	
	 private static final Key secret = MacProvider.generateKey(SignatureAlgorithm.HS256);
	    private static final byte[] secretBytes = secret.getEncoded();
	    private static final String base64SecretBytes = Base64.getEncoder().encodeToString(secretBytes);

	Logger log = LoggerFactory.getLogger(Jwtutil.class);
	
	
//	public String genrateToken(UserDetails userDetails) {
//		Map<String, Object> claims = new HashMap<String, Object>();
//		return createToken(claims,userDetails.getUsername());
//	}
	
	public  String generateToken1(String userName) throws Exception
    {
        Instant now = Instant.now();
        String jwtToken = Jwts.builder()
                .claim("name", userName)
                .setSubject("jwt_token")
                .setId(UUID.randomUUID().toString())
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(now.plus(5l, ChronoUnit.MINUTES)))
                .signWith(SignatureAlgorithm.HS256, base64SecretBytes)
                .compact();
        return jwtToken;
    }
	private String createToken(Map<String, Object> claims, String subject) {
		return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis())).setExpiration(new Date(System.currentTimeMillis() + 1000 *60*60 +10))
				.signWith(SignatureAlgorithm.HS256, base64SecretBytes).compact();
	}
	
	public boolean validateToken(String Token,UserDetails user) {
		final String userName = extractUsername(Token);
		boolean out = userName.equals(user.getUsername());
		log.info("The validated token value is :"+out);
		
		return (userName.equals(user.getUsername()) && !isTokenExpired(Token));
		
		
	}
	
	public boolean validateToken(String Token,String username) {
		final String userName = extractUsername(Token);
		boolean out = userName.equals(username);
		log.info("The validated token value is :"+out);
		
		return (userName.equals(username) && !isTokenExpired(Token));
		
		
	}
	public String extractUsername(String token) {
		log.info("context: {}",SecurityContextHolder.getContext().getAuthentication());
		return extractClaim(token,Claims::getSubject);
	}
	
	
	public Date extractExpiration(String token) {
		return extractClaim(token,Claims::getExpiration);
	}
	
	
	public<T> T extractClaim(String token, Function<Claims,T> claimsResolver) {
		final Claims claims = extractAllClaims(token);
		return claimsResolver.apply(claims);
		
	}

	
	private Claims extractAllClaims(String token) {
		return Jwts.parser().setSigningKey(base64SecretBytes).parseClaimsJws(token).getBody();
	}

	
	private boolean isTokenExpired(String token) {
		return extractExpiration(token).before(new Date());
	}


	
//
//	private String createToken(Map<String, Object> claims, String subject) {
//		return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis())).setExpiration(new Date(System.currentTimeMillis() + 1000 *60*60 +10))
//				.signWith(SignatureAlgorithm.HS256, base64SecretBytes).compact();
//	}
//	
//
//	public boolean validateToken(String Token,UserDetails user) {
//		final String userName = extractUsername(Token);
//		boolean out = userName.equals(user.getUsername());
//		log.info("The validated token value is :"+out);
//		
//		return (userName.equals(user.getUsername()) && !isTokenExpired(Token));
//		
//		
//	}
//	public boolean validateToken1(String Token,String user) {
//		final String userName = extractUsername(Token);
//		boolean out = userName.equals(user);
//		log.info("The validated token value is :"+out);
//		
//		return (userName.equals(user) && !isTokenExpired(Token));
//		
//		
//	}
//	public String extractUsername(String token) {
//		return extractClaim(token,Claims::getSubject);
//	}
//	
//	
//	public Date extractExpiration(String token) {
//		return extractClaim(token,Claims::getExpiration);
//	}
//	
//	
//	public<T> T extractClaim(String token, Function<Claims,T> claimsResolver) {
//		final Claims claims = extractAllClaims(token);
//		return claimsResolver.apply(claims);
//		
//	}
//
//	
//	private Claims extractAllClaims(String token) {
//		return Jwts.parser().setSigningKey(base64SecretBytes).parseClaimsJws(token).getBody();
//	}
//
//	
//	private boolean isTokenExpired(String token) {
//		return extractExpiration(token).before(new Date());
//	}
//
//	
//
//	

}
