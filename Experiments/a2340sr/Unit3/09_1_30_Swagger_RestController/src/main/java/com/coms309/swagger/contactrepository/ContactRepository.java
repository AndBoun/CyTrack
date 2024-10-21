package com.coms309.swagger.contactrepository;

import com.coms309.swagger.entity.Contact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


public interface ContactRepository extends JpaRepository<Contact, String> {
    // no need more code
}
