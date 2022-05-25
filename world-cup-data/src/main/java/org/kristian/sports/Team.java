package org.kristian.sports;

import java.util.Objects;

public class Team implements Comparable<Team> {

    private final String name;
    private final Group group;
    private int groupPoints = 0;
    private final Stage currentStage;

    public Team(String name, Group group) {
        this.name = name;
        this.group = group;
        this.currentStage = Stage.GROUP;
    }

    public String getName() {
        return name;
    }

    public Group getGroup() {
        return group;
    }

    public Stage getCurrentStage() {
        return currentStage;
    }

    public void addGroupPoints(int points) {
        this.groupPoints += points;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Team otherTeam)
            return name.equals(otherTeam.name);

        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, groupPoints);
    }

    @Override
    public int compareTo(Team o) {
        if (groupPoints == o.groupPoints)
            return name.compareTo(o.name);

        return Integer.valueOf(groupPoints).compareTo(o.groupPoints);
    }

    @Override
    public String toString() {
        return name;
    }
}
