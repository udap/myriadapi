package io.chainmind.myriadapi.persistence;

import java.util.Optional;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component("auditorAware")
public class AuditorAwareImpl implements AuditorAware<String> {

	@Override
	public Optional<String> getCurrentAuditor() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth == null){
			return Optional.of("myriadapi");
		}
		return Optional.ofNullable(auth)
		        .map(authentication -> {
		            if (authentication.getPrincipal() instanceof UserDetails) {
		                UserDetails securityUser = (UserDetails) authentication.getPrincipal();
		                return securityUser.getUsername();
		            } else if (authentication.getPrincipal() instanceof String) {
		                return (String) authentication.getPrincipal();
		            }
		            return "myriadapi";
		        });
	}
	
}