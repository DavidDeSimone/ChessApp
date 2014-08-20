/**
 * @author Stephen Chung
 */

package edu.rutgers.cs.chess41;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.RandomAccessFile;
import java.util.Random;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

public class GameActivity extends Activity implements BoardFragment.OnMoveListener, SaveDialogFragment.SaveDialogListener {

	private static Handler handler = new Handler();
	
	private ChessBoard gameBoard;
	private BoardFragment bFrag;
	private TextView turnText;
	private boolean versusAi, humanIsWhite;
	private RandomAccessFile fileOutput;
	
	public static final String TEMP_NAME = "chessgame.tmp";
	public static final String RECORD_NAME = "recording.tmp";
	public static final String UNDO_NAME = "undo.tmp";
	
	public static final String RESUME = "edu.rutgers.cs.chess41.RESUME";
	public static final String VERSUS_AI = "edu.rutgers.cs.chess41.VERSUS_AI";
	public static final String HUMAN_IS_WHITE = "edu.rutgers.cs.chess41.HUMAN_IS_WHITE";
	public static final String DRAW_OFFER = "edu.rutgers.cs.chess41.DRAW_OFFER";
	
	public static class ComputerMove implements Runnable {

		private GameActivity activity;
		
		public ComputerMove(GameActivity activity) {
			this.activity = activity;
		}
		
