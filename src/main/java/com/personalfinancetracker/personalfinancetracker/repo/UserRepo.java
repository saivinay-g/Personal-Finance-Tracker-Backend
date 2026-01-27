package com.personalfinancetracker.personalfinancetracker.repo;

import com.personalfinancetracker.personalfinancetracker.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepo extends JpaRepository<User, Integer> {
    User findByEmail(String email);

    User findByName(String name);

    User findByNameAndPassword(String username, String password);
}
