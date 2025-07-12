package game;

import gui.GameWindow;
import gui.PlayerSelectionApp;

import java.awt.Color;
import java.util.ArrayList;

import javax.swing.*;

public class GameRunner {
	
	private static Player currentPlayer;
	private static int numberPlayers;
	private static int index = 0;
	private static ArrayList<Player> players = new ArrayList<Player>();
	private static Game game;
	private static Player winner;
	private static PlayerSelectionApp select;

	public static void main(String[] args) {

        select = new  PlayerSelectionApp();


	}

	private static String getPlayerName(int id) {
		String playername = "";
		String finalPlayername = playername;
		while (playername.isEmpty() || players.stream().anyMatch(p -> p.getName().equals(finalPlayername))) {
			playername	= JOptionPane.showInputDialog("Player " + id + ", please enter your name");
			if (playername == null) // Cancel
				System.exit(0);
		}
		return playername;
	}

	public static void start() {
		SwingUtilities.invokeLater(() -> {
			numberPlayers = select.getSelectedPlayerCount();

			players.add(new Player(getPlayerName(1), Color.BLUE));
			players.add(new Player(getPlayerName(2), Color.BLACK));
			players.add(new Player(getPlayerName(3), Color.RED));

			if(numberPlayers == 4) {
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
	
	public static void setWinner(Player p) {
		winner = p;
	}
	
	public static Player getWinner() {
		return winner;
	}
	
	public static int getNumbPlayers() {
		return numberPlayers;
	}
	
	public static Player getPlayer(int i) {
		return players.get(i);
	}
}
