package org.example.contactdatabase.repositories;

import org.example.contactdatabase.core.annotations.Component;
import org.example.contactdatabase.core.db.JdbcTemplate;
import org.example.contactdatabase.entities.Contact;

import java.util.Collections;
import java.util.List;

@Component
public class ContactDaoJdbc implements ContactDao {

    JdbcTemplate jdbcTemplate = new JdbcTemplate();

    @Override
    public List<Contact> getAllContacts() {
        return jdbcTemplate.query("SELECT * FROM contacts", new JdbcTemplate.PropertyBeanRowMapper<>(Contact.class));

    }

    @Override
    public void addContact(Contact contact) {

        jdbcTemplate.update("INSERT INTO contacts (name, number) VALUES (?,?)", List.of(contact.getName(),contact.getNumber()));
    }

    @Override
    public void delContact(Integer id) {
        jdbcTemplate.update("DELETE FROM contacts WHERE id=?", Collections.singletonList(id));
    }
}