		@Override
		public void run() {
			activity.aiMove(null);
			activity.enableActions();
		}
		
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game);
		Intent intent = getIntent();
		if (intent.getBooleanExtra(RESUME, false)) {
			loadBoard(TEMP_NAME);
			intent.putExtra(RESUME, false);
		} else {
			deleteFile(TEMP_NAME);
			deleteFile(RECORD_NAME);
			gameBoard = ChessBoard.getNewGame();
		}
	}
	


	@Override
	protected void onStart() {
		String state;
		bFrag = (BoardFragment) getFragmentManager().findFragmentById(R.id.board_fragment);
		turnText = (TextView) findViewById(R.id.whose_turn);
		Intent intent = getIntent();
		versusAi = intent.getBooleanExtra(VERSUS_AI, false);
		humanIsWhite = intent.getBooleanExtra(HUMAN_IS_WHITE, false);
		loadBoard(TEMP_NAME);
		try {
			File rec = new File(getFilesDir(), RECORD_NAME);
			fileOutput = new RandomAccessFile(rec, "rw");
			fileOutput.seek(fileOutput.length());
		} catch (FileNotFoundException e) {
			throw new RuntimeException("Unable to open recording file.");
		} catch (IOException e) {
			throw new RuntimeException("Unable to write to recording file.");
		}
		state = gameBoard.toString();
		try {
			fileOutput.write(state.getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
		bFrag.drawBoard(state);
		updateTurnText();
		
		if (versusAi && (humanIsWhite != gameBoard.whiteToMove())) {
			queueAiMove();
		}
		
		super.onStart();
	}
	
	private void updateTurnText() {
		if (gameBoard.whiteToMove()) {
			turnText.setText("White to Move");
		} else {
			turnText.setText("Black to Move");
		}
	}
	
	private void updateTurnText(String s) {
		turnText.setText(s);
	}
	
	private void saveBoard(String fname) {
		try {
			FileOutputStream fos = openFileOutput(fname, Context.MODE_PRIVATE);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(gameBoard);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private boolean loadBoard(String fname) {
		FileInputStream fis;
		try {
			fis = openFileInput(fname);
		} catch (FileNotFoundException e) {
			fis = null;
			return false;
		}
		if (fis != null) {
			try {
				ObjectInputStream ois = new ObjectInputStream(fis);
				gameBoard = (ChessBoard) ois.readObject();
			} catch (Exception e) {
				e.printStackTrace();
				//throw new RuntimeException("Found file, but could not read from it!");
			}
		}
		return true;
	}
	
	@Override
	protected void onPause() {
		saveBoard(TEMP_NAME);
		super.onPause();
	}
	
	/**
	 * Set the game state to finished. Disable all moves as well
	 * as action buttons.
	 * 
	 * @param winner 0 for white, 1 for black, 2 for draw
	 */
	private void gameOver(int winner, String message) {
		bFrag.setEnabled(false);
		String announce = "$ " + message;
		switch(winner) {
		case 0:
			turnText.setText("White wins.");
			announce += " White wins.";
			break;
		case 1:
			turnText.setText("Black wins.");
			announce += " Black wins.";
			break;
		case 2:
			turnText.setText("The game is drawn.");
			announce += " Draw.";
			break;
		}
		findViewById(R.id.button_undo).setVisibility(View.GONE);
		findViewById(R.id.button_ai_move).setVisibility(View.GONE);
		findViewById(R.id.button_draw).setVisibility(View.GONE);
		findViewById(R.id.button_resign).setVisibility(View.GONE);
		findViewById(R.id.button_exit).setVisibility(View.VISIBLE);
		try {
			fileOutput.write(announce.getBytes());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		deleteFile(UNDO_NAME);
		deleteFile(TEMP_NAME);
	}
	
	/**
	 * Re-enable actions after an AI move. Undo is not allowed after
	 * an AI move.
	 */
	protected void enableActions() {
		bFrag.setEnabled(true);
		findViewById(R.id.button_undo).setEnabled(false);
		findViewById(R.id.button_ai_move).setEnabled(true);
		findViewById(R.id.button_draw).setEnabled(true);
		findViewById(R.id.button_resign).setEnabled(true);
	}
	
	/**
	 * Undo one move.
	 * @param view the View calling this function
	 */
	public void undo(View view) {
		String state;
		if (loadBoard(UNDO_NAME) == false || versusAi == true) {
			throw new RuntimeException("Asked for undo when no undo is possible!");
		}
		deleteFile(UNDO_NAME);
		state = gameBoard.toString();
		try {
			// truncate the game recording, deleting the last move
			fileOutput.setLength(fileOutput.length() - state.length());
		} catch (IOException e) {
			e.printStackTrace();
		}
		findViewById(R.id.button_undo).setEnabled(false);
		bFrag.drawBoard(state);
		updateTurnText();

	}
	
	/**
	 * Offer a draw to the other player.
	 * @param view the View calling this function
	 */
	public void offerDraw(View view) {
		if (versusAi) {
			String out;
			Random rand = new Random(System.currentTimeMillis());
			if (rand.nextInt() % 10 < 3) {
				out = "AI accepts draw offer.";
			} else {
				out = "AI rejects draw offer.";
			}
			Toast toast = Toast.makeText(getApplicationContext(), out, Toast.LENGTH_SHORT);
			toast.setGravity(Gravity.CENTER, 0, 0);
			toast.show();
			if (out.startsWith("AI accepts")) {
				gameOver(2, "By mutual agreement:");
			}
			return;
		}
		String text;
		if (gameBoard.whiteToMove()) {
			text = "White offers a draw.";
		} else {
			text = "Black offers a draw.";
		}
		AlertDialog ad = new AlertDialog.Builder(this)
	    .setTitle("Draw?")
	    .setMessage(text + " Do you accept?")
	    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
	        public void onClick(DialogInterface dialog, int which) { 
	            gameOver(2, "By mutual agreement:");
	        }
	     })
	    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
	        public void onClick(DialogInterface dialog, int which) { 

	        }
	     })
	    .setIcon(android.R.drawable.ic_dialog_alert)
	    .create();
		Window window = ad.getWindow();
//		WindowManager.LayoutParams wlp = window.getAttributes();
//		wlp.gravity = Gravity.BOTTOM;
//		wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
//		window.setAttributes(wlp);
		window.setGravity(Gravity.BOTTOM);
		window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
		ad.show();
	}
	
	/**
	 * Have the AI pick a random move for the current player and execute it.
	 * @param view the View calling this function
	 */
	public void aiMove(View view) {
		if (versusAi) {
			deleteFile(UNDO_NAME);
			findViewById(R.id.button_undo).setEnabled(false);
		} else {
			saveBoard(UNDO_NAME);
			findViewById(R.id.button_undo).setEnabled(true);
		}
		Move m = gameBoard.randomMove();
		if (m == null) {
			throw new RuntimeException("No legal moves possible, yet we did not stalemate.");
		}
		moveAttempted(true);
		if (view != null && versusAi) {
			// this was called by a button push
			queueAiMove();
		}
	}
	
	/**
	 * Resign from the game
	 * @param view the View calling this function
	 */
	public void resign(View view) {
		if (gameBoard.whiteToMove()) {	
			gameOver(1, "White resigns.");
		} else {
			gameOver(0, "Black resigns.");
		}
	}

	
	private void moveAttempted(boolean legal) {
		String msg = null;
		Toast toast = null;
		if (legal) {
			String state = gameBoard.toString();
			try {
				fileOutput.write(state.getBytes());
			} catch (IOException e) {
				e.printStackTrace();
			}
			bFrag.drawBoard(state);
			gameBoard.setDrawOffered(false);
			updateTurnText();
			switch (gameBoard.getCondition(true, true, true)) {
			case NONE:
				break;
			case W_IN_CHECK:
			case B_IN_CHECK:
				msg = "Check!";
				break;
			case W_MATED:
				msg = "Checkmate!";
				gameOver(1, msg);
				break;
			case B_MATED:
				msg = "Checkmate!";
				gameOver(0, msg);
				break;
			default:
				break;
			}
			if (gameBoard.inStalemate(gameBoard.whiteToMove())) {
				msg = "Stalemate.";
				gameOver(2, msg);
			}
		} else {
			msg = "Illegal Move!";
		}
		
		if (msg != null) {
			toast = Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT);
			toast.setGravity(Gravity.CENTER, 0, 0);
			toast.show();
		}
	}
	
	private void queueAiMove() {
		bFrag.setEnabled(false);
		findViewById(R.id.button_undo).setEnabled(false);
		findViewById(R.id.button_ai_move).setEnabled(false);
		findViewById(R.id.button_draw).setEnabled(false);
		findViewById(R.id.button_resign).setEnabled(false);
		if (gameBoard.whiteToMove()) {
			updateTurnText("White AI is thinking...");
		} else {
			updateTurnText("Black AI is thinking...");
		}
		handler.postDelayed(new ComputerMove(this), 1500);
	}
	
	/**
	 * Board fragment calls this function to indicate that a HUMAN (not an AI) is attempting
	 * to make a certain move.
	 */
	@Override
	public void onMove(int orow, int ocol, int nrow, int ncol, ChessType ct) {
		Move m = new Move(orow, ocol, nrow, ncol, ct);
		boolean legal = gameBoard.doMove(m, 2);
		if (!versusAi && legal) {
			saveBoard(UNDO_NAME);
			findViewById(R.id.button_undo).setEnabled(true);
		}
		if (legal) {
			gameBoard.doMove(m, 0);
		}
		moveAttempted(legal);
		if (versusAi && legal) {
			queueAiMove();
		}
	}
	
	@Override
	public boolean canSelect(int row, int col) {
		Piece p = gameBoard.get(row, col);
		if (p == null) {
			return false;
		} else {
			boolean whiteTurn = gameBoard.whiteToMove();
			boolean whitePiece = p.isWhite();
			return whiteTurn == whitePiece;
		}
	}
	
	@Override
	public boolean promotionMove(int orow, int ocol, int nrow, int ncol) {
		Piece p = gameBoard.get(orow, ocol);
		assert (p != null) : "You allowed selection of an empty square...";
		if (p.getType() == ChessType.PAWN) {
			boolean white = p.isWhite();
			return (white && nrow == 8) || (!white && nrow == 1);
		}
		return false;
	}
	
    protected void showSaveDialog(View view) {
        // Create an instance of the dialog fragment and show it
        DialogFragment dialog = new SaveDialogFragment();
        dialog.show(getFragmentManager(), "SaveDialogFragment");
    }

    // The dialog fragment receives a reference to this Activity through the
    // Fragment.onAttach() callback, which it uses to call the following methods
    // defined by the SaveDialogFragment.SaveDialogListener interface
    @Override
	public void onDialogPositiveClick(DialogFragment dialog) {
		Intent recordIntent = new Intent(this, RenameActivity.class);
		recordIntent.putExtra(OpenActivity.FILE_NAME, RECORD_NAME);
		finish();
		startActivity(recordIntent);
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
        // User touched the dialog's negative button
    	deleteFile(RECORD_NAME);
        finish();
    }
	
}
