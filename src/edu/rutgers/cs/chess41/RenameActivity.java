/**
 * @author David DeSimone
 */
package edu.rutgers.cs.chess41;

import java.io.File;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class RenameActivity extends Activity {

	String oldName;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_rename);

		Intent intent = getIntent();
		oldName = intent.getStringExtra(OpenActivity.FILE_NAME);
	}

	public void name(View v) {

		EditText et = (EditText) findViewById(R.id.editText1);
		File target = new File(getFilesDir(), et.getText().toString());
		
		if (target.exists()) {
			Toast toast = Toast.makeText(getApplicationContext(),
					"Title already in use, try again.",
					Toast.LENGTH_SHORT);
			toast.setGravity(Gravity.CENTER, 0, 0);
			toast.show();
			return;
		}
		
		try {
			new File(getFilesDir(), oldName).renameTo(target);
		} catch (Exception e) {
			e.printStackTrace();
		}

		finish();
	}
	
	public void cancel(View v) {
		new AlertDialog.Builder(this)
		    .setTitle("Confirm Exit")
		    .setMessage("Are you sure you want to exit? The recorded game will be lost!")
		    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
		        public void onClick(DialogInterface dialog, int which) { 
		        	deleteFile(oldName);
		            finish();
		        }
		     })
		    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
		        public void onClick(DialogInterface dialog, int which) { 
		        	// do nothing
		        }
		     })
		    .setIcon(android.R.drawable.ic_dialog_alert)
		    .create()
		    .show();
	}

}
