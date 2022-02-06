package com.awd.sudoku.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

@Entity
public class LeaderboardTime {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private int time;
    @Enumerated(EnumType.STRING)
    private Difficulty difficulty;

    @OneToOne
    private AppUser user;

    public LeaderboardTime(int time, Difficulty difficulty, AppUser user) {
        this.time = time;
        this.difficulty = difficulty;
        this.user = user;
    }
}
