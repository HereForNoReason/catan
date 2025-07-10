package game;

import gui.GameWindow;
import gui.PlayerSelectionApp;

import java.awt.Color;
import java.util.ArrayList;

import javax.swing.SwingUtilities;

public class GameRunner {
	
	private static Player currentPlayer;
	private static int numberPlayers;
	private static int index = 0;
	private static ArrayList<Player> players = new ArrayList<Player>();
	private static Game game;
	private static Player winner;

	public static void main(String[] args) {

		PlayerSelectionApp select = new  PlayerSelectionApp();
		 numberPlayers = select.getSelectedPlayerCount();

		 players.add(new Player("Player 1", Color.ORANGE, 10,10,10,10,10,10));
		 players.add(new Player("Player 2", Color.BLACK));
		 players.add(new Player("Player 3", Color.RED));
		 players.add(new Player("Player 4", Color.BLUE));
		 start();
		numberPlayers = players.size();


	}

	public static void start(){
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				GameWindow tmp = new GameWindow(players);
				game = tmp.getBoard().getGame();
			}
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
		index = (index - 1) % 4;
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
