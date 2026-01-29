package com.bellru.service;

import com.bellru.exception.BadRequestException;
import com.bellru.exception.ResourceNotFoundException;
import com.bellru.model.Room;
import com.bellru.repository.RoomRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoomService {

    private final RoomRepository roomRepository;

    public RoomService(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    public List<Room> getAll() {
        return roomRepository.findAll();
    }

    public Room getById(String id) {
        return roomRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Room not found: " + id));
    }

    public Room getByRoomNumber(String roomNumber) {
        return roomRepository.findByRoomNumber(roomNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Room not found: " + roomNumber));
    }

    public Room create(Room room) {
        if (roomRepository.existsByRoomNumber(room.getRoomNumber())) {
            throw new BadRequestException("Room number already exists: " + room.getRoomNumber());
        }
        room.setId(null);
        return roomRepository.save(room);
    }

    public Room update(String id, Room updated) {
        Room existing = getById(id);
        existing.setRoomNumber(updated.getRoomNumber());
        existing.setRoomName(updated.getRoomName());
        existing.setCategory(updated.getCategory());
        existing.setNote(updated.getNote());
        existing.setActive(updated.isActive());
        return roomRepository.save(existing);
    }

    public Room updateImage(String id, String imageUrl) {
        Room existing = getById(id);
        existing.setImageUrl(imageUrl);
        return roomRepository.save(existing);
    }

    public void delete(String id) {
        roomRepository.deleteById(id);
    }
}
