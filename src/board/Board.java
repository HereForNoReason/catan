package board;

import game.Player;

import java.util.ArrayList;
import java.util.Collections;


/**
 * This Board represents the board in Settlers of Catan, and contains the grids for tiles, structures, and roads.
 */
public class Board {

    private final Tile[][] tiles;
    private final Structure[][][] structures;
    private final Road[][][] roads;
    private Location robberLoc;


    /**
     * Constructor for Board, creates the hexagonal grid for the tiles, with arbitrary third axis for structures and roads.
     * Tiles randomly placed, and assigned numbers according to the Settlers of Catan rulebook, going in a spiral fashion and skipping the desert.
     * Settlements and Roads are placed at every vertex and edge, respectively, with unassigned players.
     */
    public Board() {

        tiles = new Tile[7][7];
        structures = new Structure[7][7][2];
        roads = new Road[7][7][3];
        Tile desert = new Tile("DESERT", true);

        // Create the ArrayList of all the tiles to be put in the board, with the type of resource defined
        ArrayList<Tile> tileList = new ArrayList<>();
        tileList.add(new Tile("LUMBER"));
        tileList.add(new Tile("LUMBER"));
        tileList.add(new Tile("LUMBER"));
        tileList.add(new Tile("LUMBER"));
        tileList.add(new Tile("BRICK"));
        tileList.add(new Tile("BRICK"));
        tileList.add(new Tile("BRICK"));
        tileList.add(new Tile("GRAIN"));
        tileList.add(new Tile("GRAIN"));
        tileList.add(new Tile("GRAIN"));
        tileList.add(new Tile("GRAIN"));
        tileList.add(new Tile("WOOL"));
        tileList.add(new Tile("WOOL"));
        tileList.add(new Tile("WOOL"));
        tileList.add(new Tile("WOOL"));
        tileList.add(new Tile("ORE"));
        tileList.add(new Tile("ORE"));
        tileList.add(new Tile("ORE"));
        tileList.add(desert);

        // Create random order
        Collections.shuffle(tileList);

        // Place all the tiles in the board
        int count = 0;

        for (int row = 1; row < 6; row++) {
            switch (row) {
                case 1:
                    for (int col = 1; col < 4; col++) {
                        tiles[col][row] = tileList.get(count);
                        tiles[col][row].setCoords(col, row);
                        count++;
                    }
                    break;
                case 2:
                    for (int col = 1; col < 5; col++) {
                        tiles[col][row] = tileList.get(count);
                        tiles[col][row].setCoords(col, row);
                        count++;
                    }
                    break;
                case 3:
                    for (int col = 1; col < 6; col++) {
                        tiles[col][row] = tileList.get(count);
                        tiles[col][row].setCoords(col, row);
                        count++;
                    }
                    break;
                case 4:
                    for (int col = 2; col < 6; col++) {
                        tiles[col][row] = tileList.get(count);
                        tiles[col][row].setCoords(col, row);
                        count++;
                    }
                    break;
                case 5:
                    for (int col = 3; col < 6; col++) {
                        tiles[col][row] = tileList.get(count);
                        tiles[col][row].setCoords(col, row);
                        count++;
                    }
                    break;
            }

            robberLoc = desert.getLocation();
        }

        // The order of the numbers to be assigned to the tiles, followed by an int to be used as an index
        int[] numberOrder = {5, 2, 6, 3, 8, 10, 9, 12, 11, 4, 8, 10, 9, 4, 5, 6, 3, 11};
        int numberTile = 0;

        // The x y pairs to proceed in a spiral
        int[] tileOrder = {3, 5, 2, 4, 1, 3, 1, 2, 1, 1, 2, 1, 3, 1, 4, 2, 5, 3, 5, 4, 5, 5, 4, 5, 3, 4, 2, 3, 2, 2, 3, 2, 4, 3, 4, 4, 3, 3};

        // Assigning all values from numberOrder to the Tiles in the board, proceeding in a spiral
        for (int n = 0; n < tileOrder.length - 1; n += 2) {
            if (numberTile == 18) {
                break;
            }

            if (!tiles[tileOrder[n]][tileOrder[n + 1]].getType().equals("DESERT")) {
                tiles[tileOrder[n]][tileOrder[n + 1]].setNumber(numberOrder[numberTile]);
                numberTile++;
            }
        }

        // Place all the empty Tiles in Board
        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles[0].length; j++) {
                if (tiles[i][j] == null)
                    tiles[i][j] = new Tile(i, j, 0, null);
            }
        }

        // Place all Structures in Board
        for (int row = 0; row < structures.length; row++) {
            for (int col = 0; col < structures[0].length; col++) {
                for (int ori = 0; ori < structures[0][0].length; ori++) {
                    structures[col][row][ori] = new Structure(col, row, ori);
                }
            }
        }

        // Place all the Roads in the Board
        for (int row = 0; row < roads.length; row++) {
            for (int col = 0; col < roads[0].length; col++) {
                for (int ori = 0; ori < roads[0][0].length; ori++) {
                    roads[col][row][ori] = new Road(col, row, ori);
                }
            }
        }
    }

    /**
     * Distributes resources to all Players with a Structure bordering Tiles with number roll
     *
     * @param roll the value of the Tiles that have produced
     */
    public void distributeResources(int roll) {

        ArrayList<Tile> rollTiles = getTilesWithNumber(roll);

        for (Tile t : rollTiles) {
            if (t.hasRobber() || t.getType().equals("DESERT")) {
                continue;
            }

            ArrayList<Structure> rollStructures = new ArrayList<>();

            Location loc = t.getLocation();

            // Add all the six structures to the ArrayList
            rollStructures.add(structures[loc.getXCoord()][loc.getYCoord()][0]);
            rollStructures.add(structures[loc.getXCoord()][loc.getYCoord()][1]);
            rollStructures.add(structures[loc.getXCoord() + 1][loc.getYCoord() + 1][1]);
            rollStructures.add(structures[loc.getXCoord() - 1][loc.getYCoord() - 1][0]);
            rollStructures.add(structures[loc.getXCoord()][loc.getYCoord() + 1][1]);
            rollStructures.add(structures[loc.getXCoord()][loc.getYCoord() - 1][0]);

            for (Structure s : rollStructures) {
                if (null != s.getOwner())
                    s.giveResources(t.getType());
            }
        }
    }

    /**
     * Searches the Board for any Tiles with the value of the param and returns an ArrayList of them
     *
     * @param numb the roll number to be found on the Tile
     * @return an ArrayList of found Tiles
     */
    private ArrayList<Tile> getTilesWithNumber(int numb) {

        ArrayList<Tile> rollTiles = new ArrayList<>();

        for (int i = 1; i < tiles.length; i++) {
            for (int j = 1; j < tiles[i].length; j++) {
                if (tiles[i][j].getNumber() == numb)
                    rollTiles.add(tiles[i][j]);
            }
        }
        return rollTiles;
    }

    /**
     * Assigns the settlement to the given player, even without a road
     * this is needed for the initial game setup
     *
     * @param loc    Location of settlement
     * @param player Player placing the settlement
     * @return Success of placement
     */
    public boolean placeStructureNoRoad(VertexLocation loc, Player player) {
        if (!canPlaceStructureNoRoad(loc))
            return false;

        if (checkPort(loc) != -1)
            player.addPort(checkPort(loc));
        structures[loc.getXCoord()][loc.getYCoord()][loc.getOrientation()].setOwner(player);
        return true;
    }

    /**
     * Checks if an initial settlement can be placed here
     *
     * @param loc Location of settlement
     * @return Whether the location is still valid
     */
    public boolean canPlaceStructureNoRoad(VertexLocation loc) {
        if (structures[loc.getXCoord()][loc.getYCoord()][loc.getOrientation()].getOwner() != null) { //Vertex is already occupied
            return false;
        }

        if (loc.getOrientation() == 0) {
            return structures[loc.getXCoord()][loc.getYCoord() + 1][1].getOwner() == null &&
                    structures[loc.getXCoord() + 1][loc.getYCoord() + 1][1].getOwner() == null &&
                    !(loc.getYCoord() + 2 <= 6 && !(structures[loc.getXCoord() + 1][loc.getYCoord() + 2][1].getOwner() == null));
        } else {
            return structures[loc.getXCoord()][loc.getYCoord() - 1][0].getOwner() == null &&
                    structures[loc.getXCoord() - 1][loc.getYCoord() - 1][0].getOwner() == null &&
                    !(loc.getYCoord() - 2 >= 0 && !(structures[loc.getXCoord() - 1][loc.getYCoord() - 2][0].getOwner() == null));
        }
    }

    /**
     * Checks location for validity for given player, then assigns the settlement to the given player
     *
     * @param loc    Location of settlement
     * @param player Player placing the settlement
     * @return Success of placement
     */
    public boolean placeStructure(VertexLocation loc, Player player) {
        if (!canPlaceStructure(loc, player))
            return false;

        if (checkPort(loc) != -1)
            player.addPort(checkPort(loc));
        structures[loc.getXCoord()][loc.getYCoord()][loc.getOrientation()].setOwner(player);
        return true;
    }

    /**
     * Checks location for validity for given player
     *
     * @param loc    Location of settlement
     * @param player Player placing the settlement
     * @return Whether the player can place a settlement at the given location
     */
    public boolean canPlaceStructure(VertexLocation loc, Player player) {

        if (structures[loc.getXCoord()][loc.getYCoord()][loc.getOrientation()].getOwner() != null) { //Vertex is already occupied
            return false;
        }

        if (!canPlaceStructureNoRoad(loc))
            return false;

        if (loc.getOrientation() == 0) {
            return (player.equals(roads[loc.getXCoord()][loc.getYCoord()][0].getOwner()) ||
                    player.equals(roads[loc.getXCoord()][loc.getYCoord()][1].getOwner()) ||
                    player.equals(roads[loc.getXCoord()][loc.getYCoord() + 1][2].getOwner()));
        } else {
            return (player.equals(roads[loc.getXCoord()][loc.getYCoord() - 1][0].getOwner()) ||
                    player.equals(roads[loc.getXCoord() - 1][loc.getYCoord() - 1][1].getOwner()) ||
                    player.equals(roads[loc.getXCoord() - 1][loc.getYCoord() - 1][2].getOwner()));
        }
    }

    /**
     * Checks location for validity for given player, assigns the road to the given player
     *
     * @param loc    Location of the road
     * @param player Player placing the road
     * @return Success of placement
     */
    public boolean placeRoad(EdgeLocation loc, Player player) {
        if (!canPlaceRoad(loc, player))
            return false;

        roads[loc.getXCoord()][loc.getYCoord()][loc.getOrientation()].setOwner(player);
        return true;
    }

    /**
     * Checks location for validity for given player
     *
     * @param loc    Location of the road
     * @param player Player placing the road
     * @return Whether the player can place a road at the given location
     */
    public boolean canPlaceRoad(EdgeLocation loc, Player player) {

        if (roads[loc.getXCoord()][loc.getYCoord()][loc.getOrientation()].getOwner() != null) { //Vertex is already occupied
            return false;
        }

        if (loc.getOrientation() == 0) {
            return player.equals(structures[loc.getXCoord()][loc.getYCoord() + 1][1].getOwner()) ||
                    player.equals(structures[loc.getXCoord()][loc.getYCoord()][0].getOwner()) ||
                    player.equals(roads[loc.getXCoord() - 1][loc.getYCoord()][1].getOwner()) ||
                    player.equals(roads[loc.getXCoord() - 1][loc.getYCoord()][2].getOwner()) ||
                    player.equals(roads[loc.getXCoord()][loc.getYCoord() + 1][2].getOwner()) ||
                    player.equals(roads[loc.getXCoord()][loc.getYCoord()][1].getOwner());
        } else if (loc.getOrientation() == 1) {
            return player.equals(structures[loc.getXCoord()][loc.getYCoord()][0].getOwner()) ||
                    player.equals(structures[loc.getXCoord() + 1][loc.getYCoord() + 1][1].getOwner()) ||
                    player.equals(roads[loc.getXCoord()][loc.getYCoord()][0].getOwner()) ||
                    player.equals(roads[loc.getXCoord()][loc.getYCoord() + 1][2].getOwner()) ||
                    player.equals(roads[loc.getXCoord()][loc.getYCoord()][2].getOwner()) ||
                    player.equals(roads[loc.getXCoord() + 1][loc.getYCoord()][0].getOwner());
        } else {
            return player.equals(structures[loc.getXCoord()][loc.getYCoord() - 1][0].getOwner()) ||
                    player.equals(structures[loc.getXCoord() + 1][loc.getYCoord() + 1][1].getOwner()) ||
                    player.equals(roads[loc.getXCoord()][loc.getYCoord()][1].getOwner()) ||
                    player.equals(roads[loc.getXCoord() + 1][loc.getYCoord()][0].getOwner()) ||
                    player.equals(roads[loc.getXCoord()][loc.getYCoord() - 1][0].getOwner()) ||
                    player.equals(roads[loc.getXCoord()][loc.getYCoord() - 1][1].getOwner());
        }
    }

    /**
     * Checks location for validity for given player, then upgrades settlement into city
     *
     * @param loc    Location of the road
     * @param player Player placing the road
     * @return Success of placement
     */
    public boolean placeCity(VertexLocation loc, Player player) {
        if (!canPlaceCity(loc, player))
            return false;

        structures[loc.getXCoord()][loc.getYCoord()][loc.getOrientation()].setType(1);
        return true;
    }

    /**
     * Checks location for validity for given player
     *
     * @param loc    Location of the road
     * @param player Player placing the road
     * @return Whether the player can place a city at the given location
     */
    public boolean canPlaceCity(VertexLocation loc, Player player) {
        return player.equals(structures[loc.getXCoord()][loc.getYCoord()][loc.getOrientation()].getOwner()) &&
                structures[loc.getXCoord()][loc.getYCoord()][loc.getOrientation()].getType() == 0;
    }

    /**
     * Getter for the Location of the Robber
     *
     * @return loc the current Location of the Robber in this board
     */
    public Location getRobberLocation() {
        return robberLoc;
    }

    /**
     * Setter for the Location of the Robber
     *
     * @param loc the new Location of Robber
     */
    public void setRobberLocation(Location loc) {
        robberLoc = loc;
    }

    /**
     * Checks location for validity, then moves the robber to that location
     *
     * @param loc location to move to
     * @return the location the robber moved to
     */

    public boolean moveRobber(Location loc) {
        Location current = getRobberLocation();
        if (loc.getXCoord() == current.getXCoord() &&
                loc.getYCoord() == current.getYCoord()) {
            return false;
        } else {
            tiles[current.getXCoord()][current.getYCoord()].setRobber(false);
            setRobberLocation(loc);
            tiles[loc.getXCoord()][loc.getYCoord()].setRobber(true);
            return true;
        }
    }

    /**
     * Gets all players with structures adjacent to the current robber location
     *
     * @return ArrayList of adjacent players
     */
    public ArrayList<Player> getRobberAdjacentPlayers() {
        Location loc = getRobberLocation();
        ArrayList<Structure> temp = new ArrayList<>();
        ArrayList<Player> players = new ArrayList<>();
        temp.add(structures[loc.getXCoord()][loc.getYCoord()][0]);
        temp.add(structures[loc.getXCoord() + 1][loc.getYCoord() + 1][1]);
        temp.add(structures[loc.getXCoord()][loc.getYCoord() - 1][0]);
        temp.add(structures[loc.getXCoord()][loc.getYCoord()][1]);
        temp.add(structures[loc.getXCoord() - 1][loc.getYCoord() - 1][0]);
        temp.add(structures[loc.getXCoord()][loc.getYCoord() + 1][1]);

        for (Structure s : temp) {
            if (s.getOwner() != null) {
                if (Collections.frequency(players, s.getOwner()) < 1) {
                    players.add(s.getOwner());
                }
            }
        }

        return players;
    }


    /**
     * Gives the tiles adjacent to the given VertexLocation
     *
     * @param loc location being checked
     * @return list of adjacent tiles
     */
    public ArrayList<Tile> getAdjacentTilesStructure(VertexLocation loc) {
        ArrayList<Tile> output = new ArrayList<>();
        if (loc.getOrientation() == 0) {
            Tile a = tiles[loc.getXCoord()][loc.getYCoord()];
            if (a.getType() != null)
                output.add(a);
            Tile b = tiles[loc.getXCoord()][loc.getYCoord() + 1];
            if (b.getType() != null)
                output.add(b);
            Tile c = tiles[loc.getXCoord() + 1][loc.getYCoord() + 1];
            if (c.getType() != null)
                output.add(c);
        } else {
            Tile a = tiles[loc.getXCoord()][loc.getYCoord()];
            if (a.getType() != null)
                output.add(a);
            Tile b = tiles[loc.getXCoord()][loc.getYCoord() - 1];
            if (b.getType() != null)
                output.add(b);
            Tile c = tiles[loc.getXCoord() - 1][loc.getYCoord() - 1];
            if (c.getType() != null)
                output.add(c);
        }
        return output;
    }

    /**
     * Finds the length of the longest chain of roads of the given player
     *
     * @param p player's roads to be analyzed
     * @return length of the longest chain of roads
     */
    public int findLongestRoad(Player p) {
        int max = 0;

        for (Road road : p.getRoads()) {
            EdgeLocation loc = road.getLocation();

            int x = loc.getXCoord();
            int y = loc.getYCoord();
            int o = loc.getOrientation();

            if (o == 0) {
                max = Math.max(max, Math.max(
                        searchMaxPathLength(p, road, structures[x][y + 1][1]),
                        searchMaxPathLength(p, road, structures[x][y][0])
                ));
            } else if (o == 1) {
                max = Math.max(max, Math.max(
                        searchMaxPathLength(p, road, structures[x + 1][y + 1][1]),
                        searchMaxPathLength(p, road, structures[x][y][0])
                ));
            } else {
                max = Math.max(max, Math.max(
                        searchMaxPathLength(p, road, structures[x + 1][y + 1][1]),
                        searchMaxPathLength(p, road, structures[x][y - 1][0])
                ));
            }
        }

        return max;
    }

    /**
     * Search for the length of the longest continuous path belonging to a single player.
     * The path starts with an edge in a certain direction and will ignore edges already on the path it's considering.
     *
     * @param p         Player the path must belong to
     * @param start     The edge to start at
     * @param direction The vertex at which to look for the next edges to consider
     * @return The length of the longest path from this point onwards
     */
    private int searchMaxPathLength(Player p, Road start, Structure direction) {
        if (start.isVisited())
            return 0;

        if (direction.getOwner() != null && !p.equals(direction.getOwner()))
            return 1;

        start.visit();

        EdgeLocation loc = start.getLocation();

        int x = loc.getXCoord();
        int y = loc.getYCoord();
        int o = loc.getOrientation();
        int o2 = direction.getLocation().getOrientation();

        int max = 0;

        if (o == 0) {
            if (o2 == 0) {
                if (x < 6 && y < 5 && p.equals(roads[x][y + 1][2].getOwner()))
                    max = Math.max(max, searchMaxPathLength(p, roads[x][y + 1][2], structures[x + 1][y + 2][1]));

                if (x < 6 && y < 6 && p.equals(roads[x][y][1].getOwner()))
                    max = Math.max(max, searchMaxPathLength(p, roads[x][y][1], structures[x + 1][y + 1][1]));
            } else {
                if (x > 0 && p.equals(roads[x - 1][y][1].getOwner()))
                    max = Math.max(max, searchMaxPathLength(p, roads[x - 1][y][1], structures[x - 1][y][0]));

                if (x > 0 && y > 0 && p.equals(roads[x - 1][y][2].getOwner()))
                    max = Math.max(max, searchMaxPathLength(p, roads[x - 1][y][2], structures[x - 1][y - 1][0]));
            }
        } else if (o == 1) {
            if (o2 == 0) {
                if (x < 6 && y < 5 && p.equals(roads[x][y + 1][2].getOwner()))
                    max = Math.max(max, searchMaxPathLength(p, roads[x][y + 1][2], structures[x + 1][y + 2][1]));

                if (y < 6 && p.equals(roads[x][y][0].getOwner()))
                    max = Math.max(max, searchMaxPathLength(p, roads[x][y][0], structures[x][y + 1][1]));
            } else {
                if (x < 6 && p.equals(roads[x + 1][y][0].getOwner()))
                    max = Math.max(max, searchMaxPathLength(p, roads[x + 1][y][0], structures[x + 1][y][0]));

                if (y > 0 && p.equals(roads[x][y][2].getOwner()))
                    max = Math.max(max, searchMaxPathLength(p, roads[x][y][2], structures[x][y - 1][0]));
            }
        } else {
            if (o2 == 0) {
                if (x < 6 && y > 0 && p.equals(roads[x][y - 1][1].getOwner()))
                    max = Math.max(max, searchMaxPathLength(p, roads[x][y - 1][1], structures[x + 1][y][1]));

                if (y > 0 && p.equals(roads[x][y - 1][0].getOwner()))
                    max = Math.max(max, searchMaxPathLength(p, roads[x][y - 1][0], structures[x][y][1]));
            } else {
                if (x < 6 && p.equals(roads[x + 1][y][0].getOwner()))
                    max = Math.max(max, searchMaxPathLength(p, roads[x + 1][y][0], structures[x + 1][y][0]));

                if (p.equals(roads[x][y][1].getOwner()))
                    max = Math.max(max, searchMaxPathLength(p, roads[x][y][1], structures[x][y][0]));
            }
        }

        start.resetVisited();

        return max + 1;
    }

    /**
     * Checks if given VertexLocation is a port, and returns the portTag is it is.
     * * @param loc the location to check at
     *
     * @return int portTag if port, -1 if not
     * 0 = general
     * 1 = brick
     * 2 = wool
     * 3 = ore
     * 4 = grain
     * 5 = lumber
     */
    private int checkPort(VertexLocation loc) {
        int x = loc.getXCoord();
        int y = loc.getYCoord();
        int o = loc.getOrientation();

        if ((x == 4 && y == 1 && o == 0) ||
                (x == 4 && y == 2 && o == 1)) {
            return 1;
        } else if ((x == 4 && y == 5 && o == 0) ||
                (x == 5 && y == 6 && o == 1)) {
            return 2;
        } else if ((x == 1 && y == 3 && o == 0) ||
                (x == 2 && y == 5 && o == 1)) {
            return 3;
        } else if ((x == 0 && y == 1 && o == 0) ||
                (x == 1 && y == 3 && o == 1)) {
            return 4;
        } else if ((x == 2 && y == 0 && o == 0) ||
                (x == 2 && y == 1 && o == 1)) {
            return 5;
        } else if ((x == 0 && y == 0 && o == 0) ||
                (x == 1 && y == 1 && o == 1) ||
                (x == 5 && y == 2 && o == 0) ||
                (x == 6 && y == 4 && o == 1) ||
                (x == 5 && y == 4 && o == 0) ||
                (x == 6 && y == 5 && o == 1) ||
                (x == 3 && y == 5 && o == 0) ||
                (x == 3 && y == 6 && o == 1)) {
            return 0;
        } else {
            return -1;
        }
    }

    /**
     * Getter for tiles array
     *
     * @return tiles array
     */
    public Tile[][] getTiles() {
        return tiles;
    }

    /**
     * Getter for this structure array
     *
     * @return structures array
     */
    public Structure[][][] getStructures() {
        return structures;
    }

    /**
     * Getter for roads array
     *
     * @return roads array
     */
    public Road[][][] getRoads() {
        return roads;
    }
}
