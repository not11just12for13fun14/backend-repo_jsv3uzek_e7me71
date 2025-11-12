package com.orms.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.UUID;

public class Booking implements Serializable {
    private static final long serialVersionUID = 1L;

    private final String id;
    private String userId;
    private String itemId;
    private LocalDate startDate;
    private LocalDate endDate;
    private double totalCost;
    private boolean returned;

    public Booking(String userId, String itemId, LocalDate startDate, LocalDate endDate, double totalCost) {
        this.id = UUID.randomUUID().toString();
        this.userId = userId;
        this.itemId = itemId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.totalCost = totalCost;
        this.returned = false;
    }

    public String getId() { return id; }
    public String getUserId() { return userId; }
    public String getItemId() { return itemId; }
    public LocalDate getStartDate() { return startDate; }
    public LocalDate getEndDate() { return endDate; }
    public double getTotalCost() { return totalCost; }
    public boolean isReturned() { return returned; }
    public void setReturned(boolean returned) { this.returned = returned; }

    @Override
    public String toString() {
        return "Booking{" +
                "id='" + id + '\'' +
                ", userId='" + userId + '\'' +
                ", itemId='" + itemId + '\'' +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", totalCost=" + totalCost +
                ", returned=" + returned +
                '}';
    }
}
