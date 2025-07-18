package game;

import board.Board;
import board.EdgeLocation;
import board.VertexLocation;

import java.util.ArrayList;
import java.util.Collections;


/**
 * The main game class of Settlers of Catan
 */
public class Game {

    private final Board board;
    private final ArrayList<Player> players;
    private Player longestRoadOwner;
    private int longestRoadLength = 4;

    /**
     * Constructor for game, creates the Board.
     *
     * @param givenPlayers the players of the game
     */
    public Game(ArrayList<Player> givenPlayers) {

        if (givenPlayers.size() < 3 || givenPlayers.size() > 4)
            throw new IllegalArgumentException("Game must be played with three or four players");
        Collections.shuffle(givenPlayers);

        players = givenPlayers;
        board = new Board();
        GameRunner.setFirstPlayer();
    }

    /**
     * Checks if one player has ten or more victory points and more points than any other player
     *
     * @return whether anyone has one yet
     */
    public boolean over() {
        return winningPlayer() != null;
    }

    /**
     * Returns the player that has won, or null if the game is not finished
     *
     * @return winning player
     */
    public Player winningPlayer() {

        Player maxVictoryPoints = players.get(0);

        for (Player p : players) {

            if (p.getVictoryPoints() > maxVictoryPoints.getVictoryPoints()) {
                maxVictoryPoints = p;
            }
        }

        if (maxVictoryPoints.getVictoryPoints() >= 10) {
            return maxVictoryPoints;
        } else
            return null;
    }

    /**
     * Rolls the die and allocates resources to players
     *
     * @return int the roll
     */
    public int roll() {

        // RTD
        int roll = (int) (Math.random() * 6 + 1) + (int) (Math.random() * 6 + 1);

        if (roll != 7) {
            // Distribute resources
            board.distributeResources(roll);

        }
        return roll;
    }

    /**
     * Allows the given Player to take a card from any Player with a Settlement on the Tile of the given Location
     *
     * @param p      the Player taking a card
     * @param choice resource you want to take from given player
     */
    public void takeCard(Player p, Player choice) {

        ArrayList<String> res = new ArrayList<>();
        for (int i = 0; i < choice.getNumberResourcesType("BRICK"); i++) {
            res.add("BRICK");
        }
        for (int i = 0; i < choice.getNumberResourcesType("WOOL"); i++) {
            res.add("WOOL");
        }
        for (int i = 0; i < choice.getNumberResourcesType("ORE"); i++) {
            res.add("ORE");
        }
        for (int i = 0; i < choice.getNumberResourcesType("GRAIN"); i++) {
            res.add("GRAIN");
        }
        for (int i = 0; i < choice.getNumberResourcesType("LUMBER"); i++) {
            res.add("LUMBER");
        }

        Collections.shuffle(res);

        if (res.isEmpty()) {
            return;
        }
        String result = res.get(0);

        choice.setNumberResourcesType(result, choice.getNumberResourcesType(result) - 1);

        p.setNumberResourcesType(result, p.getNumberResourcesType(result) + 1);
    }


    /**
     * Operates trade between two players with the given resources
     *
     * @param a     the first Player in the trading
     * @param b     the second Player in the trading
     * @param fromA the resources being traded from Player a to Player b
     * @param fromB the resources being traded from Player b to Player a
     */
    public void playerTrade(Player a, Player b, ArrayList<String> fromA, ArrayList<String> fromB) {

        if (!a.hasResources(fromA) || !b.hasResources(fromB)) {
            return;
        }

        for (String res : fromA) {
            a.setNumberResourcesType(res, a.getNumberResourcesType(res) - 1);
            b.setNumberResourcesType(res, b.getNumberResourcesType(res) + 1);
        }

        for (String res : fromB) {
            b.setNumberResourcesType(res, b.getNumberResourcesType(res) - 1);
            a.setNumberResourcesType(res, a.getNumberResourcesType(res) + 1);
        }
    }

    /**
     * Operates trading between given Player and the stock
     *
     * @param a              the Player trading
     * @param fromA          what they are giving up
     * @param resourceBuying what they are asking for
     */
    public void npcTrade(Player a, String resourceBuying, ArrayList<String> fromA) {
        if (!a.hasResources(fromA))
            return;

        boolean[] ports = a.getPorts();
        ArrayList<Integer> resources = new ArrayList<>();
        resources.add(Collections.frequency(fromA, "BRICK"));
        resources.add(Collections.frequency(fromA, "WOOL"));
        resources.add(Collections.frequency(fromA, "ORE"));
        resources.add(Collections.frequency(fromA, "GRAIN"));
        resources.add(Collections.frequency(fromA, "LUMBER"));
        ArrayList<String> toA = new ArrayList<>();

        for (int i = 0; i < resources.size(); i++) {
            if (resources.get(i) != 0) {
                if (ports[i + 1]) {
                    if (resources.get(i) % 2 == 0) {
                        for (int k = 0; k < resources.get(i) / 2; k++) {
                            toA.add(resourceBuying);
                        }
                    } else {
                        return;
                    }
                } else if (ports[0]) {
                    if (resources.get(i) % 3 == 0) {
                        for (int k = 0; k < resources.get(i) / 3; k++) {
                            toA.add(resourceBuying);
                        }
                    } else {
                        return;
                    }
                } else {
                    if (resources.get(i) % 4 == 0) {
                        for (int k = 0; k < resources.get(i) / 4; k++) {
                            toA.add(resourceBuying);
                        }
                    } else {
                        return;
                    }
                }
            }
        }


        for (String res : fromA) {
            a.setNumberResourcesType(res, a.getNumberResourcesType(res) - 1);
        }


        for (String res : toA) {
            a.setNumberResourcesType(res, a.getNumberResourcesType(res) + 1);
        }

    }

