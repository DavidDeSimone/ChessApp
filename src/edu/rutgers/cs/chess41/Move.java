/**
 * @author Stephen Chung
 */

package edu.rutgers.cs.chess41;

import java.io.Serializable;

public class Move implements Serializable {

	ChessType type;
	int orow, ocol, nrow, ncol;
	
	/**
	 * Construct a Move object that specifies start and end positions
	 * for one piece.
	 * 
	 * @param orow original row
	 * @param ocol original column
	 * @param nrow new row
	 * @param ncol new column
	 */
	public Move (int orow, int ocol, int nrow, int ncol) {
		this.orow = orow;
		this.ocol = ocol;
		this.nrow = nrow;
		this.ncol = ncol;
		this.type = ChessType.QUEEN;
	}
	
	/**
	 * Construct a Move object that specifies start and end positions.
	 * Designates the type of promotion to perform in the event that this
	 * move refers to a pawn reaching the final rank.
	 * 
	 * @param orow original row
	 * @param ocol original column
	 * @param nrow new row
	 * @param ncol new column
	 * @param type the type to promote to
	 */
	public Move (int orow, int ocol, int nrow, int ncol, ChessType type) {
		this(orow, ocol, nrow, ncol);
		this.type = type;
	}

	@Override
	public String toString() {
		return colToChar(ocol) + orow + " " + colToChar(ncol) + nrow;
	}
	
	@Override
	public boolean equals(Object o) {
		if (o == null || !(o instanceof Move)) {
			return false;
		}
		Move m = (Move) o;
		return orow == m.orow && ocol == m.ocol && nrow == m.nrow && ncol == m.ncol;
	}
	
	private String colToChar(int col) {
		return Character.toString((char)('a' - 1 + col));
	}
	
}
