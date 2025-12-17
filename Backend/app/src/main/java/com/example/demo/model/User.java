package com.example.demo.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "user")   // Trùng với tên bảng MySQL của đại ca
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @NotBlank(message = "Fullname is required")
    private String fullName;

    @Column(nullable = false, unique = true)
    @NotBlank(message = "Email is required")
    private String email;

    @Column(nullable = false, unique = true)
    @NotBlank(message = "Phone number is required")
    private String phoneNumber;

    @Column(nullable = false, unique = true)
    @NotBlank(message = "Username is required")
    private String username;

    @Column(nullable = false)
    @NotBlank(message = "Password is required")
    private String password;

    private String imgUrl;

    @Column(nullable = false)
    private String gender;

    @Column(nullable = false)
    private String dateOfBirth;   // yyyy-MM-dd
}
