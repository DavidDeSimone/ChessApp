/**
 * @author Stephen Chung
 */

package edu.rutgers.cs.chess41;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import edu.rutgers.cs.chess41.ChessType;

public class ChessBoard implements Serializable {

	private static final long serialVersionUID = 1L;
	private static final String datadir = "../data/";

	/** the minimum row or column value */
	public static final int MIN_COORD = 1;
	/** the maximum row or column value */
	public static final int MAX_COORD = 8;
	
	private boolean drawOffered;
    private Piece[][] grid;
    private int turnsTaken;
    private Move[] enpassant;
    private ArrayList<Piece> wlist, blist;
    /* INVARIANT: first element of each list is that color's King. */
    
    /**
     * This enum represents the different (mutually exclusive) states
     * that a chess game can be in. Either one of the players is in
     * check, one of them has been checkmated, or none of the above.
     */
    public enum Condition {
    	NONE, W_IN_CHECK, W_MATED, B_IN_CHECK, B_MATED, STALEMATE;
    }
	
    /**
	 * This enum represents the different categories of moves that can be
	 * seen in the course of a chess game. The names are mostly self-
	 * explanatory. PAWN_DOUBLE refers to when a pawn moves forward two
	 * spaces at once.
	 * 
	 * Any move that is neither ILLEGAL, ENPASSANT, CASTLING, or
	 * PAWN_DOUBLE is NORMAL.
	 */
	public enum MoveType {
		ILLEGAL, NORMAL, ENPASSANT, CASTLING, PAWN_DOUBLE;
		// PAWN_DOUBLE occurs when a pawn moves forward two spaces
	}
	
    /**
     * This constructor is private. Users should rely upon the public static
     * methods getEmpty() and getNewGame() to obtain an instance of ChessBoard.
     */
    private ChessBoard() {
        grid = new Piece[9][9];
        turnsTaken = 0;
        enpassant = new Move[2];
        wlist = new ArrayList<Piece>();
        blist = new ArrayList<Piece>();
    }
    
    @Override
    public String toString() {
    	Piece p;
    	StringBuilder sb = new StringBuilder(136);
    	for (int i = 8; i > 0; i--) {
    		for (int j = 1; j < 9; j++) {
    			if ((p = get(i, j)) == null) {
    				if ((i + j) % 2 == 0) {
    					sb.append("##");
    				} else {
    					sb.append("__");
    				}
    			} else {
    				sb.append(p.toString());
    			}
    		}
    		sb.append('\n');
    	}
    	return sb.toString();
    }
    
    /**
     * Constructs an empty chess board.
     * 
     * @return an empty chess board
     */
    public static ChessBoard getEmpty() {
    	return new ChessBoard();
    }
    
    /**
     * Add a piece to the chess board, without doing any error-checking.
     * 
     * @param type the type of the piece
     * @param white true if the piece is white, otherwise false
     * @param row the row the piece should be placed on
     * @param col the colum the piece should be placed on
     */
    public void addPiece(ChessType type, boolean white, int row, int col) {
    	Piece p = new Piece(type, white, row, col);
    	grid[row][col] = p;
    	if (white) {
    		wlist.add(p);
    	} else {
    		blist.add(p);
    	}
    }
    
