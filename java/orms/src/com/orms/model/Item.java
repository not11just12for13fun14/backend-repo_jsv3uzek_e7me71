package com.orms.model;

import java.io.Serializable;
import java.util.UUID;

public class Item implements Serializable {
    private static final long serialVersionUID = 1L;

    private final String id;
    private String name;
    private String description;
    private double pricePerDay;
    private boolean available;
    private String ownerUserId; // who added the item

    public Item(String name, String description, double pricePerDay, String ownerUserId) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.description = description;
        this.pricePerDay = pricePerDay;
        this.available = true;
        this.ownerUserId = ownerUserId;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public double getPricePerDay() { return pricePerDay; }
    public void setPricePerDay(double pricePerDay) { this.pricePerDay = pricePerDay; }
    public boolean isAvailable() { return available; }
    public void setAvailable(boolean available) { this.available = available; }
    public String getOwnerUserId() { return ownerUserId; }

    @Override
    public String toString() {
        return "Item{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", pricePerDay=" + pricePerDay +
                ", available=" + available +
                '}';
    }
}
