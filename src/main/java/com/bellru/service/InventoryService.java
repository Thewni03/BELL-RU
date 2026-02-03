package com.bellru.service;

import com.bellru.dto.InventoryItemRequest;
import com.bellru.exception.BadRequestException;
import com.bellru.exception.ResourceNotFoundException;
import com.bellru.model.InventoryCategory;
import com.bellru.model.InventoryItem;
import com.bellru.repository.InventoryItemRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class InventoryService {

    private final InventoryItemRepository inventoryItemRepository;

    public InventoryService(InventoryItemRepository inventoryItemRepository) {
        this.inventoryItemRepository = inventoryItemRepository;
    }

    public List<InventoryItem> getAll() {
        return inventoryItemRepository.findAll();
    }

    public List<InventoryItem> getByCategory(InventoryCategory category) {
        return inventoryItemRepository.findByCategory(category);
    }

    public InventoryItem getById(String id) {
        return inventoryItemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Inventory item not found: " + id));
    }

    public InventoryItem getByName(String itemName) {
        return inventoryItemRepository.findByItemName(itemName)
                .orElseThrow(() -> new ResourceNotFoundException("Inventory item not found: " + itemName));
    }

    public InventoryItem create(InventoryItemRequest req) {
        if (inventoryItemRepository.findByItemName(req.getItemName()).isPresent()) {
            throw new BadRequestException("Item already exists: " + req.getItemName());
        }
        InventoryItem item = new InventoryItem();
        applyRequest(item, req);
        return inventoryItemRepository.save(item);
    }

    public InventoryItem update(String id, InventoryItemRequest req) {
        InventoryItem item = getById(id);
        applyRequest(item, req);
        return inventoryItemRepository.save(item);
    }

    private void applyRequest(InventoryItem item, InventoryItemRequest req) {
        item.setItemName(req.getItemName());
        item.setCategory(req.getCategory());
        item.setUnit(req.getUnit());
        item.setCurrentStock(req.getCurrentStock());
        item.setLowStockThreshold(req.getLowStockThreshold());
        item.setRatePerGuest(req.getRatePerGuest());
        item.setUsagePerGuest(req.getUsagePerGuest());
    }

    public InventoryItem adjustStock(String id, double delta) {
        InventoryItem item = getById(id);
        item.setCurrentStock(item.getCurrentStock() + delta);
        return inventoryItemRepository.save(item);
    }

    /** Consumes stock by item name; if the item doesn't exist it is silently skipped. */
    public void consumeByName(String itemName, double quantity) {
        if (quantity <= 0) return;
        inventoryItemRepository.findByItemName(itemName).ifPresent(item -> {
            item.setCurrentStock(item.getCurrentStock() - quantity);
            inventoryItemRepository.save(item);
        });
    }

    /** Restores stock by item name (e.g. when a booking is cancelled). */
    public void restoreByName(String itemName, double quantity) {
        if (quantity <= 0) return;
        inventoryItemRepository.findByItemName(itemName).ifPresent(item -> {
            item.setCurrentStock(item.getCurrentStock() + quantity);
            inventoryItemRepository.save(item);
        });
    }

    public List<InventoryItem> getLowStockItems() {
        return inventoryItemRepository.findAll().stream()
                .filter(i -> i.getCurrentStock() <= i.getLowStockThreshold())
                .collect(Collectors.toList());
    }

    public void delete(String id) {
        inventoryItemRepository.deleteById(id);
    }
}
