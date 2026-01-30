package com.bellru.repository;

import com.bellru.model.Expense;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.time.LocalDate;
import java.util.List;

public interface ExpenseRepository extends MongoRepository<Expense, String> {
    List<Expense> findByDateBetween(LocalDate from, LocalDate to);
}
