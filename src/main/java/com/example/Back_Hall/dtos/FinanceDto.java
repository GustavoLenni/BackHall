package com.example.Back_Hall.dtos;

import com.example.Back_Hall.model.User;
import com.example.Back_Hall.model.enums.Type;

public record FinanceDto(String product, Double value, Integer amount, Type type, Integer userId) {
}
