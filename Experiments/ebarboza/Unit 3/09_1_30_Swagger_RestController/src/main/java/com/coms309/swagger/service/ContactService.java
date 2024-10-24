package com.coms309.swagger.service;

import com.coms309.swagger.contactrepository.ContactRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.coms309.swagger.entity.Contact;

import java.util.List;
import java.util.Optional;

@Service
public class ContactService {

    @Autowired
    ContactRepository contactRepository;

    // service to GET all
    public List<Contact> getAll(){
        return contactRepository.findAll();
    }

    // service to GET one Contact by Id
    public Optional<Contact> getById(String id){
        return contactRepository.findById(id);
    }

    // service to POST
    // Service to add a new contact
    public Contact addContact(Contact contact) {
        return contactRepository.save(contact);
    }

    // service to PUT
    // Service to update an existing contact
    public Contact updateContact(String id, Contact contactDetails) {
        Contact contact = contactRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Contact not found for this id :: " + id));
        contact.setName(contactDetails.getName());
        contact.setPhone(contactDetails.getPhone());
        final Contact updatedContact = contactRepository.save(contact);
        return updatedContact;
    }

    // service to DELETE
    // Service to delete a contact
    public void deleteContact(String id) {
        Contact contact = contactRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Contact not found for this id :: " + id));
        contactRepository.delete(contact);
    }


}
