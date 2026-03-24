package com.example.Back_Hall.controllers;

import com.example.Back_Hall.Service.UserService;
import com.example.Back_Hall.dtos.LoginDto;
import com.example.Back_Hall.dtos.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService service;

    @GetMapping
    public ResponseEntity getAll(){
        return ResponseEntity.ok(service.getAllUsers());
    }

    @PostMapping
    public ResponseEntity saveUser(@RequestBody UserDto dto){
        return ResponseEntity.status(HttpStatus.CREATED).body(service.createUser(dto));
    }

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody LoginDto dto){
        return ResponseEntity.ok(service.login(dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteUser(@PathVariable Integer id){
        service.deleteUser(id);
        return ResponseEntity.status(HttpStatus.OK).body("This User was deleted");
    }

    @PutMapping("/{id}")
    public ResponseEntity updateUser(@PathVariable Integer id,@RequestBody UserDto dto){
        return ResponseEntity.ok(service.updateUser(id,dto));
    }
}
