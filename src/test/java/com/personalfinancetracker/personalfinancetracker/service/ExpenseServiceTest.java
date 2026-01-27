package com.personalfinancetracker.personalfinancetracker.service;

import com.personalfinancetracker.personalfinancetracker.dto.ExpenseRequestDTO;
import com.personalfinancetracker.personalfinancetracker.dto.ExpenseResponseDTO;
import com.personalfinancetracker.personalfinancetracker.exception.InvalidRequestException;
import com.personalfinancetracker.personalfinancetracker.exception.ResourceNotFoundException;
import com.personalfinancetracker.personalfinancetracker.model.Expense;
import com.personalfinancetracker.personalfinancetracker.model.User;
import com.personalfinancetracker.personalfinancetracker.repo.ExpenseRepo;
import com.personalfinancetracker.personalfinancetracker.repo.UserRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ExpenseServiceTest {

    @Mock
    private ExpenseRepo expenseRepo;

    @Mock
    private UserRepo userRepo;

    @InjectMocks
    private ExpenseService expenseService;

    private User user;
    private Expense expense;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1);
        
        expense = new Expense();
        expense.setId(101);
        expense.setAmount(50.0);
        expense.setCategory("Food");
        expense.setUser(user);
    }

    @Test
    void findByUserId_Success() {
        when(userRepo.findById(1)).thenReturn(Optional.of(user));
        when(expenseRepo.findByUserId(1)).thenReturn(List.of(expense));

        List<ExpenseResponseDTO> result = expenseService.findByUserId(1);

        assertFalse(result.isEmpty());
        assertEquals(101, result.get(0).getId());
    }

    @Test
    void addExpense_Success() {
        ExpenseRequestDTO request = new ExpenseRequestDTO();
        request.setUserId(1);
        request.setAmount(100.0);
        request.setCategory("Rent");

        when(userRepo.findById(1)).thenReturn(Optional.of(user));
        when(expenseRepo.save(any(Expense.class))).thenReturn(expense);

        ExpenseResponseDTO result = expenseService.addExpense(request);

        assertNotNull(result);
        verify(expenseRepo).save(any(Expense.class));
    }

    @Test
    void addExpense_InvalidAmount() {
        ExpenseRequestDTO request = new ExpenseRequestDTO();
        request.setAmount(-10.0);

        assertThrows(InvalidRequestException.class, () -> expenseService.addExpense(request));
    }

    @Test
    void findByUserIdExpenseId_Success() {
        when(expenseRepo.findByUserIdAndExpenseId(1, 101)).thenReturn(expense);

        ExpenseResponseDTO result = expenseService.findByUserIdExpenseId(1, 101);

        assertEquals(101, result.getId());
    }

    @Test
    void updateExpense_Success() {
        ExpenseRequestDTO updateReq = new ExpenseRequestDTO();
        updateReq.setAmount(75.0);
        updateReq.setCategory("Utilities");

        when(expenseRepo.findById(101)).thenReturn(Optional.of(expense));
        when(expenseRepo.save(any(Expense.class))).thenReturn(expense);

        ExpenseResponseDTO result = expenseService.updateExpenseById(101, updateReq);

        assertNotNull(result);
        verify(expenseRepo).save(expense);
    }

    @Test
    void deleteById_Success() {
        when(expenseRepo.findById(101)).thenReturn(Optional.of(expense));
        
        expenseService.deleteById(101);

        verify(expenseRepo).delete(expense);
    }

    @Test
    void deleteById_NotFound() {
        when(expenseRepo.findById(999)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> expenseService.deleteById(999));
    }
}