package com.dgut.salesmanagementsystem.service;

import com.dgut.salesmanagementsystem.model.UserDAO;
import com.dgut.salesmanagementsystem.pojo.Role;
import com.dgut.salesmanagementsystem.pojo.User;

public class UserService {
    private final UserDAO userDAO = new UserDAO();
    public void addUser(String name, String password, Role role) {
        userDAO.addUser(name, password, role);
    }
}
