package com.awd.sudoku.repository;

import com.awd.sudoku.models.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<AppUser, Long> {

    AppUser findByUsername(String username);
    boolean existsByUsername(String username);
}
