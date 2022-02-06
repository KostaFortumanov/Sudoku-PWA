package com.awd.sudoku.service;

import com.awd.sudoku.models.AppUser;
import com.awd.sudoku.models.Difficulty;
import com.awd.sudoku.models.LeaderboardTime;
import com.awd.sudoku.repository.LeaderboardRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LeaderboardService {

    private final LeaderboardRepository leaderboardRepository;

    public LeaderboardService(LeaderboardRepository leaderboardRepository) {
        this.leaderboardRepository = leaderboardRepository;
    }

    public List<LeaderboardTime> findAllByDifficulty(String difficulty) {
        return leaderboardRepository.findAllByDifficulty(Difficulty.valueOf(difficulty.toUpperCase()));
    }

    public List<LeaderboardTime> findTopTenByDifficulty(String difficulty) {
        Sort sort = Sort.by("time");
        Pageable pageable = PageRequest.of(0, 10, sort);
        return leaderboardRepository
                .findAllByDifficulty(pageable, Difficulty.valueOf(difficulty.toUpperCase()))
                .getContent();
    }

    public List<LeaderboardTime> findAllByUserAndDifficulty(AppUser user, String difficulty) {
        return leaderboardRepository.findAllByUserAndDifficulty(user, Difficulty.valueOf(difficulty.toUpperCase()));
    }

    public boolean userHasTimes(AppUser user) {
        return leaderboardRepository.countAllByUser(user) > 0;
    }

    public void save(LeaderboardTime leaderboardTime) {
        leaderboardRepository.save(leaderboardTime);
    }
}
