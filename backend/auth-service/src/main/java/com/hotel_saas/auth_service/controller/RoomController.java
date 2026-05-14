package com.hotel_saas.auth_service.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hotel_saas.auth_service.model.Room;
import com.hotel_saas.auth_service.model.RoomType;
import com.hotel_saas.auth_service.service.RoomService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/rooms")
@RequiredArgsConstructor
public class RoomController {

    private final RoomService roomService;

    @PostMapping("/typesRoom")
    public RoomType createType(@RequestBody RoomType roomType) {
        return roomService.createRoomType(roomType);
    }

    @PostMapping("/createRoom")
    public Room createRoom(@RequestParam Long typeId,
                           @RequestParam String number,
                           @RequestParam Double price) {

        return roomService.createRoom(typeId, number, price);
    }

    @GetMapping("/getRooms")
    public List<Room> getRooms() {
        return roomService.getAllRooms();
    }
}