    /**
     * Checks if Player is able to buy a Road
     *
     * @param p the given Player
     * @return 0=success, 1=insufficient resources, 2=structure limit reached
     */
    public int canBuyRoad(Player p) {
        if (p.getNumberResourcesType("BRICK") < 1 || p.getNumberResourcesType("LUMBER") < 1) {
            return 1;
        }

        // Check Player has not exceeded capacity for an object
        if (p.getNumbRoads() >= 15) {
            return 2;
        }

        return 0;
    }

    /**
     * Buys Road for given Player
     *
     * @param p the given Player
     */

    public void buyRoad(Player p) {
        if (canBuyRoad(p) != 0)
            return;

        p.setNumberResourcesType("BRICK", p.getNumberResourcesType("BRICK") - 1);
        p.setNumberResourcesType("LUMBER", p.getNumberResourcesType("LUMBER") - 1);

        p.addRoadCount();
    }

    /**
     * Checks if the player can buy a Settlement
     *
     * @param p the given Player
     * @return 0=success, 1=insufficient resources, 2=structure limit reached
     */
    public int canBuySettlement(Player p) {
        // Check Player has sufficient resources
        if (p.getNumberResourcesType("BRICK") < 1 || p.getNumberResourcesType("GRAIN") < 1 || p.getNumberResourcesType("WOOL") < 1 || p.getNumberResourcesType("LUMBER") < 1) {
            return 1;
        }

        // Check Player has not exceeded capacity for an object
        if (p.getNumbSettlements() >= 5) {
            return 2;
        }

        return 0;
    }

    /**
     * Buys Settlement for given Player
     *
     * @param p the given Player
     */

    public void buySettlement(Player p) {
        if (canBuySettlement(p) != 0)
            return;

        p.setNumberResourcesType("BRICK", p.getNumberResourcesType("BRICK") - 1);
        p.setNumberResourcesType("LUMBER", p.getNumberResourcesType("LUMBER") - 1);
        p.setNumberResourcesType("GRAIN", p.getNumberResourcesType("GRAIN") - 1);
        p.setNumberResourcesType("WOOL", p.getNumberResourcesType("WOOL") - 1);

        p.setVictoryPoints(p.getVictoryPoints() + 1);

        p.addSettlement();
    }

    /**
     * Checks if the player can buy a City
     *
     * @param p the given Player
     * @return 0=success, 1=insufficient resources, 2=structure limit reached
     */

    public int canBuyCity(Player p) {
        // Check Player has sufficient resources
        if (p.getNumberResourcesType("GRAIN") < 2 || p.getNumberResourcesType("ORE") < 3) {
            return 1;
        }

        // Check Player has not exceeded capacity for an object
        if (p.getNumbCities() >= 4) {
            return 2;
        }

        return 0;
    }

    /**
     * Buys City for given Player
     *
     * @param p the given Player
     */

    public void buyCity(Player p) {
        if (canBuyCity(p) != 0)
            return;

        p.setNumberResourcesType("GRAIN", p.getNumberResourcesType("GRAIN") - 2);
        p.setNumberResourcesType("ORE", p.getNumberResourcesType("ORE") - 3);

        p.setVictoryPoints(p.getVictoryPoints() + 1);
        if (p.getVictoryPoints() >= 10) {
            GameRunner.setWinner(p);
        }

        p.upCity();
    }

    /**
     * Places a Road for the given Player at the given EdgeLocation
     *
     * @param p       the Player placing
     * @param loc     the EdgeLocation to place the Road
     * @param initial Whether the road is part of the initial setup
     * @return whether the Road can go there
     */
    public boolean placeRoad(EdgeLocation loc, Player p, boolean initial) {
        if (!board.placeRoad(loc, p, initial)) {
            return false;
        }

        int len = board.findLongestRoad(p);

        if (len > longestRoadLength) {
            if (longestRoadOwner != null) {
                longestRoadOwner.setVictoryPoints(longestRoadOwner.getVictoryPoints() - 2);
            }

            longestRoadOwner = p;
            longestRoadLength = len;

            p.setVictoryPoints(p.getVictoryPoints() + 2);
        }

        return true;
    }

    /**
     * Places a Settlement for the given Player at the given VertexLocation
     *
     * @param p   the Player placing
     * @param loc the VertexLocation to place the Settlement
     * @return whether the Settlement can go there
     */
    public boolean placeStructure(VertexLocation loc, Player p) {
        if (!board.placeStructure(loc, p))
            return false;

        if (longestRoadOwner == null || board.findLongestRoad(longestRoadOwner) == longestRoadLength)
            // Can't split the longest road if there is non
            // And the title cannot be transferred if it doesn't get split
            return true;

        longestRoadOwner.setVictoryPoints(longestRoadOwner.getVictoryPoints() - 2);

        longestRoadOwner = null;
        longestRoadLength = 4;

        for (Player player : players) {
            int len = board.findLongestRoad(player);

            if (len > longestRoadLength) {
                longestRoadLength = len;
                longestRoadOwner = player;
            } else if (len == longestRoadLength) {
                // Multiple people with same length of longest road
                longestRoadOwner = null;
            }
        }

        if (longestRoadOwner != null) {
            longestRoadOwner.setVictoryPoints(longestRoadOwner.getVictoryPoints() + 2);
        }

        return true;
    }

    /**
     * Places a City for the given Player at the given VertexLocation
     *
     * @param p   the Player placing
     * @param loc the VertexLocation to place the City
     * @return whether the City can go there
     */
    public boolean placeCity(VertexLocation loc, Player p) {
        return board.placeCity(loc, p);
    }

    /**
     * Getter for board's tiles
     *
     * @return tile array
     */
    public Board getBoard() {
        return board;
    }

}