    /**
     * Constructs a chess board that represents a new game of chess.
     * 
     * @return a chess board for a new game
     */
    public static ChessBoard getNewGame() {
    	
    	ChessBoard cb = new ChessBoard();
    	
    	// add all black and white pawns
    	for (int c = 1; c <= 8; c++) {
    		cb.grid[2][c] = new Piece(ChessType.PAWN, true, 2, c);
    		cb.grid[7][c] = new Piece(ChessType.PAWN, false, 7, c);
    	}
    	
    	// add all non-pawn white pieces
    	cb.grid[1][1] = new Piece(ChessType.ROOK, true, 1, 1);
    	cb.grid[1][2] = new Piece(ChessType.KNIGHT, true, 1, 2);
    	cb.grid[1][3] = new Piece(ChessType.BISHOP, true, 1, 3);
    	cb.grid[1][4] = new Piece(ChessType.QUEEN, true, 1, 4);
    	cb.grid[1][5] = new Piece(ChessType.KING, true, 1, 5);
    	cb.grid[1][6] = new Piece(ChessType.BISHOP, true, 1, 6);
    	cb.grid[1][7] = new Piece(ChessType.KNIGHT, true, 1, 7);
    	cb.grid[1][8] = new Piece(ChessType.ROOK, true, 1, 8);
    
    	// add all non-pawn black pieces
    	cb.grid[8][1] = new Piece(ChessType.ROOK, false, 8, 1);
    	cb.grid[8][2] = new Piece(ChessType.KNIGHT, false, 8, 2);
    	cb.grid[8][3] = new Piece(ChessType.BISHOP, false, 8, 3);
    	cb.grid[8][4] = new Piece(ChessType.QUEEN, false, 8, 4);
    	cb.grid[8][5] = new Piece(ChessType.KING, false, 8, 5);
    	cb.grid[8][6] = new Piece(ChessType.BISHOP, false, 8, 6);
    	cb.grid[8][7] = new Piece(ChessType.KNIGHT, false, 8, 7);
    	cb.grid[8][8] = new Piece(ChessType.ROOK, false, 8, 8);
    	
    	for(int i = 1; i < 9; i++) {
    		for(int j = 1; j < 9; j++) {
    			Piece p = cb.grid[i][j];
    			if (p == null) {
    				continue;
    			} else if (p.isWhite()) {
    				if (p.getType() == ChessType.KING) {
    					cb.wlist.add(0, p);
    				} else {
    					cb.wlist.add(p);
    				}
    			} else {
    				if (p.getType() == ChessType.KING) {
    					cb.blist.add(0, p);
    				} else {
    					cb.blist.add(p);
    				}
    			}
    		}
    	}
    	
    	return cb;
    }
    
	/**
	 * Store a chess board to the data/ directory on disk.
	 * 
	 * @param cb a chessboard object that is to be stored
	 * @param name desired filename on disk
	 * @return true if the chessboard was written to disk
	 */
	public static boolean writeOut(ChessBoard cb, String name) {
		boolean state;
		try {
			File f = new File(datadir);
			f.mkdir();
		} catch (Exception e) {
			return false;
		}
		ObjectOutputStream oos;
		try {
			oos = new ObjectOutputStream(new FileOutputStream(datadir + name));
			oos.writeObject(cb);
			state = true;
			oos.close();
		} catch (Exception e) {
			//e.printStackTrace();
			state = false;
		}
		return state;
	}
	
	/**
	 * Read a chessboard from the data/ directory on disk.
	 * 
	 * @param name the filename of the chessboard
	 * @return a chessboard, if it exists on disk
	 */
	public static ChessBoard readIn(String name) {
		ChessBoard cb;
		ObjectInputStream ois;
		try {
			ois = new ObjectInputStream(new FileInputStream(datadir + name));
			cb = (ChessBoard) ois.readObject();
			ois.close();
		} catch (Exception e) {
			cb = null;
		}
		return cb;
	}

	/**
	 * Returns a random legal move for the player whose turn it is.
	 * 
	 * @return a Move object representing a legal move
	 */
	public Move randomMove() {
		Move m;
		ArrayList<Piece> pieces;
		Random rand = new Random(System.currentTimeMillis());
		int shift1 = rand.nextInt(100);
		int shift2 = rand.nextInt(100);
		
		if (whiteToMove()) {
			pieces = new ArrayList<Piece>(wlist);
			//Collections.copy(pieces, wlist);
		} else {
			pieces = new ArrayList<Piece>(blist);
			//Collections.copy(pieces, blist);
		}
		Collections.shuffle(pieces);
		
		for (Piece p: pieces) {
			if (p.isCaptured()) {
				continue;
			}
    		for (int i = 1; i < 9; i++) {
    			for (int j = 1; j < 9; j++) {
    				m = new Move(p.getRow(), p.getCol(), (i + shift1) % 8 + 1, (j + shift2) % 8 + 1);
    				if (doMove(m, 0)) {
    					return m;
    				}
    			}
    		}
		}
    	
		return null;
		
	}
	
