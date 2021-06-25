/**
 * Class containing the logic for a checkers game
 * Author: Jared Rosenthal
 * Version: 1.1
 */

package core;
import java.util.*;


public class CheckersLogic {

    /**
     * class to create player objects
     */
    public static class Player {
        public int num_pieces;
        public String name;
        public char piece;
        public char user = 'p';

        /**
         * Constructor to create player objects
         * @param player name of the player being created
         * @param type 'x' or 'o' for player pieces
         */
        public Player(String player, char type) {
            name = player;
            piece = type;
            num_pieces = 12;
        }
    }

    /**
     * Creates a new board for a checkers game
     *
     * @return a 2D array filled with 'x' and 'o' pieces for a new checkers game
     */
    public static char[][] create_board() {

        char board[][] = new char[9][9];

        char c = 'a';
        for (int i = 8; i >= 0; i--) {
            for (int j = 0; j < 9; j++) {
                if (i == 0 && j >= 0) {
                    if (j != 0) {
                        board[i][j] = c++;
                    } else {
                        board[i][j] = ' ';
                    }
                } else if (j == 0 && i > 0) {
                    board[i][j] = (char) (i + '0');
                } else {
                    board[i][j] = '_';
                }
            }
        }
        fill_board(board);
        return board;
    }

    /**
     * Fills board with pieces for new game of checkers
     *
     * @param board empty board to fill with pieces
     */
    public static void fill_board(char[][] board) {
        for (int i = 8; i >= 0; i--) {
            for (int j = 1; j < 9; j++) {
                if ((i == 8 || i == 6) && j % 2 == 0) {
                    board[i][j] = 'o';
                }
                if (i == 7 && j % 2 == 1) {
                    board[i][j] = 'o';
                }
                if ((i == 3 || i == 1) && j % 2 == 1) {
                    board[i][j] = 'x';
                }
                if (i == 2 && j % 2 == 0) {
                    board[i][j] = 'x';
                }
            }
        }
    }

    /**
     * Prints the checkers board for players to see
     *
     * @param board 2D array of chars holding information about checkers game
     */
    public static void print_board(char[][] board) {


        for (int i = 8; i >= 0; i--) {
            for (int j = 0; j < 9; j++) {
                if (i > 0) {
                    System.out.print(board[i][j] + "|");
                } else {
                    System.out.print(board[i][j] + " ");
                }
            }
            System.out.println("");
        }
        System.out.println("");
    }

    /**
     * Checks if move is valid and updates the game board
     * @param board game board being played
     * @param move string containing formatted player piece and move (piece_position-move_position)
     * @param player player whose turn it is to make move
     * @return true or false based on validity of move
     */
    public static boolean isValid(char[][] board, String move, Player player){
        //translate string into usable move specifications
        String[] positions = move.split("-");
        String current = positions[0];
        String move_to = positions[1];
        int c1 = current.charAt(0) - '0', c2 = (current.charAt(1) - 96);
        int m1 = move_to.charAt(0) - '0', m2 = move_to.charAt(1) - 96;
        char[][] temp_board = board;
        //System.out.println(c1 + c2 + " to " + m1 + m2);

        //check if piece to be moved is inside game board
        if(m1<0 || m1 >= 9 || m2 < 0 || m2 >= 9){
            System.out.println("Invalid move. Select a spot on the game board.");
            return false;
        }
        //Check if player has selected a valid piece
        else if(board[c1][c2] != player.piece) {
            System.out.println("You have not selected a valid piece.");
            return false;
        }
        //check if move is valid single move and update game board
        else if(single_move(board,c1,c2,m1,m2, player)) {
            board[c1][c2] = '_';
            board[m1][m2] = player.piece;
            return true;
        }
        //check if move is a jump
        else if(isJump(temp_board,c1,c2,m1,m2, player)) {
            board = temp_board;
            System.out.println("Great Move!");
            return true;
        }

        System.out.println("You have not entered a valid move.");
        return false;
    }

