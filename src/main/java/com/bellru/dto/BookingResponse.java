package com.bellru.dto;

import com.bellru.model.Booking;
import com.bellru.model.InventoryItem;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class BookingResponse {
    private Booking booking;
    private List<InventoryItem> lowStockAlerts;
}
