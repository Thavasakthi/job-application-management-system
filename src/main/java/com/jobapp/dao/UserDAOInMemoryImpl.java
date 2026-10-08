package com.jobapp.dao;

import com.jobapp.exception.DatabaseException;
import com.jobapp.model.Role;
import com.jobapp.model.User;

import java.sql.Timestamp;
import java.util.*;

public class UserDAOInMemoryImpl implements UserDAO {
    private static final Map<Integer, User> userMap = new HashMap<>();
    private static int idCounter = 1;

    public UserDAOInMemoryImpl() {
        if (userMap.isEmpty()) {
            seedData();
        }
    }

    private void seedData() {
        createUserInternal(new User("System Administrator", "admin@jobportal.com", "admin123", Role.ADMIN, "9876543210"));
        createUserInternal(new User("Sarah Connor (Tech Recruiter)", "sarah@google.com", "recruiter123", Role.RECRUITER, "9876543211"));
        createUserInternal(new User("Robert Downey (HR Lead)", "robert@microsoft.com", "recruiter123", Role.RECRUITER, "9876543212"));
        createUserInternal(new User("John Doe (Software Dev)", "john.doe@email.com", "user123", Role.APPLICANT, "9876543213"));
        createUserInternal(new User("Jane Smith (Full Stack Dev)", "jane.smith@email.com", "user123", Role.APPLICANT, "9876543214"));
        createUserInternal(new User("Tamil Selvan (Graduate Engineer)", "tamil@email.com", "user123", Role.APPLICANT, "9876543215"));
    }

    private synchronized User createUserInternal(User user) {
        user.setId(idCounter++);
        user.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        userMap.put(user.getId(), user);
        return user;
    }

    @Override
    public synchronized User createUser(User user) throws DatabaseException {
        return createUserInternal(user);
    }

    @Override
    public Optional<User> findById(int id) throws DatabaseException {
        return Optional.ofNullable(userMap.get(id));
    }

    @Override
    public Optional<User> findByEmail(String email) throws DatabaseException {
        if (email == null) return Optional.empty();
        return userMap.values().stream()
                .filter(u -> u.getEmail().equalsIgnoreCase(email.trim()))
                .findFirst();
    }

    @Override
    public List<User> findAll() throws DatabaseException {
        return new ArrayList<>(userMap.values());
    }

    @Override
    public synchronized boolean updateUser(User user) throws DatabaseException {
        if (userMap.containsKey(user.getId())) {
            userMap.put(user.getId(), user);
            return true;
        }
        return false;
    }

    @Override
    public synchronized boolean deleteUser(int id) throws DatabaseException {
        return userMap.remove(id) != null;
    }
}
