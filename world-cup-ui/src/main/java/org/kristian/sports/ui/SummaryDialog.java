package org.kristian.sports.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import org.kristian.sports.data.match.MatchResult;
import org.kristian.sports.data.ScoreBoard;

public class SummaryDialog extends JDialog {

    private final JPanel contentPanel;
    private final ScoreBoard scoreBoard;

    /**
     * Create the dialog.
     */
    public SummaryDialog(JFrame parent, ScoreBoard scoreBoard) {
        super(parent, "Summary of games", ModalityType.MODELESS);
        this.scoreBoard = scoreBoard;
        setSize(400, 800);
        setLocationRelativeTo(parent);

        contentPanel = new JPanel(new GridLayout(scoreBoard.getGroupMatches().size(), 1, 5, 5));
        contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));

        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(e -> this.setVisible(false));

        JButton refreshButton = new JButton("Refresh");
        refreshButton.addActionListener(e -> this.refresh());

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                SummaryDialog.this.setVisible(false);
            }
        });

        JPanel buttonPane = new JPanel();
        buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
        buttonPane.add(closeButton);
        buttonPane.add(refreshButton);

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(contentPanel, BorderLayout.CENTER);
        getContentPane().add(buttonPane, BorderLayout.SOUTH);

        refresh();
    }

    public void refresh() {
        contentPanel.removeAll();
        var finishedMatches = scoreBoard.getFinishedGroupMatches();

        for (var result : finishedMatches) {
            contentPanel.add(createResultLabel(result));
        }

        contentPanel.validate();
        contentPanel.repaint();
    }

    private JLabel createResultLabel(MatchResult result) {
        JLabel label = new JLabel(result.toString());
        label.setSize(new Dimension(400, 14));
        label.setPreferredSize(new Dimension(400, 14));
        label.setMinimumSize(new Dimension(400, 14));
        label.setMaximumSize(new Dimension(400, 14));

        return label;
    }
}
