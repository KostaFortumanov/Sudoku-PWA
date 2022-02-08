package com.awd.sudoku.repository;

import com.awd.sudoku.models.DailyChallenge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface DailyChallengeRepository extends JpaRepository<DailyChallenge, LocalDate> {

    DailyChallenge findByDate(LocalDate date);
}
