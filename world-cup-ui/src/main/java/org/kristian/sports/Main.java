package org.kristian.sports;

import javax.swing.*;

import org.kristian.sports.ui.MainPanel;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

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

        scoreBoard = new ScoreBoard();

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
