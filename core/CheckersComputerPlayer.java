/**
 * Class containing a computer player for a Checkers game
 * Author: Jared Rosenthal
 * Version: Checkers 1.2
 */
package core;

public class CheckersComputerPlayer {

    /**
     * Checks if any moves are available and makes first available move
     * @param board 2D checkers game board
     * @return a String containing the move to be made on the game board
     */
    public static String check_moves(char[][] board) throws Exception {
            String move = "N/A";
            for (int i = 6; i >= 0; i--) {
                for (int j = 8; j >= 1; j--) {
                    if (board[i][j] == 'o') {
                        //In right most column only check left moves
                        if (j == 8) {
                            if (leftMoveCheck(board, i, j)) {
                                move = makeLeftMove(board, i, j);
                            }
                        }
                        //In left most column only check right moves
                        else if (j == 1) {
                            if (rightMoveCheck(board, i, j)) {
                                move = makeRightMove(board, i, j);
                            }
                        }
                        //Is jump available
                        else if (jumpCheck(board, i, j) != -1) {
                            //Left jump available
                            if (jumpCheck(board, i, j) == 0) {
                                move = makeLeftMove(board, i, j);
                            }
                            //Right jump available (no left jump)
                            else {
                                move = makeRightMove(board, i, j);
                            }
                        } else {
                            //Is a left move available
                            if (leftMoveCheck(board, i, j)) {
                                move = makeLeftMove(board, i, j);
                                //is a right move available
                            } else if (rightMoveCheck(board, i, j)) {
                                move = makeRightMove(board, i, j);
                            }
                        }
                    }
                    else if(i == 0 && j == 1){
                        throw new Exception("No moves found but game has not ended.");
                    }
                }
            }
            return move;

    }

    /**
     * Checks if a left move is available
     * @param board 2D checkers game board
     * @param row row number of piece to check
     * @param col column number of piece to check
     * @return true if a left move is available false if not
     */

    public static boolean leftMoveCheck(char[][] board, int row, int col) {
        if (board[row - 1][col - 1] == '_' || (board[row - 1][col - 1] == 'x' && board[row - 2][col - 2] == '_')) {
            return true;
        }
        return false;
    }

    /**
     * Checks if a right move is available
     * @param board 2D checkers game board
     * @param row row number of piece to check
     * @param col column number of piece to check
     * @return true if a right move is available false if not
     */
    public static boolean rightMoveCheck(char[][] board, int row, int col ) {
        if (board[row - 1][col + 1] == '_'|| (board[row - 1][col + 1] == 'x' && board[row - 2][col + 2] == '_')) {
            return true;
        }
        return false;
    }

    /**
     * Checks if a jump move is available
     * @param board 2D checkers game board
     * @param row row number of piece to check
     * @param col column number of piece to check
     * @return 0 if jump to be made left, 1 is jump to be made right
     */
    public static int jumpCheck(char[][] board, int row, int col) {
        //only left jump possible
        if(col == 7){
            if(board[row - 1][col - 1] == 'x' && board[row - 2][col - 2] == '_'){
                return 0;
            }
        }
        //only right jump possible
        else if(col == 2){
            if(board[row - 1][col + 1] == 'x' && board[row - 2][col + 2] == '_'){
                return 1;
            }
        }
        //Jump left available
        else if (board[row - 1][col - 1] == 'x' && board[row - 2][col - 2] == '_') {
            return 0;
        }
        //Jump right available
        else if (board[row - 1][col + 1] == 'x' && board[row - 2][col + 2] == '_') {
            return 1;
        }
        //Jump not available
        return -1;
    }


    /**
     * Creates a string to indicate a left move
     * @param board 2D checkers game board
     * @param row row number of piece to check
     * @param col column number of piece to check
     * @return String in correct format for moving piece
     */
    public static String makeLeftMove(char[][] board, int row, int col) {
        String move;
        char[] mv = {board[row][0], board[0][col], '-', board[row - 1][0], board[0][col - 1]};

        move = new String(mv);
        return move;
    }

    /**
     * Creates a string to indicate a right move
     * @param board 2D checkers game board
     * @param row row number of piece to check
     * @param col column number of piece to check
     * @return String in correct format for moving piece
     */
    public static String makeRightMove(char[][] board, int row, int col) {
        String move;
        char[] mv = {board[row][0], board[0][col], '-', board[row - 1][0], board[0][col + 1]};

        move = new String(mv);
        return move;
    }
}