    /**
     * Detects whether stalemate condition exists.
     * 
     * @param whitesTurn true if it is white's turn to move, otherwise false
     * @return true if the game has reached stalemate
     */
    public boolean inStalemate(boolean whitesTurn) {
    	if (getCondition(true, true, true) != Condition.NONE) {
    		return false;
    	}
    	if (whitesTurn) {
    		for (Piece p: wlist) {
    			if (p.isCaptured()) {
    				continue;
    			}
	    		for (int i = 1; i < 9; i++) {
	    			for (int j = 1; j < 9; j++) {
	    				if (doMove(new Move(p.getRow(), p.getCol(), i, j), 2)) {
	    					return false;
	    				}
	    			}
	    		}
    		}
    	} else {
    		for (Piece p: blist) {
    			if (p.isCaptured()) {
    				continue;
    			}
    			for (int i = 1; i < 9; i++) {
    				for (int j = 1; j < 9; j++) {
    					if (doMove(new Move(p.getRow(), p.getCol(), i, j), 2)) {
    						return false;
    					}
    				}
    			}
    		}
    	}
    	return true;
    }
    
    /**
     * Evaluate the board for check and checkmate conditions. Will
     * not detect stalemate.
     * 
     * @param testWhite if true, test to see if white is in check
     * @param testBlack if true, test to see if black is in check
     * @param testMate if true, test to see if checkmate has been attained
     * @return the condition that exists on this chessboard. If testMate was false, then
     * 		this can only return NONE, W_IN_CHECK, or B_IN_CHECK
     */
    public Condition getCondition(boolean testWhite, boolean testBlack, boolean testMate) {
    	Move testMove = null;
    	Condition c = Condition.NONE;
    	Piece wking = wlist.get(0);
    	Piece bking = blist.get(0);
    	assert wking.getType() == ChessType.KING;
    	assert bking.getType() == ChessType.KING;
    	
    	if (testWhite) {
    		
    		// begin testWhite
	    	for(Piece p: blist) {
	    		if (p.isCaptured()) {
	    			continue;
	    		}
	    		testMove = new Move(p.getRow(), p.getCol(), wking.getRow(), wking.getCol());
	    		if (doMove(testMove, 1)) {
	    			//System.out.println("Piece " + p + "can attack king on" + wking.getRow() + wking.getCol());
	    			if (testMate) {
	    				c = Condition.W_MATED;
	    				break;
	    			} else {
	    				return Condition.W_IN_CHECK;
	    			}
	    		}
	    	}
	    	if (c == Condition.W_MATED) {
	    		wsearch:
	    		for(Piece p: wlist) {
	    			if (p.isCaptured()) {
	    				continue;
	    			}
	    			for(int i = 1; i < 9; i++) {
	    				for(int j = 1; j < 9; j++) {
	    					testMove = new Move(p.getRow(), p.getCol(), i, j);
	    					if (doMove(testMove, 2)) {
	    						return Condition.W_IN_CHECK;
	    					}
	    				}
	    			}
	    		} // end wsearch
	    	}
	    	// end testWhite
	    	
    	}
    	
    	if (testBlack) {
    		
    		//begin testBlack
	    	for(Piece p: wlist) {
	    		if (p.isCaptured()) {
    				continue;
    			}
	    		testMove = new Move(p.getRow(), p.getCol(), bking.getRow(), bking.getCol());
	    		if (doMove(testMove, 1)) {
	    			assert c != Condition.W_MATED && c!= Condition.W_IN_CHECK;
	    			if (testMate) {
		    			c = Condition.B_MATED;
		    			break;
	    			} else {
	    				return Condition.B_IN_CHECK;
	    			}
	    		}
	    	}
	    	if (c == Condition.B_MATED) {
	    		bsearch:
	    		for(Piece p: blist) {
	    			if (p.isCaptured()) {
	    				continue;
	    			}
	    			for(int i = 1; i < 9; i++) {
	    				for(int j = 1; j < 9; j++) {
	    					testMove = new Move(p.getRow(), p.getCol(), i, j);
	    					if (doMove(testMove, 2)) {
	    						return Condition.B_IN_CHECK;
	    					}
	    				}
	    			}
	    		} // end bsearch
	    	}
	    	// end testBlack
	    	
    	}
    	
    	return c;
    }
    
    /**
     * Draw the chessboard to System.out.
     */
    public void draw() {
    	Piece p;
    	for (int i = 8; i > 0; i--) {
    		for (int j = 1; j < 9; j++) {
    			if ((p = get(i, j)) == null) {
    				if ((i + j) % 2 == 0) {
    					System.out.print("##");
    				} else {
    					System.out.print("  ");
    				}
    			} else {
    				System.out.print(p);
    			}
    			System.out.print(" ");
    		}
    		System.out.println(i);
    	}
    	System.out.println(" a  b  c  d  e  f  g  h");
    }
    
