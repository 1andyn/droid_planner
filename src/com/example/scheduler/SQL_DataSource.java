package com.example.scheduler;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class SQL_DataSource {
	private SQLHelper dbhelper;
	private SQLiteDatabase database;
	private String[] allColumns = {SQLHelper.COLUMN_CON, SQLHelper.COLUMN_ID, SQLHelper.COLUMN_NAME,
			SQLHelper.COLUMN_DESC, SQLHelper.COLUMN_ALARM, SQLHelper.COLUMN_MONTH, SQLHelper.COLUMN_DAY,
			SQLHelper.COLUMN_YEAR, SQLHelper.COLUMN_START,SQLHelper.COLUMN_END};
	
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
	
	public void createEvent(Event e)
	{
		ContentValues values = new ContentValues();
		values.put(SQLHelper.COLUMN_ID, e.GetID());
		values.put(SQLHelper.COLUMN_NAME, e.getName());
		values.put(SQLHelper.COLUMN_DESC, e.getDescription());
		values.put(SQLHelper.COLUMN_ALARM, e.getAlarm());
		values.put(SQLHelper.COLUMN_MONTH, e.GetMonth());
		values.put(SQLHelper.COLUMN_DAY, e.GetDay());
		values.put(SQLHelper.COLUMN_YEAR, e.GetYear());
		values.put(SQLHelper.COLUMN_START, e.GetStart());
		values.put(SQLHelper.COLUMN_END, e.GetEnd());
		
		/* Supposedly adds all values in ContentValues values to database*/
		long insertId = database.insert(SQLHelper.TABLE_NAME, null, values);
		Cursor curse = database.query(SQLHelper.TABLE_NAME, allColumns, 
				SQLHelper.COLUMN_CON + " = " + insertId, null, null, null, null);
		curse.moveToFirst();
		/* Not sure if missing code here is necessary */
		
		
	}
		
	
	
}
