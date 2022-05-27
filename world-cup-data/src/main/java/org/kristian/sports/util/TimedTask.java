package org.kristian.sports.util;

import java.util.function.BooleanSupplier;

/**
 * A timed task is a task that runs a supplier until the supplier returns true, or we run out of time due to the
 * budget set
 */
public class TimedTask {

    private final TimeBudget budget;

    public TimedTask(TimeBudget budget) {
        this.budget = budget;
    }

    /**
     * Run the provided supplier until it returns true, or the budget runs out. When the task is complete, run
     * the consumer
     *
     * @param supplier The supplier to run
     * @param consumer The consumer to run
     * @return True if the supplier returns true, false if not
     */
    public boolean run(BooleanSupplier supplier, VoidConsumer consumer) {
        boolean completed = false;

        try {
            if (budget.initialDelay() > 0)
                Thread.sleep(budget.initialDelay());

            while (budget.next()) {
                if (supplier.getAsBoolean()) {
                    completed = true;
                    break;
                }

                if (budget.canContinue())
                    Thread.sleep(budget.interval());
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        consumer.accept();
        return completed;
    }
}
