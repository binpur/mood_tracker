package com.example.gtqq;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class MainActivity extends Activity 
{
	private Context context;
	private int currentMood;
	private MoodsDatabase database;
	private static final int SWIPE_MIN_DISTANCE = 120;
	private static final int SWIPE_MAX_OFF_PATH = 250;
	private static final int SWIPE_THRESHOLD_VELOCITY = 500;
	private GestureDetector gestureDetector;
	private View.OnTouchListener gestureListener;
	private RelativeLayout relativeLayout;
	private ImageButton buttonAdd;
	private ImageView [] imageMoods;
	private ImageView moodBackground;
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		context = this;
		database = new MoodsDatabase(this);
		database.getReadableDatabase();
		relativeLayout = (RelativeLayout)findViewById(R.id.relativeLayout);
		buttonAdd = (ImageButton)findViewById(R.id.buttonAdd);
		imageMoods = new ImageView [5];
		imageMoods[0] = (ImageView)findViewById(R.id.imageMood);
		imageMoods[0].setImageResource(R.drawable.right);
		imageMoods[1] = (ImageView)findViewById(R.id.imageMood1);
		imageMoods[2] = (ImageView)findViewById(R.id.imageMood2);
		imageMoods[3] = (ImageView)findViewById(R.id.imageMood3);
		imageMoods[4] = (ImageView)findViewById(R.id.imageMood4);
		moodBackground = (ImageView)findViewById(R.id.mood_background);
		buttonAdd.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{
				// TODO Auto-generated method stub
				SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss dd-MM-yyyy");
				Calendar calendar = Calendar.getInstance();
				String date = dateFormat.format(calendar.getTime());
				Log.i("test","date = "+date);
				int day = calendar.get(Calendar.DAY_OF_WEEK);
				int week = calendar.get(Calendar.WEEK_OF_YEAR);
				Mood mood = new Mood(date, day, MoodsDatabase.getDayOFWeekString(day), week, currentMood, "comment");
				database.addMood(mood);
				Intent intent = new Intent(context,HistoryActivity.class);
				intent.putExtra("moodId", currentMood);
				startActivity(intent);
			}
		});
		for(int i = 0;i<5;i++)
		{
			imageMoods[i].setOnClickListener(new SelectMoodButtonOnClickListener());
		}
		gestureDetector = new GestureDetector(this, new MyGestureDetector());
		gestureListener = new View.OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				if (gestureDetector.onTouchEvent(event)) {
					return true;
				}
				return false;
			}
		};
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

	public void resetMood(int idMood) {
		imageMoods[idMood].setImageResource(R.drawable.point);
	}
	
	public int leftMood(int idMood) {
		if(idMood == 0)
			return 4;
		else
			return idMood - 1;
	}
	
	public int rightMood(int idMood) {
		if(idMood == 4)
			return 0;
		else
			return idMood + 1;
	}
	
	class SelectMoodButtonOnClickListener implements OnClickListener 
	{
		@Override
		public void onClick(View v) 
		{
			// TODO Auto-generated method stub
			resetMood(currentMood);
			switch(v.getId()) {
			case R.id.imageMood:
				currentMood = Constants.MOOD_AWESOME;
				relativeLayout.setBackgroundColor(Color.YELLOW);
				imageMoods[0].setImageResource(R.drawable.right);
				break;
			case R.id.imageMood1:
				currentMood = Constants.MOOD_GOOD;
				relativeLayout.setBackgroundColor(Color.GREEN);
				imageMoods[1].setImageResource(R.drawable.right);
				break;
			case R.id.imageMood2:
				currentMood = Constants.MOOD_OK;
				relativeLayout.setBackgroundColor(Color.MAGENTA);
				imageMoods[2].setImageResource(R.drawable.right);
				break;
			case R.id.imageMood3:
				currentMood = Constants.MOOD_BAD;
				relativeLayout.setBackgroundColor(Color.BLUE);
				imageMoods[3].setImageResource(R.drawable.right);
				break;
			case R.id.imageMood4:
				currentMood = Constants.MOOD_TERRIBLE;
				relativeLayout.setBackgroundColor(Color.RED);
				imageMoods[4].setImageResource(R.drawable.right);
				break;
			}
			moodBackground.setImageResource(Mood.getMoodSmily(currentMood));
		}
	}
	
	class MyGestureDetector extends SimpleOnGestureListener {
		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
				float velocityY) {
			try {
				if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH)
					return false;
				// right to left swipe
				if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE
						&& Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
					resetMood(currentMood);
					currentMood = rightMood(currentMood);
					relativeLayout.setBackgroundColor(Mood.getMoodColor(currentMood));
					moodBackground.setImageResource(Mood.getMoodSmily(currentMood));
					imageMoods[currentMood].setImageResource(R.drawable.right);
				} else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE
						&& Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
					resetMood(currentMood);
					currentMood = leftMood(currentMood);
					relativeLayout.setBackgroundColor(Mood.getMoodColor(currentMood));
					moodBackground.setImageResource(Mood.getMoodSmily(currentMood));
					imageMoods[currentMood].setImageResource(R.drawable.right);
				}
			} catch (Exception e) {
			}
			return false;
		}
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {
		if (gestureDetector.onTouchEvent(event)) {
			event.setAction(MotionEvent.ACTION_CANCEL);
		}
		return super.dispatchTouchEvent(event);
	}

}
