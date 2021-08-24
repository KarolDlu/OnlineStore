package com.karold.onlinestore.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.karold.onlinestore.security.payload.SignupRequest;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @NotBlank
    private String name;

    @Column(nullable = false)
    @NotBlank
    private String surname;

    @Column(nullable = false, unique = true)
    @Email
    private String email;

    @Column(nullable = false)
    @NotBlank
    @JsonIgnore
    private String password;

    @Enumerated(value = EnumType.STRING)
    private UserType userType;

    @Column(nullable = false)
    @ManyToMany
    private List<Address> addresses = new ArrayList<>();

    public void createCustomer(Address address, PasswordEncoder passwordEncoder){
        this.addresses.add(address);
        this.userType = UserType.CUSTOMER;
        this.password = passwordEncoder.encode(this.password);
    }
}
