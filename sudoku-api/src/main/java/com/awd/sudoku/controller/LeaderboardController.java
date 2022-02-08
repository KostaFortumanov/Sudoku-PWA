package com.awd.sudoku.controller;

import com.awd.sudoku.models.AppUser;
import com.awd.sudoku.models.Difficulty;
import com.awd.sudoku.models.LeaderboardTime;
import com.awd.sudoku.payload.request.SaveTimeRequest;
import com.awd.sudoku.payload.response.LeaderboardResponse;
import com.awd.sudoku.service.LeaderboardService;
import com.awd.sudoku.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/leaderboard")
public class LeaderboardController {

    private final UserService userService;
    private final LeaderboardService leaderboardService;

    public LeaderboardController(UserService userService, LeaderboardService leaderboardService) {
        this.userService = userService;
        this.leaderboardService = leaderboardService;
    }

    @GetMapping("/{difficulty}")
    public ResponseEntity<?> getOnlineTimes(@PathVariable String difficulty) {

        List<LeaderboardTime> topTen = leaderboardService.findTopTenByDifficulty(difficulty);
        final int[] index = {1};
        List<LeaderboardResponse> response = topTen.stream()
                .map(time -> new LeaderboardResponse(index[0]++, time.getUser().getUsername(), time.getTime()))
                .collect(Collectors.toList());

        if (SecurityContextHolder.getContext().getAuthentication().getPrincipal() != "anonymousUser") {

            Predicate<String> isUserInTopTen = username -> topTen.stream().anyMatch(time -> time.getUser().getUsername().equals(username));

            AppUser user = (AppUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            String username = user.getUsername();
            if(!isUserInTopTen.test(username) && leaderboardService.userHasTimes(user)) {
                List<LeaderboardTime> allTimes = leaderboardService.findAllByDifficulty(difficulty)
                        .stream().sorted(Comparator.comparing(LeaderboardTime::getTime)).collect(Collectors.toList());

                for(int i=0; i<allTimes.size(); i++) {
                    if(allTimes.get(i).getUser().getUsername().equals(username)) {
                        response.add(new LeaderboardResponse(i+1, username, allTimes.get(i).getTime()));
                        break;
                    }
                }
            }
        }

        return ResponseEntity.ok(response);
    }

    @GetMapping("/myTimes/{difficulty}")
    public ResponseEntity<?> getMyTimes(@PathVariable String difficulty) {
        AppUser user = (AppUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<Integer> response = leaderboardService.findAllByUserAndDifficulty(user, difficulty)
                .stream()
                .map(LeaderboardTime::getTime)
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    @PostMapping("/saveTime")
    public void saveTime(@RequestBody SaveTimeRequest saveTimeRequest) {

        AppUser user = (AppUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(saveTimeRequest.getDifficulty().equalsIgnoreCase("daily")) {
            if(user.getFinishedDaily()) {
                return;
            }
            user.setFinishedDaily(true);
            userService.save(user);
        }

        LeaderboardTime leaderboardTime = new LeaderboardTime(
                saveTimeRequest.getTime(),
                Difficulty.valueOf(saveTimeRequest.getDifficulty().toUpperCase()),
                user);

        leaderboardService.save(leaderboardTime);
    }
}
