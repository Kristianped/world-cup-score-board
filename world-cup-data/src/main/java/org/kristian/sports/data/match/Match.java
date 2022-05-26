package org.kristian.sports.data.match;

import org.kristian.sports.data.Stage;
import org.kristian.sports.data.Team;
import org.kristian.sports.util.MatchNotifier;

import java.time.Instant;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadLocalRandom;

public class Match {

    private final Stage stage;
    private final Team homeTeam;
    private final Team awayTeam;

    private Instant endTime;
    private Instant startTime;

    private RunningMatch runningMatch = null;
    private MatchNotifier matchNotifier;

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
        if (runningMatch != null)
            throw new IllegalStateException("The match has already been started started");

        this.startTime = Instant.now();
        this.matchNotifier = matchNotifier;
        this.matchNotifier.matchStarted();

        this.runningMatch = new RunningMatch(homeTeam, awayTeam);
        this.runningMatch.startMatch();

        var callable = new MatchCallable(this);
        executorService.submit(callable);
    }

    public void updateMatch() {
        runningMatch.updateScore();
        matchNotifier.matchUpdated();
    }

    public void endMatch() {
        if (runningMatch == null)
            throw new IllegalStateException("The match has not been started");


        var result = runningMatch.endMatch(this);

        // If a group-game, add score to the group and update statistics to each team
        if (stage == Stage.GROUP) {
            result.updateGroupStatistics(homeTeam);
            result.updateGroupStatistics(awayTeam);
            homeTeam.getGroup().addResult(result);
        }

        matchNotifier.matchEnded();
    }

    public boolean isFinished() {
        return runningMatch != null && runningMatch.isFinished();
    }

    public Instant getStartTime() {
        if (runningMatch == null)
            throw new IllegalStateException("Match has not started yet");

        return startTime;
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
}
