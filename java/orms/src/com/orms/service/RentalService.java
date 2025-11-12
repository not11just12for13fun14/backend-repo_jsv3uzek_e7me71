package com.orms.service;

import com.orms.model.Booking;
import com.orms.model.Item;
import com.orms.store.DataStore;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Optional;

public class RentalService {
    private final DataStore store;

    public RentalService(DataStore store) {
        this.store = store;
    }

    public Item addItem(String name, String desc, double pricePerDay, String ownerUserId) {
        return store.addItem(name, desc, pricePerDay, ownerUserId);
    }

    public Collection<Item> listItems() { return store.getAllItems(); }

    public Optional<Item> getItem(String id) { return store.getItemById(id); }

    public Booking rentItem(String userId, String itemId, LocalDate start, LocalDate end) {
        return store.createBooking(userId, itemId, start, end);
    }

    public void returnItem(String bookingId) { store.returnBooking(bookingId); }

    public Collection<Booking> listBookings() { return store.getAllBookings(); }

    public void removeItem(String id) { store.removeItem(id); }
}
