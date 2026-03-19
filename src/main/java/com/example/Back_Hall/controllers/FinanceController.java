package com.example.Back_Hall.controllers;

import com.example.Back_Hall.dtos.FinanceDto;
import com.example.Back_Hall.model.Finance;
import com.example.Back_Hall.model.User;
import com.example.Back_Hall.repositories.FinanceRepository;
import com.example.Back_Hall.repositories.UserRepository;
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


    @GetMapping
    public ResponseEntity getAllFinances(){
        return ResponseEntity.ok(financeRepository.findAll());
    }

    @GetMapping("/user/{id}")
    public ResponseEntity getByUser(@PathVariable Integer id){
        if(!userRepository.existsById(id)){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not Found");
        }
        List<Finance> finance = financeRepository.findByUserId(id);

        return ResponseEntity.ok(finance);
    }


    @PostMapping
    public ResponseEntity postFinance(@RequestBody FinanceDto dto){
        User user = userRepository.findById(dto.userId()).orElse(null);
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

    @DeleteMapping("/{financeId}/user/{userid}")
    public ResponseEntity deleteFinance(@PathVariable Integer userid ,@PathVariable Integer financeId){
        Finance finance = financeRepository.findById(financeId).orElse(null);
        if(finance == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Finance not Found");
        }
        if(!finance.getUser().getId().equals(userid)){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("This Finance doesn't belong to this user");
        }
        financeRepository.delete(finance);
        return ResponseEntity.ok("Finance deleted");
    }

    @PutMapping("/{id}")
    public ResponseEntity putFinance(@PathVariable Integer id, @RequestBody FinanceDto dto){
        Finance finance = financeRepository.findById(id).orElse(null);
        if(finance == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Finance not Found");
        }

        if(!finance.getUser().getId().equals(dto.userId())){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("This Finance doesn't belong to this user");
        }

        finance.setProduct(dto.product());
        finance.setValue(dto.value());
        finance.setAmount(dto.amount());
        finance.setType(dto.type());

        return ResponseEntity.ok(financeRepository.save(finance));
    }

}
