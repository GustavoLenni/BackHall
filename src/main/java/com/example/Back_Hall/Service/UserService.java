package com.example.Back_Hall.Service;

import com.example.Back_Hall.dtos.LoginDto;
import com.example.Back_Hall.dtos.UserDto;
import com.example.Back_Hall.dtos.UserResponseDto;
import com.example.Back_Hall.model.User;
import com.example.Back_Hall.repositories.UserRepository;
import com.example.Back_Hall.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class UserService{

    @Autowired
    private UserRepository repository;

    @Autowired
    private BCryptPasswordEncoder encoder;

    public List<UserResponseDto> getAllUsers(){
        return repository.findAll()
                .stream()
                .map(user -> new UserResponseDto(user.getId(), user.getEmail()))
                .toList();
    }

    public User createUser(UserDto dto){
        User user = new User(dto.email(), encoder.encode(dto.password()));
        return repository.save(user);
    }

    public String login(LoginDto dto){
        User user = repository.findByEmail(dto.email());

        if(user == null){
            throw new RuntimeException("User not Found");
        }
        if(!encoder.matches(dto.password(), user.getPassword())){
            throw new RuntimeException("Wrong Password");
        }
        return JwtUtil.generateToken(user.getId(), user.getEmail());
    }

    public void deleteUser(Integer id){
        if(!repository.existsById(id)){
            throw new RuntimeException("User not Found");
        }
        repository.deleteById(id);
    }

    public User updateUser(Integer id,UserDto dto){
        User user = repository.findById(id).orElseThrow();

        user.changeEmail(dto.email());
        user.changePassword(encoder.encode(dto.password()));

        return repository.save(user);
    }

}
