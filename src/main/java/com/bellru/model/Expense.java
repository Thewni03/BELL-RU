package com.bellru.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Data
@Document(collection = "expenses")
public class Expense {
    @Id
    private String id;

    private LocalDate date;
    private String description;   // e.g. "Salary", "Social Media", "Carpets"
    private double amount;
}
