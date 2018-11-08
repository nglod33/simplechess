package model;

import static model.PieceType.*;
import static model.Team.*;

public class Cell {
    private int row;
    private int col;
    private PieceType occupant;
    private Team team;
    private boolean isWhiteTargeted = false;
    private boolean isBlackTargeted = false;
    private Board board;

    public Cell(int row, int col, Board board) {
        this.row = row;
        this.col = col;
        this.occupant = NONE;
        this.team = N;
        this.board = board;
    }

    /**
     * getter for col
     *
     * @return col
     */
    public int getCol() {
        return col;
    }

    /**
     * getter for row
     *
     * @return row
     */
    public int getRow() {
        return row;
    }

    /**
     * getter for occupant
     *
     * @return occupant
     */
    public PieceType getOccupant() {
        return occupant;
    }

    /**
     * setter for occupant
     *
     * @param occupant occupant to be set
     */
    public void setOccupant(PieceType occupant) {
        this.occupant = occupant;
    }

    public boolean isBlackTargeted() {
        return isBlackTargeted;
    }

    public boolean isWhiteTargeted() {
        return isWhiteTargeted;
    }

    public void setBlackTargeted(boolean blackTargeted) {
        isBlackTargeted = blackTargeted;
    }

    public void setWhiteTargeted(boolean whiteTargeted) {
        isWhiteTargeted = whiteTargeted;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public Team getTeam() {
        return team;
    }

    //This makes piece targeting a whole lot easier
    public boolean isBetween(int min, int max, int num) {
        return ((num > min) && (num < max));
    }

    //Piece targeting functions

    public void whitePawnTargetting() {
        if (((this.row - 1) >= 0)) {
            if (((col - 1) >= 0) && (this.board.getMoveCell(row - 1, col - 1).getTeam() != this.team)) {
                this.board.getMoveCell(this.row - 1, this.col - 1).setWhiteTargeted(true);
            }
            if (((col + 1) < 8) && (this.board.getMoveCell(row - 1, col + 1).getTeam() != this.team)) {
                this.board.getMoveCell(this.row - 1, this.col - 1).setWhiteTargeted(true);
            }
        }
    }

    public void blackPawnTargetting() {
        if (((this.row + 1) < 8)) {
            if (((col - 1) < 0) && this.board.getMoveCell(this.row + 1, this.col - 1).getTeam() != this.team) {
                this.board.getMoveCell(this.row + 1, this.col - 1).setBlackTargeted(true);
            }
            if (((col + 1) < 8) && (this.board.getMoveCell(this.row + 1, this.col + 1).getTeam() != this.team)) {
                this.board.getMoveCell(this.row + 1, this.col + 1).setBlackTargeted(true);
            }
        }
    }

    public void bishopTargeting() {
        for (int x = -1; x < 2; x += 2) {
            for (int y = -1; x < 2; x += 2) {
                for (int z = 1; isBetween(-1, 8, this.col + z * x) && isBetween(-1, 8, this.row + z * y); z++) {
                    if (this.board.getMoveCell(row + z * x, this.col + z * y).getOccupant() == NONE) {
                        if (this.team == W) {
                            this.board.getMoveCell(this.row + z * x, this.col + z * y).setWhiteTargeted(true);
                        } else {
                            this.board.getMoveCell(this.row + z * x, this.col + z * y).setBlackTargeted(true);
                        }
                    } else {
                        if (this.team != this.board.getMoveCell(this.row + z * x, this.col + z * y).getTeam()) {
                            if (this.team == W) {
                                this.board.getMoveCell(this.row + z * x, this.col + z * y).setWhiteTargeted(true);
                            } else {
                                this.board.getMoveCell(this.row + z * x, this.col + z * y).setBlackTargeted(true);
                            }
                        }
                        break;
                    }
                }
            }
        }
    }

    public void rookTargeting() {
        for (int x = -1; x < 2; x += 2) {
            for (int z = 1; isBetween(-1, 8, this.row + z * x); z++) {
                if (this.board.getMoveCell(row + z * x, this.col).getOccupant() == NONE) {
                    if (this.team == W) {
                        this.board.getMoveCell(this.row + z * x, this.col).setWhiteTargeted(true);
                    } else {
                        this.board.getMoveCell(this.row + z * x, this.col).setBlackTargeted(true);
                    }
                } else {
                    if (this.team != this.board.getMoveCell(this.row + z * x, this.col).getTeam()) {
                        if (this.team == W) {
                            this.board.getMoveCell(this.row + z * x, this.col).setWhiteTargeted(true);
                        } else {
                            this.board.getMoveCell(this.row + z * x, this.col).setBlackTargeted(true);
                        }
                    }
                    break;
                }
            }
        }
        for (int x = -1; x < 2; x += 2) {
            for (int z = 1; isBetween(-1, 8, this.col + z * x); z++) {
                if (this.board.getMoveCell(this.row, this.col + z * x).getOccupant() == NONE) {
                    if (this.team == W) {
                        this.board.getMoveCell(this.row, this.col + z * x).setWhiteTargeted(true);
                    } else {
                        this.board.getMoveCell(this.row, this.col + z * x).setBlackTargeted(true);
                    }
                } else {
                    if (this.team != this.board.getMoveCell(this.row, this.col + z * x).getTeam()) {
                        if (this.team == W) {
                            this.board.getMoveCell(this.row, this.col + z * x).setWhiteTargeted(true);
                        } else {
                            this.board.getMoveCell(this.row, this.col + z * x).setBlackTargeted(true);
                        }
                    }
                    break;
                }
            }
        }
    }

    public void queenTargeting() {
        this.bishopTargeting();
        this.rookTargeting();
    }

    public void kingTargeting() {
        for (int x = -1; x < 2; x++) {
            for (int y = -1; y < 2; y++) {
                if (isBetween(-1, 8, row + x) && isBetween(-1, 8, col + y) &&
                        (this.board.getMoveCell(row + x, col + y).getTeam() == this.team)) {
                    if (this.team == W) {
                        this.board.getMoveCell(row + x, col + y).setWhiteTargeted(true);
                    } else {
                        this.board.getMoveCell(row + x, col + y).setBlackTargeted(true);
                    }
                }
            }
        }
    }

    public void knightTargeting() {
        for (int x = -2; x < 3; x += 4) {
            for (int y = -1; y < 2; y += 2) {
                if (isBetween(-1, 8, row + x) && isBetween(-1, 8, col + y) &&
                        (this.board.getMoveCell(row + x, col + y).getTeam() != this.team)) {
                    if (this.team == W) {
                        this.board.getMoveCell(row + x, col + y).setWhiteTargeted(true);
                    } else {
                        this.board.getMoveCell(row + x, col + y).setBlackTargeted(true);
                    }
                }
            }
        }
        for (int x = -2; x < 3; x += 4) {
            for (int y = -1; y < 2; y += 2) {
                if (isBetween(-1, 8, row + y) && isBetween(-1, 8, col + x) &&
                        (this.board.getMoveCell(row + y, col + x).getTeam() != this.team)) {
                    if (this.team == W) {
                        this.board.getMoveCell(row + y, col + x).setWhiteTargeted(true);
                    } else {
                        this.board.getMoveCell(row + y, col + x).setBlackTargeted(true);
                    }
                }
            }
        }
    }

    //Because the pawn just moving and capturing is different, I need separate commands for them
    //so I can move or capture
    public void whitePawnMoveForward() {
        this.whitePawnTargetting();
        if(this.row == 6){
            if(this.board.getMoveCell(this.row - 2, this.col).getTeam() != this.team){
                this.board.getMoveCell(this.row - 2, this.col).setWhiteTargeted(true);
            }
        }
        if(this.board.getMoveCell(this.row - 1, this.col).getTeam() != this.team){
            this.board.getMoveCell(this.row - 1, this.col).setWhiteTargeted(true);
        }

    }

    public void blackPawnMoveForward(){
        this.blackPawnTargetting();
        if(this.row == 6){
            if(this.board.getMoveCell(this.row + 2, this.col).getTeam() != this.team){
                this.board.getMoveCell(this.row + 2, this.col).setBlackTargeted(true);
            }
        }
        if(this.board.getMoveCell(this.row + 1, this.col).getTeam() != this.team){
            this.board.getMoveCell(this.row + 1, this.col).setBlackTargeted(true);
        }
    }

    public void generaltarget(){
        if(this.occupant == PAWN){
            if(this.team == W) {
                this.whitePawnTargetting();
                this.whitePawnMoveForward();
            }
            else{
                this.blackPawnTargetting();
                this.blackPawnMoveForward();
            }
        }
        if(this.occupant == KING){
            this.kingTargeting();
        }
        if(this.occupant == QUEEN){
            this.queenTargeting();
        }
        if(this.occupant == ROOK){
            this.rookTargeting();
        }
        if(this.occupant == BISHOP){
            this.bishopTargeting();
        }
        if(this.occupant == KNIGHT){
            this.knightTargeting();
        }
    }

}
