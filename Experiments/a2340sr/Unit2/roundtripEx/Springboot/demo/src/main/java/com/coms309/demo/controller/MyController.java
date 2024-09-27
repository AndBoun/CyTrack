package com.coms309.demo.controller;

import com.coms309.demo.Repository.StudentRepo;
import com.coms309.demo.entity.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MyController {
    @Autowired
    StudentRepo sRepo;

    @GetMapping("/mytestapi")
    public String testMyAPI() {
        return "The API works well";
    }

    @PostMapping("/students")
    public Student saveStudent(@RequestBody Student student){
        return sRepo.save(student);
    }
}
