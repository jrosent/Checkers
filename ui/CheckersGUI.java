
/**
 * Class containing graphical user interface for Checkers game
 * Author: Jared Rosenthal
 * Version: Checkers 1.2
 *
 */package ui;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;

import java.util.EventObject;

/**
 * Class creates a graphical user interface for a checkers game
 */
public class CheckersGUI extends Application {

    //Declare players turn & number of pieces
    private char whoseTurn = 'x';
    private int numXPieces = 12;
    private int numOPieces = 12;

    //Have piece and move been chosen
    private boolean pieceChosen = false;
    private boolean moveChosen = false;
    private char movetype = 'z';

    //Place to save piece and move coordinates
    private Cell selectedCell;
    private Cell oldCell;

    //Index variables for jumped pieces
    private int jrow;
    private int jcol;

    //Create and initialize board of cell objects
    private Cell[][] board = new Cell[8][8];

    //Create and initialize a status label
    private Label turnlbl = new Label("Red's turn to play. R: " + numXPieces + " W: " + numOPieces);

    /**
     * Creates the vosuals for the GUI
     * @param primaryStage stage for game board and pieces to be placed
     */
    public void start(Stage primaryStage) {

        //Pane to hold game board
        GridPane pane = new GridPane(); //Create a GridPane object called pane

        //Format game board and fill cells with pieces for new game
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                //Even index -> add yellow cell
                if ((row + col) % 2 == 0) {
                    pane.add(board[row][col] = new Cell("yellow", ' '), col, row);
                }
                //Odd index -> add green cell
                else {
                    if (row == 0 || row == 1 || row == 2) {
                        pane.add(board[row][col] = new Cell("green", 'o'), col, row);
                    } else if (row == 5 || row == 6 || row == 7) {
                        pane.add(board[row][col] = new Cell("green", 'x'), col, row);
                    } else {
                        pane.add(board[row][col] = new Cell("green", ' '), col, row);

                    }
                }
                //Set row and column index in each cell
                board[row][col].row = row;
                board[row][col].col = col;
            }
        }

        //Add border pane to the primary stage
        BorderPane borderPane = new BorderPane(); //create new border pane
        borderPane.setCenter(pane); //add pane to center of border Pane
        borderPane.setBottom(turnlbl);
        turnlbl.setFont(new Font("Arial", 16));

        //Create scene
        Scene scene = new Scene(borderPane, 450, 450);
        primaryStage.setScene(scene); // Place the scene in the stage

        primaryStage.show(); //Display the stage
    }

    /**
     * Class for creating cells on game board and handling moves
     */
    public class Cell extends Pane {
        //Piece used for the board
        private char piece = ' ';
        //Variables used to index cell in board array
        private int row;
        private int col;

        /**
         * Primary Constructor for Cells
         * @param color color for the background of the cell
         * @param piece piece type in the cell
         */
        public Cell(String color, char piece) {

            if (color.equals("yellow")) {
                setStyle("-fx-background-color: yellow; -fx-border-color: black");
            } else {
                setStyle("-fx-background-color: green; -fx-border-color: black");
            }

            this.setPrefSize(2000, 2000);
            setPiece(piece);
            setOnMouseClicked(e -> {
                selectedCell = (Cell) e.getSource();
                handleMouseClick();
            });

        }

        /**
         * Sets cell piece and creates checker to display in cell
         * @param c piece type to change to
         */
        public void setPiece(char c) {
            piece = c;

            if (piece == 'o') {
                Ellipse white_piece = new Ellipse(this.getWidth() / 2,
                        this.getHeight() / 2 - 10, this.getWidth() / 2 - 10,
                        this.getHeight() / 2 - 10);
                white_piece.centerXProperty().bind(this.widthProperty().divide(2));
                white_piece.centerYProperty().bind(this.heightProperty().divide(2));
                white_piece.radiusXProperty().bind(this.widthProperty().divide(2).subtract(10));
                white_piece.radiusYProperty().bind(this.heightProperty().divide(2).subtract(10));
                white_piece.setStroke(Color.BLACK);
                white_piece.setFill(Color.WHITE);
                getChildren().add(white_piece); // Add the ellipse to the pane

            } else if (piece == 'x') {
                Ellipse red_piece = new Ellipse(this.getWidth() / 2,
                        this.getHeight() / 2 - 10, this.getWidth() / 2 - 10,
                        this.getHeight() / 2 - 10);
                red_piece.centerXProperty().bind(this.widthProperty().divide(2));
                red_piece.centerYProperty().bind(this.heightProperty().divide(2));
                red_piece.radiusXProperty().bind(this.widthProperty().divide(2).subtract(10));
                red_piece.radiusYProperty().bind(this.heightProperty().divide(2).subtract(10));
                red_piece.setStroke(Color.BLACK);
                red_piece.setFill(Color.RED);
                getChildren().add(red_piece); // Add the ellipse to the pane
            } else {
                getChildren().clear();
            }
        }

        /**
         * Handles any mouse clicks
         */
        private void handleMouseClick() {

            if (pieceChosen == false) {
                if (selectedCell.piece == whoseTurn) {
                    if (hasValidMove(selectedCell)) {
                        choosePiece();
                        pieceChosen = true;
                    }
                }
            } else if (moveChosen == false) {
                if (selectedCell == oldCell) {
                    if (movetype != 'j') {
                        pieceChosen = false;
                        setStyle("-fx-background-color: green; -fx-border-color: black");
                    }

                } else if (isValidMove(oldCell, selectedCell)) {
                    selectMove();
                }
            }

        }

        /**
         * Helper method for selecting a piece to move
         */
        private void choosePiece() {
            if (piece == whoseTurn) {
                setStyle("-fx-background-color: black");
                oldCell = selectedCell;
            }
        }

        /**
         * Helper method for moving piece to selected location
         */
        private void selectMove() {
            if (moveChosen == false) {
                oldCell.setPiece(' ');
                oldCell.setStyle("-fx-background-color: green; -fx-border-color: black");
                setPiece(whoseTurn);
                String turn = "Red";
                if (gameOver() == true) {
                    if(whoseTurn == 'x'){
                        turn = "Red";
                    }
                    else{
                        turn = "White";
                    }
                    turnlbl.setText(turn + " has won the game!");
                }
                else {
                    if(movetype == 'j'){
                        if(findJump(selectedCell) != selectedCell){
                            oldCell = selectedCell;
                            oldCell.setStyle("-fx-background-color: black");
                            turnlbl.setText("Select double jump " + oldCell.row + oldCell.col);
                        }
                        else{
                            movetype = 'z';
                            whoseTurn = (whoseTurn == 'x') ? 'o' : 'x';

                            if (whoseTurn == 'x') {
                                turn = "Red";
                            } else {
                                turn = "White";
                            }
                            pieceChosen = false;
                            moveChosen = false;
                            turnlbl.setText(turn + "'s turn. R: " + numXPieces + "  W: " + numOPieces);
                        }
                    }
                    else {
                        movetype = 'z';
                        whoseTurn = (whoseTurn == 'x') ? 'o' : 'x';

                        if (whoseTurn == 'x') {
                            turn = "Red";
                        } else {
                            turn = "White";
                        }
                        pieceChosen = false;
                        moveChosen = false;
                        turnlbl.setText(turn + "'s turn. R: " + numXPieces + "  W: " + numOPieces);
                    }
                }
            }

        }
    }

    /**
     * Finds a jump move from the current piece position
     * @param m Cell to look for jump moves from
     * @return cell occupied after found jump move initiated
     */
    private Cell findJump(Cell m){
        if(whoseTurn == 'x') {
            if(m.row > 1) {
                if (m.col > 5) {
                    if(isJump(m, board[m.row-2][m.col -2])){
                        return board[m.row-2][m.col -2];
                    }
                }
                else if(m.col < 2){
                    if(isJump(m,board[m.row - 2][m.col + 2])){
                        return board[m.row - 2][m.col + 2];
                    }
                }
                else if (isJump(m,board[m.row - 2][m.col + 2])){
                    return board[m.row - 2][m.col + 2];
                }
                else if(isJump(m,board[m.row - 2][m.col - 2])){
                    return board[m.row-2][m.col -2];
                }
            }
        }
        else if(whoseTurn == 'o'){
            if(m.row < 6) {
                if (m.col > 5) {
                    if(isJump(m, board[m.row+2][m.col -2])){
                        return board[m.row+2][m.col -2];
                    }
                }
                else if(m.col < 2){
                    if(isJump(m,board[m.row + 2][m.col + 2])){
                        return board[m.row + 2][m.col + 2];
                    }
                }
                else if(isJump(m,board[m.row + 2][m.col + 2])){
                    return board[m.row + 2][m.col + 2];
                }
                else if(isJump(m, board[m.row+2][m.col -2])){
                    return board[m.row+2][m.col -2];
                }
            }
        }
        return m;
    }

    /**
     * Checks if the game is over
     * @return true if game ending conditions are met
     */
    private boolean gameOver(){
        boolean value = false;

        if(numOPieces == 0 || numXPieces == 0) {
            value = true;
        }
        else if (whoseTurn == 'x'){
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    if(board[i][j].piece == 'o') {
                        if(hasValidMove(board[i][j])) {
                            return false;
                        }
                    }
                }
            }
            value = true;
        }
        else if (whoseTurn == 'o'){
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    if(board[i][j].piece == 'x') {
                        if (hasValidMove(board[i][j])) {
                            return false;
                        }
                    }
                }

            }
            value = true;
        }
        return value;
    }

    /**
     * Checks if a piece has any valid moves
     * @param s Cell to start from
     * @return true if any valid moves found
     */
    private boolean hasValidMove(Cell s){
        //check if red piece has any moves
        if(s.piece == 'x') {
            //right most column
            if (s.col == 7 && s.row > 0) {
                //single move?
                if (board[s.row - 1][s.col - 1].piece == ' ') {
                    return true;
                }
                //if not in second to last row check for jump
                else if(s.row > 1){
                    if (board[s.row - 1][s.col - 1].piece == 'o' && board[s.row - 2][s.col - 2].piece == ' ') {
                        return true;
                    }
                }
            }
            //left most column
            else if (s.col == 0 && s.row > 0) {
                //single move?
                if (board[s.row - 1][s.col + 1].piece == ' ') {
                    return true;
                }
                //if not in second to last row check for jump
                else if(s.row > 1){
                    if (board[s.row - 1][s.col + 1].piece == 'o' && board[s.row - 2][s.col + 2].piece == ' ') {
                        return true;
                    }
                }
            }
            //Anywhere else on the board
            else if(s.row > 0){
                //single move?
                if(board[s.row - 1][s.col + 1].piece == ' ' || board[s.row - 1][s.col - 1].piece == ' '){
                    return true;
                }
                else if(s.row > 1){
                    if(s.col == 6) {
                        //Jump left?
                        if (board[s.row - 1][s.col - 1].piece == 'o' && board[s.row - 2][s.col - 2].piece == ' ') {
                            return true;
                        }
                    }
                    else if(s.col ==1){
                        //Jump right?
                        if(board[s.row - 1][s.col + 1].piece == 'o' && board[s.row - 2][s.col + 2].piece == ' '){
                            return true;
                        }
                    }
                    else{
                        //Jump left?
                        if (board[s.row - 1][s.col - 1].piece == 'o' && board[s.row - 2][s.col - 2].piece == ' ') {
                            return true;
                        }
                        //Jump right?
                        if(board[s.row - 1][s.col + 1].piece == 'o' && board[s.row - 2][s.col + 2].piece == ' '){
                            return true;
                        }
                    }
                }
            }
        }

        //check if white piece has any moves
        else if(s.piece == 'o') {
            //right most column
            if (s.col == 7 && s.row < 7) {
                //single move?
                if (board[s.row + 1][s.col - 1].piece == ' ') {
                    return true;
                }
                //if not in second to last row check for jump
                else if(s.row < 6){
                    if (board[s.row + 1][s.col - 1].piece == 'x' && board[s.row + 2][s.col - 2].piece == ' ') {
                        return true;
                    }
                }
            }
            //left most column
            else if (s.col == 0 && s.row < 7) {
                //single move?
                if (board[s.row + 1][s.col + 1].piece == ' ') {
                    return true;
                }
                //if not in second to last row check for jump
                else if(s.row < 6){
                    if (board[s.row + 1][s.col + 1].piece == 'x' && board[s.row + 2][s.col + 2].piece == ' ') {
                        return true;
                    }
                }
            }
            //Anywhere else on the board
            else if(s.row < 7){
                //single move?
                if(board[s.row + 1][s.col + 1].piece == ' ' || board[s.row + 1][s.col - 1].piece == ' '){
                    return true;
                }
                //if not in second to last row
                else if (s.row < 6){
                    if(s.col == 6){
                        //Jump left?
                        if(board[s.row + 1][s.col - 1].piece == 'x' && board[s.row + 2][s.col - 2].piece == ' ') {
                            return true;
                        }
                    }
                    else if(s.col == 1){
                        //Jump right?
                        if(board[s.row + 1][s.col + 1].piece == 'x' && board[s.row + 2][s.col + 2].piece == ' ') {
                            return true;
                        }
                    }
                    else{
                        //Jump left?
                        if(board[s.row + 1][s.col - 1].piece == 'x' && board[s.row + 2][s.col - 2].piece == ' ') {
                            return true;
                        }
                        //Jump right?
                        else if(board[s.row + 1][s.col + 1].piece == 'x' && board[s.row + 2][s.col + 2].piece == ' ') {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    /**
     * Checks if selected move is valid
     * @param s starting cell
     * @param m selected cell
     * @return true if move is valid
     */
    public boolean isValidMove(Cell s, Cell m){
        if(isMove(s,m)){
            movetype = 's';
            return true;
        }
        else if(isJump(s,m)){
            if(whoseTurn == 'x'){
                numOPieces--;
            }
            else{
                numXPieces--;
            }
            board[jrow][jcol].setPiece(' ');
            movetype = 'j';
            return true;
        }
        else {
            return false;
        }
    }

    /**
     * Checks if single move is valid
     * @param s starting cell
     * @param m selected cell
     * @return true if move is valid
     */
    private boolean isMove(Cell s, Cell m){
        //is move cell empty
        if(m.piece == ' ') {
            //Check if red piece
            if(s.piece == 'x' && s.row > 0){
                //move is one row less
                if(s.row - 1 == m.row){
                    return moveCheck(s,m);
                }
            }
            //check if white piece
            else if(s.piece == 'o' && s.row < 7){
                //move is one row more
                if(s.row + 1 == m.row){
                    return moveCheck(s,m);
                }
            }
        }
        return false;
    }

    /**
     * Helper method to shorten isMove
     * @param s starting cell
     * @param m selected cell
     * @return true if move is valid
     */
    private boolean moveCheck(Cell s, Cell m){
        //right most column move is one column left
        if(s.col == 7 && s.col - 1 == m.col){
            return true;
        }
        //left most column move is one column right
        else if(s.col == 0 && s.col + 1 == m.col){
            return true;
        }
        //move somewhere else on board
        else if(s.col - 1 == m.col || s.col + 1 == m.col){
            return true;
        }
        return false;
    }

    /**
     * Checks if jump move is valid
     * @param s starting cell
     * @param m selected cell
     * @return true if move is valid
     */
    private boolean isJump(Cell s, Cell m){
        //Is move to space with opposite piece
        if(s.piece == 'x' && m.piece == ' '){
            if(m.row + 2 == s.row){
                if(m.col == 0 || s.col == 7){
                    if(board[s.row - 1][s.col - 1].piece == 'o'){
                        jrow = s.row - 1;
                        return jumpCheck(s, m);
                    }
                }else if(m.col == 7 || s.col == 0){
                    if(board[s.row - 1][s.col + 1].piece == 'o') {
                        jrow = s.row - 1;
                        return jumpCheck(s, m);
                    }
                }
                else{
                    if(board[s.row - 1][s.col - 1].piece == 'o' || board[s.row - 1][s.col + 1].piece == 'o'){
                        jrow = s.row - 1;
                        return jumpCheck(s, m);
                    }
                }
            }
        }
        //Is move to space with opposite piece
        if(s.piece == 'o' && m.piece == ' '){
            if(m.row - 2 == s.row){
                if(m.col == 0 || s.col == 7){
                    if(board[s.row + 1][s.col - 1].piece == 'x'){
                        jrow = s.row + 1;
                        return jumpCheck(s, m);
                    }
                }else if(m.col == 7 || s.col == 0){
                    if(board[s.row + 1][s.col + 1].piece == 'x') {
                        jrow = s.row + 1;
                        return jumpCheck(s, m);
                    }
                }
                else{
                    if(board[s.row + 1][s.col -1].piece == 'x' || board[s.row + 1][s.col + 1].piece == 'x'){
                        jrow = s.row + 1;
                        return jumpCheck(s, m);
                    }
                }
            }
        }
        return false;
    }

    /**
     * Helper method to shorten isJump
     * @param s starting cell
     * @param m selected cell
     * @return true if move is valid
     */
    private boolean jumpCheck(Cell s,Cell m){
        if(s.col > 5){
            if(s.col - 2 == m.col ){
                jcol = s.col - 1;
                return true;
            }
        }
        else if(s.col < 2){
            if(s.col + 2 == m.col){
                jcol = s.col + 1;
                return true;
            }
        }
        else if(s.col + 2 == m.col){
            jcol = s.col + 1;
            return true;
        }
        else if(s.col - 2 == m.col){
            jcol = s.col - 1;
            return true;
        }
        return false;
    }
}




