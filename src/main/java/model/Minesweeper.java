package model;
import java.util.*;

public class Minesweeper {
    private enum Spot {
        Safe, 
        Mine
    }

    private MinesweeperObserver observer;
    private Spot[][] board; 
    private final char MINE = 'M'; 
    private final char COVERED = '-'; 
    private Set<Location> selectedLocations; 
    private Set<Location> mineLocations;
    private GameState state; 
    private int moveCount;
    private int mineCount;

    /**
     * Creates a new Minesweeper game with the given number of rows, columns, and mines.
     * @param rows the number of rows in the game
     * @param columns the number of columns in the game
     * @param mineCount the number of mines in the game
     */

    //TODO: Make first move safe?
    public Minesweeper(int rows, int columns, int mineCount) {
        //Creates a new board with X row and Y columns
        board = new Spot[rows][columns];

        //creates a new set of mine locations
        mineLocations = new HashSet<Location>();

        // place the mines 
        int minesPlanted = 0; 
        Random generator = new Random(); 

        while (minesPlanted < mineCount && minesPlanted < (rows * columns)) { // if mineCount > total spots, a mine is put in every spot 
            int row = generator.nextInt(rows); 
            int column = generator.nextInt(columns); 

            // the spot is free if it equals null
            if (null == board[row][column]) {
                board[row][column] = Spot.Mine; 
                mineLocations.add(new Location(row, column));
                minesPlanted += 1; 
            }
        }
        this.mineCount = minesPlanted;
        // wherever a mine was not placed, designate the spot as safe 
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < columns; c++) {
                if (null == board[r][c] ) {
                    board[r][c] = Spot.Safe; 
                }
            }
        }

        // set other variables 
        state = GameState.NOT_STARTED; 
        moveCount = 0; 
        selectedLocations = new HashSet<Location>();
        observer = null;
    }  
    
    /**
     * Adds an observer to make the GUI work
     * @param observer the game's observer
     */
    public void register(MinesweeperObserver observer) {
        this.observer = observer;
    }
    /**
     * Notifies the observer of the change in the game
     * @param location the location that was clicked
     */
    private final void notifyObserver(Location location) {
        if (observer != null) {
            observer.cellUpdated(location);
        }
    }

    /**
     * Gets a symbol representing the tile that was clicked
     * @param location location of the tile
     * @return the symbol that was clicked 
     * @throws MinesweeperException if the location is invalid
     */
    public char getSymbol(Location location) throws MinesweeperException{
        if (!getPossibleSelections().contains(location)) {
            throw new MinesweeperException("That selection is invalid.");
        }
        if(mineLocations.contains(location)) {
            return MINE;
        }
        if(selectedLocations.contains(location)) {
            return (char)minesAroundSpot(location);
        }
        if(isCovered(location)){
            return COVERED;
        }
        throw new MinesweeperException("That selection is invalid.");
    }

    /**
     * Checks if a tile is covered
     * @param location location of the tile
     * @return true if the tile is covered, false otherwise
     * @throws MinesweeperException if the location is invalid
     */
    public boolean isCovered(Location location) throws MinesweeperException {
        if (!getPossibleSelections().contains(location)) {
            throw new MinesweeperException("That selection is invalid.");
        }
        if(selectedLocations.contains(location)) {
            return false;
        }
        return true;
    }



    /**
     * Checks if chosen location is out of bounds
     * @param location the location that was selected
     * @return true if the location is not out of bounds
     */
    private Boolean locationIsValid(Location location) {
        if (null == location) { return false; }

        int row = location.getRow(); 
        int column = location.getCol(); 

        // if the row or column is < 0 or > the last index, the user's input 
        // must have been out of bounds 
        if (row < 0 || row >= board.length) {
            return false; 
        }
        if (column < 0 || column >= board[0].length) {
            return false; 
        }
        return true; 
    }

    /**
     * The Collection of places that the user is able to select
     * @return the possible spots that the user can select
     */
    private Collection<Location> getPossibleSelections() {
        List<Location> result = new ArrayList<Location>();
        // get all non-selected spots
        for (int row = 0; row < board.length; row++) {
            for (int column = 0; column < board[0].length; column++) {
                Location l = new Location(row, column);
                if (!(selectedLocations.contains(l))) {
                    result.add(l);
                }
            }
        }
        return result;
    }
    /**
     * Selects the location the user inputted and checks if there is a mine there
     * @param location the location that was selected
     * @throws MinesweeperException if the location is out of bounds
     */
    public void makeSelection(Location location) throws MinesweeperException {
        if (!getPossibleSelections().contains(location)) {
            throw new MinesweeperException("That selection is invalid.");
        } else {
            selectedLocations.add(location);
            moveCount++;
            // start the game if it hasn't started yet
            if (state == GameState.NOT_STARTED) { state = GameState.IN_PROGRESS; }

            //
            int row = location.getRow();
            int column = location.getCol();
            Spot selectedSpot = board[row][column];

            if (selectedSpot == Spot.Mine) {
                state = GameState.LOST;
            } 

            else { // spot is safe

                //if all the locations not chosen are the same amount as the amount of mines, the user wins
                if(selectedLocations.size() == (board.length * board[0].length) - mineLocations.size()) {
                    state = GameState.WON;
                }
            }
            notifyObserver(location);
        }
    }
    
    /**
     * Reveals the board to the user after the game is won or lost.
     */
    public void revealBoard() {
        for (int row = 0; row < board.length; row++) {
            for (int column = 0; column < board[0].length; column++) {
                Location l = new Location(row, column);
                selectedLocations.add(l);
                notifyObserver(l);
            }
        }
    }

    /**
     * Looks for the amount of bombs around a selected point
     * @param l the location that was selected
     * @return the amount of bombs around a selected point
     */
    public int minesAroundSpot(Location l) {
        int row = l.getRow();
        int column = l.getCol();
        int mines = 0;

        for (int r2 = row - 1; r2 <= row + 1; r2++) {
            for (int c2 = column - 1; c2 <= column + 1; c2++) {
                Location tmp = new Location(r2, c2);
                if (locationIsValid(tmp) && tmp != l) {
                    if (board[r2][c2] == Spot.Mine) {
                        mines++;
                    }
                }
            }
        }
        return mines;
    }
    /**
     * Checks what the state of the game is
     * @return the state of the game
     */
    public GameState getGameState() { return state; }
    
    /**
     * Checks how many moves the user has made
     * @return the amount of moves the user has made
     * @throws MinesweeperException if there are no more hints
     */
    public int getMoveCount() { return moveCount; }

    public Location giveHint() throws MinesweeperException {
        Collection<Location> possibleSelections = getPossibleSelections();
        for (Location l : possibleSelections) {
            if (!mineLocations.contains(l)) {
                return l;
            }
        }
        throw new MinesweeperException("No hint available.");
    }


    @Override
    /**
     * Returns a visual representation of the board
     */
    public String toString() {
        String result = "";

        for (int row = 0; row < board.length; row++) {
            for (int column = 0; column < board[0].length; column++) {
                Location l = new Location(row, column);
                if (selectedLocations.contains(l)) {
                    if (board[row][column] == Spot.Mine) {
                        result += MINE;
                    } else {
                        result += minesAroundSpot(l);
                    }
                } else {
                    result += COVERED;
                }
            }
            result += "\n";
        }

        return result;
    }

    public int getMineCount() {
        return this.mineCount;
    }


    public static void main(String[] args) {
        
    }
}