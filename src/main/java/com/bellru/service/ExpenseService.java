package com.bellru.service;

import com.bellru.exception.ResourceNotFoundException;
import com.bellru.model.Expense;
import com.bellru.repository.ExpenseRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ExpenseService {

    private final ExpenseRepository expenseRepository;

    public ExpenseService(ExpenseRepository expenseRepository) {
        this.expenseRepository = expenseRepository;
    }

    public List<Expense> getAll() {
        return expenseRepository.findAll();
    }

    public List<Expense> getByPeriod(LocalDate from, LocalDate to) {
        return expenseRepository.findByDateBetween(from, to);
    }

    public Expense getById(String id) {
        return expenseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Expense not found: " + id));
    }

    public Expense create(Expense expense) {
        expense.setId(null);
        return expenseRepository.save(expense);
    }

    public Expense update(String id, Expense updated) {
        Expense existing = getById(id);
        existing.setDate(updated.getDate());
        existing.setDescription(updated.getDescription());
        existing.setAmount(updated.getAmount());
        return expenseRepository.save(existing);
    }

    public void delete(String id) {
        expenseRepository.deleteById(id);
    }
}
