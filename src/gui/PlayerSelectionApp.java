package gui;

import game.GameRunner;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class PlayerSelectionApp extends Frame {

    private int selectedPlayerCount = -1; // Default to -1 to indicate no selection made yet

    public PlayerSelectionApp() {
        setTitle("Player Selection");
        setSize(300, 150);
        setLayout(new FlowLayout());

        Label label = new Label("Select number of players: ");
        Choice playerChoice = new Choice();
        playerChoice.add("3");
        playerChoice.add("4");

        // Button to confirm selection
        Button confirmButton = new Button("Confirm");
        confirmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectedPlayerCount = Integer.parseInt(playerChoice.getSelectedItem());
                System.out.println("Number of players selected: " + selectedPlayerCount);

                // Close the window after the selection
                dispose();

                GameRunner.start();
            }
        });

        add(label);
        add(playerChoice);
        add(confirmButton);

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent we) {
                dispose();
            }
        });

        setVisible(true);
    }

    public int getSelectedPlayerCount() {
        return selectedPlayerCount;
    }

    public static void main(String[] args) {
        PlayerSelectionApp app = new PlayerSelectionApp();

        // Wait for the window to be closed
        while (app.isVisible()) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }

        int playerCount = app.getSelectedPlayerCount();
        if (playerCount != -1) {
            System.out.println("Game will start with " + playerCount + " players.");
            // Start the game or any other logic here
        } else {
            System.out.println("No player count was selected.");
        }
    }
}