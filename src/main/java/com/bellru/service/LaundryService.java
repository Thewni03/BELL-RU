package com.bellru.service;

import com.bellru.dto.LaundryReportDto;
import com.bellru.exception.ResourceNotFoundException;
import com.bellru.model.Booking;
import com.bellru.model.LaundryBill;
import com.bellru.repository.BookingRepository;
import com.bellru.repository.LaundryBillRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class LaundryService {

    private final LaundryBillRepository laundryBillRepository;
    private final BookingRepository bookingRepository;

    public LaundryService(LaundryBillRepository laundryBillRepository, BookingRepository bookingRepository) {
        this.laundryBillRepository = laundryBillRepository;
        this.bookingRepository = bookingRepository;
    }

    public List<LaundryBill> getAll() {
        return laundryBillRepository.findAll();
    }

    public LaundryBill getById(String id) {
        return laundryBillRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Laundry bill not found: " + id));
    }

    public LaundryBill create(LaundryBill bill) {
        bill.setId(null);
        return laundryBillRepository.save(bill);
    }

    public LaundryBill update(String id, LaundryBill updated) {
        LaundryBill existing = getById(id);
        existing.setInvoiceNo(updated.getInvoiceNo());
        existing.setPeriodStart(updated.getPeriodStart());
        existing.setPeriodEnd(updated.getPeriodEnd());
        existing.setBillDate(updated.getBillDate());
        existing.setAmount(updated.getAmount());
        existing.setNote(updated.getNote());
        return laundryBillRepository.save(existing);
    }

    public void delete(String id) {
        laundryBillRepository.deleteById(id);
    }

    /** Sums each booking's manually-entered laundry cost per room for the period, plus the grand total. */
    public LaundryReportDto getReport(LocalDate from, LocalDate to) {
        List<Booking> bookings = bookingRepository.findByBookingDateBetween(from, to);
        Map<String, Double> perRoom = new LinkedHashMap<>();
        double grandTotal = 0;
        for (Booking b : bookings) {
            perRoom.merge(b.getRoomNumber(), b.getLaundryCost(), Double::sum);
            grandTotal += b.getLaundryCost();
        }
        double billed = laundryBillRepository.findByBillDateBetween(from, to)
                .stream().mapToDouble(LaundryBill::getAmount).sum();
        return new LaundryReportDto(perRoom, grandTotal, billed);
    }
}
