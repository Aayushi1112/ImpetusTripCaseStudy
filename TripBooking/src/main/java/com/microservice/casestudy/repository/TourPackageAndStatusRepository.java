package com.microservice.casestudy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.microservice.casestudy.entity.TourPackageAndStatus;

@Repository
public interface TourPackageAndStatusRepository extends JpaRepository<TourPackageAndStatus,Integer> {

}
