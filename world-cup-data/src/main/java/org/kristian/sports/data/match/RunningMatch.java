package org.kristian.sports.data.match;

import org.kristian.sports.data.Stage;
import org.kristian.sports.data.Team;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicBoolean;

public class RunningMatch {

    private static final int MINIMUM_GOALS = 0;
    private static final int MAXIMUM_GOALS = 6;

    private final MatchStatistics homeTeamStats;
    private final MatchStatistics awayTeamStats;

    private AtomicBoolean running = null;

    RunningMatch(Team homeTeam, Team awayTeam) {
        homeTeamStats = new MatchStatistics(homeTeam);
        awayTeamStats = new MatchStatistics(awayTeam);
    }

    @Override
    public String toString() {
        return homeTeamStats.getGoals() + " - " + awayTeamStats.getGoals();
    }

    void startMatch() {
        running = new AtomicBoolean(true);
    }

    MatchResult endMatch(Match match) {
        if (!running.get())
            return null;

        running.set(false);

        if (match.getStage() != Stage.GROUP) {
            if (isTied())
                playExtraTime();

            if (isTied()) {
                playPenaltyShootout(5);
                homeTeamStats.setPenaltiesPlayed();
                awayTeamStats.setPenaltiesPlayed();
            }
        }

        return new MatchResult(match, homeTeamStats, awayTeamStats);
    }

    boolean isFinished() {
        return !running.get();
    }

    void updateScore() {
        // 0-50, nothing happens
        // 51-80, goal
        // 81-95, yellow card
        // 96-100, red card
        var event = ThreadLocalRandom.current().nextInt(0, 101);

        if (event <= 50) {
            return;
        } else {
            var homeTeamEvent = ThreadLocalRandom.current().nextBoolean();

            if (event <= 80) {
                if (homeTeamEvent)
                    homeTeamScored();
                else
                    awayTeamScored();
            } else if (event <= 95) {
                if (homeTeamEvent)
                    homeTeamReceivedYellowCard();
                else
                    awayTeamReceivedYellowCard();
            } else {
                if (homeTeamEvent)
                    homeTeamReceivedRedCard();
                else
                    awayTeamReceivedRedCard();
            }
        }
    }

    private void playExtraTime() {
        var homeGoals = ThreadLocalRandom.current().nextInt(MINIMUM_GOALS, MAXIMUM_GOALS);
        var awayGoals = ThreadLocalRandom.current().nextInt(MINIMUM_GOALS, MAXIMUM_GOALS);

        for (int i = 0; i < homeGoals; i++)
            homeTeamStats.goalScored();

        for (int i = 0; i < awayGoals; i++)
            awayTeamStats.goalScored();
    }

    private void playPenaltyShootout(int iterations) {
        for (int i = 0; i < iterations; i++) {
            var homeToScore = ThreadLocalRandom.current().nextBoolean();
            var awayToScore = ThreadLocalRandom.current().nextBoolean();

            if (homeToScore)
                homeTeamScored();
            if (awayToScore)
                awayTeamScored();
        }

        if (isTied())
            playPenaltyShootout(1);
    }

    private boolean isTied() {
        return homeTeamStats.getGoals() == awayTeamStats.getGoals();
    }

    private void homeTeamScored() {
        homeTeamStats.goalScored();
    }

    private void awayTeamScored() {
        awayTeamStats.goalScored();
    }

    private void homeTeamReceivedYellowCard() {
        homeTeamStats.yellowCardReceived();
    }

    private void awayTeamReceivedYellowCard() {
        awayTeamStats.yellowCardReceived();
    }

    private void homeTeamReceivedRedCard() {
        homeTeamStats.redCardReceived();
    }

    private void awayTeamReceivedRedCard() {
        awayTeamStats.redCardReceived();
    }
}
