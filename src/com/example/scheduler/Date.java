package com.example.scheduler;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;

/**
 * A Date object contains its date in mmddyy format, its start time, and its end time.
 * 
 * Still needs: 
 * mmddyy format? 
 * 24 hour times?
 * Can you have a description without a name?
 * Can times be removed individually?
 * 
 * @author BP
 *
 */

public class Date{

	private int startTime; //military time
	private int endTime;
	private int day; //MMDDYYYY
	
	// negative value only if data is not given
	private static final int NONE = -1;
	
	// constructors
	// no due date given
	public Date() {
		startTime = NONE;
		endTime = NONE;
		day = NONE;
	}
	
	// only day given
	public Date(int givenDay) {
		startTime = NONE;
		endTime = NONE;
		day = givenDay;
	}
	
	// all data given
	public Date(int givenStart, int givenEnd, int givenDay) {
		startTime = givenStart;
		endTime = givenEnd;
		day = givenDay;
	}

	// accessors
	public int getStartTime() {
		return startTime;
	}
	
	public int getEndTime() {
		return endTime;
	}
	
	public int getDay() {
		return day;
	}
	
	// mutators
	public void setStartTime(int givenStart) {
		startTime = givenStart;
	}
	
	public void setEndTime(int givenEnd) {
		endTime = givenEnd;
	}
	
	public void setDay(int givenDay) {
		day = givenDay;
	}
	
}
