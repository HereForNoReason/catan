package gui;

import game.Player;
import lib.GraphPaperLayout;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * The Main game window
 */
public class GameWindow {

    private final static int INTERVAL = 20;
    private final static int scrSize = 1000; //TODO specify
    private final CatanBoard board;
    private final BottomBar bottom;

    /**
     * Create a new game with window with the given players
     *
     * @param players Players that are playing
     */
    public GameWindow(ArrayList<Player> players) {
        board = new CatanBoard(players);
        bottom = new BottomBar();

        createAndShowGUI();

        Timer timer = new Timer(INTERVAL,
                evt -> {
                    // Refresh the board
                    board.repaint(); //TODO fix validate
                    bottom.repaint();
                });

        timer.start();
    }

    private void createAndShowGUI() {

        JFrame frame = new JFrame("Catan");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Dimension d = new Dimension(5, 6);

        frame.setSize(scrSize + 500, scrSize);
        //frame.setLayout(new GraphPaperLayout(d));
        Container content = frame.getContentPane();
        content.setLayout(new GraphPaperLayout(d));
        //content.add(board);
        content.add(board, new Rectangle(0, 0, 4, 4));

        SideBar side = new SideBar(this);
        content.add(side, new Rectangle(4, 0, 1, 4));

        content.add(bottom, new Rectangle(0, 4, 5, 2));


        frame.setResizable(true);
        frame.setLocationRelativeTo(null);

        frame.setVisible(true);

        board.repaint();
    }

    /**
     * Get the visual board for this window
     *
     * @return The visual board
     */
    public CatanBoard getBoard() {
        return board;
    }
}
