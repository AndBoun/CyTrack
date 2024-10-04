package com.coms309.cruddemo.repository;

import com.coms309.cruddemo.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepo extends JpaRepository <Student, Integer> {
}
