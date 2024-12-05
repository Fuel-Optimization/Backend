package com.example.model.model;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")

public class UserController {

        @GetMapping("/hello")
        @CrossOrigin(origins = "*")
        public String sayHello() {
            return "Hello, World!";
        }
    }