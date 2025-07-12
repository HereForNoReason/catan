package game;

import board.Road;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * This class is a Player in the game Settlers of Catan
 */
public class Player {

    private final String name;
    private final Color color;
    private final HashMap<String, Integer> resources;
    private final ArrayList<Road> roads;
    private int numbSettlements = 2;
    private int victoryPoints = 2;
    private int numbRoads = 0;
    private int numbCities = 0;
    private final boolean[] ports = {false, false, false, false, false, false};
    // 0 = general
    // 1 = brick
    // 2 = wool
    // 3 = ore
    // 4 = grain
    // 5 = lumber


    /**
     * Constructor takes params for assignment to fields
     *
     * @param n is the Player's name
     * @param c Color of the player
     */
    public Player(String n, Color c) {

        name = n;
        color = c;
        roads = new ArrayList<>();
        resources = new HashMap<>(5);
        resources.put("BRICK", 0);
        resources.put("WOOL", 0);
        resources.put("ORE", 0);
        resources.put("GRAIN", 0);
        resources.put("LUMBER", 0);

    }

    /**
     * Dev testing constructor to make player w/ predefined fields
     *
     * @param n      is the player name
     * @param c      is the given color
     * @param brick  amount of this resource
     * @param wool   amount of this resource
     * @param ore    amount of this resource
     * @param grain  amount of this resource
     * @param lumber amount of this resource
     * @param vP     number of victory points
     */
    public Player(String n, Color c, int brick, int wool, int ore, int grain, int lumber, int vP) {

        this(n, c);

        setNumberResourcesType("BRICK", brick);
        setNumberResourcesType("WOOL", wool);
        setNumberResourcesType("ORE", ore);
        setNumberResourcesType("GRAIN", grain);
        setNumberResourcesType("LUMBER", lumber);

        victoryPoints = vP;
    }

    /**
     * Getter for the Player's name
     *
     * @return name of Player
     */
    public String getName() {
        return name;
    }

    /**
     * Getter for the Player's color
     *
     * @return color of Player
     */
    public Color getColor() {
        return color;
    }

    /**
     * Getter for the number of victory points from this Player
     *
     * @return number of victory points
     */
    public int getVictoryPoints() {
        return victoryPoints;
    }

    /**
     * Setter for the number of victory points from this Player
     *
     * @param vP new number of victory points
     */
    public void setVictoryPoints(int vP) {
        victoryPoints = vP;
    }

    /**
     * Getter for this Player's quantity of this given resource type
     *
     * @param str resource to work with
     * @return number of resources str owned by this Player
     */
    public int getNumberResourcesType(String str) {
        if (str == null || str.equals("DESERT"))
            return 0;
        return resources.get(str);
    }

    /**
     * Setter for this Player's quantity of this given resource type
     *
     * @param str resource to work with
     * @param n   new number of resources of type str
     */
    public void setNumberResourcesType(String str, int n) {
        resources.put(str, n);
    }


    /**
     * Adds the given road to the list of owned roads
     *
     * @param r road added
     */
    public void addRoad(Road r) {
        roads.add(r);
    }

    /**
     * Getter for this player's roads
     *
     * @return list or owned roads
     */
    public ArrayList<Road> getRoads() {
        return roads;
    }


    /**
     * Checks if this Player has the specified resources
     *
     * @param res the resources to check
     * @return whether the Player has those resources
     */
    public boolean hasResources(ArrayList<String> res) {
        int wool = 0,
                ore = 0,
                lumber = 0,
                brick = 0,
                grain = 0;

        for (String r : res) {
            switch (r) {
                case "WOOL":
                    wool++;
                    break;
                case "ORE":
                    ore++;
                    break;
                case "LUMBER":
                    lumber++;
                    break;
                case "BRICK":
                    brick++;
                    break;
                case "GRAIN":
                    grain++;
                    break;
            }
        }

        return wool <= resources.get("WOOL") && ore <= resources.get("ORE") && lumber <= resources.get("LUMBER") && brick <= resources.get("BRICK") && grain <= resources.get("GRAIN");
    }


    /**
     * Sets the corresponding port to true
     *
     * @param portTag 0 = general
     *                1 = brick
     *                2 = wool
     *                3 = ore
     *                4 = grain
     *                5 = lumber
     */
    public void addPort(int portTag) {
        ports[portTag] = true;
    }

    /**
     * Getter for the list of ports
     *
     * @return ports list of ports
     */
    public boolean[] getPorts() {
        return ports;
    }

    /**
     * Overridden toString method
     *
     * @return name
     */
    public String toString() {
        return name;
    }

    /**
     * Getter for numbSettlements
     *
     * @return int number of settlements
     */
    public int getNumbSettlements() {
        return numbSettlements;
    }

    /**
     * Getter for numbCities
     *
     * @return int number of cities
     */
    public int getNumbCities() {
        return numbCities;
    }

    /**
     * Getter for numbRoads
     *
     * @return int number of roads
     */
    public int getNumbRoads() {
        return numbRoads;
    }

    /**
     * Adds 1 to numbSettlements
     */
    public void addSettlement() {
        numbSettlements++;
    }

    /**
     *  Increase City count and therefore decrease Settlement count
     */
    public void upCity() {
        numbSettlements--;
        numbCities++;
    }

    /**
     * Adds 1 to numbRoads
     */
    public void addRoadCount() {
        numbRoads++;
    }

    /**
     * Adds one to specified resource
     *
     * @param str the resource type to increment
     */
    public void giveResourceType(String str) {
        if (str == null || str.equals("DESERT")) {
            return;
        }
        resources.put(str, resources.get(str) + 1);
    }

    /**
     * Removes all resources in given list from this player
     *
     * @param rez list of resources to be removed
     */
    public void removeResources(ArrayList<String> rez) {
        for (String s : rez) {
            //System.out.println("Removed " + s);
            setNumberResourcesType(s, getNumberResourcesType(s) - 1);
        }
    }


    /**
     * Gets the total amount of resources this player has
     *
     * @return int total resources
     */
    public int getTotalResources() {
        return getNumberResourcesType("BRICK") +
                getNumberResourcesType("WOOL") +
                getNumberResourcesType("ORE") +
                getNumberResourcesType("GRAIN") +
                getNumberResourcesType("LUMBER");
    }

}
