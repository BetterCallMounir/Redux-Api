package com.example.redux.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.time.Instant;

@Getter
@Setter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;
    @NotBlank(message = "Username is required")
    private String username;
    @Email
    @NotEmpty(message = "Email is required")
    private String email;
    @NotBlank(message = "Password is required")
    private String password;
    @NotBlank
    private String salt;
    private Role role;
    @Column(unique = true)
    private Instant created;
    private boolean verified;
}
