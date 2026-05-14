package com.hotel_saas.auth_service.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.hotel_saas.auth_service.model.Room;
import com.hotel_saas.auth_service.model.RoomType;
import com.hotel_saas.auth_service.repository.RoomRepository;
import com.hotel_saas.auth_service.repository.RoomTypeRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RoomService {

    private final RoomRepository roomRepository;
    private final RoomTypeRepository roomTypeRepository;

    public RoomType createRoomType(RoomType roomType) {
        return roomTypeRepository.save(roomType);
    }

    public Room createRoom(Long roomTypeId, String number, Double price) {

        RoomType type = roomTypeRepository.findById(roomTypeId)
                .orElseThrow(() -> new RuntimeException("Tipo no encontrado"));

        Room room = Room.builder()
                .number(number)
                .roomType(type)
                .price(price)
                .status("AVAILABLE")
                .build();

        return roomRepository.save(room);
    }

    public List<Room> getAllRooms() {
        return roomRepository.findAll();
    }
}