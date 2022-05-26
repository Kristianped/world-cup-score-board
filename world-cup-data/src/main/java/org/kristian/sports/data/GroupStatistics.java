package org.kristian.sports.data;

import java.util.HashMap;
import java.util.Map;

/**
 * A data class to sum up goals scored, goals against and so forth for a Team in the group stages
 */
public class GroupStatistics {

    int totalPoints;
    int totalGoalsScored;
    int totalGoalsConceded;
    int yellowCards;
    int redCards;

    Map<Team, Integer> pointsAgainstOtherTeams = new HashMap<>();
    Map<Team, Integer> goalsScoredAgainstOtherTeams = new HashMap<>();
    Map<Team, Integer> goalsConcededAgainstOtherTeams = new HashMap<>();

}
