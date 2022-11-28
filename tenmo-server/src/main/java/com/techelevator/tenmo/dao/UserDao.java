package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.User;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

public interface UserDao {

    List<User> findAll();

    User getUserById(int id);

    User findByUsername(String username);

    double getBalance(int id);

    int findIdByUsername(String username);

    boolean create(String username, String password);
}
