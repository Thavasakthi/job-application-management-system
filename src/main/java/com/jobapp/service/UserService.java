package com.jobapp.service;

import com.jobapp.dao.DAOFactory;
import com.jobapp.dao.UserDAO;
import com.jobapp.exception.DatabaseException;
import com.jobapp.exception.InvalidInputException;
import com.jobapp.exception.UserNotFoundException;
import com.jobapp.model.Role;
import com.jobapp.model.User;

import java.util.List;
import java.util.Optional;

public class UserService {
    private final UserDAO userDAO;

    public UserService() {
        this.userDAO = DAOFactory.getUserDAO();
    }

    public UserService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public User registerUser(String name, String email, String password, Role role, String phone)
            throws InvalidInputException, DatabaseException {
        if (name == null || name.trim().isEmpty()) {
            throw new InvalidInputException("User name cannot be empty.");
        }
        if (email == null || !email.contains("@") || !email.contains(".")) {
            throw new InvalidInputException("Invalid email format.");
        }
        if (password == null || password.trim().length() < 4) {
            throw new InvalidInputException("Password must be at least 4 characters long.");
        }

        Optional<User> existing = userDAO.findByEmail(email.trim());
        if (existing.isPresent()) {
            throw new InvalidInputException("An account with this email already exists: " + email);
        }

        User newUser = new User(name.trim(), email.trim(), password.trim(), role != null ? role : Role.APPLICANT, phone != null ? phone.trim() : "");
        return userDAO.createUser(newUser);
    }

    public User login(String email, String password) throws UserNotFoundException, InvalidInputException, DatabaseException {
        if (email == null || password == null) {
            throw new InvalidInputException("Email and password cannot be null.");
        }

        Optional<User> userOpt = userDAO.findByEmail(email.trim());
        if (userOpt.isEmpty()) {
            throw new UserNotFoundException("No user account found with email: " + email);
        }

        User user = userOpt.get();
        if (!user.getPassword().equals(password.trim())) {
            throw new InvalidInputException("Incorrect password for account: " + email);
        }

        return user;
    }

    public User getUserById(int userId) throws UserNotFoundException, DatabaseException {
        return userDAO.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + userId));
    }

    public List<User> getAllUsers() throws DatabaseException {
        return userDAO.findAll();
    }
}
