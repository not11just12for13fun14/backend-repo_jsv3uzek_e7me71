package com.orms.store;

import com.orms.model.Booking;
import com.orms.model.Item;
import com.orms.model.Role;
import com.orms.model.User;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class DataStore {
    private static final String DATA_DIR = "data";
    private static final String USERS_FILE = DATA_DIR + "/users.ser";
    private static final String ITEMS_FILE = DATA_DIR + "/items.ser";
    private static final String BOOKINGS_FILE = DATA_DIR + "/bookings.ser";

    private Map<String, User> users = new HashMap<>();
    private Map<String, Item> items = new HashMap<>();
    private Map<String, Booking> bookings = new HashMap<>();

    public DataStore() {
        ensureDataDir();
        loadAll();
        seedAdmin();
    }

    private void ensureDataDir() {
        try {
            Files.createDirectories(Path.of(DATA_DIR));
        } catch (IOException e) {
            throw new RuntimeException("Failed to create data directory", e);
        }
    }

    @SuppressWarnings("unchecked")
    private void loadAll() {
        users = (Map<String, User>) readObject(USERS_FILE, new HashMap<String, User>());
        items = (Map<String, Item>) readObject(ITEMS_FILE, new HashMap<String, Item>());
        bookings = (Map<String, Booking>) readObject(BOOKINGS_FILE, new HashMap<String, Booking>());
    }

    private Object readObject(String file, Object defaultObj) {
        File f = new File(file);
        if (!f.exists()) return defaultObj;
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(f))) {
            return ois.readObject();
        } catch (Exception e) {
            System.err.println("Warning: failed to read " + file + ", recreating. " + e);
            return defaultObj;
        }
    }

    private void saveObject(String file, Object obj) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
            oos.writeObject(obj);
        } catch (IOException e) {
            throw new RuntimeException("Failed to save data to " + file, e);
        }
    }

    private void saveAll() {
        saveObject(USERS_FILE, users);
        saveObject(ITEMS_FILE, items);
        saveObject(BOOKINGS_FILE, bookings);
    }

    private void seedAdmin() {
        if (users.values().stream().noneMatch(u -> u.getRole() == Role.ADMIN)) {
            User admin = new User("admin", "admin123", Role.ADMIN);
            users.put(admin.getId(), admin);
            saveAll();
        }
    }

    // Users
    public Optional<User> findUserByUsername(String username) {
        return users.values().stream().filter(u -> u.getUsername().equalsIgnoreCase(username)).findFirst();
    }

    public User addUser(String username, String password, Role role) {
        if (findUserByUsername(username).isPresent()) {
            throw new IllegalArgumentException("Username already exists");
        }
        User u = new User(username, password, role);
        users.put(u.getId(), u);
        saveAll();
        return u;
    }

    public Collection<User> getAllUsers() { return users.values(); }

    // Items
    public Item addItem(String name, String desc, double pricePerDay, String ownerUserId) {
        Item item = new Item(name, desc, pricePerDay, ownerUserId);
        items.put(item.getId(), item);
        saveAll();
        return item;
    }

    public Collection<Item> getAllItems() { return items.values(); }

    public Optional<Item> getItemById(String id) { return Optional.ofNullable(items.get(id)); }

    public void removeItem(String id) {
        items.remove(id);
        saveAll();
    }

    // Bookings
    public Booking createBooking(String userId, String itemId, java.time.LocalDate start, java.time.LocalDate end) {
        Item item = items.get(itemId);
        if (item == null || !item.isAvailable()) {
            throw new IllegalStateException("Item not available");
        }
        long days = java.time.temporal.ChronoUnit.DAYS.between(start, end);
        if (days <= 0) throw new IllegalArgumentException("End date must be after start date");
        double total = days * item.getPricePerDay();
        Booking b = new Booking(userId, itemId, start, end, total);
        bookings.put(b.getId(), b);
        item.setAvailable(false);
        saveAll();
        return b;
    }

    public void returnBooking(String bookingId) {
        Booking b = bookings.get(bookingId);
        if (b == null) throw new IllegalArgumentException("Booking not found");
        if (b.isReturned()) throw new IllegalStateException("Already returned");
        b.setReturned(true);
        getItemById(b.getItemId()).ifPresent(i -> i.setAvailable(true));
        saveAll();
    }

    public Collection<Booking> getAllBookings() { return bookings.values(); }
}
