package game;

import gui.GameWindow;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * The starting class which will create and run the game
 */

public class GameRunner {

    private static final ArrayList<Player> players = new ArrayList<>();
    private static Player currentPlayer;
    private static int numberPlayers;
    private static int index = 0;
    private static Player winner;

    public static void main(String[] args) {
        start();
    }

    /**
     * Player enters their name here
     *
     * @param id the given Player
     * @return the chosen name for this Player
     */
    private static String getPlayerName(int id) {
        String playerName = "";
        String finalPlayerName = playerName;
        while (playerName.isEmpty() || players.stream().anyMatch(p -> p.getName().equals(finalPlayerName))) {
            playerName = JOptionPane.showInputDialog("Player " + id + ", please enter your name");
            if (playerName == null) // Cancel
                System.exit(0);
        }
        return playerName;
    }

    /**
     * Starting the game after initial Setup
     */
    public static void start() {
        SwingUtilities.invokeLater(() -> {
            Integer playerCount = (Integer) JOptionPane.showInputDialog(
                    null,
                    "How many Players?",
                    "How many Players?",
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    new Integer[]{3, 4},
                    3
            );

            if (playerCount == null)
                System.exit(0);

            numberPlayers = playerCount;

            players.add(new Player(getPlayerName(1), Color.BLUE));
            players.add(new Player(getPlayerName(2), Color.BLACK));
            players.add(new Player(getPlayerName(3), Color.RED));

            if (numberPlayers == 4) {
                players.add(new Player(getPlayerName(4), Color.MAGENTA));
            }
            new GameWindow(players);
        });
    }

    /**
     * Getter for current PLayer
     *
     * @return Player
     */
    public static Player getCurrentPlayer() {
        return currentPlayer;
    }

    /**
     * Select the next Player
     */
    public static void nextPlayer() {
        currentPlayer = players.get((index + 1) % numberPlayers);
        index = (index + 1) % numberPlayers;
    }

    /**
     * Selects the previous Player
     */
    public static void prevPlayer() {
        currentPlayer = players.get((index - 1) % numberPlayers);
        index = (index - 1) % numberPlayers;
    }

    /**
     * Chooses who starts the game
     */
    public static void setFirstPlayer() {
        currentPlayer = players.get(0);
    }


    /**
     * Gives the Player who won the game
     * If there is no winner yet this will return null
     *
     * @return the Winner / null
     */
    public static Player getWinner() {
        return winner;
    }

    /**
     * Setting the winning Player
     *
     * @param p the winner
     */
    public static void setWinner(Player p) {
        winner = p;
    }

    /**
     * The Amount of Players
     *
     * @return amount of Players playing this game
     */
    public static int getNumbPlayers() {
        return numberPlayers;
    }

    /**
     * Get Player out of the List
     *
     * @param i the position of a single player in the list
     * @return player at given location
     */

    public static Player getPlayer(int i) {
        return players.get(i);
    }
}
