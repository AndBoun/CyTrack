package com.coms309.cruddemo.controller;

import com.coms309.cruddemo.entity.Student;
import com.coms309.cruddemo.exception.ResourceNotFoundException;
import com.coms309.cruddemo.repository.StudentRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
public class MyController {

    @Autowired
    StudentRepo sRepo;

    @GetMapping("/mytestapi")
    public String testMyAPI(){
        return "The API works welll";
    }


    // This code is for POST a new student hardcoded
//    @PostMapping
//    public Student saveStudent(){
//        Student stu = new Student(100,"Abraham","Aldaco","aaldaco@iastate.edu");
//        return sRepo.save(stu);
//    }

    // Old Postmapping with response student
//    @PostMapping("/students")
//    public Student saveStudent(@RequestBody Student student){
//        return sRepo.save(student);
//    }

    @GetMapping("/students")
    public List<Student> getAllStudents() {
        // Find and return all students in the repository
        return sRepo.findAll();
    }


    @GetMapping("/students/{id}")
    public Map<String, Object> getStudentById(@PathVariable Integer id) {

        Map<String, Object> response = new HashMap<>();

        // Find the student by id
        Optional<Student> existingStudentOptional = sRepo.findById(id);

        if (existingStudentOptional.isPresent()) {
            Student existingStudent = existingStudentOptional.get();

            // Return the student information and HTTP status code
            response.put("student", existingStudent);
            response.put("status", "200"); // HTTP 200 OK
        } else {
            // If student does not exist, return a 404 response
            response.put("message", "Student not found with id: " + id);
            response.put("status", "404"); // HTTP 404 Not Found
        }

        // Return the JSON response
        return response;
    }


    @PostMapping("/students")
    public Map<String, String> saveStudent(@RequestBody Student student) {

        Map<String, String> response = new HashMap<>();

        // Check if a student with the same firstName, lastName, and email already exists
        Optional<Student> existingStudent = sRepo.findByFirstNameAndLastNameAndEmail(
                student.getFirstName(), student.getLastName(), student.getEmail()
        );


        if (existingStudent.isPresent()) {
            // If the student already exists, return a message indicating the same
            response.put("message", "Student already exists");
            response.put("status", "409"); // 409 Conflict HTTP status
        } else {
            // Save the new student
            sRepo.save(student);

            // Return success message
            response.put("message", "New student posted correctly");
            response.put("status", "200"); // 200 OK HTTP status
        }

        // Return the JSON response
        return response;
    }


    // Adding PUT request to update student details
    @PutMapping("/students/{id}")
    public Map<String, String> updateStudent(@PathVariable Integer id, @RequestBody Student studentDetails){

        Map<String, String> response = new HashMap<>();

        // Find the student by id
        Optional<Student> existingStudentOptional = sRepo.findById(id);


        if (existingStudentOptional.isPresent()) {
            Student existingStudent = existingStudentOptional.get();

            // Update the fields
            existingStudent.setFirstName(studentDetails.getFirstName());
            existingStudent.setLastName(studentDetails.getLastName());
            existingStudent.setEmail(studentDetails.getEmail());

            // Save the updated student
            sRepo.save(existingStudent);

            // Return success message
            response.put("message", "Student updated successfully");
            response.put("status", "200"); // HTTP 200 OK

        } else {
            // If student does not exist, return a 404 response
            response.put("message", "Student not found with id: " + id);
            response.put("status", "404"); // HTTP 404 Not Found
        }

        // Return the JSON response
        return response;
    }

    @DeleteMapping("/students/{id}")
    public Map<String, String> deleteStudent(@PathVariable Integer id) {

        Map<String, String> response = new HashMap<>();

        // Find the student by id
        Optional<Student> existingStudentOptional = sRepo.findById(id);

        if (existingStudentOptional.isPresent()) {
            // If the student exists, delete the student
            sRepo.deleteById(id);

            // Return success message
            response.put("message", "Student deleted successfully");
            response.put("status", "200"); // HTTP 200 OK
        } else {
            // If student does not exist, return a 404 response
            response.put("message", "Student not found with id: " + id);
            response.put("status", "404"); // HTTP 404 Not Found
        }

        // Return the JSON response
        return response;
    }


}
