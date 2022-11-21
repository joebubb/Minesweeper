package model;

public interface MinesweeperObserver {
    
    /**
     * Called when the state of a cell is changed on click.
     * @param location the location that was clicked
     */
    public void cellUpdated(Location location) throws MinesweeperException;
}
