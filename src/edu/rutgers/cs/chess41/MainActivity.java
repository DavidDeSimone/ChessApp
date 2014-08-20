/**
 * @author Stephen Chung
 */

package edu.rutgers.cs.chess41;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	@Override
	protected void onStart() {
		super.onStart();
		enableButtons(true);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return false;
	}

	private void enableButtons(boolean b) {
		findViewById(R.id.button_new_game).setEnabled(b);
		findViewById(R.id.button_new_ai_game).setEnabled(b);
		findViewById(R.id.button_load).setEnabled(b);	
	}
	
	/**
	 * Start a new game, either two humans or a human vs an AI.
	 * @param v the view calling this function
	 */
	public void launchGame(View v) {
		final Intent intent = new Intent(this, GameActivity.class);
		switch(v.getId()) {
			case R.id.button_new_game:
				intent.putExtra(GameActivity.VERSUS_AI, false);
				startActivity(intent);
				break;
			case R.id.button_new_ai_game:

				intent.putExtra(GameActivity.VERSUS_AI, true);
				new AlertDialog.Builder(this)
				    .setTitle("Select Color")
				    .setMessage("Which color would you like to play as?")
				    .setPositiveButton(R.string.white, new DialogInterface.OnClickListener() {
				        public void onClick(DialogInterface dialog, int which) {
				        	enableButtons(false);
				        	intent.putExtra(GameActivity.HUMAN_IS_WHITE, true);
				        	startActivity(intent);
				        }
				     })
				    .setNegativeButton(R.string.black, new DialogInterface.OnClickListener() {
				        public void onClick(DialogInterface dialog, int which) {
				        	enableButtons(false);
				        	intent.putExtra(GameActivity.HUMAN_IS_WHITE, false);
				        	startActivity(intent);
				        }
				     })
				    .setIcon(android.R.drawable.ic_dialog_alert)
				    .create()
				    .show();
				
				break;
			default:
				throw new RuntimeException("Unknown button ID");
		}
	}
	
	/**
	 * Browse the list of recorded games.
	 * @param v the View calling this function
	 */
	public void launchOpen(View v) {
		Intent intent = new Intent(this, OpenActivity.class);
		startActivity(intent);
	}


}
