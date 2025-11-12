package com.orms;

import com.orms.model.Item;
import com.orms.model.Role;
import com.orms.model.User;
import com.orms.service.AuthService;
import com.orms.service.RentalService;
import com.orms.store.DataStore;

import java.time.LocalDate;
import java.util.Optional;
import java.util.Scanner;

public class App {
    private final DataStore store = new DataStore();
    private final AuthService auth = new AuthService(store);
    private final RentalService rentals = new RentalService(store);

    private User currentUser = null;

    public static void main(String[] args) {
        new App().run();
    }

    private void run() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Welcome to Online Renting Management System (Java Console)");

        loop:
        while (true) {
            try {
                if (currentUser == null) {
                    System.out.println("\n1) Register  2) Login  0) Exit");
                    switch (sc.nextLine().trim()) {
                        case "1": doRegister(sc); break;
                        case "2": doLogin(sc); break;
                        case "0": break loop;
                        default: System.out.println("Invalid choice");
                    }
                } else if (currentUser.getRole() == Role.ADMIN) {
                    adminMenu(sc);
                } else {
                    userMenu(sc);
                }
            } catch (Exception ex) {
                System.out.println("Error: " + ex.getMessage());
            }
        }
        System.out.println("Goodbye!");
    }

    private void doRegister(Scanner sc) {
        System.out.print("Choose username: ");
        String u = sc.nextLine().trim();
        System.out.print("Choose password: ");
        String p = sc.nextLine().trim();
        currentUser = auth.register(u, p, false);
        System.out.println("Registered and logged in as " + currentUser.getUsername());
    }

    private void doLogin(Scanner sc) {
        System.out.print("Username: ");
        String u = sc.nextLine().trim();
        System.out.print("Password: ");
        String p = sc.nextLine().trim();
        Optional<User> user = auth.login(u, p);
        if (user.isPresent()) {
            currentUser = user.get();
            System.out.println("Logged in as " + currentUser.getUsername() + " (" + currentUser.getRole() + ")");
        } else {
            System.out.println("Invalid credentials");
        }
    }

    private void logout() {
        currentUser = null;
        System.out.println("Logged out.");
    }

    private void userMenu(Scanner sc) {
        System.out.println("\nUser Menu: 1) Add Item  2) List Items  3) Rent Item  4) My Bookings  5) Return Booking  9) Logout");
        switch (sc.nextLine().trim()) {
            case "1":
                System.out.print("Item name: "); String name = sc.nextLine();
                System.out.print("Description: "); String desc = sc.nextLine();
                System.out.print("Price per day: "); double ppd = Double.parseDouble(sc.nextLine());
                Item it = rentals.addItem(name, desc, ppd, currentUser.getId());
                System.out.println("Added: " + it);
                break;
            case "2":
                rentals.listItems().forEach(System.out::println);
                break;
            case "3":
                System.out.print("Item ID to rent: "); String iid = sc.nextLine();
                System.out.print("Start date (YYYY-MM-DD): "); LocalDate s = LocalDate.parse(sc.nextLine());
                System.out.print("End date (YYYY-MM-DD): "); LocalDate e = LocalDate.parse(sc.nextLine());
                System.out.println("Created booking: " + rentals.rentItem(currentUser.getId(), iid, s, e));
                break;
            case "4":
                rentals.listBookings().stream().filter(b -> b.getUserId().equals(currentUser.getId())).forEach(System.out::println);
                break;
            case "5":
                System.out.print("Booking ID to return: "); String bid = sc.nextLine();
                rentals.returnItem(bid);
                System.out.println("Returned.");
                break;
            case "9": logout(); break;
            default: System.out.println("Invalid choice");
        }
    }

    private void adminMenu(Scanner sc) {
        System.out.println("\nAdmin Menu: 1) List Users  2) List Items  3) Remove Item  4) List Bookings  5) Promote User  9) Logout");
        switch (sc.nextLine().trim()) {
            case "1":
                store.getAllUsers().forEach(System.out::println);
                break;
            case "2":
                rentals.listItems().forEach(System.out::println);
                break;
            case "3":
                System.out.print("Item ID to remove: "); String iid = sc.nextLine();
                rentals.removeItem(iid);
                System.out.println("Removed item.");
                break;
            case "4":
                rentals.listBookings().forEach(System.out::println);
                break;
            case "5":
                System.out.print("Username to promote to ADMIN: "); String uname = sc.nextLine();
                store.findUserByUsername(uname).ifPresentOrElse(u -> { u.setRole(Role.ADMIN); System.out.println("Promoted."); }, () -> System.out.println("User not found"));
                break;
            case "9": logout(); break;
            default: System.out.println("Invalid choice");
        }
    }
}
