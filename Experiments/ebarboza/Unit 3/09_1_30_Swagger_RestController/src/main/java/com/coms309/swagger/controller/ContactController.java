package com.coms309.swagger.controller;

import com.coms309.swagger.entity.Contact;
import com.coms309.swagger.service.ContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
//@RequestMapping("/api")
public class ContactController {

    @Autowired
    private ContactService contactService;

    @GetMapping("/contacts")
    public List<Contact> getAll(){
        List<Contact> contacts = contactService.getAll();
        return contacts;
    }

    @GetMapping("/contacts/{id}")
    public Optional<Contact> getById(@PathVariable String id){
        Optional<Contact> contact = contactService.getById(id);
        return contact;
    }

    // POST endpoint to add a new contact
    @PostMapping("/contacts")
    public Contact addContact(@RequestBody Contact contact) {
        return contactService.addContact(contact);
    }

    // PUT endpoint to update an existing contact
    @PutMapping("/contacts/{id}")
    public Contact updateContact(@PathVariable String id, @RequestBody Contact contactDetails) {
        return contactService.updateContact(id, contactDetails);
    }

    // DELETE endpoint to delete a contact
    @DeleteMapping("/contacts/{id}")
    public Map<String, Boolean> deleteContact(@PathVariable String id) {
        contactService.deleteContact(id);
        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return response;
    }
}
