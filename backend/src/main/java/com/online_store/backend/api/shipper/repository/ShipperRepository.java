package com.online_store.backend.api.shipper.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.online_store.backend.api.shipper.entity.Shipper;

public interface ShipperRepository extends JpaRepository<Shipper, Long> {

}