    /**
     * checks if move is valid single move and move piece if true
     * @param board current game board
     * @param c1 current piece row
     * @param c2 current piece column
     * @param m1 row to move piece to
     * @param m2 column to move piece to
     * @param player player making move
     * @return true if move is a valid single move false if not
     */
    public static boolean single_move(char[][] board, int c1, int c2, int m1, int m2, Player player) {
        //Check if piece is moving to empty space
        if(board[m1][m2] == '_') {
            //Condition for player x single move
            if (player.piece == 'x') {
                if (m1 == (c1 + 1) && ((m2 == c2 + 1) || m2 == c2 - 1)) {
                    return true;
                }
            }
            //Condition for player o single move
            else if (player.piece == 'o') {
                if (m1 == (c1 - 1) && ((m2 == c2 + 1) || m2 == c2 - 1)) {
                    return true;
                }
            }
        }

        return false;

    }

    /**
     * Checks is move is a jump and updates board appropriately
     * @param board game board
     * @param c1 current row
     * @param c2 current column
     * @param m1 row to move to
     * @param m2 column to move to
     * @param player player whose turn it is
     * @return true if move is a jump false if not
     */
    public static boolean isJump(char[][] board, int c1, int c2, int m1, int m2, Player player){
        //If player x
        if(player.piece == 'x' && board[m1][m2] == 'o'){
            //If jump is left update variables
            if(m2 == c2-1 && board[m1+1][m2-1] == '_'){
                board[c1][c2] = '_';
                board[m1][m2] = '_';
                c1 = m1 + 1;
                c2 = m2 - 1;
                player.num_pieces--;
            }
            //If jump right update variables
            else if(m2 == c2 + 1 && board[m1-1][m2+1] == '_'){
                board[c1][c2] = '_';
                board[m1][m2] = '_';
                c1 = m1 + 1;
                c2 = m2 + 1;
                player.num_pieces--;
            }
            else{
                return false;
            }
            //Look for jumps until no jumps are found
            while ((c2 < 7 && board[c1+1][c2-1] == 'o' && board[c1+2][c2-2] == '_') || ( c2 > 1 &&
                    board[c1+1][c2-1] == 'o' && board[c1+2][c2+2] == '_')) {
                player.num_pieces--;
                //If jumps are available left and right give player option
                if (board[c1+1][c2+1] == 'x' && board[c1+1][c2-1] == 'x') {
                    char dir = 'U';
                    //Prompt for direction to jump
                    while (dir == 'U') {
                        System.out.println("Enter L for left jump or R for right jump.");
                        Scanner in = new Scanner(System.in);
                        dir = in.next().charAt(0);
                        //Jump Left
                        if (dir == 'L') {
                            m1 = c1 + 1;
                            m2 = c2 - 1;
                            board[m1][m2] = '_';
                            c1 = m1 + 1;
                            c2 = m2 - 1;
                            board[c1][c2] = 'x';
                        }
                        //Jump Right
                        else if (dir == 'R') {
                            m1 = c1 + 1;
                            m2 = c2 + 1;
                            board[m1][m2] = '_';
                            c1 = m1 + 1;
                            c2 = m2 + 1;
                        }
                        //If value entered was not L or R
                        else {
                            dir = 'U';
                            System.out.println("Invalid value entered.");
                        }
                    }
                }
                //Only right jump available
                else if (board[c1 - 1][c2 + 1] == 'o') {
                    m1 = c1 + 1;
                    m2 = c2 + 1;
                    board[m1][m2] = '_';
                    c1 = m1 + 1;
                    c2 = m2 + 1;
                }
                //Only left jump available
                else {
                    m1 = c1 + 1;
                    m2 = c2 - 1;
                    board[m1][m2] = '_';
                    c1 = m1 + 1;
                    c2 = m2 - 1;

                }
            }
            board[c1][c2] = 'x';
            return true;
        }
        //If player o
        if(player.piece == 'o' && board[m1][m2] == 'x'){
            //If jump is left update variables
            if(m2 == c2-1 && board[m1-1][m2-1] == '_'){
                board[c1][c2] = '_';
                board[m1][m2] = '_';
                c1 = m1 -1;
                c2 = m2 -1;
                player.num_pieces--;
            }
            //if jump is right update variable
            else if(m2 == c2 + 1 && board[m1-1][m2+1] == '_'){
                board[c1][c2] = '_';
                board[m1][m2] = '_';
                c1 = m1 - 1;
                c2 = m2 + 1;
                player.num_pieces--;
            }
            else{
                return false;
            }
            //Look for jumps until no jumps are found
            while ((c2 > 1 && board[c1-1][c2-1] == 'x' && board[c1-2][c2-2] == '_') ||
                    ( c2 < 7 && board[c1-1][c2-1] == 'x' && board[c1-2][c2+2] == '_')) {
                player.num_pieces--;
                //If jumps are available left and right give player option
                if (board[c1-1][c2+1] == 'x' && board[c1-1][c2-1] == 'x') {
                    char dir = 'U';
                    //Prompt for direction to jump
                    while (dir == 'U') {
                        System.out.println("Enter L for left jump or R for right jump.");
                        Scanner in = new Scanner(System.in);
                        dir = in.next().charAt(0);
                        //Jump Left
                        if (dir == 'L') {
                            m1 = c1 - 1;
                            m2 = c2 - 1;
                            board[m1][m2] = '_';
                            c1 = m1 - 1;
                            c2 = m2 - 1;
                        }
                        //Jump Right
                        else if (dir == 'R') {
                            m1 = c1 - 1;
                            m2 = c2 + 1;
                            board[m1][m2] = '_';
                            c1 = m1 - 1;
                            c2 = m2 + 1;
                        }
                        //If value entered was not L or R
                        else {
                            dir = 'U';
                            System.out.println("Invalid value entered.");
                        }
                    }
                }
                //Only right jump available
                else if (board[c1 - 1][c2 + 1] == 'x') {
                    m1 = c1 - 1;
                    m2 = c2 + 1;
                    board[m1][m2] = '_';
                    c1 = m1 - 1;
                    c2 = m2 + 1;
                }
                //Only left jump available
                else {
                    m1 = c1 - 1;
                    m2 = c2 - 1;
                    board[m1][m2] = '_';
                    c1 = m1 - 1;
                    c2 = m2 - 1;

                }
            }
            board[c1][c2] = 'o';
            return true;
        }
        return false;

    }

