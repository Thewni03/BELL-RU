package com.bellru.repository;

import com.bellru.model.Booking;
import com.bellru.model.BookingStatus;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.time.LocalDate;
import java.util.List;

public interface BookingRepository extends MongoRepository<Booking, String> {
    List<Booking> findByStatus(BookingStatus status);
    List<Booking> findByBookingDateBetween(LocalDate from, LocalDate to);
    List<Booking> findByRoomNumberAndStatusIn(String roomNumber, List<BookingStatus> statuses);
    List<Booking> findByCheckInDateLessThanEqualAndCheckOutDateGreaterThanEqual(LocalDate checkOut, LocalDate checkIn);
}
