package com.microservice.casestudy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.microservice.casestudy.entity.TripCatalog;

@Repository
public interface TripCatalogRepository extends JpaRepository<TripCatalog,Integer> {

}
