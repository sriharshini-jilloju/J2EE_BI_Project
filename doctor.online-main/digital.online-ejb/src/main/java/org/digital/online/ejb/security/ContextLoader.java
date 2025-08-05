package org.digital.online.ejb.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class ContextLoader {

	public static CustomUserDetails getLoggedInUser() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication != null && authentication.getPrincipal() instanceof CustomUserDetails) {
			CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
			return userDetails;
		}
		throw new RuntimeException("User is not authenticated");
	}
}

