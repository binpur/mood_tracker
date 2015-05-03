package com.example.gtqq;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MoodsDatabase extends SQLiteOpenHelper
{
	public static final String TAG = "MoodsDatabase";
    public static final String MOODS_TABLE_NAME = "moods";
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "gtqq.db";
    // key name
    public static final String KEY_ID = "_id";
	// ex 31/05/2014
    public static final String KEY_DATE = "date";
    // Monday,Tuesday...,Sunday
	public static final String KEY_DAY_OF_WEEK = "day";
	public static final String KEY_DAY_OF_WEEK_STRING = "day_string";
	// num week
	public static final String KEY_WEEK = "week";
	public static final String KEY_MOOD_TYPE = "mood";
	public static final String KEY_COMMENT = "comment";
	//context
	private Context context;
	
	private static final String MOODS_TABLE_CREATE = "CREATE TABLE " +
			MOODS_TABLE_NAME + " (" 
			+ KEY_ID + " INTEGER PRIMARY KEY NOT NULL,"
			+ KEY_DATE + " CHAR(16) NOT NULL,"
			+ KEY_DAY_OF_WEEK + " INTEGER NOT NULL,"
			+ KEY_DAY_OF_WEEK_STRING + " CHAR(16) NOT NULL,"
			+ KEY_WEEK + " INTEGER NOT NULL,"
			+ KEY_MOOD_TYPE + " INTEGER NOT NULL,"
			+ KEY_COMMENT + " TEXT NOT NULL);";
	
	//constructor
	public MoodsDatabase(Context context) 
	{
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		this.context = context;
	}

	@Override
	public void onCreate(SQLiteDatabase db) 
	{
		Log.v(TAG,"onCreate");
		db.execSQL(MOODS_TABLE_CREATE);
		Log.v(TAG,MOODS_TABLE_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) 
	{
		db.execSQL("DROP TABLE IF EXISTS " + MOODS_TABLE_NAME);
		onCreate(db);
	}
	
	public static String getDayOFWeekString(int dayOfWeek) {
		switch(dayOfWeek) {
		case 1: return "Sunday";
		case 2: return "Monday";
		case 3: return "Tuesday";
		case 4: return "Wednesday";
		case 5: return "Thursday";
		case 6: return "Friday";
		case 7: return "Saturday";
		default: return "";
		}
	}
	
    public void addMood(Mood mood) 
    {
    	Log.v(TAG,"addMood(), date="+mood.getDate()+"day="+mood.getDay()+"week="+mood.getWeek()+"moodType="+mood.getMoodType()+", comment="+mood.getComment());
    	SQLiteDatabase database = this.getWritableDatabase();
		String [] args = null;
		Cursor cursor = database.rawQuery("SELECT * FROM " + MOODS_TABLE_NAME, args);
		int nbLine = cursor.getCount();
        if (nbLine >= 100) 
        {
        	database.execSQL("DELETE FROM news WHERE _id = (SELECT _id FROM moods ORDER BY _id LIMIT 1)");
        	Log.i("test delete", "size > = 100");
        }
        cursor.close();
        ContentValues values = new ContentValues();
        values.put(KEY_DATE, mood.getDate());
        values.put(KEY_DAY_OF_WEEK, mood.getDay());
        values.put(KEY_DAY_OF_WEEK_STRING, getDayOFWeekString(mood.getDay()));
        values.put(KEY_WEEK, mood.getWeek());
        values.put(KEY_MOOD_TYPE, mood.getMoodType());
        values.put(KEY_COMMENT, mood.getComment());
        if(database.insert(MOODS_TABLE_NAME, null, values) != -1) 
        {
        	Log.i("insert good","size = "+nbLine);
        }
        else {
        	Log.i("test nbLine ","size = "+nbLine);
        }
        database.close();
    }
    
	public Mood getMood(Cursor cursor) 
	{
		if (cursor.getCount() <= 0) 
		{
			Log.i("test","cursor size = "+cursor.getCount());
			return null;
		}
		cursor.moveToFirst();
		Mood mood = new Mood(cursor.getInt(0),cursor.getString(1),cursor.getInt(2),cursor.getString(3),cursor.getInt(4),cursor.getInt(5),cursor.getString(6));
		cursor.close();
		return mood;
	}
	
	//[0]-awesome,[1]-good,...
	public int [] getMoodsOfAWeek(int weekNum)
	{
    	SQLiteDatabase database = this.getWritableDatabase();
		String [] args = null;
		Cursor cursor = database.rawQuery("SELECT "+KEY_MOOD_TYPE+" FROM " + MOODS_TABLE_NAME +" WHERE "+KEY_WEEK+" = "+weekNum, args);
		Log.v("test","SELECT "+KEY_MOOD_TYPE+" FROM " + MOODS_TABLE_NAME +" WHERE "+KEY_WEEK+" = "+weekNum);
		if (cursor.getCount() <= 0) 
		{
			return null;
		}
		else
		{
			int counterMood1 = 0,counterMood2 = 0,counterMood3 = 0,counterMood4 = 0,counterMood5 = 0;
			while(cursor.moveToNext()) 
			{
				switch(cursor.getInt(0))
				{
				case Constants.MOOD_AWESOME:counterMood1++;break;
				case Constants.MOOD_GOOD:counterMood2++;break;
				case Constants.MOOD_OK:counterMood3++;break;
				case Constants.MOOD_BAD:counterMood4++;break;
				case Constants.MOOD_TERRIBLE:counterMood5++;break;
				}
			}
			int [] moods = {counterMood1,counterMood2,counterMood3,counterMood4,counterMood5};
			cursor.close();
			return moods;
		}
	}
    
    public Cursor query(String sql,String[] args) 
    {
    	SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(sql, args);
        return cursor;
    }
}