    /**
     * Returns true if the coordinates exist within the chessboard.
     * 
     * @param row the desired row
     * @param col the desired column
     * @return true if the coordinates are in the board
     */
    public static boolean inRange(int row, int col)
    {
    	if (row >= MIN_COORD && row <= MAX_COORD &&
    			col >= MIN_COORD && col <= MAX_COORD) {
    		return true;
    	} else {
    		return false;
    	}
    }
    
    Piece get(int row, int col) {
        return grid[row][col];
    }
    
    boolean isEmpty(int row, int col) {
    	return grid[row][col] == null;
    }
    
    Piece clear(int row, int col) {
        Piece orig = grid[row][col];
        grid[row][col] = null;
        return orig;
    }
    
    Piece put(Piece p, int row, int col) {
        Piece orig = grid[row][col];
        grid[row][col] = p;
        return orig;
    }
    
    /**
     * Perform a move and update the turn counter, if and only
     * if the move is legal.
     * 
     * @param m a Move object specifying the start and end
     * 		coordinates of the move
     * @param simulate If 0, attempt to perform the move. If
     * 		1, simulate the move WITHOUT determining whether
     * 		it would put the mover in check. If 2, simulate the
     * 		move taking into consideration whether it would put
     * 		the mover in check.
     * @return true if the move was legal, otherwise false
     */
    public boolean doMove(Move m, int simulate) {
    	Piece start, end;
    	MoveType mt;
    	
    	if (!inRange(m.orow, m.ocol) || !inRange(m.nrow, m.ncol)) {
    		// out of range
    		return false;
    	}
    	if (m.orow == m.nrow && m.ocol == m.ncol) {
    		// a move of 0 distance
    		return false;
    	}
    	start = get(m.orow, m.ocol);
    	if (start == null || start.isCaptured()) {
    		// no piece exists at the start coordinates or piece has been captured
    		return false;
    	}
    	
    	// For real moves, ensure that the correct player is taking her turn.
    	// turnsTaken is not checked or updated during simulations.
    	if (simulate == 0) {
	    	if (start.isWhite() && turnsTaken % 2 != 0) {
	    		return false;
	    	}
	    	if (!start.isWhite() && turnsTaken % 2 != 1) {
	    		return false;
	    	}
    	}
    	
    	end = get(m.nrow, m.ncol);
    	if (end != null && end.isWhite() == start.isWhite()) {
    		// cannot displace another piece of the same color at target coordinates
    		return false;
    	}
    	
    	switch (start.getType()) {
			case BISHOP:
				mt = bishopMove(m);
				break;
			case KING:
				mt = kingMove(m);
				break;
			case KNIGHT:
				mt = knightMove(m);
				break;
			case PAWN:
				mt = pawnMove(m);
				break;
			case QUEEN:
				mt = queenMove(m);
				break;
			case ROOK:
				mt = rookMove(m);
				break;
			default:
				mt = MoveType.ILLEGAL;
				break;
    	}
    	
    	if (mt == MoveType.ILLEGAL) {
    		return false;
    	}
    	
    	if (mt == MoveType.CASTLING) {
    		// king can not "pass through check" while castling
    		if ( !doMove(new Move(m.orow, m.ocol, m.nrow, (m.ocol + m.ncol)/2), 2) ) {
    			return false;
    		}
    	}
    	
    	if (simulate == 0) {
    		return tryMove(mt, m, false);
    	} else  if (simulate == 1){
    		return true;
    	} else {
    		assert simulate == 2;
    		return tryMove(mt, m, true);
    	}
    }
    
    /**
     * Returns true if it is white's turn to move.
     * @return true if it is white's turn to move.
     */
    public boolean whiteToMove() {
    	return turnsTaken % 2 == 0;
    }
    
    /**
     * Returns the number of turns taken so far.
     * @return the number of turns taken
     */
    public int getTurnsTaken() {
		return turnsTaken;
	}

