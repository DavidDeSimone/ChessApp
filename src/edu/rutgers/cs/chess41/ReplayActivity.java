/**
 * @author David DeSimone
 */
package edu.rutgers.cs.chess41;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.RandomAccessFile;
import java.util.StringTokenizer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class ReplayActivity extends Activity implements
		BoardFragment.OnMoveListener {

	public static final int size = (64 * 2) + 8;

	private String gameFile;
	private RandomAccessFile board;
	private BoardFragment bFrag;
	private int position = 0;
	private TextView tf;
	private boolean whiteToMove;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_replay);
		Intent intent = getIntent();
		gameFile = intent.getStringExtra(OpenActivity.FILE_NAME);

		StringTokenizer str = new StringTokenizer(gameFile);
		gameFile = str.nextToken();
		loadGame(gameFile);

	}

	protected void onStart() {
		tf = (TextView) findViewById(R.id.whose_turn);
		bFrag = (BoardFragment) getFragmentManager().findFragmentById(
				R.id.board_fragment);
		forward(null);
		findViewById(R.id.button_back).setEnabled(false);
		super.onStart();
	}

	private String getStatus() {
		if (whiteToMove) {
			return "White to Move";
		} else {
			return "Black to Move";
		}
	}

	public void back(View v) {

		try {
			
			assert(position >= 2 * size);
			board.seek(position - 2 * size);
			byte[] bytes = new byte[size];
			board.read(bytes);
			String str = new String(bytes);
			bFrag.drawBoard(str);
			position -= size;

			findViewById(R.id.button_forward).setEnabled(true);
			if (position == size) {
				findViewById(R.id.button_back).setEnabled(false);
			}
			
			whiteToMove = !whiteToMove;
			tf.setText(getStatus());
			
		} catch (Exception e) {

		}

	}

	public void forward(View v) {

		int len;
		
		try {

			byte[] bytes = new byte[size];
			len = board.read(bytes);
			String str = new String(bytes);
			assert (len > 0) : "Forward button should not have been enabled!";
			bFrag.drawBoard(str);
			findViewById(R.id.button_back).setEnabled(true);

			if (position == 0)	// draw initial state, no moves yet
				whiteToMove = true;
			else
				whiteToMove = !whiteToMove;
			
			// Read ahead one step, then rewind the file pointer.
			position += size;
			len = board.read(bytes);
			board.seek(position);

			// Read-ahead reached end of file
			if (len < size) {
				findViewById(R.id.button_forward).setEnabled(false);
				tf.setText(new String(bytes, 1, len - 1));
			} else {
				tf.setText(getStatus());
			}
			
		} catch (Exception e) {

		}

	}

	private boolean loadGame(String filename) {

		try {
			File rec = new File(getFilesDir(), filename);
			board = new RandomAccessFile(rec, "r");
		} catch (FileNotFoundException e) {

		}
		return true;
	}

	@Override
	public void onMove(int orow, int ocol, int nrow, int ncol, ChessType ct) {
		//throw new UnsupportedOperationException();
	}
	
	@Override
	public boolean canSelect(int row, int col) {
		//throw new UnsupportedOperationException();
		return false;
	}
	
	@Override
	public boolean promotionMove(int a, int b, int c, int d) {
		//throw new UnsupportedOperationException();
		return false;
	}

}
