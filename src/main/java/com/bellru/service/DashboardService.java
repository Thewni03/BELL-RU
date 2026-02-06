package com.bellru.service;

import com.bellru.dto.DashboardSummaryDto;
import com.bellru.model.Booking;
import com.bellru.model.BookingStatus;
import com.bellru.repository.BookingRepository;
import com.bellru.repository.ExpenseRepository;
import com.bellru.repository.LaundryBillRepository;
import com.bellru.repository.RoomRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class DashboardService {

    private final BookingRepository bookingRepository;
    private final ExpenseRepository expenseRepository;
    private final LaundryBillRepository laundryBillRepository;
    private final RoomRepository roomRepository;
    private final InventoryService inventoryService;

    public DashboardService(BookingRepository bookingRepository,
                             ExpenseRepository expenseRepository,
                             LaundryBillRepository laundryBillRepository,
                             RoomRepository roomRepository,
                             InventoryService inventoryService) {
        this.bookingRepository = bookingRepository;
        this.expenseRepository = expenseRepository;
        this.laundryBillRepository = laundryBillRepository;
        this.roomRepository = roomRepository;
        this.inventoryService = inventoryService;
    }

    public DashboardSummaryDto getSummary(LocalDate from, LocalDate to) {
        List<Booking> bookings = bookingRepository.findByBookingDateBetween(from, to)
                .stream()
                .filter(b -> b.getStatus() != BookingStatus.CANCELLED)
                .collect(Collectors.toList());

        double totalIncome = bookings.stream().mapToDouble(Booking::getAmount).sum();

        double manualExpenses = expenseRepository.findByDateBetween(from, to)
                .stream().mapToDouble(e -> e.getAmount()).sum();
        double laundryBillsTotal = laundryBillRepository.findByBillDateBetween(from, to)
                .stream().mapToDouble(l -> l.getAmount()).sum();
        double totalExpenses = manualExpenses + laundryBillsTotal;

        Map<String, Double> byType = new HashMap<>();
        Map<String, Double> byPayment = new HashMap<>();
        for (Booking b : bookings) {
            String type = b.getBookingType() != null ? b.getBookingType().toString() : "UNSPECIFIED";
            String pay = b.getPaymentMethod() != null ? b.getPaymentMethod().toString() : "UNSPECIFIED";
            byType.merge(type, b.getAmount(), Double::sum);
            byPayment.merge(pay, b.getAmount(), Double::sum);
        }

        long totalRooms = roomRepository.count();
        LocalDate today = LocalDate.now();
        long occupiedNow = bookingRepository
                .findByCheckInDateLessThanEqualAndCheckOutDateGreaterThanEqual(today, today)
                .stream()
                .filter(b -> b.getStatus() == BookingStatus.RESERVED || b.getStatus() == BookingStatus.CHECKED_IN)
                .map(Booking::getRoomNumber)
                .distinct()
                .count();

        return new DashboardSummaryDto(
                totalIncome,
                totalExpenses,
                totalIncome - totalExpenses,
                byType,
                byPayment,
                totalRooms,
                occupiedNow,
                inventoryService.getLowStockItems()
        );
    }
}
