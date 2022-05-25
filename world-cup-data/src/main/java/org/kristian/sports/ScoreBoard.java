package org.kristian.sports;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.kristian.sports.util.MatchNotifier;

public class ScoreBoard {

    private List<Group> groups;
    private List<Match> groupMatches;

    private final ExecutorService executorService;

    public ScoreBoard() {
        executorService = Executors.newFixedThreadPool(50);

        groups = initializeGroups();
        groupMatches = groups.stream()
                .flatMap(group -> group.initializeMatches().stream())
                .toList();

    }

    public void closeScoreBoard() {
        executorService.shutdownNow();
    }

    public List<Match> getGroupMatches() {
        return groupMatches;
    }

    public List<Result> getFinishedGroupMatches() {
        return groups.stream()
                .flatMap(group -> group.getResults().stream())
                .sorted()
                .toList();
    }

    public void startMatch(MatchNotifier notifier, Match match) {
        match.startMatch(executorService, notifier);
    }

    public void endMatch(Match match) {
        match.endMatch();
    }

    private List<Group> initializeGroups() {
        // Group A
        Group groupA = new Group("A");
        groupA.addTeam(new Team("Qatar", groupA))
                .addTeam(new Team("Ecuador", groupA))
                .addTeam(new Team("Senegal", groupA))
                .addTeam(new Team("Netherlands", groupA));

        // Group B
        Group groupB = new Group("B");
        groupB.addTeam(new Team("England", groupB))
                .addTeam(new Team("Iran", groupB))
                .addTeam(new Team("United States", groupB))
                .addTeam(new Team("Ukraine", groupB));

        // Group C
        Group groupC = new Group("C");
        groupC.addTeam(new Team("Argentina", groupC))
                .addTeam(new Team("Saudi Arabia", groupC))
                .addTeam(new Team("Mexico", groupC))
                .addTeam(new Team("Poland", groupC));

        // Group D
        Group groupD = new Group("D");
        groupD.addTeam(new Team("France", groupD))
                .addTeam(new Team("Peru", groupD))
                .addTeam(new Team("Denmark", groupD))
                .addTeam(new Team("Tunisia", groupD));

        //Group E
        Group groupE = new Group("E");
        groupE.addTeam(new Team("Spain", groupE))
                .addTeam(new Team("Costa Rica", groupE))
                .addTeam(new Team("Germany", groupE))
                .addTeam(new Team("Japan", groupE));

        // Group F
        Group groupF = new Group("F");
        groupF.addTeam(new Team("Belgium", groupF))
                .addTeam(new Team("Canada", groupF))
                .addTeam(new Team("Morocoo", groupF))
                .addTeam(new Team("Croatia", groupF));

        // Group G
        Group groupG = new Group("G");
        groupG.addTeam(new Team("Brazil", groupG))
                .addTeam(new Team("Serbia", groupG))
                .addTeam(new Team("Switzerland", groupG))
                .addTeam(new Team("Cameroon", groupG));

        // Group H
        Group groupH = new Group("H");
        groupH.addTeam(new Team("Portugal", groupH))
                .addTeam(new Team("Ghana", groupH))
                .addTeam(new Team("Uruguay", groupH))
                .addTeam(new Team("South Kora", groupH));

        return List.of(groupA, groupB, groupC, groupD, groupE, groupF, groupG, groupH);
    }
}
