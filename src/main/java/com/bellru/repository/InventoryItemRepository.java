package com.bellru.repository;

import com.bellru.model.InventoryCategory;
import com.bellru.model.InventoryItem;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;
import java.util.Optional;

public interface InventoryItemRepository extends MongoRepository<InventoryItem, String> {
    Optional<InventoryItem> findByItemName(String itemName);
    List<InventoryItem> findByCategory(InventoryCategory category);
}
