package com.example.gtqq;

public class NewsItem 
{
	private String date;		//date of publication
	private String title;		//news title
	private String summary;		//news summary
	private String content;		//news content
	
	public NewsItem(String date, String title, String summary, String content) 
	{
		super();
		this.date = date;
		this.title = title;
		this.summary = summary;
		this.content = content;
	}

	public String getDate() 
	{
		return date;
	}

	public void setDate(String date) 
	{
		this.date = date;
	}

	public String getTitle() 
	{
		return title;
	}

	public void setTitle(String title) 
	{
		this.title = title;
	}

	public String getSummary() 
	{
		return summary;
	}

	public void setSummary(String summary) 
	{
		this.summary = summary;
	}

	public String getContent() 
	{
		return content;
	}

	public void setContent(String content) 
	{
		this.content = content;
	}
	
}
