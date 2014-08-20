/**
 * @author Stephen Chung
 */

package edu.rutgers.cs.chess41;

import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class OpenActivity extends Activity implements Comparator<String> {

	public static final String FILE_NAME = "edu.rutgers.cs.chess41.FILE_NAME";
	private static DateFormat df = DateFormat.getDateInstance();
	
	private ArrayAdapter<String> adapter;
	private ArrayList<String> pruned;
	private HashMap<String, File> hm;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_open);
		
		final Context context = this;
		
		hm = new HashMap<String, File>();
		String[] files = fileList();
		pruned = new ArrayList<String>();
		for(int i = 0; i < files.length; i++) {
			if (files[i].endsWith("tmp")) {
				continue;
			}
			File f = new File(getFilesDir(), files[i]);
			String subDate = new Date(f.lastModified()).toString().substring(0, 20);
			String concat = files[i] + " - " + subDate;
			pruned.add(concat);
			hm.put(concat, f);

		}
	
		ListView listview = (ListView) findViewById(R.id.file_list);
		adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, pruned);
		listview.setAdapter(adapter);
		listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
		
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				Intent intent = new Intent(context, ReplayActivity.class);
				intent.putExtra(FILE_NAME, (String) parent.getItemAtPosition(position));
				startActivity(intent);
			}
		});
		sortByName(null);

	}
	
	@Override
	protected void onStart() {
		super.onStart();
		
		if (adapter == null) {
			throw new RuntimeException("ArrayAdapter for listing files not found!");
		} else {
			adapter.notifyDataSetChanged();
		}
		
	}
	
	/**
	 * Sort all recorded games by title (name).
	 * @param view the View (usually a button) that calls this function
	 */
	public void sortByName(View view) {
		Collections.sort(pruned);
		adapter.notifyDataSetChanged();
	}
	
	/**
	 * Sort all recorded games by last modified date.
	 * @param view
	 */
	public void sortByDate(View view) {
		Collections.sort(pruned, this);
		adapter.notifyDataSetChanged();
	}

	/**
	 * A comparator that enables comparison between two list entries
	 * based on last modified date.
	 */
	@Override
	public int compare(String s1, String s2) {
		File f1, f2;
		f1 = hm.get(s1);
		f2 = hm.get(s2);
		long res = f1.lastModified() - f2.lastModified();
		if (res < 0)
			return -1;
		else if (res == 0)
			return 0;
		else
			return 1;
	}

}
