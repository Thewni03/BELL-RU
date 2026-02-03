package com.bellru.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "inventory_items")
public class InventoryItem {
    @Id
    private String id;

    @Indexed(unique = true)
    private String itemName;

    private InventoryCategory category; // AMENITY or LINEN
    private String unit;                // e.g. "pcs", "g"
    private double currentStock;
    private double lowStockThreshold;

    // Used only for AMENITY items: cost charged to guest per person, and
    // also the quantity of stock consumed per guest (usually 1).
    private double ratePerGuest;
    private double usagePerGuest = 1;
}
