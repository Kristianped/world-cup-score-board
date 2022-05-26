package org.kristian.sports.data.match;

import org.kristian.sports.data.Team;

public class MatchStatistics {

    private final Team team;

    private int goals = 0;
    private int yellowCards = 0;
    private int redCards = 0;

    private boolean penaltiesPlayed = false;

    MatchStatistics(Team team) {
        this.team = team;
    }

    public Team getTeam() {
        return team;
    }

    public int getGoals() {
        return goals;
    }

    int getYellowCards() {
        return yellowCards;
    }

    int getRedCards() {
        return redCards;
    }

    boolean isPenaltiesPlayed() {
        return penaltiesPlayed;
    }

    void goalScored() {
        this.goals++;
    }

    void yellowCardReceived() {
        this.yellowCards++;
    }

    void redCardReceived() {
        this.redCards++;
    }

    void setPenaltiesPlayed() {
        penaltiesPlayed = true;
    }
}
