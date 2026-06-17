package com.bellru.controller;

import com.bellru.model.Room;
import com.bellru.service.FileStorageService;
import com.bellru.service.RoomService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/rooms")
public class RoomController {

    private final RoomService roomService;
    private final FileStorageService fileStorageService;

    public RoomController(RoomService roomService, FileStorageService fileStorageService) {
        this.roomService = roomService;
        this.fileStorageService = fileStorageService;
    }

    @GetMapping
    public List<Room> getAll() {
        return roomService.getAll();
    }

    @GetMapping("/{id}")
    public Room getById(@PathVariable String id) {
        return roomService.getById(id);
    }

    @PostMapping
    public Room create(@RequestBody Room room) {
        return roomService.create(room);
    }

    @PutMapping("/{id}")
    public Room update(@PathVariable String id, @RequestBody Room room) {
        return roomService.update(id, room);
    }

    @PostMapping("/{id}/image")
    public Room uploadImage(@PathVariable String id, @RequestParam("file") MultipartFile file) {
        String url = fileStorageService.storeRoomImage(file);
        return roomService.updateImage(id, url);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) {
        roomService.delete(id);
    }
}
