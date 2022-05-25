package org.kristian.sports.util;

import java.util.function.BooleanSupplier;

public class TimedTask {

    private final TimeBudget budget;

    public TimedTask(TimeBudget budget) {
        this.budget = budget;
    }

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
