package com.bellru.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Data
@Document(collection = "laundry_bills")
public class LaundryBill {
    @Id
    private String id;

    private String invoiceNo;     // e.g. the laundry service's invoice/bill number
    private LocalDate periodStart;
    private LocalDate periodEnd;
    private LocalDate billDate;
    private double amount;        // total amount paid to the laundry service for the week
    private String note;
}
