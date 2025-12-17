package com.example.demo.dto;

import com.example.demo.model.User;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class LoginResponse {
    private int status;
    private String message;
    private String token;
}
