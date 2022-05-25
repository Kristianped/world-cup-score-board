package org.kristian.sports.ui;

import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.kristian.sports.Match;
import org.kristian.sports.ScoreBoard;
import org.kristian.sports.util.MatchNotifier;

import java.awt.Dimension;

public class MatchPanel extends JPanel implements MatchNotifier {

    private final ScoreBoard scoreBoard;
    private final Match match;

    private final MainPanel mainPanel;
    private JLabel matchLabel;
    private JButton startMatch;
    private JButton endMatch;
    private JButton removeWatch;

    /**
     * Create the panel.
     */
    public MatchPanel(MainPanel mainPanel, ScoreBoard scoreBoard, Match match) {
        this.mainPanel = mainPanel;
        this.scoreBoard = scoreBoard;
        this.match = match;

        this.initialize();
    }

    private void initialize() {
        setLayout(new FlowLayout(FlowLayout.LEFT));

        matchLabel = new JLabel(match.toString());
        matchLabel.setPreferredSize(new Dimension(250, 14));
        matchLabel.setMinimumSize(new Dimension(250, 14));
        matchLabel.setMaximumSize(new Dimension(400, 14));
        matchLabel.setSize(new Dimension(250, 0));
        startMatch = new JButton("Start match");
        endMatch = new JButton("End match");
        removeWatch = new JButton("Remove match");

        endMatch.setEnabled(false);
        removeWatch.setEnabled(false);

        startMatch.addActionListener(e -> {
            scoreBoard.startMatch(this, match);
            startMatch.setEnabled(false);
            endMatch.setEnabled(true);
        });

        endMatch.addActionListener(e -> {
            endMatch.setEnabled(false);
            removeWatch.setEnabled(true);
            scoreBoard.endMatch(match);
        });

        removeWatch.addActionListener(e -> mainPanel.removeMatchPanel(this));

        add(matchLabel);
        add(startMatch);
        add(endMatch);
        add(removeWatch);
    }

    @Override
    public void notifyMatchUpdate() {
        matchLabel.setText(match.toString());
        this.validate();
        this.repaint();
    }

    @Override
    public void notifyMatchEnded() {
        endMatch.setEnabled(false);
        removeWatch.setEnabled(true);
        mainPanel.matchEnded();
        this.validate();
        this.repaint();
    }
}
