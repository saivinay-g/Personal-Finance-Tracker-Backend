package com.personalfinancetracker.personalfinancetracker.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExpenseResponseDTO {
    private int id;
    private double amount;
    private String category;
    private String description;
    private LocalDateTime date;
    private int userId;
}
