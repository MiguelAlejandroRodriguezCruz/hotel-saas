package com.hotel_saas.auth_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hotel_saas.auth_service.model.Room;

public interface RoomRepository extends JpaRepository<Room, Long> {}

