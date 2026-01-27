package com.personalfinancetracker.personalfinancetracker.repo;

import com.personalfinancetracker.personalfinancetracker.model.Expense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExpenseRepo extends JpaRepository<Expense, Integer> {
    List<Expense> findByUserId(int id);

    @Query("SELECT e FROM Expense e WHERE e.id=:expenseId AND e.user.id = :userId")
    Expense findByUserIdAndExpenseId(int userId, int expenseId);
}