    /**
     * Checks if one player has won the game
     * @param board 2D array game board
     * @param players array of players in the game
     * @return true if game is over
     */
    public static boolean game_over(char[][] board, Player[] players){
        if(players[0].num_pieces == 0 || players[1].num_pieces == 0){
            return true;
        }
        else{
            check_o(board, players);
            check_x(board, players);
        }
        return false;
    }

    /**
     * Checks if player o has any valid moves
     * @param board 2D array game board
     * @param players array of players in game
     * @return true if player o has no valid moves
     */
    private static boolean check_o(char[][] board, Player[] players){
        for(int i = 1; i < 9;i++){
            for(int j = 1; j < 9; j++){
                if(board[i][j] == 'o'){
                    if (j == 8) {
                        if(board[i-1][j-1] == '_'){
                            return false;
                        }
                    }
                    if(board[i-1][j-1] == '_' || board[i-1][j+1] == '_'){
                        return false;
                    }
                    else if((board[i-1][j-1] == 'x' && board[i-2][j-2] == '_')
                            || (board[i-1][j+1] == 'x' && board[i-2][j+2] == '_')){
                        return false;
                    }
                }
            }
        }
        System.out.println("Player X has Won the Game");
        return true;
    }

    /**
     * Checks if player x has any valid moves
     * @param board 2D array game board
     * @param players array of players in game
     * @return true if player x has no valid moves
     */
    private static boolean check_x(char[][] board, Player[] players){
        for(int i = 1; i < 9;i++){
            for(int j = 1; j < 9; j++){
                if(board[i][j] == 'x'){
                    if (j == 8) {
                        if(board[i+1][j-1] == '_'){
                            return false;
                        }
                    }
                    else if(board[i+1][j-1] == '_' || board[i+1][j+1] == '_'){
                        return false;
                    }
                    else if((board[i+1][j-1] == 'o' && board[i+2][j-2] == '_')
                            || (board[i+1][j+1] == 'o' && board[i+2][j+2] == '_')){
                        return false;
                    }
                }
            }
        }
        System.out.println("Player O has Won the Game");
        return true;
    }
}

