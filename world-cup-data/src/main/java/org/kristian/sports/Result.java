package org.kristian.sports;

import java.time.Duration;
import java.time.Instant;
import java.util.Objects;

public class Result implements Comparable<Result> {

    public enum Type {
        TIE, HOME_WIN, AWAY_WIN;
    }

    private final Instant started;
    private final Instant finished;

    private final Team homeTeam;
    private final Team awayTeam;
    private final int homeGoals;
    private final int awayGoals;

    public Result(Match match, int homeGoals, int awayGoals) {
        this.finished = Instant.now();
        this.started = match.getStartTime();
        this.homeTeam = match.getHomeTeam();
        this.awayTeam = match.getAwayTeam();
        this.homeGoals = homeGoals;
        this.awayGoals = awayGoals;
    }

    public int getTotalGoals() {
        return homeGoals + awayGoals;
    }

    public String getDuration() {
        var duration = Duration.between(started, finished);

        return String.format("%02d", duration.toSecondsPart()) + "s, "
                + String.format("%02d", duration.toMillisPart()) + "ms";
    }

    @Override
    public String toString() {
        return homeTeam.getName() + " " + homeGoals + " - " + awayTeam.getName() + " " + awayGoals;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Result other) {
            return homeTeam.equals(other.homeTeam) &&
                    awayTeam.equals(other.awayTeam) &&
                    getTotalGoals() == other.getTotalGoals() &&
                    finished.equals(other.finished);
        }

        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(homeTeam, awayTeam, homeGoals, awayGoals, finished);
    }

    @Override
    public int compareTo(Result o) {
        var totalGoals = getTotalGoals();
        var otherTotalGoals = o.getTotalGoals();

        if (totalGoals == otherTotalGoals)
            return finished.compareTo(o.finished);

        return totalGoals > otherTotalGoals ? -1 : 1;
    }
}
