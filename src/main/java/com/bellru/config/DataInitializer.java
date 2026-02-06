package com.bellru.config;

import com.bellru.model.InventoryCategory;
import com.bellru.model.InventoryItem;
import com.bellru.model.User;
import com.bellru.repository.InventoryItemRepository;
import com.bellru.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final InventoryItemRepository inventoryItemRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.default-admin-username}")
    private String defaultUsername;

    @Value("${app.default-admin-password}")
    private String defaultPassword;

    public DataInitializer(UserRepository userRepository,
                            InventoryItemRepository inventoryItemRepository,
                            PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.inventoryItemRepository = inventoryItemRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        seedAdminUser();
        seedInventoryItems();
    }

    private void seedAdminUser() {
        if (userRepository.findByUsername(defaultUsername).isEmpty()) {
            User user = new User();
            user.setUsername(defaultUsername);
            user.setPasswordHash(passwordEncoder.encode(defaultPassword));
            user.setRole("ADMIN");
            userRepository.save(user);
        }
    }

    private void seedInventoryItems() {
        seedItem("Soap", InventoryCategory.AMENITY, "pcs", 200, 20, 30, 1);
        seedItem("Milk Powder", InventoryCategory.AMENITY, "g", 5000, 500, 40, 1);
        seedItem("Nescafe", InventoryCategory.AMENITY, "g", 5000, 500, 60, 1);
        seedItem("Bodywash Set", InventoryCategory.AMENITY, "pcs", 100, 10, 180, 1);
        seedItem("Sugar", InventoryCategory.AMENITY, "g", 5000, 500, 10, 1);
        seedItem("Water Bottle", InventoryCategory.AMENITY, "pcs", 200, 20, 53, 1);
        seedItem("Tea Bags", InventoryCategory.AMENITY, "pcs", 300, 30, 20, 1);

        seedItem("Bedsheet", InventoryCategory.LINEN, "pcs", 100, 10, 0, 0);
        seedItem("Pillow Case", InventoryCategory.LINEN, "pcs", 150, 15, 0, 0);
        seedItem("Bathroom Towel", InventoryCategory.LINEN, "pcs", 100, 10, 0, 0);
        seedItem("Face Towel", InventoryCategory.LINEN, "pcs", 100, 10, 0, 0);
    }

    private void seedItem(String name, InventoryCategory category, String unit,
                           double stock, double threshold, double ratePerGuest, double usagePerGuest) {
        if (inventoryItemRepository.findByItemName(name).isEmpty()) {
            InventoryItem item = new InventoryItem();
            item.setItemName(name);
            item.setCategory(category);
            item.setUnit(unit);
            item.setCurrentStock(stock);
            item.setLowStockThreshold(threshold);
            item.setRatePerGuest(ratePerGuest);
            item.setUsagePerGuest(usagePerGuest);
            inventoryItemRepository.save(item);
        }
    }
}
