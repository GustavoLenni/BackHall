package com.example.Back_Hall.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String email;
    private String password;

    @OneToMany(mappedBy = "user")
    @JsonIgnore
    private List<Finance> finances;

    public User(){

    }
    public User(String email, String password){
        this.email = email;
        this.password = password;
    }

    public Integer getId(){
        return id;
    }
    public String getEmail(){
        return email;
    }
    public String getPassword(){
        return password;
    }

    public void changeEmail(String email){
        if(email == null || email.isBlank()){
            throw new RuntimeException("Invalid Email");
        }
        this.email = email;
    }

    public void changePassword(String password){
        this.password = password;
    }

}
