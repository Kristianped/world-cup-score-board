package org.kristian.sports;

import org.kristian.sports.util.MatchNotifier;
import org.kristian.sports.util.TimeBudget;
import org.kristian.sports.util.TimedTask;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicBoolean;

public class Match {

    private static final int MINIMUM_GOALS = 0;
    private static final int MAXIMUM_GOALS = 6;

    private static final long DURATION_BEFORE_FIRST_GOAL = 1000l;
    private static final long DURATION_BETWEEN_GOALS = 1000l;
    private static final int MAX_GOALS_IN_90_MIHUTES = 10;

    private final Stage stage;
    private final Team homeTeam;
    private final Team awayTeam;

    private RunningMatch runningMatch = null;

    public static Match createGroupGame(Team teamA, Team teamB) {
        return createKnockoutGame(Stage.GROUP, teamA, teamB);
    }

    public static Match createKnockoutGame(Stage stage, Team teamA, Team teamB) {
        if (ThreadLocalRandom.current().nextBoolean())
            return new Match(stage, teamA, teamB);

        return new Match(stage, teamB, teamA);
    }

    private Match(Stage stage, Team homeTeam, Team awayTeam) {
        this.stage = stage;
        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
    }

    public void startMatch(ExecutorService executorService, MatchNotifier matchNotifier) {
        runningMatch = new RunningMatch(matchNotifier);
        runningMatch.startMatch();

        var callable = new RunningMatchCallable();
        executorService.submit(callable);
    }

    public void endMatch() {
        if (runningMatch == null)
            throw new IllegalStateException("The match has not been started");

        runningMatch.endMatch(homeTeam.getGroup());
    }

    public boolean isFinished() {
        return runningMatch != null && runningMatch.isFinished();
    }

    public Instant getStartTime() {
        if (runningMatch == null)
            throw new IllegalStateException("Match has not started yet");

        return runningMatch.started;
    }

    public Team getHomeTeam() {
        return homeTeam;
    }

    public Team getAwayTeam() {
        return awayTeam;
    }

    public Stage getStage() {
        return stage;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        if (stage == Stage.GROUP) {
            sb.append(homeTeam.getGroup().toString());
        } else
            sb.append(stage.toString());

        sb.append(": ").append(homeTeam.getName()).append(" - ").append(awayTeam).append(": ");

        if (runningMatch != null)
            sb.append(runningMatch);
        else
            sb.append("(Not started");

        return sb.toString();
    }

    private class RunningMatch {
        private final MatchNotifier matchNotifier;
        private final Instant started;

        private int homeGoals;
        private int awayGoals;

        private AtomicBoolean running = null;

        RunningMatch(MatchNotifier matchNotifier) {
            this.matchNotifier = matchNotifier;
            started = Instant.now();
        }

        @Override
        public String toString() {
            return homeGoals + " - " + awayGoals;
        }

        void startMatch() {
            homeGoals = 0;
            awayGoals = 0;
            running = new AtomicBoolean(true);
            matchNotifier.notifyMatchUpdate();
        }

        void endMatch(Group group) {
            if (!running.get())
                return;

            running.set(false);

            if (stage != Stage.GROUP) {
                if (homeGoals == awayGoals)
                    playExtraTime();

                if (homeGoals == awayGoals)
                    playPenaltyShootout(5);
            }

            var result = new Result(Match.this, homeGoals, awayGoals);
            // If a group-game, add score to the group
            if (stage == Stage.GROUP)
                group.addResult(result);

            matchNotifier.notifyMatchEnded();
        }

        boolean isFinished() {
            return !running.get();
        }

        void updateScore() {
            var homeToScore = ThreadLocalRandom.current().nextBoolean();

            if (homeToScore)
                incrementHomeGoals();
            else
                incrementAwayGoals();

            matchNotifier.notifyMatchUpdate();
        }

        private void playExtraTime() {
            homeGoals += ThreadLocalRandom.current().nextInt(MINIMUM_GOALS, MAXIMUM_GOALS);
            awayGoals += ThreadLocalRandom.current().nextInt(MINIMUM_GOALS, MAXIMUM_GOALS);
        }

        private void playPenaltyShootout(int iterations) {
            for (int i = 0; i < iterations; i++) {
                var homeToScore = ThreadLocalRandom.current().nextBoolean();
                var awayToScore = ThreadLocalRandom.current().nextBoolean();

                if (homeToScore)
                    incrementHomeGoals();
                if (awayToScore)
                    incrementAwayGoals();
            }

            if (homeGoals == awayGoals)
                playPenaltyShootout(1);
        }

        private void incrementHomeGoals() {
            homeGoals++;
        }

        private void incrementAwayGoals() {
            awayGoals++;
        }
    }

    private class RunningMatchCallable implements Callable<Boolean> {

        public boolean updateMatch() {
            if (runningMatch.isFinished())
                return true;

            runningMatch.updateScore();
            return false;
        }

        @Override
        public Boolean call() throws Exception {
            var timeBudget = new TimeBudget.Builder()
                    .withInitialDelay(Duration.ofMillis(DURATION_BEFORE_FIRST_GOAL))
                    .withInterval(Duration.ofMillis(DURATION_BETWEEN_GOALS))
                    .withMaxIterations(MAX_GOALS_IN_90_MIHUTES)
                    .build();

            var task = new TimedTask(timeBudget);

            return task.run(this::updateMatch, Match.this::endMatch);
        }
    }
}
