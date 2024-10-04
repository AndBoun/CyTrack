package com.coms309.cruddemo.controller;

import com.coms309.cruddemo.entity.Student;
import com.coms309.cruddemo.exception.ResourceNotFoundException;
import com.coms309.cruddemo.repository.StudentRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class MyController {

    @Autowired
    StudentRepo sRepo;

    @GetMapping("/mytestapi")
    public String testMyAPI(){
        return "The API works well";
    }

//    @PostMapping
//    public Student saveStudent(){
//        Student stu = new Student(100,"Abraham","Aldaco","aaldaco@iastate.edu");
//        return sRepo.save(stu);
//    }

    @PostMapping("/students")
    public Student saveStudent(@RequestBody Student student){
        return sRepo.save(student);
    }

    // Adding PUT request to update student details
    @PutMapping("/students/{id}")
    public ResponseEntity<Student> updateStudent(@PathVariable Integer id, @RequestBody Student studentDetails){
        // Find the student by id
        Student existingStudent = sRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + id));

        // Update the fields
        existingStudent.setFirstName(studentDetails.getFirstName());
        existingStudent.setLastName(studentDetails.getLastName());
        existingStudent.setEmail(studentDetails.getEmail());

        // Save the updated student
        final Student updatedStudent = sRepo.save(existingStudent);

        // Return response entity
        return ResponseEntity.ok(updatedStudent);
    }

}
