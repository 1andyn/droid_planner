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
	
	private final static String NO_OVERLAP = "N";
	
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
	
	public Event getEvent(long id)
	{
		Event my_event = new Event();
		Cursor curse = database.query(SQLHelper.TABLE_NAME, allColumns, null, null, null, null, SQLHelper.COLUMN_YEAR + " ASC, "
				+ SQLHelper.COLUMN_MONTH + " ASC, " + SQLHelper.COLUMN_DAY + " ASC, " + SQLHelper.COLUMN_END + " ASC");
		curse.moveToFirst();
		while(!curse.isAfterLast())
		{
			Event event = cursorToEvent(curse);
			if(event.GetID() == id)
			{
				return event;
			}
			curse.moveToNext();
		}
		curse.close();
		return my_event;
	}
	
	public ArrayList<Event> getAllEvents()
	{		
		ArrayList<Event> allEvents = new ArrayList<Event>();
		Cursor curse = database.query(SQLHelper.TABLE_NAME, allColumns, null, null, null, null, SQLHelper.COLUMN_YEAR + " ASC, "
				+ SQLHelper.COLUMN_MONTH + " ASC, " + SQLHelper.COLUMN_DAY + " ASC, " + SQLHelper.COLUMN_END + " ASC");
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
	
	public ArrayList<Event> getEventsForDate(Cal_Date d)
	{
		ArrayList<Event> partialEvents = new ArrayList<Event>();
		Cursor curse = database.query(SQLHelper.TABLE_NAME, allColumns, null, null, null, null, SQLHelper.COLUMN_YEAR + " ASC, "
				+ SQLHelper.COLUMN_MONTH + " ASC, " + SQLHelper.COLUMN_DAY + " ASC, " + SQLHelper.COLUMN_END + " ASC");
		curse.moveToFirst();
		while(!curse.isAfterLast())
		{
			Event event = cursorToEvent(curse);
			if(event.GetDate().get_CDate().isEqual(d))
			{
				partialEvents.add(event);
			}
			curse.moveToNext();
		}
		curse.close();
		return partialEvents;
	}
	
	public String overlapExists(Date d)
	{
		String nofaultEvent = NO_OVERLAP;
		ArrayList<Event> coreEvents = getEventsForDate(d.get_CDate());
		
		for(int INDEX = 0; INDEX < coreEvents.size(); INDEX++)
		{
			if(d.overlapDate(coreEvents.get(INDEX).GetDate()))
			{
				return coreEvents.get(INDEX).getName();
			}
		}
		return nofaultEvent;
	}
	
	public String endTimeExists(Date d)
	{
		String nofaultTodo = NO_OVERLAP;
		Cursor curse = database.query(SQLHelper.TABLE_NAME, allColumns, 
				null, null, null ,null, null);
		curse.moveToFirst();
		while(!curse.isAfterLast())
		{
			Event event = cursorToEvent(curse);
			if(d.endTimeEqual(event.GetDate()))
			{
				return event.getName();
			}
			curse.moveToNext();
		}
		curse.close();
		return nofaultTodo;
	}
	
	private Event cursorToEvent(Cursor curs)
	{
		Event newEvent = new Event();
		newEvent.setID(curs.getLong(COL_ID));
		newEvent.setName(curs.getString(COL_NAME));
		newEvent.setDescription(curs.getString(COL_DESC));
		newEvent.setAlarm(curs.getString(COL_ALARM));
		newEvent.setColor(curs.getInt(COL_COL));
		
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
