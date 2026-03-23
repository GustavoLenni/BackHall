package com.example.Back_Hall.controllers;

import com.example.Back_Hall.dtos.FinanceDto;
import com.example.Back_Hall.model.Finance;
import com.example.Back_Hall.model.User;
import com.example.Back_Hall.repositories.FinanceRepository;
import com.example.Back_Hall.repositories.UserRepository;
import com.example.Back_Hall.security.JwtUtil;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/finances")
public class FinanceController {


    @Autowired
    FinanceRepository financeRepository;
    @Autowired
    UserRepository userRepository;

    @GetMapping("/my")
    public ResponseEntity getMyFinances(@RequestHeader("Authorization") String  authHeader){
        String token =  authHeader.replace("Bearer ","");
        Integer userId = JwtUtil.getUserId(token);

        if(userId == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
        }

        return ResponseEntity.ok(financeRepository.findByUserId(userId));
    }


    @PostMapping
    public ResponseEntity postFinance(@RequestHeader("Authorization") String authHeader,@RequestBody FinanceDto dto){

        String token = authHeader.replace("Bearer ","");
        Integer userId = JwtUtil.getUserId(token);
        if(userId == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid Token");
        }

        User user = userRepository.findById(userId).orElse(null);
        if(user == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        Finance finance = new Finance();
        finance.setProduct(dto.product());
        finance.setValue(dto.value());
        finance.setAmount(dto.amount());
        finance.setType(dto.type());
        finance.setUser(user);

        return ResponseEntity.status(HttpStatus.CREATED).body(financeRepository.save(finance));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteFinance(@RequestHeader("Authorization") String authHeader,@PathVariable Integer id){
        String token = authHeader.replace("Bearer ", "");
        Integer userId = JwtUtil.getUserId(token);

        if(userId == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid Token");
        }

        Finance finance = financeRepository.findById(id).orElse(null);
        if(finance == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Finance not Found");
        }

        if(!finance.getUser().getId().equals(userId)){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("This Finance doesn't belong to this user");
        }
        financeRepository.delete(finance);
        return ResponseEntity.ok("Finance deleted");
    }

    @PutMapping("/{id}")
    public ResponseEntity putFinance(@RequestHeader("Authorization") String authHeader,@PathVariable Integer id, @RequestBody FinanceDto dto){
        String token = authHeader.replace("Bearer ", "");
        Integer userId = JwtUtil.getUserId(token);

        if(userId == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
        }

        Finance finance = financeRepository.findById(id).orElse(null);
        if(finance == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Finance not Found");
        }

        if(!finance.getUser().getId().equals(userId)){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("This Finance doesn't belong to this user");
        }

        finance.setProduct(dto.product());
        finance.setValue(dto.value());
        finance.setAmount(dto.amount());
        finance.setType(dto.type());

        return ResponseEntity.ok(financeRepository.save(finance));
    }

}
