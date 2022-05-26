package org.kristian.sports.data.match;

import org.kristian.sports.data.Stage;
import org.kristian.sports.data.Team;

import java.time.Duration;
import java.time.Instant;
import java.util.Objects;

public class MatchResult implements Comparable<MatchResult> {

    public enum Type {
        TIE, HOME_WIN, AWAY_WIN, HOME_WIN_AFTER_EXTRA_TIME, AWAY_WIN_AFTER_EXTRA_TIME, HOME_WIN_AFTER_PENALTY_SHOOTOUT,
        AWAY_WIN_AFTER_PENALTY_SHOOTOUT
    }

    private final Match match;

    private final MatchStatistics homeTeamStats;
    private final MatchStatistics awayTeamStats;

    private final Instant finished;

    public MatchResult(Match match, MatchStatistics homeTeamStats, MatchStatistics awayTeamStats) {
        this.finished = Instant.now();
        this.match = match;
        this.homeTeamStats = homeTeamStats;
        this.awayTeamStats = awayTeamStats;
    }

    public int getTotalGoals() {
        return homeTeamStats.getGoals() + awayTeamStats.getGoals();
    }

    public void updateGroupStatistics(Team team) {
        var outfall = getOutfall();

        Team opposingTeam;
        int points;
        int yellowCards;
        int redCards;
        int goalsScored;
        int goalsConceded;

        if (team.equals(homeTeamStats.getTeam())) {
            opposingTeam = awayTeamStats.getTeam();
            points = outfall.equals(Type.HOME_WIN) ? 3 : (outfall.equals(Type.AWAY_WIN) ? 0 : 1);
            yellowCards = homeTeamStats.getYellowCards();
            redCards = homeTeamStats.getRedCards();
            goalsScored = homeTeamStats.getGoals();
            goalsConceded = awayTeamStats.getGoals();
        } else if (team.equals(awayTeamStats.getTeam())) {
            opposingTeam = awayTeamStats.getTeam();
            points = outfall.equals(Type.HOME_WIN) ? 0 : (outfall.equals(Type.AWAY_WIN) ? 3 : 1);
            yellowCards = awayTeamStats.getYellowCards();
            redCards = awayTeamStats.getRedCards();
            goalsScored = awayTeamStats.getGoals();
            goalsConceded = homeTeamStats.getGoals();
        } else {
            throw new IllegalArgumentException("Team " + team + " did not play this match: " + this);
        }

        team.addGroupStatistics(opposingTeam, points, goalsScored, goalsConceded, yellowCards, redCards);
    }

    public Type getOutfall() {
        var homeGoals = homeTeamStats.getGoals();
        var awayGoals = awayTeamStats.getGoals();

        if (match.getStage() == Stage.GROUP) {
            if (homeGoals == awayGoals)
                return Type.TIE;
            else if (homeGoals > awayGoals)
                return Type.HOME_WIN;
            else
                return Type.AWAY_WIN;
        } else {
            if (homeTeamStats.isPenaltiesPlayed()) {
                if (homeGoals > awayGoals)
                    return Type.HOME_WIN_AFTER_PENALTY_SHOOTOUT;
                else
                    return Type.AWAY_WIN_AFTER_PENALTY_SHOOTOUT;
            } else {
                if (homeGoals > awayGoals)
                    return Type.HOME_WIN_AFTER_EXTRA_TIME;
                else
                    return Type.AWAY_WIN_AFTER_EXTRA_TIME;
            }
        }
    }

    public Duration getDuration() {
        return Duration.between(match.getStartTime(), finished);
    }

    @Override
    public String toString() {
        return homeTeamStats.getTeam().getName() + " " + homeTeamStats.getGoals()
                + " - "
                + awayTeamStats.getTeam().getName() + " " + awayTeamStats.getGoals();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof MatchResult other) {
            return homeTeamStats.getTeam().equals(other.homeTeamStats.getTeam()) &&
                    awayTeamStats.getTeam().equals(other.awayTeamStats.getTeam()) &&
                    finished.equals(other.finished);
        }

        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(homeTeamStats.getTeam(), awayTeamStats.getTeam(), homeTeamStats.getGoals(), awayTeamStats.getGoals(), finished);
    }

    @Override
    public int compareTo(MatchResult o) {
        var totalGoals = getTotalGoals();
        var otherTotalGoals = o.getTotalGoals();

        if (totalGoals == otherTotalGoals)
            return o.finished.compareTo(finished);

        return totalGoals > otherTotalGoals ? -1 : 1;
    }
}
