package com.bellru.repository;

import com.bellru.model.Room;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.Optional;

public interface RoomRepository extends MongoRepository<Room, String> {
    Optional<Room> findByRoomNumber(String roomNumber);
    boolean existsByRoomNumber(String roomNumber);
}
