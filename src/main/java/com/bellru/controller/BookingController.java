package com.bellru.controller;

import com.bellru.dto.BookingRequest;
import com.bellru.dto.BookingResponse;
import com.bellru.model.Booking;
import com.bellru.model.BookingStatus;
import com.bellru.service.BookingService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @GetMapping
    public List<Booking> getAll(@RequestParam(required = false) String status,
                                 @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
                                 @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
        return bookingService.getAll(status, from, to);
    }

    @GetMapping("/{id}")
    public Booking getById(@PathVariable String id) {
        return bookingService.getById(id);
    }

    @PostMapping
    public BookingResponse create(@RequestBody BookingRequest req) {
        return bookingService.create(req);
    }

    @PutMapping("/{id}")
    public BookingResponse update(@PathVariable String id, @RequestBody BookingRequest req) {
        return bookingService.update(id, req);
    }

    @PatchMapping("/{id}/status")
    public Booking updateStatus(@PathVariable String id, @RequestBody Map<String, String> body) {
        return bookingService.updateStatus(id, BookingStatus.valueOf(body.get("status").toUpperCase()));
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) {
        bookingService.delete(id);
    }
}
