package org.kristian.sports.ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.kristian.sports.Main;
import org.kristian.sports.data.match.Match;
import org.kristian.sports.data.ScoreBoard;

public class MainPanel extends JPanel {

    private SummaryDialog summaryDialog;
    private JPanel centerPanel;
    private JButton btnShowSummary;
    private JButton btnClose;

    private List<MatchPanel> matchPanels = new ArrayList<>();

    private final ScoreBoard scoreBoard;
    private final Main main;

    /**
     * Create the panel.
     */
    public MainPanel(Main main, ScoreBoard scoreBoard) {
        this.main = main;
        this.scoreBoard = scoreBoard;
        this.initialize();

        setLayout(new BorderLayout(0, 0));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(btnShowSummary);
        buttonPanel.add(btnClose);

        centerPanel = new JPanel(new GridLayout(scoreBoard.getGroupMatches().size(), 1));

        add(new JScrollPane(centerPanel), BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void initialize() {
        summaryDialog = new SummaryDialog(main.getMainFrame(), scoreBoard);
        btnShowSummary = new JButton("Show summary");
        btnShowSummary.addActionListener(e -> summaryDialog.setVisible(true));
        btnClose = new JButton("Close");
        btnClose.addActionListener(e -> main.close());
    }

    public void addMatches(List<Match> matches) {
        for (var match : matches)
            matchPanels.add(new MatchPanel(this, scoreBoard, match));

        refresh();
    }

    public void removeMatchPanel(MatchPanel matchPanel) {
        matchPanels.remove(matchPanel);
        refresh();
    }

    public void matchEnded() {
        summaryDialog.refresh();
    }

    public void refresh() {
        centerPanel.removeAll();

        for (var matchPanel : matchPanels)
            centerPanel.add(matchPanel);

        this.validate();
        this.repaint();
    }
}
