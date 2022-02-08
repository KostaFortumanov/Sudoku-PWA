package com.awd.sudoku.controller;

import com.awd.sudoku.models.AppUser;
import com.awd.sudoku.payload.response.MessageResponse;
import com.awd.sudoku.service.DailyChallengeService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/daily")
public class DailyChallengeController {

    private final DailyChallengeService dailyChallengeService;

    public DailyChallengeController(DailyChallengeService dailyChallengeService) {
        this.dailyChallengeService = dailyChallengeService;
    }

    @GetMapping
    public ResponseEntity<?> getDailyChallenge() {
        AppUser user = (AppUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(user.getFinishedDaily()) {
            return ResponseEntity.badRequest().body(new MessageResponse("Daily challenge already completed. Come back tomorrow"));
        }

        return ResponseEntity.ok(dailyChallengeService.findByDate(LocalDate.now()).getSudokuString());
    }
}
