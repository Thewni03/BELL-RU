package com.bellru.dto;

import com.bellru.model.InventoryItem;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
public class DashboardSummaryDto {
    private double totalIncome;
    private double totalExpenses;
    private double netProfit;
    private Map<String, Double> incomeByBookingType;
    private Map<String, Double> incomeByPaymentMethod;
    private long totalRooms;
    private long occupiedRoomsNow;
    private List<InventoryItem> lowStockItems;
}
