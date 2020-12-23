package org.example.contactdatabase.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.example.contactdatabase.entities.Contact;
import java.util.List;

@Data
@AllArgsConstructor
public class GetContactsResponse {
    private String status;
    private String message;
    private List<Contact> data;
}
