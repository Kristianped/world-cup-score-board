package org.kristian.sports.data.match;

import org.kristian.sports.util.TimeBudget;
import org.kristian.sports.util.TimedTask;

import java.time.Duration;
import java.util.concurrent.Callable;

public class MatchCallable implements Callable<Boolean> {

    private static final long DURATION_BEFORE_FIRST_GOAL = 1000L;
    private static final long DURATION_BETWEEN_GOALS = 1000L;
    private static final int MAX_UPDATES_IN_90_MINUTES = 10;

    private final Match match;

    MatchCallable(Match match) {
        this.match = match;
    }

    public boolean updateMatch() {
        if (match.isFinished())
            return true;

        match.updateMatch();
        return false;
    }

    @Override
    public Boolean call() throws Exception {
        var timeBudget = new TimeBudget.Builder()
                .withInitialDelay(Duration.ofMillis(DURATION_BEFORE_FIRST_GOAL))
                .withInterval(Duration.ofMillis(DURATION_BETWEEN_GOALS))
                .withMaxIterations(MAX_UPDATES_IN_90_MINUTES)
                .build();

        var task = new TimedTask(timeBudget);

        return task.run(this::updateMatch, match::endMatch);
    }
}
