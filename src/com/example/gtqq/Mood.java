package com.example.gtqq;

import android.graphics.Color;

public class Mood 
{
	private int id;
	private String date;
	/* 1-Sunday,2-Monday,...,7-Saturday */
	private int day;
	private String dayString;
	private int week;
	/* 0-awesome,1-good,2-ok,3-bad,4-terrible */
	private int moodType;
	private String comment;
	
	public Mood(int id, String date, int day, String dayString, int week, int moodType, String comment) 
	{
		this.id = id;
		this.moodType = moodType;
		this.date = date;
		this.day = day;
		this.dayString = dayString;
		this.setWeek(week);
		this.comment = comment;
	}
	
	public Mood(String date, int day, String dayString, int week, int moodType, String comment) 
	{
		this.moodType = moodType;
		this.date = date;
		this.day = day;
		this.dayString = dayString;
		this.setWeek(week);
		this.comment = comment;
	}
	
	public static int getMoodColor(int idMood) {
		switch(idMood) {
		case Constants.MOOD_AWESOME: return Color.YELLOW;
		case Constants.MOOD_GOOD: return Color.parseColor("#9bcf01");
		case Constants.MOOD_OK: return Color.parseColor("#ae69d1");
		case Constants.MOOD_BAD: return Color.parseColor("#33b5e5");
		case Constants.MOOD_TERRIBLE: return Color.parseColor("#e75b5b");
		default: return 0;
		}
	}
	
	public static int getMoodSmily(int idMood) {
		switch(idMood) {
		case Constants.MOOD_AWESOME: return R.drawable.awesome;
		case Constants.MOOD_GOOD: return R.drawable.good;
		case Constants.MOOD_OK: return R.drawable.ok;
		case Constants.MOOD_BAD: return R.drawable.bad;
		case Constants.MOOD_TERRIBLE: return R.drawable.terrible;
		default: return 0;
		}
	}
	
	public int getId() 
	{
		return id;
	}
	public void setId(int id) 
	{
		this.id = id;
	}
	public int getMoodType() 
	{
		return moodType;
	}
	public void setMoodType(int moodType) 
	{
		this.moodType = moodType;
	}
	public String getDate() 
	{
		return date;
	}
	public void setDate(String date) 
	{
		this.date = date;
	}
	public int getDay() 
	{
		return day;
	}
	public void setDay(int day) 
	{
		this.day = day;
	}
	public String getComment() 
	{
		return comment;
	}
	public void setComment(String comment) 
	{
		this.comment = comment;
	}

	public int getWeek() {
		return week;
	}

	public void setWeek(int week) {
		this.week = week;
	}

	public String getDayString() {
		return dayString;
	}

	public void setDayString(String dayString) {
		this.dayString = dayString;
	}
	
}
