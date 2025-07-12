package game;

import gui.GameWindow;
import gui.PlayerSelectionApp;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class GameRunner {

    private static Player currentPlayer;
    private static int numberPlayers;
    private static int index = 0;
    private static final ArrayList<Player> players = new ArrayList<>();
    private static Game game;
    private static Player winner;
    private static PlayerSelectionApp select;

    public static void main(String[] args) {

        select = new PlayerSelectionApp();


    }

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

    public static void start() {
        SwingUtilities.invokeLater(() -> {
            numberPlayers = select.getSelectedPlayerCount();

            players.add(new Player(getPlayerName(1), Color.BLUE));
            players.add(new Player(getPlayerName(2), Color.BLACK));
            players.add(new Player(getPlayerName(3), Color.RED));

            if (numberPlayers == 4) {
                players.add(new Player(getPlayerName(4), Color.MAGENTA));
            }
            GameWindow tmp = new GameWindow(players);
            game = tmp.getBoard().getGame();
        });
    }

    public static Player getCurrentPlayer() {
        return currentPlayer;
    }

    public static void nextPlayer() {
        currentPlayer = players.get((index + 1) % numberPlayers);
        index = (index + 1) % numberPlayers;
    }

    public static void prevPlayer() {
        currentPlayer = players.get((index - 1) % numberPlayers);
        index = (index - 1) % numberPlayers;
    }

    public static void setFirstPlayer() {
        currentPlayer = players.get(0);
    }

    public static Player getWinner() {
        return winner;
    }

    public static void setWinner(Player p) {
        winner = p;
    }

    public static int getNumbPlayers() {
        return numberPlayers;
    }

    public static Player getPlayer(int i) {
        return players.get(i);
    }
}
