/**
 * @author Stephen Chung
 */

package edu.rutgers.cs.chess41;

import java.io.Serializable;
import static edu.rutgers.cs.chess41.ChessBoard.MIN_COORD;
import static edu.rutgers.cs.chess41.ChessBoard.MAX_COORD;

class Piece implements Serializable {

	private static final long serialVersionUID = 1L;
	private final boolean white;
	
    private ChessType type, prevType;
    private int row, col, prevRow, prevCol;
    private boolean moved, capt, prevMoved, prevCapt;
    private boolean canRevert;
    
    /**
     * Construct a new chess piece.
     * 
     * @param type the type of this chess piece e.g. Pawn
     * @param white true if the piece is white, false if it is black
     * @param row the starting row (rank) of this piece
     * @param col the starting column (file) of this piece
     * @throws IllegalArgumentException if row or column out
     * 			of range
     */
    Piece(ChessType type, boolean white, int row, int col) {
        if (row < MIN_COORD || row > MAX_COORD) {
            throw new IllegalArgumentException("Row (rank) out of range: " + row);
        }
        if (col < MIN_COORD || col > MAX_COORD) {
        	throw new IllegalArgumentException("Column (file) out of range: " + col);
        }
        this.white = white;
        this.type = prevType = type;
        this.row = prevRow = row;
        this.col = prevCol = col;
        moved = prevMoved = false;
        capt = prevCapt = false;
        canRevert = false;
    }
    
    @Override
    public boolean equals(Object o) {
    	if (o == null || !(o instanceof Piece)) {
    		return false;
    	}
    	Piece p = (Piece) o;
    	return type == p.type && row == p.row && col == p.col;
    }
    
    @Override
    public String toString() {
    	String color, name;
    	if (white) {
    		color = "w";
    	} else {
    		color = "b";
    	}
    	switch(type) {
    	case PAWN:
    		name = "p";
    		break;
    	case ROOK:
    		name = "R";
    		break;
    	case KNIGHT:
    		name = "N";
    		break;
    	case BISHOP:
    		name = "B";
    		break;
    	case QUEEN:
    		name = "Q";
    		break;
    	case KING:
    		name = "K";
    		break;
    	default:
    		assert false: "This piece is of an unknown type";
    		name ="?";
    		break;
    	}
    	return color + name;
    }
    
    private void updatePrev() {
    	prevType = type;
    	prevRow = row;
    	prevCol = col;
    	prevMoved = moved;
    	prevCapt = capt;
    	canRevert = true;
    }
    
    boolean revert() {
    	if (canRevert) {
    		type = prevType;
    		row = prevRow;
    		col = prevCol;
    		moved = prevMoved;
    		capt = prevCapt;
    		canRevert = false;
    		return true;
    	} else {
    		assert false: "Only one revert at a time possible";
    		return false;
    	}
    }
    
    /**
     * Update this piece's internal record of its own position. Does
     * not update a ChessBoard's record of this piece's position. Sets
     * the moved indicator to true.
     * 
     * @param newRow the new row (rank)
     * @param newCol the new column (file)
     */
    void setPos(int newRow, int newCol) {
    	assert !capt;
    	assert ChessBoard.inRange(newRow, newCol);
    	updatePrev();
    	row = newRow;
    	col = newCol;
    	moved = true;
    }
    
	void setPos(int newRow, int newCol, ChessType newType) {
		assert !capt;
		assert ChessBoard.inRange(newRow, newCol);
		updatePrev();
		row = newRow;
		col = newCol;
		moved = true;
		type = newType;
	}
    
    /**
     * Returns the row position of this piece.
     * 
     * @return current row number
     */
    int getRow() {
    	return row;
    }
    
    /**
     * Returns the column position of this piece.
     * 
     * @return current column number
     */
    int getCol() {
    	return col;
    }
    
    /**
     * Set this piece's status to captured. A piece that has been
     * captured is no longer in play.
     */
    void setCaptured() {
        assert !capt;
        updatePrev();
        capt = true;
    }
    
    /**
     * Returns true iff this piece has been captured.
     * 
     * @return true if this piece has been captured, otherwise false
     */
    boolean isCaptured() {
    	return capt;
    }
    
    /**
     * Returns true iff this piece has been moved.
     * 
     * @return true if this piece has been moved, otherwise false
     */
	boolean hasMoved() {
		return moved;
	}
	
	/**
	 * Returns the type of this piece (e.g. pawn, rook, king).
	 * 
	 * @return this piece's type
	 */
	ChessType getType() {
		return type;
	}
	
	/**
	 * Promote a pawn that has reached the last row (rank) to a
	 * different type.
	 * 
	 * @param t the type to promote to
	 */
    void promote(ChessType t) {
    	assert type == ChessType.PAWN;
    	assert (white && row == 8) || (!white && row == 1);
    	updatePrev();
    	type = t;
    }
    
    /**
     * Returns this piece's color (white or black).
     * 
     * @return true if white, false if black
     */
    boolean isWhite() {
    	return white;
    }

}