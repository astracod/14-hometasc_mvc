package org.example.contactdatabase.dto;

import lombok.Data;

@Data
public class AddContactRequest {
    private String name;
    private String number;
}
