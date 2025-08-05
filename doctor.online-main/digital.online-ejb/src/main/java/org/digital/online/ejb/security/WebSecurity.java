package org.digital.online.ejb.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class WebSecurity {

	@Autowired
	private CustomUserDetailsService userDetailsService;

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.csrf().disable().authorizeHttpRequests(auth -> auth
				.requestMatchers("/login", "/signup","/user/check","/user/register", "/css/**", "/docs/**", "/img/**", "/js/**", "/lib/**", "/scss/**")
				.permitAll().anyRequest().authenticated())
				.formLogin(form -> form.loginPage("/login").defaultSuccessUrl("/", true).permitAll())
				.logout(logout -> logout.logoutRequestMatcher(new AntPathRequestMatcher("/logout", "GET"))
						.logoutSuccessUrl("/login?logout").permitAll());

		return http.build();
	}

	@Bean
	@SuppressWarnings("deprecation")
	public DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
		authProvider.setUserDetailsService(userDetailsService);
		authProvider.setPasswordEncoder(passwordEncoder());
		return authProvider;
	}

	@Bean
	public AuthenticationManager authManager(HttpSecurity http) throws Exception {
		return http.getSharedObject(AuthenticationManagerBuilder.class).authenticationProvider(authenticationProvider())
				.build();
	}

}
