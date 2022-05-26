package org.kristian.sports.data;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.kristian.sports.data.match.Match;
import org.kristian.sports.data.match.MatchResult;
import org.kristian.sports.data.match.MatchStatistics;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GroupTest {

    Team norway;
    Team sweden;
    Team denmark;
    Team iceland;

    Group validGroup;
    Group invalidGroup;

    Match match = mock(Match.class);
    MatchStatistics norwayStats = mock(MatchStatistics.class);
    MatchStatistics swedenStats = mock(MatchStatistics.class);
    MatchStatistics denmarkStats = mock(MatchStatistics.class);
    MatchStatistics icelandStats = mock(MatchStatistics.class);

    @BeforeEach
    void beforeAll() {
        validGroup = new Group("ValidGroup");
        invalidGroup = new Group("InvalidGroup");
        norway = new Team("Norway", validGroup);
        sweden = new Team("Sweden", validGroup);
        denmark = new Team("Denmark", validGroup);
        iceland = new Team("Iceland", validGroup);

        validGroup.addTeam(norway).addTeam(sweden).addTeam(denmark).addTeam(iceland);
        invalidGroup.addTeam("England").addTeam("Scotland").addTeam("Wales");
    }

    @Test
    void initializeMatches() {
        var matches = validGroup.initializeMatches();
        assertEquals(6, matches.size());

        assertThrows(IllegalStateException.class, () -> invalidGroup.initializeMatches());
    }

    @Test
    void addTeam() {
        assertThrows(IllegalArgumentException.class, () -> validGroup.addTeam("Finland"));
        assertThrows(IllegalArgumentException.class, () -> invalidGroup.addTeam("Wales"));

        var group = invalidGroup.addTeam("Northern Ireland");
        assertEquals(invalidGroup, group);
    }

    @Test
    void addResult() {
        when(match.getStage()).thenReturn(Stage.GROUP);

        when(norwayStats.getTeam()).thenReturn(norway);
        when(swedenStats.getTeam()).thenReturn(sweden);
        when(denmarkStats.getTeam()).thenReturn(denmark);
        when(icelandStats.getTeam()).thenReturn(iceland);

        when(norwayStats.getGoals()).thenReturn(5);
        when(swedenStats.getGoals()).thenReturn(1);
        when(denmarkStats.getGoals()).thenReturn(3);
        when(icelandStats.getGoals()).thenReturn(1);

        addResults(validGroup, norway, sweden, norwayStats, swedenStats);
        addResults(validGroup, norway, denmark, norwayStats, denmarkStats);
        addResults(validGroup, norway, iceland, norwayStats, icelandStats);
        assertFalse(validGroup.isFinished());
        assertEquals(9, norway.getPoints());
        assertEquals(0, sweden.getPoints());

        addResults(validGroup, sweden, denmark, swedenStats, denmarkStats);
        addResults(validGroup, sweden, iceland, swedenStats, icelandStats);
        assertFalse(validGroup.isFinished());
        assertEquals(3, denmark.getPoints());
        assertEquals(1, sweden.getPoints());

        addResults(validGroup, denmark, iceland, denmarkStats, icelandStats);
        assertTrue(validGroup.isFinished());
        assertEquals(9, norway.getPoints());
        assertEquals(6, denmark.getPoints());
        assertEquals(1, sweden.getPoints());
        assertEquals(1, iceland.getPoints());

        var knockoutTeams = validGroup.getTeamsForKnockoutStages();
        assertEquals(2, knockoutTeams.size());
        assertEquals(knockoutTeams, List.of(norway, denmark));
    }

    void addResults(Group group, Team team1, Team team2, MatchStatistics stats1, MatchStatistics stats2) {
        var result = new MatchResult(match, stats1, stats2);
        result.updateGroupStatistics(team1);
        result.updateGroupStatistics(team2);
        group.addResult(result);
    }
}