	/**
     * Helper function that should only be called by doMove. This function
     * performs a move, then determines whether the mover is now in check.
     * Should that be the case, the move will be reverted.
     * 
     * @param t the MoveType, as determined by doMove
     * @param m the Move to perform
     * @param simulate if true, revert all changes to the board
     * 		prior to returning
     * @return true if move was legal, otherwise false
     */
    private boolean tryMove(MoveType t, Move m, boolean simulate) {
    	Piece start = get(m.orow, m.ocol);
    	Piece end = get(m.nrow, m.ncol);
    	boolean result = false;
    	
    	if (t == MoveType.NORMAL || t == MoveType.PAWN_DOUBLE) {
	    	clear(m.orow, m.ocol);
	    	put(start, m.nrow, m.ncol);
	    	if ((start.getType() == ChessType.PAWN) && (m.nrow == 1 || m.nrow == 8)) {
	    		start.setPos(m.nrow, m.ncol, m.type);
	    	} else  {
	    		start.setPos(m.nrow, m.ncol);
	    	}
	    	if (end != null) {
	    		end.setCaptured();
	    	}
	    	if (inCheck(start.isWhite())) {
	    		// player put self into check; revert move
	    		put(start, m.orow, m.ocol);
	    		put(end, m.nrow, m.ncol);
	    		start.revert();
	    		if (end != null) {
	    			end.revert();
	    		}
	    		result = false;
	    	} else if (simulate) {
	    		put(start, m.orow, m.ocol);
	    		put(end, m.nrow, m.	ncol);
	    		start.revert();
	    		if (end != null) {
	    			end.revert();
	    		}
	    		result = true;
	    	} else {
	    		turnsTaken++;
	    		result = true;
	    	}
    	} else if (t == MoveType.CASTLING) {
    		assert start.getType() == ChessType.KING;
    		Piece rook = partnerRook(start.isWhite(), m.nrow, m.ncol);
    		clear(m.orow, m.ocol);
    		put(start, m.nrow, m.ncol);
			start.setPos(m.nrow, m.ncol);
    		clear(rook.getRow(), rook.getCol());
    		put(rook, m.nrow, rookCol(m.ncol));
			rook.setPos(m.nrow, rookCol(m.ncol));
    		if (inCheck(start.isWhite())) {
    			start.revert();
    			rook.revert();
    			clear(m.nrow, m.ncol);
    			clear(m.nrow, rookCol(m.ncol));
    			put(start, m.orow, m.ocol);
    			put(rook, m.orow, rook.getCol()); // reverted earlier
    			result = false;
    		} else if (simulate) {
    			start.revert();
    			rook.revert();
    			clear(m.nrow, m.ncol);
    			clear(m.nrow, rookCol(m.ncol));
    			put(start, m.orow, m.ocol);
    			put(rook, m.orow, rook.getCol()); // reverted earlier
    			result = true;
    		} else {
    			turnsTaken++;
    			result = true;
    		}
    	} else if (t == MoveType.ENPASSANT) {
    		assert end == null;
    		Piece cap = get(m.orow, m.ncol);
    		clear(m.orow, m.ocol); // capturing piece
    		clear(m.orow, m.ncol); // piece to be captured
    		put(start, m.nrow, m.ncol);
    		start.setPos(m.nrow, m.ncol);
    		cap.setCaptured();
	    	if (inCheck(start.isWhite())) {
	    		clear(m.nrow, m.ncol);
	    		put(start, m.orow, m.ocol);
	    		put(cap, m.orow, m.ncol);
	    		start.revert();
	    		cap.revert();
	    		result = false;
	    	} else if (simulate) {
	    		clear(m.nrow, m.ncol);
	    		put(start, m.orow, m.ocol);
	    		put(cap, m.orow, m.ncol);
	    		start.revert();
	    		cap.revert();
	    		result = true;
	    	} else {
	    		turnsTaken++;
	    		result = true;
	    	}
    	} else {
    		result = false;
    	}
    		
    	if (result && !simulate) {
    		// a legal move was executed
    		enpassant[0] = null;
    		enpassant[1] = null;
    		if (t == MoveType.PAWN_DOUBLE) {
    			setEnpassant(m);
    		}
    	}
    	return result;
    }
    
    /**
     * For a castling move, determine the column that the rook
     * should finish on.
     * 
     * @param kcol the King's ending column
     * @return the column that the rook should end on
     */
    private int rookCol(int kcol) {
    	if (kcol == 3) {
    		return 4;
    	} else if (kcol == 7) {
    		return 6;
    	} else {
    		assert false: "This is not a valid castling move";
    		return 0;
    	}
	}

//    private int prevRookCol(int col) {
//    	if (col == 4) {
//    		return 1;
//    	} else if (col == 6) {
//    		return 8;
//    	} else {
//    		assert false: "Invalid input";
//    		return 0;
//    	}
//    }
    
//	private boolean pawnLastRank(Piece p) {
//    	if (p.getType() != ChessType.PAWN) {
//    		return false;
//    	}
//    	if (p.isWhite()) {
//    		return p.getRow() == 8;
//    	} else {
//    		return p.getRow() == 1;
//    	}
//    }
    
