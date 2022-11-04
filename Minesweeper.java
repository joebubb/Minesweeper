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

    public void makeSelection(Location location) throws MinesweeperException {
        if (selectedLocations.contains(location)) {
            throw new MinesweeperException("That spot has already been selected."); 
        } else if (!locationIsValid(location)) {
            throw new MinesweeperException("That spot does not exist."); 
        } else {
            selectedLocations.add(location); 
        }
    }
    
    public GameState getGameState() {
        return state; 
    }

    public int getMoveCount() {
        return moveCount; 
    }
}
