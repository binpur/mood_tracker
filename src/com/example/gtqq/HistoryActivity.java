package com.example.gtqq;

import java.util.Calendar;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.support.v4.widget.SimpleCursorAdapter;

public class HistoryActivity extends Activity 
{
	private MoodsDatabase database;
	private SimpleCursorAdapter adapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.history_layout);
		Intent intent = getIntent();
		int mood = intent.getIntExtra("moodId",0);
		Log.v("test","moodid = "+mood);
		database = new MoodsDatabase(this);
		ListView listView = (ListView) findViewById(R.id.moodListView);
		database.getReadableDatabase();
		Cursor cursor = database.query("SELECT * FROM "+MoodsDatabase.MOODS_TABLE_NAME+" ORDER BY _id DESC", null);
		adapter = new MyCursorAdapter(this,R.layout.list_item,cursor,new String[]{MoodsDatabase.KEY_DAY_OF_WEEK_STRING,MoodsDatabase.KEY_DATE},new int[]{R.id.day,R.id.date});
		listView.setAdapter(adapter);
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	public boolean onOptionsItemSelected(MenuItem item) 
	{
		switch (item.getItemId()) 
		{
		case R.id.statistic:
			Calendar calendar = Calendar.getInstance();
			int week = calendar.get(Calendar.WEEK_OF_YEAR);
			int [] moodsOfTheWeek = database.getMoodsOfAWeek(week);
			Intent intent = new Intent().setClass(this,PieChartBuilder.class);
			intent.putExtra("mood_of_the_week", moodsOfTheWeek);
			startActivity(intent);
			Log.v("test","statistic");
			return true;
		default:return false;
		}
	}
	
	class MyCursorAdapter extends SimpleCursorAdapter {

		private Context context;
//		private Cursor cursor;
		public MyCursorAdapter(Context context, int layout, Cursor c,
				String[] from, int[] to) {
			super(context, layout, c, from, to);
			this.context = context;
			// TODO Auto-generated constructor stub
		}
		
	    public View getView(int position, View convertView, ViewGroup parent) {
	    	convertView = LayoutInflater.from(context).inflate(  
                    R.layout.list_item, null);
	    	getCursor().moveToPosition(position);
	    	int moodType = getCursor().getInt(5);
	    	ImageView icon = (ImageView)convertView.findViewById(R.id.imageMoodSmall);
	    	icon.setImageResource(Mood.getMoodSmily(moodType));
	    	convertView.setBackgroundColor(Mood.getMoodColor(moodType));
	    	TextView date = (TextView)convertView.findViewById(R.id.date);
	    	TextView day = (TextView)convertView.findViewById(R.id.day);
	    	date.setText(getCursor().getString(1));
	    	day.setText(getCursor().getString(3));
            return convertView;
	    }
		
	}

}