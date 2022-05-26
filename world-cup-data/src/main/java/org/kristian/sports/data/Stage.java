package org.kristian.sports.data;

public enum Stage {

    GROUP("Group stage"),
    ROUND_OF_16("Round of 16"),
    QUARTER_FINALS("Quarter Final"),
    SEMI_FINALS("Semi Final"),
    BRONZE_FINALE("Third place play.off"),
    FINALE("Final");

    private final String name;

    Stage(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
