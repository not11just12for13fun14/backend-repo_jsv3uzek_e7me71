package com.orms.model;

import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;
import java.util.UUID;

public class User implements Serializable {
    private static final long serialVersionUID = 1L;

    private final String id;
    private String username;
    private String passwordHash;
    private Role role;

    public User(String username, String rawPassword, Role role) {
        this.id = UUID.randomUUID().toString();
        this.username = username;
        this.passwordHash = hash(rawPassword);
        this.role = role;
    }

    public String getId() { return id; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }

    public boolean checkPassword(String raw) {
        return Objects.equals(passwordHash, hash(raw));
    }

    public void setPassword(String raw) {
        this.passwordHash = hash(raw);
    }

    private static String hash(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] bytes = md.digest(input.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : bytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Hashing algorithm not available", e);
        }
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", username='" + username + '\'' +
                ", role=" + role +
                '}';
    }
}
