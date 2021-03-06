/**
 *
 */
package io.chainmind.myriadapi.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.annotation.RequestScope;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import io.chainmind.myriadapi.domain.RequestUser;

/**
 * @author matrix
 *
 */
@Configuration
public class MvcConfigurer implements WebMvcConfigurer {

	@Autowired
	private WebInterceptor webInterceptor;
	/*
	 * Define a request scoped bean to hold user profile
	 */
	@Bean
	@RequestScope
	public RequestUser requestOrg() {
		return new RequestUser();
	}

//	@Bean
//	@RequestScope
//	public RequestUser getCurrentUser() {
//		return new RequestUser();
//	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(webInterceptor).addPathPatterns("/**");
	}
}
