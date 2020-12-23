package org.example.contactdatabase.repositories;

import org.example.contactdatabase.entities.Contact;

import java.util.List;

public interface ContactDao {
    List<Contact> getAllContacts();
    void addContact(Contact contact);
    void delContact(Integer id);
}
