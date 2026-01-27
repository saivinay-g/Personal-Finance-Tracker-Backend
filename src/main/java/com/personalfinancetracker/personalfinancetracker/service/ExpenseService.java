package com.personalfinancetracker.personalfinancetracker.service;

import com.personalfinancetracker.personalfinancetracker.dto.ExpenseRequestDTO;
import com.personalfinancetracker.personalfinancetracker.dto.ExpenseResponseDTO;
import com.personalfinancetracker.personalfinancetracker.exception.InvalidRequestException;
import com.personalfinancetracker.personalfinancetracker.exception.ResourceNotFoundException;
import com.personalfinancetracker.personalfinancetracker.model.Expense;
import com.personalfinancetracker.personalfinancetracker.model.User;
import com.personalfinancetracker.personalfinancetracker.repo.ExpenseRepo;
import com.personalfinancetracker.personalfinancetracker.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ExpenseService {

    private final ExpenseRepo expenseRepo;
    private final UserRepo userRepo;

    @Autowired
    public ExpenseService(ExpenseRepo expenseRepo, UserRepo userRepo){
        this.expenseRepo = expenseRepo;
        this.userRepo = userRepo;
    }

    public List<ExpenseResponseDTO> findByUserId(int userId) {
        // Check if user exists
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        // Fetch expenses for the user
        List<Expense> expenses = expenseRepo.findByUserId(userId);

        // Map to DTO
        List<ExpenseResponseDTO> expensesDto = new ArrayList<>();
        for (Expense exp : expenses) {
            ExpenseResponseDTO expenseDto = new ExpenseResponseDTO();
            expenseDto.setId(exp.getId());
            expenseDto.setUserId(user.getId());
            expenseDto.setCategory(exp.getCategory());
            expenseDto.setAmount(exp.getAmount());
            expenseDto.setDate(exp.getDate());
            expenseDto.setDescription(exp.getDescription());
            expensesDto.add(expenseDto);
        }

        return expensesDto;
    }


    public ExpenseResponseDTO addExpense(ExpenseRequestDTO expense) {
        // Validate input
        if (expense.getAmount() <= 0) {
            throw new InvalidRequestException("Amount must be greater than zero");
        }
        if (expense.getCategory() == null || expense.getCategory().isBlank()) {
            throw new InvalidRequestException("Category cannot be empty");
        }

        // Fetch user or throw exception
        User user = userRepo.findById(expense.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User not found with id: " + expense.getUserId()
                ));

        // Map DTO to entity
        Expense exp = new Expense();
        exp.setAmount(expense.getAmount());
        exp.setCategory(expense.getCategory());
        exp.setDescription(expense.getDescription());
        exp.setUser(user);

        // Save entity
        Expense saved = expenseRepo.save(exp);

        // Map back to DTO for response
        ExpenseResponseDTO response = new ExpenseResponseDTO();
        response.setId(saved.getId());
        response.setUserId(user.getId());
        response.setAmount(saved.getAmount());
        response.setCategory(saved.getCategory());
        response.setDescription(saved.getDescription());
        response.setDate(saved.getDate());

        return response;
    }


    public ExpenseResponseDTO findByUserIdExpenseId(int userId, int expenseId) {
        Expense exp = expenseRepo.findByUserIdAndExpenseId(userId, expenseId);
        if(exp==null){
            throw new ResourceNotFoundException("Expense not found with the user "+userId+" and with the expense id "+expenseId);
        }else {
            ExpenseResponseDTO expDto = new ExpenseResponseDTO();
            expDto.setId(exp.getId());
            expDto.setCategory(exp.getCategory());
            expDto.setDescription(exp.getDescription());
            expDto.setDate(exp.getDate());
            expDto.setAmount(exp.getAmount());
            expDto.setUserId(exp.getUser().getId());

            return expDto;
        }
    }

    public ExpenseResponseDTO updateExpenseById(int expenseId, ExpenseRequestDTO expReqDto) {
        // Fetch expense or throw exception
        Expense exp = expenseRepo.findById(expenseId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Expense not found with id: " + expenseId));

        // Validate input
        if (expReqDto.getAmount() <= 0) {
            throw new InvalidRequestException("Amount must be greater than zero");
        }
        if (expReqDto.getCategory() == null || expReqDto.getCategory().isBlank()) {
            throw new InvalidRequestException("Category cannot be empty");
        }

        // Update fields
        exp.setAmount(expReqDto.getAmount());
        exp.setDescription(expReqDto.getDescription());
        exp.setCategory(expReqDto.getCategory());

        // Save entity
        Expense updated = expenseRepo.save(exp);

        // Map to DTO for response
        ExpenseResponseDTO response = new ExpenseResponseDTO();
        response.setId(updated.getId());
        response.setUserId(updated.getUser().getId());
        response.setAmount(updated.getAmount());
        response.setCategory(updated.getCategory());
        response.setDescription(updated.getDescription());
        response.setDate(updated.getDate());

        return response;
    }


    public void deleteById(int expenseId) {
        Expense exp = expenseRepo.findById(expenseId).orElseThrow(()-> new ResourceNotFoundException("Expense Doesn't exists in the DB with id: "+expenseId));
        expenseRepo.delete(exp);
    }

    public List<ExpenseResponseDTO> findAllExpenses() {
        List<Expense> expenses = expenseRepo.findAll();
        List<ExpenseResponseDTO> expenseResponseDTOS = new ArrayList<>();
        for(Expense expense: expenses){
            ExpenseResponseDTO responseDTO = new ExpenseResponseDTO();
            responseDTO.setUserId(expense.getUser().getId());
            responseDTO.setAmount(expense.getAmount());
            responseDTO.setDate(expense.getDate());
            responseDTO.setDescription(expense.getDescription());
            responseDTO.setCategory(expense.getCategory());
            responseDTO.setId(expense.getId());
            expenseResponseDTOS.add(responseDTO);
        }
        return expenseResponseDTOS;
    }
}
