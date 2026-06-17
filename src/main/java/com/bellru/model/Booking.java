package com.bellru.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.LinkedHashMap;
import java.util.Map;

@Data
@Document(collection = "bookings")
public class Booking {
    @Id
    private String id;

    private String billNo;            // entered manually by admin
    private LocalDate bookingDate;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private LocalTime checkInTime;

    private String guestName;
    private int numberOfGuests;

    private String roomId;
    private String roomNumber;
    private String roomName;          // auto-filled from Room
    private RoomCategory roomCategory; // auto-filled from Room

    private BookingType bookingType;
    private PaymentMethod paymentMethod;

    private double amount;            // total amount charged to guest
    private double roomCommission;    // manual entry - staff commission for this booking

    private BookingStatus status = BookingStatus.RESERVED;

    // ---- Linen usage (manual quantities, deducted from inventory) ----
    private int bedsheetCount;
    private int pillowCaseCount;
    private int bathroomTowelCount;
    private int faceTowelCount;
    private double laundryCost;       // manual entry per booking

    // ---- Amenities (auto-calculated from numberOfGuests x rate card) ----
    private Map<String, Double> amenityBreakdown = new LinkedHashMap<>();
    private double amenityCost;

    // ---- Totals (auto-calculated) ----
    private double totalExpense;      // laundryCost + amenityCost + roomCommission
    private double profit;            // amount - totalExpense

    private LocalDateTime createdAt = LocalDateTime.now();
}
