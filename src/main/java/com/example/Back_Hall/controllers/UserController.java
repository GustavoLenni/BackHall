package com.example.Back_Hall.controllers;

import com.example.Back_Hall.dtos.LoginDto;
import com.example.Back_Hall.dtos.UserDto;
import com.example.Back_Hall.dtos.UserResponseDto;
import com.example.Back_Hall.repositories.UserRepository;
import com.example.Back_Hall.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import com.example.Back_Hall.model.User;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    UserRepository repository;
    @Autowired
    private BCryptPasswordEncoder encoder;

    @GetMapping
    public ResponseEntity getAll(){
        List<User> listUsers =repository.findAll();

        List<UserResponseDto> response = listUsers.stream()
                .map(user -> new UserResponseDto(user.getId(), user.getEmail()))
                .toList();
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity saveUser(@RequestBody UserDto dto){
        var user = new User();
        user.setEmail(dto.email());
        user.setPassword(encoder.encode(dto.password()));
        return ResponseEntity.status(HttpStatus.CREATED).body(repository.save(user));
    }

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody LoginDto dto){
        User user = repository.findByEmail(dto.email());

        if(user == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not Found");
        }

        if(!encoder.matches(dto.password(), user.getPassword())){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Wrong password");
        }

        String token = JwtUtil.generateToken(user.getId(),user.getEmail());

        return ResponseEntity.ok(token);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteUser(@PathVariable Integer id){
        if(!repository.existsById(id)){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not Found");
        }
        repository.deleteById(id);
        return ResponseEntity.status(HttpStatus.OK).body("This User was deleted");
    }

    @PutMapping("/{id}")
    public ResponseEntity updateUser(@PathVariable Integer id,@RequestBody UserDto dto){
        var user = repository.findById(id).orElse(null);
        if(user == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not Found");
        }
        user.setEmail(dto.email());
        user.setPassword(encoder.encode(dto.password()));
        return ResponseEntity.ok(repository.save(user));
    }



}
