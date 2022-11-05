import java.util.*;

public class Minesweeper {
    private enum Spot {
        Safe, 
        Mine
    }

    private Spot[][] board; 
    private final char MINE = 'M'; 
    private final char COVERED = '-'; 
    private Set<Location> selectedLocations; 
    private GameState state; 
    private int moveCount; 

    public Minesweeper(int rows, int columns, int mineCount) {
        board = new Spot[rows][columns];

        // place the mines 
        int minesPlanted = 0; 
        Random generator = new Random(); 

        while (minesPlanted < mineCount && minesPlanted < (rows * columns)) { // if mineCount > total spots, a mine is put in every spot 
            int row = generator.nextInt(rows); 
            int column = generator.nextInt(columns); 

            // the spot is free if it equals null
            if (null == board[row][column]) {
                board[row][column] = Spot.Mine; 
                minesPlanted += 1; 
            }
        }

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
    }  

    public static void main(String[] args) {
        
    }
    
    private Boolean locationIsValid(Location location) {
        if (null == location) { return false; }

        int row = location.getRow(); 
        int column = location.getColumn(); 

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
            int column = location.getColumn();
            Spot selectedSpot = board[row][column];

            if (selectedSpot == Spot.Mine) {
                state = GameState.LOST;
            } else { // spot is safe

            }
        }
    }

    public int minesAroundSpot(Location l) {
        int row = l.getRow();
        int column = l.getColumn();
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
    
    public GameState getGameState() { return state; }
    public int getMoveCount() { return moveCount; }

    @Override
    public String toString() {
        String result = "";

        for (int row = 0; row < board.length; row++) {
            for (int column = 0; column < board[0].length; column++) {
                Location l = new Location(row, column);

            }
            result += "\n";
        }

        return result;
    }
}
