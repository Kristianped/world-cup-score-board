package org.kristian.sports.util;

import java.time.Duration;

/**
 * A budget used when doing a timed task. It consists of how many iterations a task can have, an initial delay before
 * the task can start and an interval between each iteration of the task
 */
public class TimeBudget {

    public static class Builder {

        private static final int DEFAULT_MAX_ITERATIONS = Integer.MAX_VALUE;
        private static final long DEFAULT_INITIAL_DELAY = 0;
        private static final long DEFAULT_INTERVAL = 1000;

        private long initialDelay = DEFAULT_INITIAL_DELAY;
        private long interval = DEFAULT_INTERVAL;
        private int maxIterations = DEFAULT_MAX_ITERATIONS;

        public Builder withInitialDelay(Duration duration) {
            this.initialDelay = duration.toMillis();
            return this;
        }

        public Builder withInterval(Duration duration) {
            this.interval = duration.toMillis();
            return this;
        }

        public Builder withMaxIterations(int maxIterations) {
            this.maxIterations = maxIterations;
            return this;
        }

        public TimeBudget build() {
            return new TimeBudget(initialDelay, interval, maxIterations);
        }
    }

    private final long initialDelay;
    private final int maxIterations;
    private final long interval;
    private int iterations;

    private TimeBudget(long initialDelay, long interval, int maxIterations) {
        this.initialDelay = initialDelay;
        this.maxIterations = maxIterations;
        this.interval = interval;
        this.iterations = 0;
    }

    /**
     * Get the initial delay
     * @return Initial delay
     */
    public long initialDelay() {
        return initialDelay;
    }

    /**
     * Get the interval
     * @return The interval
     */
    public long interval() {
        return interval;
    }

    /**
     * Get maximum number of iterations
     * @return Max iterations
     */
    public int maxIterations() {
        return maxIterations;
    }

    /**
     * Get amount of iterations currently done
     * @return Done iterations
     */
    public int iterations() {
        return iterations;
    }

    /**
     * Check if a task can continue, and if it can the amount of iterations done is added with 1
     * @return True if a task can continue, false if not
     */
    public boolean next() {
        if (canContinue()) {
            iterations++;
            return true;
        }

        return false;
    }

    /**
     * Check if current amount of iterations done is less than the maximum allowed
     * @return True if current iterations is less than the max, false if not
     */
    public boolean canContinue() {
        return iterations < maxIterations;
    }
}
