package com.bellru.controller;

import com.bellru.dto.LaundryReportDto;
import com.bellru.model.LaundryBill;
import com.bellru.service.LaundryService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/laundry")
public class LaundryController {

    private final LaundryService laundryService;

    public LaundryController(LaundryService laundryService) {
        this.laundryService = laundryService;
    }

    @GetMapping("/bills")
    public List<LaundryBill> getAll() {
        return laundryService.getAll();
    }

    @PostMapping("/bills")
    public LaundryBill create(@RequestBody LaundryBill bill) {
        return laundryService.create(bill);
    }

    @PutMapping("/bills/{id}")
    public LaundryBill update(@PathVariable String id, @RequestBody LaundryBill bill) {
        return laundryService.update(id, bill);
    }

    @DeleteMapping("/bills/{id}")
    public void delete(@PathVariable String id) {
        laundryService.delete(id);
    }

    @GetMapping("/report")
    public LaundryReportDto getReport(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
                                       @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
        return laundryService.getReport(from, to);
    }
}
