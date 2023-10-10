package com.microservice.practise.SecurityService.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.microservice.practise.SecurityService.entity.UserCredential;
@Repository
public interface UserCredentialRepository extends JpaRepository<UserCredential,Integer> {

	Optional<UserCredential> findByName(String username);

}
