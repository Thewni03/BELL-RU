package com.bellru.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Map;

@Data
@AllArgsConstructor
public class LaundryReportDto {
    private Map<String, Double> laundryCostPerRoom;
    private double grandTotal;
    private double totalBilledByLaundryService;
}
