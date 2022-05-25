package org.kristian.sports.util;

import java.time.Duration;

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

    public long initialDelay() {
        return initialDelay;
    }

    public long interval() {
        return interval;
    }

    public int maxIterations() {
        return maxIterations;
    }

    public int iterations() {
        return iterations;
    }

    public boolean next() {
        if (canContinue()) {
            iterations++;
            return true;
        }

        return false;
    }

    public boolean canContinue() {
        return iterations < maxIterations;
    }
}
