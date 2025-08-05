package org.digital.online.ejb.security;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.digital.online.ejb.entities.Permission;
import org.digital.online.ejb.entities.Role;
import org.digital.online.ejb.entities.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class CustomUserDetails implements UserDetails {

	private final User user;

	public CustomUserDetails(User user) {
		this.user = user;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		Set<GrantedAuthority> authorities = new HashSet<>();

		Role role = user.getRole();
		if (role != null) {
			authorities.add(new SimpleGrantedAuthority(role.getName()));

			// Add permissions of role
			for (Permission perm : role.getPermissions()) {
				authorities.add(new SimpleGrantedAuthority(perm.getName()));
			}

		}
		return authorities;
	}

	@Override
	public String getPassword() {
		return user.getPassword();
	}

	@Override
	public String getUsername() {
		return user.getUsername();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	public User getUser() {
		return user;
	}
}
