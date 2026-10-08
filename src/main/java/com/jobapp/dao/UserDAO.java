package com.jobapp.dao;

import com.jobapp.exception.DatabaseException;
import com.jobapp.model.User;

import java.util.List;
import java.util.Optional;

public interface UserDAO {
    User createUser(User user) throws DatabaseException;
    Optional<User> findById(int id) throws DatabaseException;
    Optional<User> findByEmail(String email) throws DatabaseException;
    List<User> findAll() throws DatabaseException;
    boolean updateUser(User user) throws DatabaseException;
    boolean deleteUser(int id) throws DatabaseException;
}
