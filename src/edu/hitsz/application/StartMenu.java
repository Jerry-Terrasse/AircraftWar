package edu.hitsz.application;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class StartMenu {
    private JPanel mainPanel;
    private JButton simpleModeButton;
    private JButton normalModeButton;
    private JButton hardModeButton;
    private JComboBox musicComboBox;
    private JLabel musicLabel;
    public StartMenu() {
        simpleModeButton.addActionListener((ActionEvent e) -> {
            Game game = new Game();
            Main.cardPanel.add(game);
            Main.cardLayout.last(Main.cardPanel);
            game.setWithMusic(musicComboBox.getSelectedIndex() == 0);
            game.action();
        });
        normalModeButton.addActionListener((ActionEvent e) -> {
            Game game = new Game();
            Main.cardPanel.add(game);
            Main.cardLayout.last(Main.cardPanel);
            game.setWithMusic(musicComboBox.getSelectedIndex() == 0);
            game.action();
        });
        hardModeButton.addActionListener((ActionEvent e) -> {
            Game game = new Game();
            Main.cardPanel.add(game);
            Main.cardLayout.last(Main.cardPanel);
            game.setWithMusic(musicComboBox.getSelectedIndex() == 0);
            game.action();
        });
        musicComboBox.getSelectedIndex();
    }
    public JPanel getMainPanel() {
        return mainPanel;
    }
}
