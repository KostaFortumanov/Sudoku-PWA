package com.awd.sudoku.service;

import com.awd.sudoku.models.DailyChallenge;
import com.awd.sudoku.repository.DailyChallengeRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class DailyChallengeService {

    private final DailyChallengeRepository dailyChallengeRepository;

    public DailyChallengeService(DailyChallengeRepository dailyChallengeRepository) {
        this.dailyChallengeRepository = dailyChallengeRepository;
    }

    public void save(DailyChallenge dailyChallenge) {
        dailyChallengeRepository.save(dailyChallenge);
    }

    public DailyChallenge findByDate(LocalDate date) {
        return dailyChallengeRepository.findByDate(date);
    }
}
