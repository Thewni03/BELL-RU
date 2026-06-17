package com.bellru.service;

import com.bellru.dto.BookingRequest;
import com.bellru.dto.BookingResponse;
import com.bellru.exception.BadRequestException;
import com.bellru.exception.ResourceNotFoundException;
import com.bellru.model.*;
import com.bellru.repository.BookingRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;
    private final RoomService roomService;
    private final InventoryService inventoryService;

    public BookingService(BookingRepository bookingRepository, RoomService roomService, InventoryService inventoryService) {
        this.bookingRepository = bookingRepository;
        this.roomService = roomService;
        this.inventoryService = inventoryService;
    }

    public List<Booking> getAll(String status, LocalDate from, LocalDate to) {
        if (status != null) {
            return bookingRepository.findByStatus(BookingStatus.valueOf(status.toUpperCase()));
        }
        if (from != null && to != null) {
            return bookingRepository.findByBookingDateBetween(from, to);
        }
        return bookingRepository.findAll();
    }

    public Booking getById(String id) {
        return bookingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found: " + id));
    }

    public BookingResponse create(BookingRequest req) {
        Room room = roomService.getByRoomNumber(req.getRoomNumber());

        Booking booking = new Booking();
        mapRequestToBooking(req, booking);
        booking.setRoomId(room.getId());
        booking.setRoomNumber(room.getRoomNumber());
        booking.setRoomName(room.getRoomName());
        booking.setRoomCategory(room.getCategory());
        booking.setStatus(BookingStatus.RESERVED);

        applyAmenityCalculation(booking);
        consumeInventoryFor(booking);
        recomputeTotals(booking);

        Booking saved = bookingRepository.save(booking);
        return new BookingResponse(saved, inventoryService.getLowStockItems());
    }

    public BookingResponse update(String id, BookingRequest req) {
        Booking existing = getById(id);

        // Reverse the inventory effect of the old booking before applying the new one.
        restoreInventoryFor(existing);

        Room room = roomService.getByRoomNumber(req.getRoomNumber());
        mapRequestToBooking(req, existing);
        existing.setRoomId(room.getId());
        existing.setRoomNumber(room.getRoomNumber());
        existing.setRoomName(room.getRoomName());
        existing.setRoomCategory(room.getCategory());

        applyAmenityCalculation(existing);
        consumeInventoryFor(existing);
        recomputeTotals(existing);

        Booking saved = bookingRepository.save(existing);
        return new BookingResponse(saved, inventoryService.getLowStockItems());
    }

    public Booking updateStatus(String id, BookingStatus status) {
        Booking booking = getById(id);
        if (status == BookingStatus.CANCELLED && booking.getStatus() != BookingStatus.CANCELLED) {
            restoreInventoryFor(booking);
        }
        booking.setStatus(status);
        return bookingRepository.save(booking);
    }

    public void delete(String id) {
        Booking booking = getById(id);
        if (booking.getStatus() != BookingStatus.CANCELLED) {
            restoreInventoryFor(booking);
        }
        bookingRepository.deleteById(id);
    }

    // ---------------------------------------------------------------

    private void mapRequestToBooking(BookingRequest req, Booking booking) {
        if (req.getRoomNumber() == null || req.getRoomNumber().isBlank()) {
            throw new BadRequestException("Room number is required");
        }
        booking.setBillNo(req.getBillNo());
        booking.setBookingDate(req.getBookingDate() != null ? req.getBookingDate() : LocalDate.now());
        booking.setCheckInDate(req.getCheckInDate());
        booking.setCheckOutDate(req.getCheckOutDate());
        booking.setCheckInTime(req.getCheckInTime());
        booking.setGuestName(req.getGuestName());
        booking.setNumberOfGuests(Math.max(req.getNumberOfGuests(), 0));
        booking.setBookingType(req.getBookingType());
        booking.setPaymentMethod(req.getPaymentMethod());
        booking.setAmount(req.getAmount());
        booking.setRoomCommission(req.getRoomCommission());
        booking.setBedsheetCount(req.getBedsheetCount());
        booking.setPillowCaseCount(req.getPillowCaseCount());
        booking.setBathroomTowelCount(req.getBathroomTowelCount());
        booking.setFaceTowelCount(req.getFaceTowelCount());
        booking.setLaundryCost(req.getLaundryCost());
    }

    private void applyAmenityCalculation(Booking booking) {
        List<InventoryItem> amenityItems = inventoryService.getByCategory(InventoryCategory.AMENITY);
        Map<String, Double> breakdown = new LinkedHashMap<>();
        double total = 0;
        for (InventoryItem item : amenityItems) {
            double cost = item.getRatePerGuest() * booking.getNumberOfGuests();
            if (cost > 0) {
                breakdown.put(item.getItemName(), cost);
                total += cost;
            }
        }
        booking.setAmenityBreakdown(breakdown);
        booking.setAmenityCost(total);
    }

    private void recomputeTotals(Booking booking) {
        double totalExpense = booking.getLaundryCost() + booking.getAmenityCost() + booking.getRoomCommission();
        booking.setTotalExpense(totalExpense);
        booking.setProfit(booking.getAmount() - totalExpense);
    }

    private void consumeInventoryFor(Booking booking) {
        // Linens
        inventoryService.consumeByName("Bedsheet", booking.getBedsheetCount());
        inventoryService.consumeByName("Pillow Case", booking.getPillowCaseCount());
        inventoryService.consumeByName("Bathroom Towel", booking.getBathroomTowelCount());
        inventoryService.consumeByName("Face Towel", booking.getFaceTowelCount());

        // Amenities - quantity consumed per item is usagePerGuest x numberOfGuests
        List<InventoryItem> amenityItems = inventoryService.getByCategory(InventoryCategory.AMENITY);
        for (InventoryItem item : amenityItems) {
            double qty = item.getUsagePerGuest() * booking.getNumberOfGuests();
            inventoryService.consumeByName(item.getItemName(), qty);
        }
    }

    private void restoreInventoryFor(Booking booking) {
        inventoryService.restoreByName("Bedsheet", booking.getBedsheetCount());
        inventoryService.restoreByName("Pillow Case", booking.getPillowCaseCount());
        inventoryService.restoreByName("Bathroom Towel", booking.getBathroomTowelCount());
        inventoryService.restoreByName("Face Towel", booking.getFaceTowelCount());

        List<InventoryItem> amenityItems = inventoryService.getByCategory(InventoryCategory.AMENITY);
        for (InventoryItem item : amenityItems) {
            double qty = item.getUsagePerGuest() * booking.getNumberOfGuests();
            inventoryService.restoreByName(item.getItemName(), qty);
        }
    }
}
