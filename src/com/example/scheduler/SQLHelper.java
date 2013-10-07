package com.example.scheduler;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLHelper extends SQLiteOpenHelper {
	
	/* Class used for Getting Email Data*/
	private final Email_Fetcher e_f = new Email_Fetcher();
	private String identifier;
	
	/* This is just a Skeleton Class, Needs to be Updated */
	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_NAME = "name";
	public static final String COLUMN_DESC = "desc";
	public static final String COLUMN_ALARM = "alarm";
	public static final String COLUMN_MONTH = "month";
	public static final String COLUMN_DAY = "day";
	public static final String COLUMN_YEAR = "year";
	public static final String COLUMN_START = "start_time";
	public static final String COLUNMN_END = "end_time";
	
	
    
	private static final int DATABASE_VERSION = 2;
    private static final String TABLE_NAME = "USREVENTS";
	private final static String DATABASE_NAME = "Events.db";
	
	
	
    SQLHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DICTIONARY_TABLE_CREATE);
    }

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}
}
