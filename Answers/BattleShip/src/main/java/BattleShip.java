import java.util.Random;
import java.util.Scanner;

/**
  The BattleShip class manages the gameplay of the Battleship game between two players.
  It includes methods to manage grids, turns, and check the game status.
 */
public class BattleShip {

    // Grid size for the game
    static final int GRID_SIZE = 10;

    // Player 1's main grid containing their ships
    static char[][] player1Grid = new char[GRID_SIZE][GRID_SIZE];

    // Player 2's main grid containing their ships
    static char[][] player2Grid = new char[GRID_SIZE][GRID_SIZE];

    // Player 1's tracking grid to show their hits and misses
    static char[][] player1TrackingGrid = new char[GRID_SIZE][GRID_SIZE];

    // Player 2's tracking grid to show their hits and misses
    static char[][] player2TrackingGrid = new char[GRID_SIZE][GRID_SIZE];

    // Scanner object for user input
    static Scanner scanner = new Scanner(System.in);

    /**
      The main method that runs the game loop.
      It initializes the grids for both players, places ships randomly, and manages turns.
      The game continues until one player's ships are completely sunk.
     */
    public static void main(String[] args) {
        // Initialize grids for both players
        initializeGrid(player1Grid);
        initializeGrid(player2Grid);
        initializeGrid(player1TrackingGrid);
        initializeGrid(player2TrackingGrid);

        // Place ships randomly on each player's grid
        placeShips(player1Grid);
        placeShips(player2Grid);

        // Variable to track whose turn it is
        boolean player1Turn = true;

        // Main game loop, runs until one player's ships are all sunk
        while (!isGameOver()) {
            if (player1Turn) {
                System.out.println("Player 1's turn:");
                printGrid(player1TrackingGrid);
                playerTurn(player2Grid, player1TrackingGrid);
            } else {
                System.out.println("Player 2's turn:");
                printGrid(player2TrackingGrid);
                playerTurn(player1Grid, player2TrackingGrid);
            }
            player1Turn = !player1Turn;
        }

        System.out.println("Game Over!");
    }

    /**
      Initializes the grid by filling it with water ('~').

      @param grid The grid to initialize.
     */
    static void initializeGrid(char[][] grid) {
        for (int i = 0; i < 10; i++){
            for (int j = 0; j < 10; j++){
                grid[i][j] = '~';
            }
        }
    }

    /**
      Places ships randomly on the given grid.
      This method is called for both players to place their ships on their respective grids.

      @param grid The grid where ships need to be placed.
     */
    static void placeShips(char[][] grid) {
        int[] shipSize = {5 , 4 , 3 , 2};
        Random random = new Random();
        for (int i = 0; i < 4; i++){
            int size = shipSize[i];
            boolean isPlaced = false;
            while (!isPlaced){
                int row = random.nextInt(GRID_SIZE);
                int col = random.nextInt(GRID_SIZE);
                boolean horizontal = random.nextInt(2) == 0;
                if (canPlaceShip(grid , row , col , size , horizontal)){
                    if (horizontal){
                        for (int j = 0; j < size; j++){
                            grid[row][col + j] = 'S';
                        }
                    }else {
                        for (int k = 0; k < size; k++){
                            grid[row + k][col] = 'S';
                        }
                    }
                    isPlaced = true;
                }
            }
        }
    }

    /**
      Checks if a ship can be placed at the specified location on the grid.
      This includes checking the size of the ship, its direction (horizontal or vertical),
      and if there's enough space to place it.

      @param grid The grid where the ship is to be placed.
      @param row The starting row for the ship.
      @param col The starting column for the ship.
      @param size The size of the ship.
      @param horizontal The direction of the ship (horizontal or vertical).
      @return true if the ship can be placed at the specified location, false otherwise.
     */
    static boolean canPlaceShip(char[][] grid, int row, int col, int size, boolean horizontal) {
        if (horizontal){
            if (col + size > GRID_SIZE) {
                return false;
            }
            for (int i = 0; i < size; i++){
                if (grid[row][col + i] != '~'){
                    return false;
                }
            }
        }else {
            if (row + size > GRID_SIZE){
                return false;
            }
            for (int i = 0; i < size; i++){
                if (grid[row + i][col] != '~'){
                    return false;
                }
            }
        }
        return true;
    }

    /**
      Manages a player's turn, allowing them to attack the opponent's grid
      and updates their tracking grid with hits or misses.

      @param opponentGrid The opponent's grid to attack.
      @param trackingGrid The player's tracking grid to update.
     */
    static void playerTurn(char[][] opponentGrid, char[][] trackingGrid) {
        String input = scanner.nextLine();
        if (!isValidInput(input)){
            System.out.println("Invalid input! Try again!");
            return;
        }
        int col = Character.toUpperCase(input.charAt(0)) - 'A';
        int row = Integer.parseInt(input.substring(1));
        if (opponentGrid[row][col] == '~'){
            System.out.println("Miss!");
            opponentGrid[row][col] = '0';
            trackingGrid[row][col] = '0';
        }else if (opponentGrid[row][col] == 'S'){
            System.out.println("Hit!");
            opponentGrid[row][col] = '*';
            trackingGrid[row][col] = '*';
        }else {
            System.out.println("Miss!");
        }
    }

    /**
      Checks if the game is over by verifying if all ships are sunk.

      @return true if the game is over (all ships are sunk), false otherwise.
     */
    static boolean isGameOver() {
        if (allShipsSunk(player1Grid)){
            System.out.println("Player 2 wins!");
            return true;
        }
        if (allShipsSunk(player2Grid)){
            System.out.println("Player 1 wins!");
            return true;
        }
        return false;
    }

    /**
      Checks if all ships have been destroyed on a given grid.

      @param grid The grid to check for destroyed ships.
      @return true if all ships are sunk, false otherwise.
     */
    static boolean allShipsSunk(char[][] grid) {
        for (int i = 0; i < GRID_SIZE; i++){
            for (int j = 0; j < GRID_SIZE; j++){
                if (grid[i][j] == 'S'){
                   return false;
                }
            }
        }
        return true;
    }

    /**
      Validates if the user input is in the correct format (e.g., A5).

      @param input The input string to validate.
      @return true if the input is in the correct format, false otherwise.
     */
    static boolean isValidInput(String input) {
        if (input.length() != 2){
            return false;
        }
        char col = input.charAt(0);
        char row = input.charAt(1);
        if (Character.toUpperCase(col) < 'A' || Character.toUpperCase(col) > 'J'){
            return false;
        }
        if (row < '0' || row > '9'){
            return false;
        }
        return true;
    }

    /**
      Prints the current state of the player's tracking grid.
      This method displays the grid, showing hits, misses, and untried locations.

      @param grid The tracking grid to print.
     */
    static void printGrid(char[][] grid) {
        System.out.print("    ");
        for (char i = 'A'; i <= 'J'; i++){
            System.out.print(" " + i + " ");
        }
        System.out.println();
        for (int i = 0; i < 10; i++){
            System.out.print(" " + i + "  ");
            for (int j = 0; j < 10; j++) {
                System.out.print(" " + grid[i][j] + " " );
            }
            System.out.println();
        }
    }
}
