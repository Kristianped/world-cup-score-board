package org.kristian.sports;

import javax.swing.*;

import org.kristian.sports.data.Group;
import org.kristian.sports.data.ScoreBoard;
import org.kristian.sports.data.Team;
import org.kristian.sports.ui.MainPanel;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

public class Main {

    private final JFrame mainFrame;

    private final ScoreBoard scoreBoard;

    public Main() {
        mainFrame = new JFrame();
        mainFrame.setSize(800, 800);
        mainFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                close();
            }
        });

        scoreBoard = new ScoreBoard(createGroups());

        MainPanel mainPanel = new MainPanel(this, scoreBoard);
        mainPanel.addMatches(scoreBoard.getGroupMatches());
        mainFrame.getContentPane().add(mainPanel);
    }

    public JFrame getMainFrame() {
        return mainFrame;
    }

    public void show() {
        mainFrame.toFront();
        mainFrame.requestFocus();
        mainFrame.setVisible(true);
    }

    public void close() {
        int confirm = JOptionPane.showConfirmDialog(mainFrame, "Do you want to close the application?",
                "Close application", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            scoreBoard.closeScoreBoard();
            System.exit(0);
        }
    }

    private List<Group> createGroups() {
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

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                Main main = new Main();
                main.show();
            } catch (Exception e) {
                System.err.print(e.getMessage());
            }
        });

    }
}