	/**
	 * Returns true if specified player is in check or has been
	 * checkmated (no distinction is made between these two states).
	 * 
	 * @param white true if this player is white
	 * @return true if this player has been checked or mated
	 */
    private boolean inCheck(boolean white) {
    	Condition cond;
		if (white) {
			cond = getCondition(true, false, false);
			return cond == Condition.W_IN_CHECK;
		} else {
			cond = getCondition(false, true, false);
			return cond == Condition.B_IN_CHECK;
		}
	}

	private MoveType rookMove(Move m) {
		int dx = m.ncol - m.ocol;
		int dy = m.nrow - m.orow;
		if (dx != 0 && dy != 0) {
			// not moving along a rank or file
			return MoveType.ILLEGAL;
		}
		
		if (dx > 0) {
			for (int i = 1; i < dx; i++) {
				if (!isEmpty(m.orow, m.ocol + i)) {
					return MoveType.ILLEGAL;
				}
			}
		} else if (dx < 0) {
			for (int i = -1; i > dx; i--) {
				if (!isEmpty(m.orow, m.ocol + i)) {
					return MoveType.ILLEGAL;
				}
			}
		} else if (dy > 0) {
			for (int i = 1; i < dy; i++) {
				if(!isEmpty(m.orow + i, m.ocol)) {
					return MoveType.ILLEGAL;
				}
			}
		} else {
			for (int i = -1; i > dy; i--) {
				if(!isEmpty(m.orow + i, m.ocol)) {
					return MoveType.ILLEGAL;
				}
			}
		}
		
		return MoveType.NORMAL;
	}

	private MoveType queenMove(Move m) {
		int dx = m.ncol - m.ocol;
		int dy = m.nrow - m.orow;
		
		if (dx == 0 || dy == 0) {
			return rookMove(m);
		} else {
			return bishopMove(m);
		}
	}

	private int setEnpassant(Move m) {
		int total = 0;
		Piece ep;
		if (inRange(m.nrow, m.ncol - 1)) {
			ep = get(m.nrow, m.ncol - 1);
			if (ep != null && ep.getType() == ChessType.PAWN) {
				enpassant[0] = new Move(m.nrow, m.ncol - 1, (m.orow + m.nrow)/2, m.ncol);
				++total;
			}
		}
		if (inRange(m.nrow, m.ncol + 1)) {
			ep = get(m.nrow, m.ncol + 1);
			if (ep != null && ep.getType() == ChessType.PAWN) {
				enpassant[1] = new Move(m.nrow, m.ncol + 1, (m.orow + m.nrow)/2, m.ncol);
				++total;
			}
		}
		//System.out.println("Found enpassant " + total);
		return total;
	}
	
	private MoveType pawnMove(Move m) {
		int adx = Math.abs(m.ncol - m.ocol);
		int dy = m.nrow - m.orow;
		boolean destEmpty = isEmpty(m.nrow, m.ncol);
		Piece pawn = get(m.orow, m.ocol);

		if (pawn.isWhite()) {
			if (adx == 0 && dy == 1 && destEmpty) {
				return MoveType.NORMAL;
			}
			if (adx == 0 && dy == 2 && isEmpty(m.orow + 1, m.ocol)
					&& destEmpty && !pawn.hasMoved()) {
				return MoveType.PAWN_DOUBLE;
			}
			if (adx == 1 && dy == 1) {
				if (!destEmpty) {
					return MoveType.NORMAL;
				} else if (m.equals(enpassant[0]) || m.equals(enpassant[1])) {
					return MoveType.ENPASSANT;
				} else {
					return MoveType.ILLEGAL;
				}
			}
		} else {
			// black pawn
			if (adx == 0 && dy == -1 && destEmpty) {
				return MoveType.NORMAL;
			}
			if (adx == 0 && dy == -2 && isEmpty(m.orow - 1, m.ocol)
					&& destEmpty && !pawn.hasMoved()) {
				return MoveType.PAWN_DOUBLE;
			}
			if (adx == 1 && dy == -1) {
				if (!destEmpty) {
					return MoveType.NORMAL;
				} else if (m.equals(enpassant[0]) || m.equals(enpassant[1])) {
					return MoveType.ENPASSANT;
				} else {
					return MoveType.ILLEGAL;
				}
			}
		}
		
		return MoveType.ILLEGAL;
	}

