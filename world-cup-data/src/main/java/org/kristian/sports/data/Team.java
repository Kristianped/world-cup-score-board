package org.kristian.sports.data;

import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

/**
 * A team consists of a name, which group the team is in and what stage(group, quarter-finals etc.) the team currently
 * is in
 */
public class Team implements Comparable<Team> {

    private final String name;
    private final Group group;
    private final Stage currentStage;

    private GroupStatistics groupStatistics;

    public Team(String name, Group group) {
        this.name = name;
        this.group = group;
        this.currentStage = Stage.GROUP;
    }

    /**
     * Get the name of the team
     * @return The team-name
     */
    public String getName() {
        return name;
    }

    /**
     * Get the group the team is in
     * @return The group
     */
    public Group getGroup() {
        return group;
    }

    /**
     * Get the stage the team is currently in
     * @return The stage
     */
    public Stage getCurrentStage() {
        return currentStage;
    }

    /**
     * Add statistics for the team about a match in the group stages
     * @param opposingTeam The opposing team
     * @param points The points gathered in the match (3 for win, 1 for draw, 0 for loss)
     * @param goalsScored Goals scored against opposing team
     * @param goalsConceded Goals conceded against opposing team
     * @param yellowCards Yellow cards received in the match
     * @param redCards Red cards received in the match
     */
    public void addGroupStatistics(Team opposingTeam, int points, int goalsScored, int goalsConceded, int yellowCards, int redCards) {
        if (groupStatistics == null)
            groupStatistics = new GroupStatistics();

        groupStatistics.totalPoints += points;
        groupStatistics.totalGoalsScored += goalsScored;
        groupStatistics.totalGoalsConceded += goalsConceded;
        groupStatistics.yellowCards += yellowCards;
        groupStatistics.redCards += redCards;
        groupStatistics.pointsAgainstOtherTeams.put(opposingTeam, points);
        groupStatistics.goalsScoredAgainstOtherTeams.put(opposingTeam, goalsScored);
        groupStatistics.goalsConcededAgainstOtherTeams.put(opposingTeam, goalsConceded);
    }

    /**
     * Get points collected in the group stage
     * @return The points
     */
    public int getPoints() {
        return groupStatistics.totalPoints;
    }

    /**
     * Get the goal difference in the group stage
     * @return The goal difference
     */
    public int getGoalDifference() {
        return groupStatistics.totalGoalsScored - groupStatistics.totalGoalsConceded;
    }

    /**
     * Get amount of goals scored in the group stage
     * @return Goals scored
     */
    public int getGoalsScored() {
        return groupStatistics.totalGoalsScored;
    }

    /**
     * Get amount of goals conceded in the group stage
     * @return Goals conceded
     */
    public int getGoalsConceded() {
        return groupStatistics.totalGoalsConceded;
    }

    /**
     * Get amount of yellow cards in the group stage
     * @return Yellow cards
     */
    public int getYellowCards() {
        return groupStatistics.yellowCards;
    }

    /**
     * Get amount of red cards in the group stage
     * @return Red cards
     */
    public int getRedCards() {
        return groupStatistics.redCards;
    }

    /**
     * Get amount of fair play points in the group stage; -1 for a yellow card, -5 for a red card
     * @return Fair play points
     */
    public int getFairPlayPoints() {
        return Math.negateExact(getYellowCards() + (5 * getRedCards()));
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Team otherTeam)
            return name.equals(otherTeam.name);

        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, currentStage.toString());
    }

    @Override
    public int compareTo(Team other) {
        // The ranking of teams in the group stage is determined as follows:
        // 1. Points obtained in all group matches
        // 2. Goal difference in all group matches
        // 3. Number of goals scored in all group matches
        // 4. Points obtained in the matches played between the teams in question
        // 5. Goal difference in the matches played between the teams in question
        // 6. Fair play points in all group matches (only one deduction can be applied to a player in a single match):
        //     Yellow card: −1 point;
        //     Indirect red card (second yellow card): −3 points;
        //     Direct red card: −4 points;
        //     Yellow card and direct red card: −5 points;
        // 7. Drawing of lots.

        // If no matches played, sort by name
        if (groupStatistics == null || other.groupStatistics == null)
            return name.compareTo(other.name);

        // 1:
        if (getPoints() > other.getPoints())
            return -1;
        else if (getPoints() < other.getPoints())
            return 1;

        // 2:
        if (getGoalDifference() > other.getGoalDifference())
            return -1;
        else if (getGoalDifference() < other.getGoalDifference())
            return 1;

        // 3:
        if (getGoalsScored() > other.getGoalsScored())
            return -1;
        else if (getGoalsScored() < other.getGoalsScored())
            return 1;

        // 4: If the two teams has not played against each other we jump to 7
        if (groupStatistics.pointsAgainstOtherTeams.containsKey(other)) {
            var points = groupStatistics.pointsAgainstOtherTeams.get(other);

            if (points == 3)
                return -1;
            else if (points == 0)
                return 1;

            var goalsScored = groupStatistics.goalsScoredAgainstOtherTeams.get(other);
            var goalsConceded = groupStatistics.goalsConcededAgainstOtherTeams.get(other);
            var goalDifference = goalsScored - goalsConceded;

            // 5:
            if (goalDifference > 0)
                return -1;
            else if (goalDifference < 0)
                return 1;
        }

        // 6: We only count yellow red cards and direct red cards, not time yet to implement two yellows to
        // a single player
        if (getFairPlayPoints() > other.getFairPlayPoints())
            return -1;
        else if (getFairPlayPoints() < other.getFairPlayPoints())
            return 1;

        // 7:
        return ThreadLocalRandom.current().nextBoolean() ? 1 : -1;
    }

    @Override
    public String toString() {
        return name;
    }
}
