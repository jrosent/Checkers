/**
 * Class containing the UI for a Checkers game
 * Author: Jared Rosenthal
 * Version: Checkers 1.2
 */

package ui;
import java.util.*;
import core.CheckersLogic;
import core.CheckersLogic.Player;
import core.CheckersComputerPlayer;
import javafx.application.Application;

import java.io.*;

public class CheckersTextConsole{

    public static char gameType = 'x';

    public static void main(String[] args) throws Exception {
        //Create Scanner
        Scanner in = new Scanner(System.in);

        char game_layout = 't';
        System.out.println("Enter 't' for text console or 'g' for graphical user" +
                "interface.");
        game_layout = in.next().charAt(0);

        if(game_layout == 'g'){
            Application.launch(CheckersGUI.class);

        }
        else {

            //Create and print empty board
            char[][] new_board = CheckersLogic.create_board();
            CheckersLogic.print_board(new_board);


            //Create Player objects
            int moves = 0;
            int comp_moves = 0;
            Player[] players = new Player[2];
            Player playerx = new Player("Player X", 'x');
            Player playery = new Player("Player O", 'o');
            players[0] = playerx;
            players[1] = playery;


            //Prompt to begin game
            while(gameType != 'c' && gameType != 'p') {
                gameType = in.next().charAt(0);
                System.out.println("Begin Game. ");
                if (gameType == 'c') {
                    playery.user = gameType;
                } else if (gameType == 'p') {
                    playery.user = gameType;
                }
                else{
                    System.out.println("Enter a valid option.");
                }
            }

            while (CheckersLogic.game_over(new_board, players) == false) {
                CheckersLogic.print_board(new_board);
                //Prompt for player move
                System.out.println(players[moves % 2].name + " - your turn.\n");
                System.out.println("Choose a cell position of piece to be moved and the new position. e.g., 3a-4b.");
                //Read in player move
                String move;
                if (players[moves % 2].user == 'c') {
                    move = CheckersComputerPlayer.check_moves(new_board);
                    comp_moves++;
                    if (comp_moves > moves) {
                        throw new Exception("Computer attempted move invalid.");
                    }
                } else {
                    move = in.next();
                }

                //Check if player move is valid
                if (CheckersLogic.isValid(new_board, move, players[moves % 2]) == true) {
                    //Add one to total moves count and switch player turn
                    moves++;
                }
            }
            if (moves % 2 == 0) {
                System.out.println(players[1].name + " Won the Game");
            } else {
                System.out.println(players[0].name + " Won the Game");
            }
        }
    }
}


