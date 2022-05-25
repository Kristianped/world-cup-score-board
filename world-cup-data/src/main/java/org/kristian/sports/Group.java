package org.kristian.sports;

import java.util.ArrayList;
import java.util.List;

public class Group {

    private final String name;
    private final List<Team> teams;

    private final List<Result> results;

    public Group(String name) {
        this.name = name;
        this.teams = new ArrayList<>();
        this.results = new ArrayList<>();
    }

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

    public Group addTeam(Team team) {
        if (teams.contains(team))
            throw new IllegalArgumentException(toString() + " already contains team " + team.toString());

        this.teams.add(team);
        teams.sort(null);
        return this;
    }

    public void addResult(Result result) {
        results.add(result);
    }

    public List<Result> getResults() {
        return results;
    }

    public boolean isFinished() {
        return results.size() == 6;
    }

    @Override
    public String toString() {
        return "Group " + name;
    }
}
