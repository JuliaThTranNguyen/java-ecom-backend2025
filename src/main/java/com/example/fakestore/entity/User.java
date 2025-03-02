package com.example.fakestore.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Entity
@Table(name = "users")
@FieldDefaults(level = AccessLevel.PRIVATE)
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    Long userId;
    @Column(name = "first_name")
    String firstName;
    @Column(name = "last_name")
    String lastName;
    String email;
    String password;
    @Enumerated(EnumType.STRING)
    Role role;
    String avatar;
    Boolean active;

    @OneToMany(mappedBy = "user")
    List<Order> orders;

    @OneToOne(mappedBy = "user")
    ForgotPassword forgotPassword;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    RefreshToken refreshToken;


}
