Online Renting Management System (Java - Core OOP)

Overview
A console-based Online Renting Management System built with core Java and OOP principles. It supports:
- User registration and login (roles: USER, ADMIN)
- Adding items for rent
- Browsing available items
- Creating and managing bookings (rent and return)
- Admin management (seed admin account, list users/items/bookings, remove items, promote users)

Persistence
- File-based persistence using Java serialization. Data files are stored under a local data directory created at runtime.

How to Compile and Run (using JDK 11+)
1) Compile
   javac -d out $(find src -name "*.java")

2) Run
   java -cp out com.orms.App

Default Admin
- Username: admin
- Password: admin123

Notes
- All data is saved locally in the data folder next to the application.
- Passwords are stored as SHA-256 hashes.
- IDs are generated with UUIDs.
