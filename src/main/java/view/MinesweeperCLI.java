package view;

import java.util.Scanner;

import model.*;


public class MinesweeperCLI {
    
    private static int rows;
    private static int columns;
    private static int mineCount;


    /**
     * Process to start the game
     * @return a new game
     */
    public static Minesweeper startGame(Scanner scan) throws MinesweeperException {
        System.out.println("Enter the number of rows, columns, and mines you would like to play with in the format: rows columns mines (ex: 10 15 25)");
        try{
            String input = scan.nextLine();
            String[] inputArray = input.split(" ");
            if(inputArray.length != 3){
                throw new MinesweeperException("Invalid input. Please enter the number of rows, columns, and mines you would like to play with in the format: rows columns mines (ex: 10 15 25)");
            }
            rows = Integer.parseInt(inputArray[0]);
            columns = Integer.parseInt(inputArray[1]);
            mineCount = Integer.parseInt(inputArray[2]);
        } catch (MinesweeperException e){
            System.out.println(e.getMessage());
            return startGame(scan);
        }
        return new Minesweeper(rows, columns, mineCount);
    }

    /**
     * Resets the board with the same parameters as it was started with
     * @param rows the number of rows in the game as inputted by the user in the startGame(scan) method
     * @param col the number of columns in the game as inputted by the user in the startGame(scan) method
     * @param mineCount the number of mines in the game as inputted by the user in the startGame(scan) method
     * @return a new game with the same parameters as the previous game
     */
    public static Minesweeper startGame(int rows, int col, int mineCount){
        return new Minesweeper(rows, col, mineCount);
    }

    /**
     * Help Menu for the game if the user inputs "help"
     */
    public static void getHelp(){
        System.out.println("help - Displays this list of commands");
        System.out.println("pick {row} {column} - Selects a spot on the board");
        System.out.println("hint - Shows a possible selection on the board");
        System.out.println("reset - creates a new board with the same parameters");
        System.out.println("quit - Exits the game");
    }

    public static void getHint(Minesweeper game){
        try{
            Location hint = game.giveHint();
            System.out.println("Hint: " + hint.getRow() + " " + hint.getCol());
        } catch (MinesweeperException e){
            System.out.println(e.getMessage());
        }
    }

    public static void main(String[] args) throws MinesweeperException{
        Scanner scan = new Scanner(System.in);
        System.out.println("Welcome to Minesweeper!");
        Minesweeper game = startGame(scan);
        while(true){
            System.out.println(game.toString());
            System.out.println(game.getMoveCount() + " moves made");
            System.out.println("Enter a command (help for a list of commands)");
            String input = scan.nextLine();

            String[] inputArray = input.split(" ");
            switch (inputArray[0]){
                case "help":
                    getHelp();
                    break;
                case "pick":
                    try{
                        int row = Integer.parseInt(inputArray[1]);
                        int column = Integer.parseInt(inputArray[2]);
                        Location location = new Location(row, column);
                        game.makeSelection(location);
                    } catch (MinesweeperException e){
                        System.out.println(e.getMessage());
                    }
                    break;
                case "hint":
                    getHint(game);
                    System.out.println("hint");
                    break;
                case "reset":
                    startGame(rows, columns, mineCount);
                    break;
                case "quit":
                    break;
                default:
                    System.out.println("Invalid command. Please enter a valid command.");
                    break;
            }
            if(inputArray[0].equals("quit")){
                break;
            }
            if(game.getGameState() == GameState.LOST){
                System.out.println("You lost!");
                game.revealBoard();
                System.out.println(game.toString());
                System.out.println("Game Over!");
                break;
            }
            if(game.getGameState() == GameState.WON){
                System.out.println("You won!");
                game.revealBoard();
                System.out.println(game.toString());
                System.out.println("Game Over!");
                break;
            }
        }
    }
}