	private MoveType knightMove(Move m) {
		int adx = Math.abs(m.ncol - m.ocol);
		int ady = Math.abs(m.nrow - m.orow);
		
		if (adx == 1 && ady == 2) {
			return MoveType.NORMAL;
		} else if (adx == 2 && ady == 1) {
			return MoveType.NORMAL;
		} else {
			return MoveType.ILLEGAL;
		}
	}

	private MoveType kingMove(Move m) {
		int dx = m.ncol - m.ocol;
		int adx = Math.abs(dx);
		int ady = Math.abs(m.nrow - m.orow);
		MoveType result = MoveType.ILLEGAL;
		Piece king = get(m.orow, m.ocol);
		Piece rook;
		
		if (adx <= 1 && ady <= 1) {
			return MoveType.NORMAL;
		}
		
		rook = partnerRook(king.isWhite(), m.nrow, m.ncol);
		if (king.hasMoved() || rook == null || rook.hasMoved()) {
			return MoveType.ILLEGAL;
		}

		if (inCheck(king.isWhite())) {
			return MoveType.ILLEGAL;
		}
		
		// ensure no pieces between king and rook, and king does
		// not pass through check
		if (dx > 0) {
			if (get(m.orow, 6) != null || get(m.orow, 7) != null) {
				return MoveType.ILLEGAL;
			}
			clear(m.orow, m.ocol);
			put(king, m.orow, 6);
			if (inCheck(king.isWhite())) {
				result = MoveType.ILLEGAL;
			} else {
				result = MoveType.CASTLING;
			}
			clear(m.orow, 6);
			put(king, m.orow, m.ocol);
		} else {
			if (get(m.orow, 2) != null || get(m.orow, 3) != null || get(m.orow, 4) != null) {
				return MoveType.ILLEGAL;
			}
			clear(m.orow, m.ocol);
			put(king, m.orow, 4);
			if (inCheck(king.isWhite())) {
				result = MoveType.ILLEGAL;
			} else {
				result = MoveType.CASTLING;
			}
			clear(m.orow, 4);
			put(king, m.orow, m.ocol);
		}
		return result;
	}

	private MoveType bishopMove(Move m) {
    	int dx = m.ncol - m.ocol;
    	int dy = m.nrow - m.orow;
    	int adx = Math.abs(dx);
    	int ady = Math.abs(dy);
    	if (adx != ady) {
    		// not diagonal
    		return MoveType.ILLEGAL;
    	}
    	
    	if (dx > 0 && dy > 0) {
    		for (int i = 1; i < adx; i++) {
    			if (!isEmpty(m.orow + i, m.ocol + i)) {
    				// path blocked
    				return MoveType.ILLEGAL;
    			}
    		}
    	} else if (dx > 0 && dy < 0){
    		for (int i = 1; i < adx; i++) {
    			if (!isEmpty(m.orow - i, m.ocol + i)) {
    				// path blocked
    				return MoveType.ILLEGAL;
    			}
    		}
    	} else if (dx < 0 && dy > 0) {
    		for (int i = 1; i < adx; i++) {
    			if (!isEmpty(m.orow + i, m.ocol - i)) {
    				// path blocked
    				return MoveType.ILLEGAL;
    			}
    		}
    	} else {
    		for (int i = 1; i < adx; i++) {
    			if (!isEmpty(m.orow - i, m.ocol - i)) {
    				// path blocked
    				return MoveType.ILLEGAL;
    			}
    		}
    	}
    	
    	return MoveType.NORMAL;
    }
	
	/**
	 * Returns the rook involved in a castling move.
	 * 
	 * @param nrow the row that the king is to move to
	 * @param ncol the column that the king is to move to
	 * @return the rook involved in the castling, if it exists
	 */
	private Piece partnerRook(boolean white, int nrow, int ncol) {
		if ( (white && nrow == 1) || (!white && nrow == 8) ) {
			if (ncol == 3) {
				return get(nrow, 1);
			} else if (ncol == 7) {
				return get(nrow, 8);
			} else {
				return null;
			}
		} else {
			return null;
		}
	}

	public boolean isDrawOffered() {
		return drawOffered;
	}

	public void setDrawOffered(boolean drawOffered) {
		this.drawOffered = drawOffered;
	}

}