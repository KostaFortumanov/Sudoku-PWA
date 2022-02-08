package com.awd.sudoku.service;

import com.awd.sudoku.models.AppUser;
import com.awd.sudoku.models.LeaderboardTime;
import com.google.firebase.messaging.FirebaseMessagingException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ScheduledEvents {

    private final FirebaseMessagingService firebaseMessagingService;
    private final UserService userService;
    private final LeaderboardService leaderboardService;

    public ScheduledEvents(FirebaseMessagingService firebaseMessagingService, UserService userService, LeaderboardService leaderboardService) {
        this.firebaseMessagingService = firebaseMessagingService;
        this.userService = userService;
        this.leaderboardService = leaderboardService;
    }

    @Scheduled(cron = "0 0 */6 * * ?")
    public void sendReminderToPlay() {
        try {
            firebaseMessagingService.sendNotificationToAll("Sudoku", "Don't forget to play");
        } catch (FirebaseMessagingException e) {
            System.out.println("Error sending notification");
        }
    }

    @Scheduled(cron = "0 0 */6 * * ?")
    public void sendDailyChallengeReminder() {
        userService.findAll()
                .stream()
                .filter(user -> !user.getFinishedDaily() && user.getNotificationToken() != null)
                .forEach(user -> {
                    try {
                        firebaseMessagingService.sendNotificationToUser(
                                "Sudoku",
                                "New daily challenge is available",
                                user.getNotificationToken());
                    } catch (FirebaseMessagingException e) {
                        System.out.println("Error sending notification to user " + user.getUsername());
                    }
                });
    }

    @Scheduled(cron = "0 0 0 * * ?")
    public void clearDailyChallenge() {
        List<LeaderboardTime> dailyTimes = leaderboardService.findAllByDifficulty("DAILY");
        leaderboardService.deleteAll(dailyTimes);

        List<AppUser> users = userService.findAll()
                .stream()
                .filter(AppUser::getFinishedDaily)
                .collect(Collectors.toList());

        users.forEach(user -> user.setFinishedDaily(false));

        userService.saveAll(users);
    }
}
