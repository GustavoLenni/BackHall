package com.example.Back_Hall.repositories;

import com.example.Back_Hall.model.Finance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FinanceRepository extends JpaRepository<Finance, Integer> {
    List<Finance> findByUserId(Integer userId);
}
