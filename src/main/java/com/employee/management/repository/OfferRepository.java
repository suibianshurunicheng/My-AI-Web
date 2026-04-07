package com.employee.management.repository;

import com.employee.management.entity.Offer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OfferRepository extends JpaRepository<Offer, Long> {
    
    List<Offer> findByStatus(String status);
}