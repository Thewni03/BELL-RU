package com.bellru.controller;

import com.bellru.dto.InventoryItemRequest;
import com.bellru.dto.StockAdjustRequest;
import com.bellru.model.InventoryCategory;
import com.bellru.model.InventoryItem;
import com.bellru.service.InventoryService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventory")
public class InventoryController {

    private final InventoryService inventoryService;

    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @GetMapping
    public List<InventoryItem> getAll(@RequestParam(required = false) InventoryCategory category) {
        return category != null ? inventoryService.getByCategory(category) : inventoryService.getAll();
    }

    @GetMapping("/low-stock")
    public List<InventoryItem> getLowStock() {
        return inventoryService.getLowStockItems();
    }

    @GetMapping("/{id}")
    public InventoryItem getById(@PathVariable String id) {
        return inventoryService.getById(id);
    }

    @PostMapping
    public InventoryItem create(@RequestBody InventoryItemRequest req) {
        return inventoryService.create(req);
    }

    @PutMapping("/{id}")
    public InventoryItem update(@PathVariable String id, @RequestBody InventoryItemRequest req) {
        return inventoryService.update(id, req);
    }

    @PostMapping("/{id}/adjust")
    public InventoryItem adjustStock(@PathVariable String id, @RequestBody StockAdjustRequest req) {
        return inventoryService.adjustStock(id, req.getQuantity());
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) {
        inventoryService.delete(id);
    }
}
