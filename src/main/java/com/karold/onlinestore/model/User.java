package com.karold.onlinestore.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.karold.onlinestore.security.payload.SignupRequest;
import lombok.AllArgsConstructor;
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

    public User(Long id, @NotBlank String name, @NotBlank String surname, @Email String email, @NotBlank String password, UserType userType, Address address) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.password = password;
        this.userType = userType;
        this.addresses.add(address);
    }

    public void createCustomer(Address address, PasswordEncoder passwordEncoder){
        this.addresses.add(address);
        this.userType = UserType.CUSTOMER;
        this.password = passwordEncoder.encode(this.password);
    }

    public String getFullName(){
        return this.name+" "+this.surname;
    }
}
