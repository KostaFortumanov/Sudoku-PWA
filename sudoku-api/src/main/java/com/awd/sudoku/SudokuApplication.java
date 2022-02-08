package com.awd.sudoku;

import com.awd.sudoku.models.DailyChallenge;
import com.awd.sudoku.service.DailyChallengeService;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.time.LocalDate;

@EnableScheduling
@SpringBootApplication
public class SudokuApplication {

    private final DailyChallengeService dailyChallengeService;

    public SudokuApplication(DailyChallengeService dailyChallengeService) {
        this.dailyChallengeService = dailyChallengeService;
    }

    public static void main(String[] args) {
        SpringApplication.run(SudokuApplication.class, args);
    }

    @Bean
    FirebaseMessaging firebaseMessaging() throws IOException {
        GoogleCredentials googleCredentials = GoogleCredentials
                .fromStream(new ClassPathResource("firebase-service-account.json").getInputStream());
        FirebaseOptions firebaseOptions = FirebaseOptions
                .builder()
                .setCredentials(googleCredentials)
                .build();
        FirebaseApp app = FirebaseApp.initializeApp(firebaseOptions, "my-app");
        return FirebaseMessaging.getInstance(app);
    }

    @PostConstruct
    public void addDailyChallenges() {
        long increment = 0;
        if (dailyChallengeService.findByDate(LocalDate.now()) == null) {
            dailyChallengeService.save(new DailyChallenge(LocalDate.now().plusDays(increment++),
                    "4.1...86..78..1..2.5.......5..62.1.....3.8.....7.49..3.......3.2..1..45..46...9.7"));
            dailyChallengeService.save(new DailyChallenge(LocalDate.now().plusDays(increment++),
                    "1..674............47..18...63....254....9....712....89...76..98............853..1"));
            dailyChallengeService.save(new DailyChallenge(LocalDate.now().plusDays(increment++),
                    "42........3..26...7.....62..9.5.8...2..7.9..6...1.2.3..17.....2...87..6........41"));
            dailyChallengeService.save(new DailyChallenge(LocalDate.now().plusDays(increment++),
                    "1..39.6..3..4...5.....8..4....97.28...6...9...29.14....5..4.....7...8..6..1.69..5"));
            dailyChallengeService.save(new DailyChallenge(LocalDate.now().plusDays(increment++),
                    ".5..97.8.4.....7.6.8.2.6...87..5.....2.....4.....3..97...8.5.3.5.8.....1.9.72..6."));
            dailyChallengeService.save(new DailyChallenge(LocalDate.now().plusDays(increment++),
                    "2.....4.....95....86.....3......7.56.25...84.43.5......4.....27....61.....3.....5"));
            dailyChallengeService.save(new DailyChallenge(LocalDate.now().plusDays(increment++),
                    "5....42...1.5.7.36.9.......9..8..1.38.......27.5..1..4.......4.42.6.8.7...67....9"));
            dailyChallengeService.save(new DailyChallenge(LocalDate.now().plusDays(increment++),
                    ".86...1.....9.6...5...1..27.24.3..5....6.4....6..9.24.89..5...3...7.9.....1...97."));
            dailyChallengeService.save(new DailyChallenge(LocalDate.now().plusDays(increment++),
                    ".9..3..2......5....78.....1.26......5.74.26.9......73.4.....19....8......1..6..4."));
            dailyChallengeService.save(new DailyChallenge(LocalDate.now().plusDays(increment++),
                    ".6.7..2...1...23.....59....1.....5.6.2.....9.7.5.....4....23.....34...1...7..8.5."));
            dailyChallengeService.save(new DailyChallenge(LocalDate.now().plusDays(increment++),
                    "..8.3...4..3..526......98...1...7..8..9.6.5..4..3...7...47......875..4..9...8.1.."));
            dailyChallengeService.save(new DailyChallenge(LocalDate.now().plusDays(increment),
                    "..3.....8.497..1.....65......59....178.....323....15......67.....7..261.4.....3.."));
        }
    }
}
