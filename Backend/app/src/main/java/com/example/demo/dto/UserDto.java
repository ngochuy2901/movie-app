package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserDto {
    private Long id;                 // nullable
    private String fullName;
    private String email;
    private String phoneNumber;
    private String username;
    private String imgUrl;          // nullable
    private String gender;
    private String dateOfBirth;
}