package com.bellru.dto;

import lombok.Data;

@Data
public class StockAdjustRequest {
    private double quantity;   // positive to add stock, negative to remove
    private String reason;
}
