package com.orms.service;

import com.orms.model.Role;
import com.orms.model.User;
import com.orms.store.DataStore;

import java.util.Optional;

public class AuthService {
    private final DataStore store;

    public AuthService(DataStore store) {
        this.store = store;
    }

    public User register(String username, String password, boolean asAdmin) {
        return store.addUser(username, password, asAdmin ? Role.ADMIN : Role.USER);
    }

    public Optional<User> login(String username, String password) {
        return store.findUserByUsername(username).filter(u -> u.checkPassword(password));
    }
}
