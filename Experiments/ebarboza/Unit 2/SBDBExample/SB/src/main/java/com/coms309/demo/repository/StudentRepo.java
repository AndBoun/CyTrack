package com.coms309.demo.repository;

import com.coms309.demo.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;


public interface StudentRepo extends JpaRepository <Student, Integer> {

}
