package board;

import game.Player;


/**
 * This is a class for cities and settlements
 */
public class Structure {

    private Player owner = null;
    private VertexLocation location;
    private int type;
    // Either 0 (Settlement), or 1 (City)

    /**
     * Constructor with three values to be used for the Structure's location
     *
     * @param x the x coordinate of the location
     * @param y the y coordinate of the location
     * @param o the orientation of the location
     */
    public Structure(int x, int y, int o) {
        setLocation(new VertexLocation(x, y, o));
        setType(0);
    }

    /**
     * This method will give resources of type resType to the owner of the Structure
     * 1 for settlements
     * 2 for cities
     *
     * @param resType the type of resource to be given to the owner
     */
    public void giveResources(String resType) {
        getOwner().setNumberResourcesType(resType, getOwner().getNumberResourcesType(resType) + 1 + getType());
    }

    /**
     * Getter for the Structure's owner
     *
     * @return the Structure's owner
     */
    public Player getOwner() {
        return owner;
    }

    /**
     * Setter for the Structure's owner
     * Can only be set if the Structure is unowned (only settable once)
     *
     * @param p the new owner of the Structure
     */
    public void setOwner(Player p) {
        if (null == owner)
            owner = p;
    }

    /**
     * Getter for the Structure's location
     *
     * @return the Structure's location
     */
    public VertexLocation getLocation() {
        return location;
    }

    /**
     * Setter for the Structure's location
     *
     * @param loc the new value for the Structure's location
     */
    public void setLocation(VertexLocation loc) {
        location = loc;
    }

    /**
     * Getter for the Structure's type
     *
     * @return the Structure's type
     */
    public int getType() {
        return type;
    }

    /**
     * Setter for the Structure's type
     *
     * @param t the new type of the Structure
     */
    public void setType(int t) {
        type = t;
    }

}