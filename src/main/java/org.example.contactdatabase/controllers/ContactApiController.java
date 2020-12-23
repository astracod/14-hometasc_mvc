package org.example.contactdatabase.controllers;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.example.contactdatabase.core.annotations.Autowired;
import org.example.contactdatabase.core.annotations.Controller;
import org.example.contactdatabase.core.annotations.GetMapping;
import org.example.contactdatabase.core.annotations.PostMapping;
import org.example.contactdatabase.dto.AddContactRequest;
import org.example.contactdatabase.dto.DeleteContactRequest;
import org.example.contactdatabase.dto.GetContactsResponse;
import org.example.contactdatabase.dto.StatusResponse;
import org.example.contactdatabase.entities.Contact;
import org.example.contactdatabase.repositories.ContactDao;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@Controller
public class ContactApiController {

    @Autowired
    ContactDao contactDao;

    Gson gson = new Gson();

    @GetMapping("/contacts/all")
    public void getAllContacts(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        System.out.println("get");
        List<Contact> contacts = contactDao.getAllContacts();
        GetContactsResponse response = new GetContactsResponse("ok", "", contacts);
        resp.getWriter().write(gson.toJson(response));
    }

    @PostMapping("/contacts/add")
    public void addContact(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String bodyStr = new String(req.getInputStream().readAllBytes());
        AddContactRequest addContactRequest = gson.fromJson(bodyStr, AddContactRequest.class);
        Contact contact = new Contact(null, addContactRequest.getName(), addContactRequest.getNumber());
        contactDao.addContact(contact);
        resp.setHeader("Content-type","application/json");
        resp.getWriter().write(gson.toJson(new StatusResponse("ok", "")));
    }

    //  curl -XPOST http://localhost:8080/contactdatabase/contacts/del -H "Content-Type:application/json" -d'{"id": 1}'
    @PostMapping("/contacts/del")
    public void delContact(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String myId = new String(req.getInputStream().readAllBytes());
        DeleteContactRequest deleteContactRequest = gson.fromJson(myId,DeleteContactRequest.class);
        contactDao.delContact(deleteContactRequest.getId());
        resp.setHeader("Content-type","application/json");
        resp.getWriter().write(gson.toJson(new StatusResponse("ok", "")));
    }

}
