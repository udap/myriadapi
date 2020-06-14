package io.chainmind.myriadapi.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import io.chainmind.myriadapi.domain.RequestUser;
import io.chainmind.myriadapi.domain.entity.AppRegistration;
import io.chainmind.myriadapi.service.AppRegistrationService;

@Configuration
@EnableWebSecurity
@Order(1)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
    @Value("${myriad.http.auth-token-header-name}")
    private String authTokenHeaderName;
    
	@Autowired
	private AppRegistrationService appRegistrationService;
	
	@Autowired
	private RequestUser requestOrg;
	
	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring()
			.antMatchers("/actuator/**");
	}
	
    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
    	
        APIKeyAuthFilter filter = new APIKeyAuthFilter(authTokenHeaderName);
        filter.setAuthenticationManager(new AuthenticationManager() {
            @Override
            public Authentication authenticate(Authentication authentication) throws AuthenticationException {
                String principal = (String) authentication.getPrincipal();
                
        		AppRegistration registration = appRegistrationService.findWithOrgByAppId(principal);
        		if (registration == null){
        			throw new BadCredentialsException("The App Id was not found or not the expected value.");
        		}
        		// set request scope bean value
        		requestOrg.setAppId(principal);
        		requestOrg.setAppOrg(registration.getOrg());
        		// by default use current principal as the request user id
        		requestOrg.setId(principal);
        		
                authentication.setAuthenticated(true);
                return authentication;
            }
        });
        
        httpSecurity.
            antMatcher("/api/**").
            csrf().disable().
            sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).
            and().addFilter(filter).authorizeRequests().anyRequest().authenticated();
    }

}
