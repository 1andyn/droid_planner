package com.example.scheduler;

import java.util.Arrays;

import android.os.Parcel;
import android.os.Parcelable;

public class Cal_Date implements Parcelable{
	
	final int SIZE_OF_ARRAY = 3;
	final int MONTH_INDEX = 0;
	final int DATE_INDEX = 1;
	final int YEAR_INDEX = 2;
	final int EMPTY = -1;
	
	private int[] cal_ARRAY;
	
	/* Main Constructor */
	public Cal_Date()
	{
		cal_ARRAY = new int[SIZE_OF_ARRAY];
		Arrays.fill(cal_ARRAY, EMPTY); /* Sets all values to EMPTY */
	}
	
	public Cal_Date(int m, int d, int y)
	{
		cal_ARRAY = new int[SIZE_OF_ARRAY];
		cal_ARRAY[MONTH_INDEX] = m;
		cal_ARRAY[DATE_INDEX] = d;
		cal_ARRAY[YEAR_INDEX] = y;
	}
	
	public Cal_Date(Parcel in)
	{
	     readFromParcel(in);
	}
	
	/* Accessors */ 
	public int get_day()
	{
		return cal_ARRAY[DATE_INDEX];
	}
	
	public int get_month()
	{
		return cal_ARRAY[MONTH_INDEX];
	}
	
	public int get_year()
	{
		return cal_ARRAY[YEAR_INDEX];
	}
	
	/* Mutators */
	public void set_day(int d)
	{
		cal_ARRAY[DATE_INDEX] = d;
	}
	public void set_month(int m)
	{
		cal_ARRAY[MONTH_INDEX] = m;
	}
	public void set_year(int y)
	{
		cal_ARRAY[YEAR_INDEX] = y;
	}
	
	public String dateToString()
	{
		String dateString = " " + cal_ARRAY[DATE_INDEX] + ", " + cal_ARRAY[YEAR_INDEX];
		return dateString;
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	/* Can probably use 1 line of code for this */
	@Override
	public void writeToParcel(Parcel dest, int flag) 
	{
		dest.writeInt(cal_ARRAY[MONTH_INDEX]);
		dest.writeInt(cal_ARRAY[DATE_INDEX]);
		dest.writeInt(cal_ARRAY[YEAR_INDEX]);
	}


    public void readFromParcel(Parcel in)
    {
        this.cal_ARRAY[MONTH_INDEX] = in.readInt();
        this.cal_ARRAY[DATE_INDEX] = in.readInt();
        this.cal_ARRAY[YEAR_INDEX] = in.readInt();
    }

    @SuppressWarnings("unchecked")
    public static final Parcelable.Creator CREATOR = new Parcelable.Creator()
    {
        @Override
        public Cal_Date createFromParcel(Parcel in)
        {
            return new Cal_Date(in);
        }

        @Override
        public Object[] newArray(int size)
        {
            return new Cal_Date[size];
        }
    };
	
}
