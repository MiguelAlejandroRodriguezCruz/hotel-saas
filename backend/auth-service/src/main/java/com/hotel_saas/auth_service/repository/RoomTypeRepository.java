package com.hotel_saas.auth_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hotel_saas.auth_service.model.RoomType;

public interface RoomTypeRepository extends JpaRepository<RoomType, Long> {}
