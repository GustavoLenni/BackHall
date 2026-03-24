package com.example.Back_Hall.controllers;

import com.example.Back_Hall.Service.FinanceService;
import com.example.Back_Hall.dtos.FinanceDto;
import com.example.Back_Hall.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/finances")
public class FinanceController {

    @Autowired
    FinanceService service;

    @GetMapping("/my")
    public ResponseEntity getMyFinances(@RequestHeader("Authorization") String  authHeader){
        Integer userId = JwtUtil.getUserId(authHeader.replace("Bearer ",""));
        return ResponseEntity.ok(service.getUserFinance(userId));
    }

    @PostMapping
    public ResponseEntity postFinance(@RequestHeader("Authorization") String authHeader,@RequestBody FinanceDto dto){
        Integer userId = JwtUtil.getUserId(authHeader.replace("Bearer ",""));
        return ResponseEntity.status(HttpStatus.CREATED).body(service.createFinance(userId,dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteFinance(@RequestHeader("Authorization") String authHeader,@PathVariable Integer id){
        Integer userId = JwtUtil.getUserId(authHeader.replace("Bearer ",""));
        service.deleteFinance(userId,id);
        return ResponseEntity.ok("Finance deleted");
    }

    @PutMapping("/{id}")
    public ResponseEntity putFinance(@RequestHeader("Authorization") String authHeader,@PathVariable Integer id, @RequestBody FinanceDto dto){
        Integer userId = JwtUtil.getUserId(authHeader.replace("Bearer ",""));
        return ResponseEntity.ok(service.updateFinance(userId,id,dto));
    }
}
