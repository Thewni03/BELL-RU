package com.bellru.dto;

import com.bellru.model.BookingType;
import com.bellru.model.PaymentMethod;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class BookingRequest {
    private String billNo;
    private LocalDate bookingDate;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private LocalTime checkInTime;

    private String guestName;
    private int numberOfGuests;

    private String roomNumber;

    private BookingType bookingType;
    private PaymentMethod paymentMethod;

    private double amount;
    private double roomCommission;

    private int bedsheetCount;
    private int pillowCaseCount;
    private int bathroomTowelCount;
    private int faceTowelCount;
    private double laundryCost;
}
