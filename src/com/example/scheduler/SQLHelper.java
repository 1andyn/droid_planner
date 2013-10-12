package com.example.scheduler;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class SQLHelper extends SQLiteOpenHelper {
		
	///* This is just a Skeleton Class, Needs to be Updated */
	public static final String COLUMN_USR = "user";
	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_NAME = "name";
	public static final String COLUMN_DESC = "desc";
	public static final String COLUMN_ALARM = "alarm";
	public static final String COLUMN_MONTH = "month";
	public static final String COLUMN_DAY = "day";
	public static final String COLUMN_YEAR = "year";
	public static final String COLUMN_START = "start_time";
	public static final String COLUMN_END = "end_time";
	public static final String COLUMN_COLOR = "color";
	 
	private static final int DATABASE_VERSION = 2;
    public static final String TABLE_NAME = "USREVENTS";
	private final static String DATABASE_NAME = "Events.db";
		
	private static final String DATABASE_CREATE = "CREATE TABLE "
		      + TABLE_NAME 
		      + "(" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
		      + COLUMN_NAME + " TEXT NOT NULL, "
		      + COLUMN_DESC + " TEXT, "
		      + COLUMN_ALARM + " TEXT NOT NULL, "
		      + COLUMN_MONTH + " INT NOT NULL, "
		      + COLUMN_DAY + " INT NOT NULL, "
		      + COLUMN_YEAR + " INT NOT NULL, "
		      + COLUMN_START + " INT NOT NULL, "
		      + COLUMN_END  + " INT, " 
		      + COLUMN_COLOR + "TEXT NOT NULL);";
	
	  public SQLHelper(Context context) {
		    super(context, DATABASE_NAME, null, DATABASE_VERSION);
		  }
	
	  @Override
	  public void onCreate(SQLiteDatabase database) {
	    database.execSQL(DATABASE_CREATE);
	  }
	
	  @Override
	  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	    Log.w(SQLHelper.class.getName(),
	        "Upgrading database from version " + oldVersion + " to "
	            + newVersion + ", which will destroy all old data");
	    db.execSQL("DROP TABLE IF EXISTS " + COLUMN_USR);
	    onCreate(db);
	  }
}
