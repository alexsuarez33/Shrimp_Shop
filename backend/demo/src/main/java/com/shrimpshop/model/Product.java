package com.shrimpshop.model;

import java.util.HashMap;
import java.util.Map;
import java.util.List;

public class Product {
    private String id;           // Unique product ID
    private String type;         // e.g., "shrimp", "plant", "tank"
    private String name;         // Display name
    private double price;        // Unit price
    private List<String> imageUrls; //Images

    // Optional: flexible attributes for product-specific data
    private Map<String, String> attributes = new HashMap<>();

    // Constructors
    public Product() {}

    public Product(String id, String type, String name, double price) {
        this.id = id;
        this.type = type;
        this.name = name;
        this.price = price;
    }

    // Getters and setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public Map<String, String> getAttributes() { return attributes; }
    public void setAttributes(Map<String, String> attributes) { this.attributes = attributes; }

    public List<String> getImageUrls() { return imageUrls; }
    public void setImageUrls(List<String> imageUrls) { this.imageUrls = imageUrls; }
}
