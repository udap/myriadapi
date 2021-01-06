package io.chainmind.myriadapi;

import java.lang.reflect.Method;
import java.util.Arrays;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@Configuration
@EnableCaching //(mode=AdviceMode.ASPECTJ)
@EnableAspectJAutoProxy(proxyTargetClass = true, exposeProxy = true)
public class CacheConfiguration {
	public static final String REGISTRY_CACHE = "myriad.registries";
	public static final String ACCOUNT_BY_CODE_CACHE = "myriad.accounts.code";
	public static final String ACCOUNT_CACHE = "myriad.accounts";
	public static final String GROUP_CACHE = "myriad.groups";
//	public static final String ORGANIZATION_CACHE = "myriad.organizations";
	public static final String ORGANIZATION_BY_ID_CACHE = "myriad.organizations.id";
	public static final String ORGANIZATION_CACHE_BY_CODE = "myriad.organizations.code";
	public static final String ORGANIZATION_ANCESTOR_CACHE = "myriad.organization.ancestor";
	public static final String CUSTOMER_BY_ACCT_ORG_CACHE = "myriad.customers.acct-org";
	public static final String EMPLOYEE_CACHE = "myriad.employees.org-account";	
	
	@Bean
	public KeyGenerator multiplyKeyGenerator() {
		return (Object target, Method method, Object... params) -> Arrays.toString(params);
	}
	
	@Bean
	public KeyGenerator entitiesKeyGenerator() {
		return (Object target, Method method, Object... params) -> {
			if (params == null)
				return "null";
	        int iMax = params.length - 1;
	        if (iMax == -1)
	            return "[]";

	        StringBuilder b = new StringBuilder();
	        b.append('[');
	        for (int i = 0; ; i++) {
	            b.append(params[i].hashCode());
	            if (i == iMax)
	                return b.append(']').toString();
	            b.append(", ");
	        }
		};
	}

	
}
