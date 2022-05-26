package org.kristian.sports.data;

import org.kristian.sports.data.match.Match;
import org.kristian.sports.data.match.MatchResult;

import java.util.ArrayList;
import java.util.List;

/**
 * A Group contains a group-name, list of teams in the group and results from all completed matches between
 * the teams in the group
 */
public class Group {

    private final String name;
    private final List<Team> teams;

    private final List<MatchResult> results;

    public Group(String name) {
        this.name = name;
        this.teams = new ArrayList<>();
        this.results = new ArrayList<>();
    }

    /**
     * Create matches between the teams in the group. If the group does not consist of 4 teams, and exception is
     * thrown
     *
     * @return A {@link List} of matches between the teams in the group
     */
    public List<Match> initializeMatches() {
        if (teams.size() != 4)
            throw new IllegalStateException("The group must have 4 teams to start matches");

        List<Match> matches = new ArrayList<>();

        for (int i = 0; i < teams.size(); i++) {
            for (int j = i + 1; j < teams.size(); j++) {
                matches.add(Match.createGroupGame(teams.get(i), teams.get(j)));
            }
        }

        return matches;
    }

    /**
     * Add a team to this group. If the provided team is already in this group or the group is full,
     * an exception is thrown
     *
     * @param team The team to add
     * @return This group-object, for chaining
     */
    public Group addTeam(Team team) {
        if (teams.contains(team))
            throw new IllegalArgumentException(this + " already contains team " + team.toString());

        if (teams.size() == 4)
            throw new IllegalArgumentException(this + " already contains 4 teams");

        this.teams.add(team);
        teams.sort(null);
        return this;
    }

    /**
     * Add a team to this group. If the provided team is already in this group, an exception is thrown
     *
     * @param teamName The name of the team
     * @return This group-object, for chaining
     */
    public Group addTeam(String teamName) {
        Team team = new Team(teamName, this);
        return this.addTeam(team);
    }

    /**
     * Add a result of a match to this group
     *
     * @param result The result to add
     */
    public void addResult(MatchResult result) {
        results.add(result);
        teams.sort(null);
    }

    /**
     * Get all match-results that has been played in this group
     *
     * @return A {@link List} of results
     */
    public List<MatchResult> getResults() {
        return results;
    }

    /**
     * Checks if this group is finished or not
     *
     * @return True if all 6 matches has been played, false if not
     */
    public boolean isFinished() {
        return results.size() == 6;
    }

    /**
     * Get the 2 teams ready for the knockout stages. If the group has un-played matches, an exception is thrown
     *
     * @return A {@link List} of the 2 teams ready for knockout stages
     */
    public List<Team> getTeamsForKnockoutStages() {
        if (!isFinished())
            throw new IllegalStateException("This group is not finished playing yet");

        return List.of(teams.get(0), teams.get(1));
    }

    @Override
    public String toString() {
        return "Group " + name;
    }
}
