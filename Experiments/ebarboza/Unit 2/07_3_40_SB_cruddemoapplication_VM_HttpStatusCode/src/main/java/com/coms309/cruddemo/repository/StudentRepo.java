package com.coms309.cruddemo.repository;

import com.coms309.cruddemo.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StudentRepo extends JpaRepository <Student, Integer> {
    Optional<Student> findByFirstNameAndLastNameAndEmail(String firstName, String lastName, String email);
}
