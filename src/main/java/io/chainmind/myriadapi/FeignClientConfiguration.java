package io.chainmind.myriadapi;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.security.oauth2.client.feign.OAuth2FeignRequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.DefaultOAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.client.token.grant.client.ClientCredentialsResourceDetails;
import org.springframework.security.oauth2.common.AuthenticationScheme;

import feign.RequestInterceptor;

@Configuration
public class FeignClientConfiguration {
	@Value("${myriad.oidc.token-url}") 
	private String tokenUrl;
	@Value("${myriad.oidc.client-id}")
	private String clientId;
	@Value("${myriad.oidc.client-secret}")
	private String clientSecret;

    @Bean
    public RequestInterceptor requestInterceptor() {
        return new OAuth2FeignRequestInterceptor(new DefaultOAuth2ClientContext(), resource());
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
