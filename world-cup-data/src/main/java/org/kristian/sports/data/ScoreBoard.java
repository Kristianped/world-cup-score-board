package org.kristian.sports.data;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.kristian.sports.data.match.Match;
import org.kristian.sports.data.match.MatchResult;
import org.kristian.sports.util.MatchNotifier;

/**
 * The score boards keeps track of the groups in the WC, all group matches and has methods for starting/ending a match
 */
public class ScoreBoard {

    private final List<Group> groups;
    private final List<Match> groupMatches;

    private final ExecutorService executorService;

    public ScoreBoard(List<Group> groups) {
        this.executorService = Executors.newFixedThreadPool(50);
        this.groups = groups;
        this.groupMatches = groups.stream()
                .flatMap(group -> group.initializeMatches().stream())
                .toList();

    }

    /**
     * Close the score board
     */
    public void closeScoreBoard() {
        executorService.shutdownNow();
    }

    /**
     * Get all group matches
     *
     * @return A {@link List} of all group stage matches
     */
    public List<Match> getGroupMatches() {
        return groupMatches;
    }

    /**
     * Get finished group matches
     *
     * @return A {@link List} of all finished group stage matches
     */
    public List<MatchResult> getFinishedGroupMatches() {
        return groups.stream()
                .flatMap(group -> group.getResults().stream())
                .sorted()
                .toList();
    }

    /**
     * Start a match
     *
     * @param notifier The {@link MatchNotifier} to notify about match updates
     * @param match    The match to start
     */
    public void startMatch(MatchNotifier notifier, Match match) {
        match.startMatch(executorService, notifier);
    }

    /**
     * End a match
     *
     * @param match The match to end
     */
    public void endMatch(Match match) {
        match.endMatch();
    }
}
