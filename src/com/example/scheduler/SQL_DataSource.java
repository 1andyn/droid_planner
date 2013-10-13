package com.example.scheduler;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class SQL_DataSource {
	
	/* Enumeration for COLUMNS */
	private int COL_ID = 0;
	private int COL_NAME = 1;
	private int COL_DESC = 2;
	private int COL_ALARM = 3;
	private int COL_MONTH = 4;
	private int COL_DAY = 5;
	private int COL_YEAR = 6;
	private int COL_START = 7;
	private int COL_END = 8;
	private int COL_COL = 9;
	
	
	private SQLHelper dbhelper;
	private SQLiteDatabase database;
	private String[] allColumns = { SQLHelper.COLUMN_ID, SQLHelper.COLUMN_NAME, SQLHelper.COLUMN_DESC, 
			SQLHelper.COLUMN_ALARM, SQLHelper.COLUMN_MONTH, SQLHelper.COLUMN_DAY, SQLHelper.COLUMN_YEAR, 
			SQLHelper.COLUMN_START, SQLHelper.COLUMN_END, SQLHelper.COLUMN_COLOR };
	
	public SQL_DataSource(Context context)
	{
		dbhelper = new SQLHelper(context);
	}
	
	public void open() throws SQLException{
		database = dbhelper.getWritableDatabase();
	}
	
	public void close()
	{
		dbhelper.close();
	}
	
	public Event createEvent(Event e)
	{
		ContentValues values = new ContentValues();
		values.put(SQLHelper.COLUMN_NAME, e.getName());
		values.put(SQLHelper.COLUMN_DESC, e.getDescription());
		values.put(SQLHelper.COLUMN_ALARM, e.getAlarm());
		values.put(SQLHelper.COLUMN_MONTH, e.GetMonth());
		values.put(SQLHelper.COLUMN_DAY, e.GetDay());
		values.put(SQLHelper.COLUMN_YEAR, e.GetYear());
		values.put(SQLHelper.COLUMN_START, e.GetStart());
		values.put(SQLHelper.COLUMN_END, e.GetEnd());
		values.put(SQLHelper.COLUMN_COLOR, e.getColor());
		
		/* Supposedly adds all values in ContentValues values to database*/
		long insertId = database.insert(SQLHelper.TABLE_NAME, null, values);
		Cursor curse = database.query(SQLHelper.TABLE_NAME, allColumns, 
				SQLHelper.COLUMN_ID + " = " + insertId, null, null, null, null);
		curse.moveToFirst();
		Event newEvent = cursorToEvent(curse);
		curse.close();
		return newEvent;
	}
		
	public void deleteEvent(Event e)
	{
		long id = e.GetID();
		System.out.println("Comment deleted with id: " + id);
		database.delete(SQLHelper.TABLE_NAME, SQLHelper.COLUMN_ID + " = " + id, null);
	}
	
	public ArrayList<Event> getAllEvents()
	{
		ArrayList<Event> allEvents = new ArrayList<Event>();
		Cursor curse = database.query(SQLHelper.TABLE_NAME, allColumns, 
				null, null, null ,null, null);
		curse.moveToFirst();
		while(!curse.isAfterLast())
		{
			Event event = cursorToEvent(curse);
			allEvents.add(event);
			curse.moveToNext();
		}
		curse.close();
		return allEvents;
	}
	
	
	private Event cursorToEvent(Cursor curs)
	{
		Event newEvent = new Event();
		newEvent.setID(curs.getLong(COL_ID));
		newEvent.setName(curs.getString(COL_NAME));
		newEvent.setDescription(curs.getString(COL_DESC));
		newEvent.setAlarm(curs.getString(COL_ALARM));
		newEvent.setColor(curs.getString(COL_COL));
		
		Date newDate = new Date();
		newDate.setMth(curs.getInt(COL_MONTH));
		newDate.setDay(curs.getInt(COL_DAY));
		newDate.setYr(curs.getInt(COL_YEAR));
		newDate.setStartTime(curs.getInt(COL_START));
		newDate.setEndTime(curs.getInt(COL_END));
		
		newEvent.setDate(newDate);
		return newEvent;
	}
	
	public void clear_table()
	{
		database.delete(SQLHelper.TABLE_NAME, null, null);
		System.out.println("Removed all Table Elements");
	}
	
	
}
