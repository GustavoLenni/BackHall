package com.example.Back_Hall.Service;

import com.example.Back_Hall.dtos.FinanceDto;
import com.example.Back_Hall.model.Finance;
import com.example.Back_Hall.model.User;
import com.example.Back_Hall.repositories.FinanceRepository;
import com.example.Back_Hall.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class FinanceService {
    @Autowired
    FinanceRepository financeRepository;
    @Autowired
    UserRepository userRepository;

    public List<Finance> getUserFinance(Integer userId){
        return financeRepository.findByUserId(userId);
    }

    public Finance createFinance(Integer userId,FinanceDto dto){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not Found"));

        Finance finance = new Finance(
                dto.product(),
                dto.value(),
                dto.amount(),
                dto.type(),
                user
        );
        return financeRepository.save(finance);
    }

    public void deleteFinance(Integer userId,Integer financeId){
        Finance finance = financeRepository.findById(financeId)
                .orElseThrow(() -> new RuntimeException("Finance not found"));
        if(!finance.getUser().getId().equals(userId)){
            throw new RuntimeException("Not allowed");
        }

        financeRepository.delete(finance);
    }
    public Finance updateFinance(Integer userId,Integer financeId,FinanceDto dto){
        Finance finance = financeRepository.findById(financeId).orElseThrow(() -> new RuntimeException("Finance not found"));
        if(!finance.getUser().getId().equals(userId)){
            throw new RuntimeException("Not allowed");
        }

        finance.updateData(
                dto.product(),
                dto.value(),
                dto.amount(),
                dto.type()
        );
        return financeRepository.save(finance);
    }
}
