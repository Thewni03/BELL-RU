package com.bellru.dto;

import com.bellru.model.InventoryCategory;
import lombok.Data;

@Data
public class InventoryItemRequest {
    private String itemName;
    private InventoryCategory category;
    private String unit;
    private double currentStock;
    private double lowStockThreshold;
    private double ratePerGuest;
    private double usagePerGuest;
}
