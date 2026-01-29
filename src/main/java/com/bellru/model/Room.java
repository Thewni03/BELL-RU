package com.bellru.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "rooms")
public class Room {
    @Id
    private String id;

    @Indexed(unique = true)
    private String roomNumber;

    private String roomName;
    private RoomCategory category;
    private String note;
    private String imageUrl;
    private boolean active = true;
}
