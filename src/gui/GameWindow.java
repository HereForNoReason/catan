package gui;

import game.Player;
import lib.GraphPaperLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;


public class GameWindow {

    public final static int INTERVAL = 20;
    final static int scrSize = 1000; //TODO specify
    CatanBoard board;
    BottomBar bottom;
    SideBar side;


    public GameWindow(ArrayList<Player> players) {
        board = new CatanBoard(players);
        bottom = new BottomBar();

        createAndShowGUI();

        Timer timer = new Timer(INTERVAL,
                new ActionListener() {
                    public void actionPerformed(ActionEvent evt) {
                        // Refresh the board
                        board.repaint(); //TODO fix validate
                        bottom.repaint();
                    }
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

        side = new SideBar(this);
        content.add(side, new Rectangle(4, 0, 1, 4));

        content.add(bottom, new Rectangle(0, 4, 5, 2));


        frame.setResizable(true);
        frame.setLocationRelativeTo(null);

        frame.setVisible(true);

        board.repaint();
    }

    public CatanBoard getBoard() {
        return board;
    }
}
