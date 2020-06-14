package io.chainmind.myriadapi.web;

import java.util.Objects;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.security.oauth2.client.feign.OAuth2FeignRequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.oauth2.client.DefaultOAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.client.token.grant.client.ClientCredentialsResourceDetails;
import org.springframework.security.oauth2.common.AuthenticationScheme;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import feign.RequestInterceptor;
import feign.RequestTemplate;

@Configuration
public class FeignClientConfiguration {
	@Value("${myriad.oidc.token-url}") 
	private String tokenUrl;
	@Value("${myriad.oidc.client-id}")
	private String clientId;
	@Value("${myriad.oidc.client-secret}")
	private String clientSecret;
	@Value("${myriad.reqUser.headerName:x-request-user}")
	private String reqUserHeaderName;

    @Bean
    @Order(1)
    public RequestInterceptor requestInterceptor() {
        return new OAuth2FeignRequestInterceptor(new DefaultOAuth2ClientContext(), resource());
    }
    
    @Bean
    @Order(2)
    public RequestInterceptor addHeaderRequestInterceptor() {
		return new RequestInterceptor() {
			@Override
			public void apply(RequestTemplate template) {
				ServletRequestAttributes reqAttrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
				if(Objects.nonNull(reqAttrs)) {
					HttpServletRequest request = reqAttrs.getRequest();
			        String reqUser = request.getHeader(reqUserHeaderName);
			        if (!StringUtils.hasText(reqUser))
			        	reqUser = (String)reqAttrs.getRequest().getAttribute(reqUserHeaderName);
			        // pass header to downstream service
			        if (StringUtils.hasText(reqUser))
			        	template.header(reqUserHeaderName, reqUser);
				}				
			}
		};
    }
    
    @Bean
    public OAuth2RestTemplate oAuth2RestTemplate() {
        OAuth2RestTemplate oAuth2RestTemplate = new OAuth2RestTemplate(resource());
        return oAuth2RestTemplate;
    }

    private OAuth2ProtectedResourceDetails resource() {
        ClientCredentialsResourceDetails details = new ClientCredentialsResourceDetails();
        details.setAccessTokenUri(tokenUrl);
        details.setClientId(clientId);
        details.setClientSecret(clientSecret);
        details.setGrantType("client_credentials");
        details.setClientAuthenticationScheme(AuthenticationScheme.form);
        details.setAuthenticationScheme(AuthenticationScheme.form);
//        details.setScope(Arrays.asList("read", "write"));
        return details;
    }
}
