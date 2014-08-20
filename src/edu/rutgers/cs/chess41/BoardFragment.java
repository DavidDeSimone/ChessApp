/**
 * @author David DeSimone
 */
package edu.rutgers.cs.chess41;

import java.util.StringTokenizer;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.ImageButton;

public class BoardFragment extends Fragment implements OnClickListener, OnTouchListener {

	private int selectedRow, selectedCol;
	private boolean enabled, whiteToMove;
	private OnMoveListener mListener;
	private boolean highlighted = false;
	private ImageButton previous;
	private int rmov, cmov;

	/**
	 * Interface that activities attached to this fragment should implement.
	 */
	public interface OnMoveListener {
		public void onMove(int orow, int ocol, int nrow, int ncol, ChessType ct);
		public boolean promotionMove(int orow, int ocol, int nrow, int ncol);
		public boolean canSelect(int row, int col);

	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.board_fragment, container, false);
		initListeners(v);
		// Inflate the layout for this fragment
		return v;
	}

	private void initListeners(View v) {

		ImageButton bl = (ImageButton) v.findViewById(R.id.imageButton001);
		bl.setOnClickListener(this);

		bl = (ImageButton) v.findViewById(R.id.imageButton002);
		bl.setOnClickListener(this);
		bl.setOnTouchListener(this);

		bl = (ImageButton) v.findViewById(R.id.imageButton003);
		bl.setOnClickListener(this);

		bl = (ImageButton) v.findViewById(R.id.imageButton004);
		bl.setOnClickListener(this);

		bl = (ImageButton) v.findViewById(R.id.imageButton005);
		bl.setOnClickListener(this);

		bl = (ImageButton) v.findViewById(R.id.imageButton006);
		bl.setOnClickListener(this);

		bl = (ImageButton) v.findViewById(R.id.imageButton007);
		bl.setOnClickListener(this);

		bl = (ImageButton) v.findViewById(R.id.imageButton008);
		bl.setOnClickListener(this);

		bl = (ImageButton) v.findViewById(R.id.imageButton009);
		bl.setOnClickListener(this);

		bl = (ImageButton) v.findViewById(R.id.imageButton0010);
		bl.setOnClickListener(this);

		bl = (ImageButton) v.findViewById(R.id.imageButton0011);
		bl.setOnClickListener(this);
		//
		bl = (ImageButton) v.findViewById(R.id.imageButton0012);
		bl.setOnClickListener(this);

		bl = (ImageButton) v.findViewById(R.id.imageButton0013);
		bl.setOnClickListener(this);

		bl = (ImageButton) v.findViewById(R.id.imageButton0014);
		bl.setOnClickListener(this);

		bl = (ImageButton) v.findViewById(R.id.imageButton0015);
		bl.setOnClickListener(this);

		bl = (ImageButton) v.findViewById(R.id.imageButton0016);
		bl.setOnClickListener(this);

		bl = (ImageButton) v.findViewById(R.id.imageButton0017);
		bl.setOnClickListener(this);

		bl = (ImageButton) v.findViewById(R.id.imageButton0018);
		bl.setOnClickListener(this);

		bl = (ImageButton) v.findViewById(R.id.ImageButton0019);
		bl.setOnClickListener(this);

		bl = (ImageButton) v.findViewById(R.id.ImageButton0020);
		bl.setOnClickListener(this);

		bl = (ImageButton) v.findViewById(R.id.ImageButton0021);
		bl.setOnClickListener(this);

		bl = (ImageButton) v.findViewById(R.id.ImageButton0022);
		bl.setOnClickListener(this);
		//
		bl = (ImageButton) v.findViewById(R.id.ImageButton0023);
		bl.setOnClickListener(this);

		bl = (ImageButton) v.findViewById(R.id.ImageButton0024);
		bl.setOnClickListener(this);

		bl = (ImageButton) v.findViewById(R.id.ImageButton0025);
		bl.setOnClickListener(this);

		bl = (ImageButton) v.findViewById(R.id.ImageButton0026);
		bl.setOnClickListener(this);

		bl = (ImageButton) v.findViewById(R.id.ImageButton0027);
		bl.setOnClickListener(this);

		bl = (ImageButton) v.findViewById(R.id.ImageButton0028);
		bl.setOnClickListener(this);

		bl = (ImageButton) v.findViewById(R.id.ImageButton0029);
		bl.setOnClickListener(this);

		bl = (ImageButton) v.findViewById(R.id.ImageButton0030);
		bl.setOnClickListener(this);

		bl = (ImageButton) v.findViewById(R.id.ImageButton0031);
		bl.setOnClickListener(this);

		bl = (ImageButton) v.findViewById(R.id.ImageButton0032);
		bl.setOnClickListener(this);

		bl = (ImageButton) v.findViewById(R.id.ImageButton0033);
		bl.setOnClickListener(this);
		//
		bl = (ImageButton) v.findViewById(R.id.ImageButton0034);
		bl.setOnClickListener(this);

		bl = (ImageButton) v.findViewById(R.id.ImageButton0035);
		bl.setOnClickListener(this);

		bl = (ImageButton) v.findViewById(R.id.ImageButton0036);
		bl.setOnClickListener(this);

		bl = (ImageButton) v.findViewById(R.id.ImageButton0037);
		bl.setOnClickListener(this);

		bl = (ImageButton) v.findViewById(R.id.ImageButton0038);
		bl.setOnClickListener(this);

		bl = (ImageButton) v.findViewById(R.id.ImageButton0039);
		bl.setOnClickListener(this);

		bl = (ImageButton) v.findViewById(R.id.ImageButton0040);
		bl.setOnClickListener(this);

		bl = (ImageButton) v.findViewById(R.id.ImageButton0041);
		bl.setOnClickListener(this);

		bl = (ImageButton) v.findViewById(R.id.ImageButton0042);
		bl.setOnClickListener(this);

		bl = (ImageButton) v.findViewById(R.id.ImageButton0043);
		bl.setOnClickListener(this);

		bl = (ImageButton) v.findViewById(R.id.ImageButton0044);
		bl.setOnClickListener(this);
		//
		bl = (ImageButton) v.findViewById(R.id.ImageButton0045);
		bl.setOnClickListener(this);

		bl = (ImageButton) v.findViewById(R.id.ImageButton0046);
		bl.setOnClickListener(this);

		bl = (ImageButton) v.findViewById(R.id.ImageButton0047);
		bl.setOnClickListener(this);

		bl = (ImageButton) v.findViewById(R.id.ImageButton0048);
		bl.setOnClickListener(this);

		bl = (ImageButton) v.findViewById(R.id.imageButton0049);
		bl.setOnClickListener(this);

		bl = (ImageButton) v.findViewById(R.id.imageButton0050);
		bl.setOnClickListener(this);

		bl = (ImageButton) v.findViewById(R.id.ImageButton0051);
		bl.setOnClickListener(this);

		bl = (ImageButton) v.findViewById(R.id.ImageButton0052);
		bl.setOnClickListener(this);

		bl = (ImageButton) v.findViewById(R.id.ImageButton0053);
		bl.setOnClickListener(this);

		bl = (ImageButton) v.findViewById(R.id.ImageButton0054);
		bl.setOnClickListener(this);

		bl = (ImageButton) v.findViewById(R.id.ImageButton0055);
		bl.setOnClickListener(this);
		//
		bl = (ImageButton) v.findViewById(R.id.ImageButton0056);
		bl.setOnClickListener(this);

		bl = (ImageButton) v.findViewById(R.id.imageButton0057);
		bl.setOnClickListener(this);

		bl = (ImageButton) v.findViewById(R.id.imageButton0058);
		bl.setOnClickListener(this);

		bl = (ImageButton) v.findViewById(R.id.imageButton0059);
		bl.setOnClickListener(this);

		bl = (ImageButton) v.findViewById(R.id.imageButton0060);
		bl.setOnClickListener(this);

		bl = (ImageButton) v.findViewById(R.id.imageButton0061);
		bl.setOnClickListener(this);

		bl = (ImageButton) v.findViewById(R.id.imageButton0062);
		bl.setOnClickListener(this);

		bl = (ImageButton) v.findViewById(R.id.imageButton0063);
		bl.setOnClickListener(this);

		bl = (ImageButton) v.findViewById(R.id.imageButton0064);
		bl.setOnClickListener(this);

	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		selectedRow = -1; selectedCol = -1;
		try {
			mListener = (OnMoveListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString() + " must implement OnMoveListener");
		}
	}

	public void drawBoard(String board) {
		// TODO read the string representation and update the UI
		StringTokenizer str = new StringTokenizer(board, "\n", false);

		int total = 1;
		int xpos = 8;
		while(str.hasMoreTokens()) {

			String next = str.nextToken();
			for(int i = 0, j = 0; i < next.length() / 2; i++, j += 2 ) {

				String peice = next.substring(j, j + 2);
				setButton(peice, total, xpos, i + 1);
				total++;	
			}
			xpos--;
		}


	}

	private void setButton(String piece, int pos, int xpos, int ypos) {

		ImageButton ib;

		switch(pos) {
		case 1: ib = (ImageButton)getView().findViewById(R.id.imageButton001); break;
		case 2: ib = (ImageButton)getView().findViewById(R.id.imageButton002); break;
		case 3: ib = (ImageButton)getView().findViewById(R.id.imageButton003); break;
		case 4: ib = (ImageButton)getView().findViewById(R.id.imageButton004); break;
		case 5: ib = (ImageButton)getView().findViewById(R.id.imageButton005); break;
		case 6: ib = (ImageButton)getView().findViewById(R.id.imageButton006); break;
		case 7: ib = (ImageButton)getView().findViewById(R.id.imageButton007); break;
		case 8: ib = (ImageButton)getView().findViewById(R.id.imageButton008); break;
		case 9: ib = (ImageButton)getView().findViewById(R.id.imageButton009); break;
		case 10: ib = (ImageButton)getView().findViewById(R.id.imageButton0010); break;
		case 11: ib = (ImageButton)getView().findViewById(R.id.imageButton0011); break;
		case 12: ib = (ImageButton)getView().findViewById(R.id.imageButton0012); break;
		case 13: ib = (ImageButton)getView().findViewById(R.id.imageButton0013); break;
		case 14: ib = (ImageButton)getView().findViewById(R.id.imageButton0014); break;
		case 15: ib = (ImageButton)getView().findViewById(R.id.imageButton0015); break;
		case 16: ib = (ImageButton)getView().findViewById(R.id.imageButton0016); break;
		case 17: ib = (ImageButton)getView().findViewById(R.id.imageButton0017); break;
		case 18: ib = (ImageButton)getView().findViewById(R.id.imageButton0018); break;
		case 19: ib = (ImageButton)getView().findViewById(R.id.ImageButton0019); break;
		case 20: ib = (ImageButton)getView().findViewById(R.id.ImageButton0020); break;
		case 21: ib = (ImageButton)getView().findViewById(R.id.ImageButton0021); break;
		case 22: ib = (ImageButton)getView().findViewById(R.id.ImageButton0022); break;
		case 23: ib = (ImageButton)getView().findViewById(R.id.ImageButton0023); break;
		case 24: ib = (ImageButton)getView().findViewById(R.id.ImageButton0024); break;
		case 25: ib = (ImageButton)getView().findViewById(R.id.ImageButton0025); break;
		case 26: ib = (ImageButton)getView().findViewById(R.id.ImageButton0026); break;
		case 27: ib = (ImageButton)getView().findViewById(R.id.ImageButton0027); break;
		case 28: ib = (ImageButton)getView().findViewById(R.id.ImageButton0028); break;
		case 29: ib = (ImageButton)getView().findViewById(R.id.ImageButton0029); break;
		case 30: ib = (ImageButton)getView().findViewById(R.id.ImageButton0030); break;
		case 31: ib = (ImageButton)getView().findViewById(R.id.ImageButton0031); break;
		case 32: ib = (ImageButton)getView().findViewById(R.id.ImageButton0032); break;
		case 33: ib = (ImageButton)getView().findViewById(R.id.ImageButton0033); break;
		case 34: ib = (ImageButton)getView().findViewById(R.id.ImageButton0034); break;
		case 35: ib = (ImageButton)getView().findViewById(R.id.ImageButton0035); break;
		case 36: ib = (ImageButton)getView().findViewById(R.id.ImageButton0036); break;
		case 37: ib = (ImageButton)getView().findViewById(R.id.ImageButton0037); break;
		case 38: ib = (ImageButton)getView().findViewById(R.id.ImageButton0038); break;
		case 39: ib = (ImageButton)getView().findViewById(R.id.ImageButton0039); break;
		case 40: ib = (ImageButton)getView().findViewById(R.id.ImageButton0040); break;
		case 41: ib = (ImageButton)getView().findViewById(R.id.ImageButton0041); break;
		case 42: ib = (ImageButton)getView().findViewById(R.id.ImageButton0042); break;
		case 43: ib = (ImageButton)getView().findViewById(R.id.ImageButton0043); break;
		case 44: ib = (ImageButton)getView().findViewById(R.id.ImageButton0044); break;
		case 45: ib = (ImageButton)getView().findViewById(R.id.ImageButton0045); break;
		case 46: ib = (ImageButton)getView().findViewById(R.id.ImageButton0046); break;
		case 47: ib = (ImageButton)getView().findViewById(R.id.ImageButton0047); break;
		case 48: ib = (ImageButton)getView().findViewById(R.id.ImageButton0048); break;
		case 49: ib = (ImageButton)getView().findViewById(R.id.imageButton0049); break;
		case 50: ib = (ImageButton)getView().findViewById(R.id.imageButton0050); break;
		case 51: ib = (ImageButton)getView().findViewById(R.id.ImageButton0051); break;
		case 52: ib = (ImageButton)getView().findViewById(R.id.ImageButton0052); break;
		case 53: ib = (ImageButton)getView().findViewById(R.id.ImageButton0053); break;
		case 54: ib = (ImageButton)getView().findViewById(R.id.ImageButton0054); break;
		case 55: ib = (ImageButton)getView().findViewById(R.id.ImageButton0055); break;
		case 56: ib = (ImageButton)getView().findViewById(R.id.ImageButton0056); break;
		case 57: ib = (ImageButton)getView().findViewById(R.id.imageButton0057); break;
		case 58: ib = (ImageButton)getView().findViewById(R.id.imageButton0058); break;
		case 59: ib = (ImageButton)getView().findViewById(R.id.imageButton0059); break;
		case 60: ib = (ImageButton)getView().findViewById(R.id.imageButton0060); break;
		case 61: ib = (ImageButton)getView().findViewById(R.id.imageButton0061); break;
		case 62: ib = (ImageButton)getView().findViewById(R.id.imageButton0062); break;
		case 63: ib = (ImageButton)getView().findViewById(R.id.imageButton0063); break;
		case 64: ib = (ImageButton)getView().findViewById(R.id.imageButton0064); break;

		default: ib = null;
		}

		if(ib == null) {
			//ERROR, Shouldnt happen
			return;
		} 

		if( (xpos + ypos) % 2 == 0) {
			setBlackBackground(piece, ib);
		} else {
			setWhiteBackground(piece, ib);
		}
	}

	private void setBlackBackground(String piece, ImageButton ib) {


		if(piece.charAt(0) == '#') {
			ib.setImageResource(R.drawable.black_bg);
		} 
		if(piece.charAt(0) == '_') {
			ib.setImageResource(R.drawable.white_bg);
		}


		if(piece.charAt(0) == 'b') {

			if(piece.charAt(1) == 'p') {
				ib.setImageResource(R.drawable.black_black_pawn);
			} 
			if(piece.charAt(1) == 'R') {
				ib.setImageResource(R.drawable.black_black_rook);	
			}
			if(piece.charAt(1) == 'N') {
				ib.setImageResource(R.drawable.black_black_knight);
			}
			if(piece.charAt(1) == 'B') {
				ib.setImageResource(R.drawable.black_black_bishop);
			}
			if(piece.charAt(1) == 'K') {
				ib.setImageResource(R.drawable.black_black_queen);
			}
			if(piece.charAt(1) == 'Q') {
				ib.setImageResource(R.drawable.black_black_king);
			}


		} else 
			if(piece.charAt(0) == 'w') {
				if(piece.charAt(1) == 'p') {
					ib.setImageResource(R.drawable.white_black_pawn);
				} 
				if(piece.charAt(1) == 'R') {
					ib.setImageResource(R.drawable.white_black_rook);	
				}
				if(piece.charAt(1) == 'N') {
					ib.setImageResource(R.drawable.white_black_knight);
				}
				if(piece.charAt(1) == 'B') {
					ib.setImageResource(R.drawable.white_black_bishop);
				}
				if(piece.charAt(1) == 'K') {
					ib.setImageResource(R.drawable.white_black_queen);
				}
				if(piece.charAt(1) == 'Q') {
					ib.setImageResource(R.drawable.white_black_king);
				}

			}

	}


	private void setWhiteBackground(String piece, ImageButton ib) {
		if(piece.charAt(0) == '#') {
			ib.setImageResource(R.drawable.black_bg);
		} 
		if(piece.charAt(0) == '_') {
			ib.setImageResource(R.drawable.white_bg);
		}	

		if(piece.charAt(0) == 'b') {

			if(piece.charAt(1) == 'p') {
				ib.setImageResource(R.drawable.black_white_pawn);
			} 
			if(piece.charAt(1) == 'R') {
				ib.setImageResource(R.drawable.black_white_rook);	
			}
			if(piece.charAt(1) == 'N') {
				ib.setImageResource(R.drawable.black_white_knight);
			}
			if(piece.charAt(1) == 'B') {
				ib.setImageResource(R.drawable.black_white_bishop);
			}
			if(piece.charAt(1) == 'K') {
				ib.setImageResource(R.drawable.black_white_queen);
			}
			if(piece.charAt(1) == 'Q') {
				ib.setImageResource(R.drawable.black_white_king);
			}


		} else 
			if(piece.charAt(0) == 'w') {
				if(piece.charAt(1) == 'p') {
					ib.setImageResource(R.drawable.white_white_pawn);
				} 
				if(piece.charAt(1) == 'R') {
					ib.setImageResource(R.drawable.white_white_rook);	
				}
				if(piece.charAt(1) == 'N') {
					ib.setImageResource(R.drawable.white_white_knight);
				}
				if(piece.charAt(1) == 'B') {
					ib.setImageResource(R.drawable.white_white_bishop);
				}
				if(piece.charAt(1) == 'K') {
					ib.setImageResource(R.drawable.white_white_queen);
				}
				if(piece.charAt(1) == 'Q') {
					ib.setImageResource(R.drawable.white_white_king);
				}
			}
	}


	public void setEnabled(boolean flag) {
		enabled = flag;
	}

	public void setWhiteToMove(boolean flag) {
		whiteToMove = flag;
	}

	@Override
	public void onClick(View v) {

		ImageButton ib = (ImageButton)getView().findViewById(v.getId());



		if(selectedRow == -1 && selectedCol == -1) { //User is selecting a piece for the first time
			switch(v.getId()) {
			case R.id.imageButton001: selectedRow = 8; selectedCol = 1; break;
			case R.id.imageButton002: selectedRow = 8; selectedCol = 2; break;
			case R.id.imageButton003: selectedRow = 8; selectedCol = 3; break;
			case R.id.imageButton004: selectedRow = 8; selectedCol = 4; break;
			case R.id.imageButton005: selectedRow = 8; selectedCol = 5; break;
			case R.id.imageButton006: selectedRow = 8; selectedCol = 6; break;
			case R.id.imageButton007: selectedRow = 8; selectedCol = 7; break;
			case R.id.imageButton008: selectedRow = 8; selectedCol = 8; break;
			case R.id.imageButton009: selectedRow = 7; selectedCol = 1; break;
			case R.id.imageButton0010: selectedRow = 7; selectedCol = 2; break;
			case R.id.imageButton0011: selectedRow = 7; selectedCol = 3; break;
			case R.id.imageButton0012: selectedRow = 7; selectedCol = 4; break;
			case R.id.imageButton0013: selectedRow = 7; selectedCol = 5; break;
			case R.id.imageButton0014: selectedRow = 7; selectedCol = 6; break;
			case R.id.imageButton0015: selectedRow = 7; selectedCol = 7; break;
			case R.id.imageButton0016: selectedRow = 7; selectedCol = 8; break;
			case R.id.imageButton0017: selectedRow = 6; selectedCol = 1; break;
			case R.id.imageButton0018: selectedRow = 6; selectedCol = 2; break;
			case R.id.ImageButton0019: selectedRow = 6; selectedCol = 3; break;
			case R.id.ImageButton0020: selectedRow = 6; selectedCol = 4; break;
			case R.id.ImageButton0021: selectedRow = 6; selectedCol = 5; break;
			case R.id.ImageButton0022: selectedRow = 6; selectedCol = 6; break;
			case R.id.ImageButton0023: selectedRow = 6; selectedCol = 7; break;
			case R.id.ImageButton0024: selectedRow = 6; selectedCol = 8; break;
			case R.id.ImageButton0025: selectedRow = 5; selectedCol = 1; break;
			case R.id.ImageButton0026: selectedRow = 5; selectedCol = 2; break;
			case R.id.ImageButton0027: selectedRow = 5; selectedCol = 3; break;
			case R.id.ImageButton0028: selectedRow = 5; selectedCol = 4; break;
			case R.id.ImageButton0029: selectedRow = 5; selectedCol = 5; break;
			case R.id.ImageButton0030: selectedRow = 5; selectedCol = 6; break;
			case R.id.ImageButton0031: selectedRow = 5; selectedCol = 7; break;
			case R.id.ImageButton0032: selectedRow = 5; selectedCol = 8; break;
			case R.id.ImageButton0033: selectedRow = 4; selectedCol = 1; break;
			case R.id.ImageButton0034: selectedRow = 4; selectedCol = 2; break;
			case R.id.ImageButton0035: selectedRow = 4; selectedCol = 3; break;
			case R.id.ImageButton0036: selectedRow = 4; selectedCol = 4; break;
			case R.id.ImageButton0037: selectedRow = 4; selectedCol = 5; break;
			case R.id.ImageButton0038: selectedRow = 4; selectedCol = 6; break;
			case R.id.ImageButton0039: selectedRow = 4; selectedCol = 7; break;
			case R.id.ImageButton0040: selectedRow = 4; selectedCol = 8; break;
			case R.id.ImageButton0041: selectedRow = 3; selectedCol = 1; break;
			case R.id.ImageButton0042: selectedRow = 3; selectedCol = 2; break;
			case R.id.ImageButton0043: selectedRow = 3; selectedCol = 3; break;
			case R.id.ImageButton0044: selectedRow = 3; selectedCol = 4; break;
			case R.id.ImageButton0045: selectedRow = 3; selectedCol = 5; break;
			case R.id.ImageButton0046: selectedRow = 3; selectedCol = 6; break;
			case R.id.ImageButton0047: selectedRow = 3; selectedCol = 7; break;
			case R.id.ImageButton0048: selectedRow = 3; selectedCol = 8; break;
			case R.id.imageButton0049: selectedRow = 2; selectedCol = 1; break;
			case R.id.imageButton0050: selectedRow = 2; selectedCol = 2; break;
			case R.id.ImageButton0051: selectedRow = 2; selectedCol = 3; break;
			case R.id.ImageButton0052: selectedRow = 2; selectedCol = 4; break;
			case R.id.ImageButton0053: selectedRow = 2; selectedCol = 5; break;
			case R.id.ImageButton0054: selectedRow = 2; selectedCol = 6; break;
			case R.id.ImageButton0055: selectedRow = 2; selectedCol = 7; break;
			case R.id.ImageButton0056: selectedRow = 2; selectedCol = 8; break;
			case R.id.imageButton0057: selectedRow = 1; selectedCol = 1; break;
			case R.id.imageButton0058: selectedRow = 1; selectedCol = 2; break;
			case R.id.imageButton0059: selectedRow = 1; selectedCol = 3; break;
			case R.id.imageButton0060: selectedRow = 1; selectedCol = 4; break;
			case R.id.imageButton0061: selectedRow = 1; selectedCol = 5; break;
			case R.id.imageButton0062: selectedRow = 1; selectedCol = 6; break;
			case R.id.imageButton0063: selectedRow = 1; selectedCol = 7; break;
			case R.id.imageButton0064: selectedRow = 1; selectedCol = 8; break;
			//etc

			default: selectedRow = -1; selectedCol = -1;
			}

			if(!highlighted) {	
				if(mListener.canSelect(selectedRow, selectedCol)) {
					ib.setColorFilter(Color.argb(155, 125, 190, 230));
					previous = ib;
					highlighted = true;
				} else {
					selectedRow = -1; selectedCol = -1;  
				}
			} else 
				if(highlighted) {
					previous.setColorFilter(Color.argb(0, 185, 185, 185));
					highlighted = false;
				}


		}
		else {
			//User is attemping a move. Get position of latest button press;
			rmov = -1; cmov = -1;

			switch(v.getId()) {
			case R.id.imageButton001: rmov = 8; cmov = 1; break;
			case R.id.imageButton002: rmov = 8; cmov = 2; break;
			case R.id.imageButton003: rmov = 8; cmov = 3; break;
			case R.id.imageButton004: rmov = 8; cmov = 4; break;
			case R.id.imageButton005: rmov = 8; cmov = 5; break;
			case R.id.imageButton006: rmov = 8; cmov = 6; break;
			case R.id.imageButton007: rmov = 8; cmov = 7; break;
			case R.id.imageButton008: rmov = 8; cmov = 8; break;
			case R.id.imageButton009: rmov = 7; cmov = 1; break;
			case R.id.imageButton0010: rmov = 7; cmov = 2; break;
			case R.id.imageButton0011: rmov = 7; cmov = 3; break;
			case R.id.imageButton0012: rmov = 7; cmov = 4; break;
			case R.id.imageButton0013: rmov = 7; cmov = 5; break;
			case R.id.imageButton0014: rmov = 7; cmov = 6; break;
			case R.id.imageButton0015: rmov = 7; cmov = 7; break;
			case R.id.imageButton0016: rmov = 7; cmov = 8; break;
			case R.id.imageButton0017: rmov = 6; cmov = 1; break;
			case R.id.imageButton0018: rmov = 6; cmov = 2; break;
			case R.id.ImageButton0019: rmov = 6; cmov = 3; break;
			case R.id.ImageButton0020: rmov = 6; cmov = 4; break;
			case R.id.ImageButton0021: rmov = 6; cmov = 5; break;
			case R.id.ImageButton0022: rmov = 6; cmov = 6; break;
			case R.id.ImageButton0023: rmov = 6; cmov = 7; break;
			case R.id.ImageButton0024: rmov = 6; cmov = 8; break;
			case R.id.ImageButton0025: rmov = 5; cmov = 1; break;
			case R.id.ImageButton0026: rmov = 5; cmov = 2; break;
			case R.id.ImageButton0027: rmov = 5; cmov = 3; break;
			case R.id.ImageButton0028: rmov = 5; cmov = 4; break;
			case R.id.ImageButton0029: rmov = 5; cmov = 5; break;
			case R.id.ImageButton0030: rmov = 5; cmov = 6; break;
			case R.id.ImageButton0031: rmov = 5; cmov = 7; break;
			case R.id.ImageButton0032: rmov = 5; cmov = 8; break;
			case R.id.ImageButton0033: rmov = 4; cmov = 1; break;
			case R.id.ImageButton0034: rmov = 4; cmov = 2; break;
			case R.id.ImageButton0035: rmov = 4; cmov = 3; break;
			case R.id.ImageButton0036: rmov = 4; cmov = 4; break;
			case R.id.ImageButton0037: rmov = 4; cmov = 5; break;
			case R.id.ImageButton0038: rmov = 4; cmov = 6; break;
			case R.id.ImageButton0039: rmov = 4; cmov = 7; break;
			case R.id.ImageButton0040: rmov = 4; cmov = 8; break;
			case R.id.ImageButton0041: rmov = 3; cmov = 1; break;
			case R.id.ImageButton0042: rmov = 3; cmov = 2; break;
			case R.id.ImageButton0043: rmov = 3; cmov = 3; break;
			case R.id.ImageButton0044: rmov = 3; cmov = 4; break;
			case R.id.ImageButton0045: rmov = 3; cmov = 5; break;
			case R.id.ImageButton0046: rmov = 3; cmov = 6; break;
			case R.id.ImageButton0047: rmov = 3; cmov = 7; break;
			case R.id.ImageButton0048: rmov = 3; cmov = 8; break;
			case R.id.imageButton0049: rmov = 2; cmov = 1; break;
			case R.id.imageButton0050: rmov = 2; cmov = 2; break;
			case R.id.ImageButton0051: rmov = 2; cmov = 3; break;
			case R.id.ImageButton0052: rmov = 2; cmov = 4; break;
			case R.id.ImageButton0053: rmov = 2; cmov = 5; break;
			case R.id.ImageButton0054: rmov = 2; cmov = 6; break;
			case R.id.ImageButton0055: rmov = 2; cmov = 7; break;
			case R.id.ImageButton0056: rmov = 2; cmov = 8; break;
			case R.id.imageButton0057: rmov = 1; cmov = 1; break;
			case R.id.imageButton0058: rmov = 1; cmov = 2; break;
			case R.id.imageButton0059: rmov = 1; cmov = 3; break;
			case R.id.imageButton0060: rmov = 1; cmov = 4; break;
			case R.id.imageButton0061: rmov = 1; cmov = 5; break;
			case R.id.imageButton0062: rmov = 1; cmov = 6; break;
			case R.id.imageButton0063: rmov = 1; cmov = 7; break;
			case R.id.imageButton0064: rmov = 1; cmov = 8; break;
			default: rmov = -1; cmov = -1;
			}


			if(highlighted) {
				previous.setColorFilter(Color.argb(0, 185, 185, 185));
				highlighted = false;
			}


			if(mListener.promotionMove(selectedRow, selectedCol, rmov, cmov)) {

				String[] arv = { "Queen", "Knight", "Bishop", "Rook" };
				AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
				builder.setTitle("Promote pawn to...")
				.setItems(arv, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						// The 'which' argument contains the index position
						// of the selected item
						if(which == 0) {
							mListener.onMove(selectedRow, selectedCol, rmov, cmov, ChessType.QUEEN);
							selectedRow = -1;
							selectedCol = -1;

						} else if(which == 1) {
							mListener.onMove(selectedRow, selectedCol, rmov, cmov, ChessType.KNIGHT);
							selectedRow = -1;
							selectedCol = -1;
						} else if(which == 2) {
							mListener.onMove(selectedRow, selectedCol, rmov, cmov, ChessType.BISHOP);
							selectedRow = -1;
							selectedCol = -1;
						} else if(which == 3) {
							mListener.onMove(selectedRow, selectedCol, rmov, cmov, ChessType.ROOK);
							selectedRow = -1;
							selectedCol = -1;
						}

					}
				});
				builder.create();  
				builder.show();
				return;
			}

			if (selectedRow != rmov || selectedCol != cmov) {
				mListener.onMove(selectedRow, selectedCol, rmov, cmov, ChessType.NONE);
			}
			selectedRow = -1;
			selectedCol = -1;

		}

	}


	@Override
	public boolean onTouch(final View v, final MotionEvent motionEvent) {
		return false;
	}
}
