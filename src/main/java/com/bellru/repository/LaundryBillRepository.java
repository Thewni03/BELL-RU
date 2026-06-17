package com.bellru.repository;

import com.bellru.model.LaundryBill;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.time.LocalDate;
import java.util.List;

public interface LaundryBillRepository extends MongoRepository<LaundryBill, String> {
    List<LaundryBill> findByBillDateBetween(LocalDate from, LocalDate to);
}
