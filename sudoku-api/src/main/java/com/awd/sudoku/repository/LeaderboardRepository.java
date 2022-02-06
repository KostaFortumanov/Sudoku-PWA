package com.awd.sudoku.repository;

import com.awd.sudoku.models.AppUser;
import com.awd.sudoku.models.Difficulty;
import com.awd.sudoku.models.LeaderboardTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LeaderboardRepository extends JpaRepository<LeaderboardTime, Long> {

    List<LeaderboardTime> findAllByDifficulty(Difficulty difficulty);
    Page<LeaderboardTime> findAllByDifficulty(Pageable pageable, Difficulty difficulty);
    Integer countAllByUser(AppUser user);
    List<LeaderboardTime> findAllByUserAndDifficulty(AppUser user, Difficulty difficulty);
}
