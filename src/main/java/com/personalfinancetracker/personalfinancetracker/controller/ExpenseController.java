package com.personalfinancetracker.personalfinancetracker.controller;

import com.personalfinancetracker.personalfinancetracker.dto.ExpenseRequestDTO;
import com.personalfinancetracker.personalfinancetracker.dto.ExpenseResponseDTO;
import com.personalfinancetracker.personalfinancetracker.service.ExpenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/expense")
@CrossOrigin(origins = {"http://localhost:5173"})
public class ExpenseController {
    private final ExpenseService service;

    @Autowired
    public ExpenseController(ExpenseService service){
        this.service = service;
    }

    @GetMapping("all")
    public ResponseEntity<List<ExpenseResponseDTO>> getExpenses(){
        return new ResponseEntity<>(service.findAllExpenses(), HttpStatus.OK);
    }

    @GetMapping("expenses/{user_id}")
    public ResponseEntity<List<ExpenseResponseDTO>> getAllExpensesByUid(@PathVariable int user_id){
        List<ExpenseResponseDTO> expenses = service.findByUserId(user_id);

        return new ResponseEntity<>(expenses, HttpStatus.OK);
    }

    @PostMapping("expenses")
    public ResponseEntity<ExpenseResponseDTO> addExpense(@RequestBody ExpenseRequestDTO expense){
        return new ResponseEntity<>(service.addExpense(expense), HttpStatus.CREATED);
    }

    @GetMapping("expenses/{userId}/{expenseId}")
    public ResponseEntity<ExpenseResponseDTO> getExpenseByUidAndExpenseId(@PathVariable int userId, @PathVariable int expenseId){
        return new ResponseEntity<>(service.findByUserIdExpenseId(userId, expenseId), HttpStatus.OK);
    }

    @PutMapping("expenses/{expenseId}")
    public ResponseEntity<ExpenseResponseDTO> updateExpenseById(@PathVariable int expenseId, @RequestBody ExpenseRequestDTO expReqDto){
        return new ResponseEntity<>(service.updateExpenseById(expenseId, expReqDto), HttpStatus.CREATED);
    }

    @DeleteMapping("expenses/{expenseId}")
    public ResponseEntity<Void> deleteExpenseById(@PathVariable int expenseId){
        service.deleteById(expenseId);
        return ResponseEntity.noContent().build();
    }
}
