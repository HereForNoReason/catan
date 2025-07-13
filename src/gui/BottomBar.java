package gui;

import game.GameRunner;
import game.Player;
import lib.GraphPaperLayout;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * The Bar at the bottom of the screen with the Players resources
 */
public class BottomBar extends JPanel {

    private final static int INTERVAL = 50;
    private final ArrayList<ArrayList<KComponent>> playerComponents = new ArrayList<>();

    /**
     * Creates and initializes the resource information at the bottom
     */
    public BottomBar() {
        setBackground(new Color(255, 255, 255, 255));

        setLayout(new GraphPaperLayout(new Dimension(GameRunner.getNumbPlayers(), 8)));

        for (int i = 0; i < GameRunner.getNumbPlayers(); i++) {
            ArrayList<KComponent> components = new ArrayList<>();
            components.add(new KComponent(new JLabel(GameRunner.getPlayer(i).getName()), new Rectangle(i, 0, 1, 1)));
            components.get(0).getComponent().setForeground(GameRunner.getPlayer(i).getColor());
            components.get(0).getComponent().setFont(new Font("Arial", Font.BOLD, 20));
            add(components.get(0).getComponent(), components.get(0).getRectangle());

            for (int k = 1; k <= 11; k++) {
                components.add(new KComponent(new JLabel(""), new Rectangle(i, k, 1, 1)));
                components.get(k).getComponent().setFont(new Font("Arial", Font.BOLD, 20));
                add(components.get(k).getComponent(), components.get(k).getRectangle());
            }

            playerComponents.add(components);
        }

        Timer timer = new Timer(INTERVAL,
                evt -> {
                    for (int i = 0; i < playerComponents.size(); i++) {
                        updatePlayer(playerComponents.get(i), GameRunner.getPlayer(i));
                    }
                });
        timer.start();

    }

    /**
     * Update the displayed resources for the given player
     *
     * @param components Where to put the resources
     * @param p          The player for whom to display the resources
     */
    private void updatePlayer(ArrayList<KComponent> components, Player p) {
        ((JLabel) components.get(1).getComponent()).setText("Brick: " + p.getNumberResourcesType("BRICK"));
        ((JLabel) components.get(2).getComponent()).setText("Wool: " + p.getNumberResourcesType("WOOL"));
        ((JLabel) components.get(3).getComponent()).setText("Ore: " + p.getNumberResourcesType("ORE"));
        ((JLabel) components.get(4).getComponent()).setText("Grain: " + p.getNumberResourcesType("GRAIN"));
        ((JLabel) components.get(5).getComponent()).setText("Lumber: " + p.getNumberResourcesType("LUMBER"));
        ((JLabel) components.get(6).getComponent()).setText("Victory Points: " + p.getVictoryPoints());
    }

}
