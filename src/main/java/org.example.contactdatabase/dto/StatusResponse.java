package org.example.contactdatabase.dto;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StatusResponse {
    private String status;
    private String message;
}
