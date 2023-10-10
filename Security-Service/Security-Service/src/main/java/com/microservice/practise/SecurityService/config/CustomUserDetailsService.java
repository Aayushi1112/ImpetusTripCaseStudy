package com.microservice.practise.SecurityService.config;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.microservice.practise.SecurityService.entity.UserCredential;
import com.microservice.practise.SecurityService.repository.UserCredentialRepository;

public class CustomUserDetailsService implements UserDetailsService {

	@Autowired
	private UserCredentialRepository repository;
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<UserCredential> credential  =repository.findByName(username);
	
		return credential.map(CustomUserDetails::new)
				.orElseThrow(()->new UsernameNotFoundException("user not found"));
				
	}

}
