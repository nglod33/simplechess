package model;

import static model.PieceType.*;
import static model.Team.*;

public class Board {

    //moveBoard is used to make moves, saveBoard is used to revert if there is an invalid move
    private Cell[][] saveBoard = new Cell[8][8];
    private Cell[][] moveBoard;
    //White will be true, black will be false
    private boolean turn = true;
    private boolean isOver = false;

    public Board(){
        //Creating the initial saveBoard for the board
        for(int n = 0; n < 8; n ++){
            for(int m = 0; m < 8; m ++){
                saveBoard[n][m] = new Cell(n, m, this);
            }
        }
        //populating the saveBoard
        saveBoard[0][0].setOccupant(ROOK);
        saveBoard[0][0].setTeam(B);
        saveBoard[0][1].setOccupant(KNIGHT);
        saveBoard[0][1].setTeam(B);
        saveBoard[0][2].setOccupant(BISHOP);
        saveBoard[0][2].setTeam(B);
        saveBoard[0][3].setOccupant(QUEEN);
        saveBoard[0][3].setTeam(B);
        saveBoard[0][4].setOccupant(KING);
        saveBoard[0][4].setTeam(B);
        saveBoard[0][5].setOccupant(BISHOP);
        saveBoard[0][5].setTeam(B);
        saveBoard[0][6].setOccupant(KNIGHT);
        saveBoard[0][6].setTeam(B);
        saveBoard[0][7].setOccupant(ROOK);
        saveBoard[0][7].setTeam(B);
        for(int n = 0; n < 8; n++){
            saveBoard[1][n].setOccupant(PAWN);
            saveBoard[1][n].setTeam(B);
        }
        for(int n = 0; n < 8; n++){
            saveBoard[6][n].setOccupant(PAWN);
            saveBoard[6][n].setTeam(W);
        }
        saveBoard[7][0].setOccupant(ROOK);
        saveBoard[7][0].setTeam(W);
        saveBoard[7][1].setOccupant(KNIGHT);
        saveBoard[7][1].setTeam(W);
        saveBoard[7][2].setOccupant(BISHOP);
        saveBoard[7][2].setTeam(W);
        saveBoard[7][3].setOccupant(QUEEN);
        saveBoard[7][3].setTeam(W);
        saveBoard[7][4].setOccupant(KING);
        saveBoard[7][4].setTeam(W);
        saveBoard[7][5].setOccupant(BISHOP);
        saveBoard[7][5].setTeam(W);
        saveBoard[7][6].setOccupant(KNIGHT);
        saveBoard[7][6].setTeam(W);
        saveBoard[7][7].setOccupant(ROOK);
        saveBoard[7][7].setTeam(W);

        moveBoard = saveBoard;
    }

    //Getter for the moveBoard array
    public Cell[][] getMoveBoard() {
        return moveBoard;
    }

    //Getter for the saveBoard array
    public Cell[][] getSaveBoard() {
        return saveBoard;
    }

    //Getter for an individual cell
    public Cell getMoveCell(int row, int col) {
        return moveBoard[row][col];
    }

    public boolean isTurn() {
        return turn;
    }

    public boolean isOver() {
        return isOver;
    }

    public void setTurn(boolean turn) {
        this.turn = turn;
    }

    /*
    Undoes the last move in the case of an invalid move
     */
    public void undoMove(){
        moveBoard = saveBoard;
    }

    /**
     * updates the saveBoard to the latest game version
     */
    public void confirmMove(){
        saveBoard = moveBoard;
    }

    /**
     * checks to see if the move puts the king in check, and either confirms the move and changes turns and moves on
     * or resets the board. This also takes care of promotion for pawns, which will be automatically promoted to
     * Queens for now. Returns true if the move was confirmed, and then false otherwise
     */
    public boolean confirmTurn(){
        if(turn){
            for(int x = 0; x < 8; x++){
                for(int y = 0; y < 8; y++){
                    if(moveBoard[x][y].getTeam() == B){
                        if(moveBoard[x][y].getOccupant() == PAWN){
                            if(x == 7){
                                moveBoard[x][y] = new Cell(x, y, this);
                                moveBoard[x][y].setTeam(B);
                                moveBoard[x][y].setOccupant(QUEEN);
                                moveBoard[x][y].queenTargeting();
                            }
                            else {
                                moveBoard[x][y].generaltarget();
                            }
                        }
                        moveBoard[x][y].generaltarget();
                    }
                }
            }
            for(int x = 0; x < 7; x++){
                for(int y = 0; y < 7; y++){
                    if((moveBoard[x][y].getOccupant() == KING) && (moveBoard[x][y].getTeam() == W) && moveBoard[x][y].isBlackTargeted()){
                        System.out.println("That move leaves your King in check! You must choose another");
                        this.undoMove();
                        return false;
                    }
                }
            }
        }
        else{
            for(int x = 0; x < 8; x++){
                for(int y = 0; y < 8; y++){
                    if(moveBoard[x][y].getTeam() == W){
                        if(moveBoard[x][y].getOccupant() == PAWN){
                            if(x == 0){
                                moveBoard[x][y] = new Cell(x, y, this);
                                moveBoard[x][y].setTeam(W);
                                moveBoard[x][y].setOccupant(QUEEN);
                                moveBoard[x][y].queenTargeting();
                            }
                            else {
                                moveBoard[x][y].generaltarget();
                            }
                        }
                        moveBoard[x][y].generaltarget();
                    }
                }
            }
            for(int x = 0; x < 7; x++){
                for(int y = 0; y < 7; y++){
                    if((moveBoard[x][y].getOccupant() == KING) && (moveBoard[x][y].getTeam() == W) && moveBoard[x][y].isBlackTargeted()){
                        System.out.println("That move leaves your King in check! You must choose another");
                        this.undoMove();
                        return false;
                    }
                }
            }
        }
        this.clearCell();
        this.confirmMove();
        this.setTurn(!this.turn);
        return true;
    }

    /**
     * Moves the piece from the start cell to the end cell
     */
    public void makeMove(Cell startCell, Cell endCell){
        endCell.setOccupant(startCell.getOccupant());
        endCell.setTeam(startCell.getTeam());
        startCell.setOccupant(NONE);
        startCell.setTeam(N);
    }

    // Clears every single Cell of targets
    public void clearCell(){
        for(int x = 0; x < 8; x++){
            for(int y = 0; y < 8; y++){
                moveBoard[x][y].setWhiteTargeted(false);
                moveBoard[x][y].setBlackTargeted(false);
            }
        }
    }

    public boolean validateMove(Cell startCell, Cell endCell){
        startCell.generaltarget();
        if( !this.turn && (startCell.getTeam() == B) && endCell.isBlackTargeted()){
            makeMove(startCell, endCell);
            clearCell();
            return true;
        }
        //I know this is repeating myself but it looks a bit nicer
        if( this.turn && (startCell.getTeam() == W) && endCell.isWhiteTargeted()){
            makeMove(startCell, endCell);
            clearCell();
            return true;
        }
        return false;
    }

}
