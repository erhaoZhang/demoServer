package com.example.demo.pdf.controller;

import com.example.demo.pdf.bean.User;
import com.example.demo.pdf.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * ClassName: userController
 * Package: com.example.demo.pdf.controller
 * Description:
 *
 * @Author 张尔豪
 * @Create 2025/6/19 22:59
 * @Version 1.0
 */
@RestController
@RequestMapping(path="/api/v1")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @PostMapping(path = "user",consumes = {MediaType.APPLICATION_JSON_VALUE})
    public User addNewUser(@RequestBody User user) {
        return userRepository.save(user);
    }

    @GetMapping(path = "users")
    public List<User> getAllUsers() {
        List<User> all = userRepository.findAll();
        return all;
    }

}

