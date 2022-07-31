package com.servicenow.demoservice.model;




import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.google.common.collect.Lists;
import com.servicenow.demoservice.clintreq.Businesslogic;
import com.servicenow.demoservice.clintreq.LdapAuthenticationProvider;
import com.servicenow.demoservice.conf.Jwtfilter;
import com.servicenow.demoservice.conf.MyCustomUserDetails;


import net.bytebuddy.implementation.bytecode.Throw;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.SwaggerResource;
import springfox.documentation.swagger.web.SwaggerResourcesProvider;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableWebSecurity
@EnableSwagger2
public class Confg  extends WebSecurityConfigurerAdapter {
	 //WebSecurityConfigurerAdapter
	
	@Autowired
	public Jwtfilter jwtfil;
	
   private Environment env;
    
    public Confg(Environment env){
        this.env = env;
    }
	
	@Autowired
	public MyCustomUserDetails myCustomUserDetails;
//	@Bean
//	public Docket newsApi() {
//	    return new Docket(DocumentationType.SWAGGER_2)
//	            .select()
//	            .apis(RequestHandlerSelectors.any())
//	            .paths(PathSelectors.any())
//	            .build()
//	            .securitySchemes(Lists.newArrayList(apiKey()))
//	            .securityContexts(Lists.newArrayList(securityContext()));
//	            //.apiInfo(generateApiInfo());
//	}
//
//	@Bean
//	SecurityContext securityContext() {
//	    return SecurityContext.builder()
//	            .securityReferences(defaultAuth())
//	            .forPaths(PathSelectors.any())
//	            .build();
//	}
//	 @Override
//	    public void configure(WebSecurity web) throws Exception {
//	        web.ignoring().antMatchers("/v2/api-docs",
//	                                   "/configuration/ui",
//	                                   "/swagger-resources/**",
//	                                   "/configuration/security",
//	                                   "/swagger-ui.html",
//	                                   "/webjars/**","/apis/api/ui.yaml");
//	    }
//
//	List<SecurityReference> defaultAuth() {
//	    AuthorizationScope authorizationScope
//	            = new AuthorizationScope("global", "accessEverything");
//	    AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
//	    authorizationScopes[0] = authorizationScope;
//	    return Lists.newArrayList(
//	            new SecurityReference("JWT", authorizationScopes));
//	}

	
	
//	private ApiKey apiKey() {
//	    return new ApiKey("JWT", "Authorization", "header");
//	}
	
	
	
	@Bean
	AuthenticationManager authManager() throws Exception {
		return super.authenticationManager();
	}


	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
	    
		
		auth.authenticationProvider(new LdapAuthenticationProvider(env)).eraseCredentials(false); 
		
	}
	
	
	
	
	@Override
	protected void configure(HttpSecurity http)  {
		try {
			http.csrf().disable(); //spring default security
			
			http.authorizeHttpRequests().antMatchers("/sn/auth").permitAll().anyRequest().authenticated();
			
			//http.exceptionHandling().accessDeniedHandler(custAccessDeniedException());
			
			http.addFilterBefore(jwtfil,UsernamePasswordAuthenticationFilter.class);
			
		} catch (Exception e) {
			
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
			
		} 	}


	@Bean
	   public LdapAuthenticationProvider ldapAuthenticationProvider() {
		   return new LdapAuthenticationProvider(env);
	   }

	@Bean
	public RestTemplate restTemp() {
		//restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());
		return new RestTemplate();
	}
//	 @Primary
//	    @Bean
//	    public SwaggerResourcesProvider swaggerResourcesProvider() {
//	        return () -> {
//	            List<SwaggerResource> resources = new ArrayList<>();
//	            Collections.singletonList("api")
//	                    .forEach(resourceName -> resources.add(loadResource(resourceName)));
//	            return resources;
//	        };
//	    }
//
//	    private SwaggerResource loadResource(String resource) {
//	        SwaggerResource swResource = new SwaggerResource();
//	        swResource.setName(resource);
//	        swResource.setSwaggerVersion("2.0");
//	        swResource.setLocation("/apis/" + resource + "/ui.yaml");
//	        return swResource;
//	    }
//	

	
	
	
	

}
