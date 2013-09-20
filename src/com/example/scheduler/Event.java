package com.example.scheduler;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;

/**
 * An Event object contains its name, description, list of dates and alarms, and its background color.
 * 
 * Still needs: 
 * Not sure how to implement alarm?? Each date has an alarm instead?? Or alarms implemented in container?
 * Color in string or char?
 * Can you have a description without a name?
 * 
 * @author BP
 *
 */

public class Event{
	
	private String eventName;	// add name and desc to Event class instead
	private String eventDesc;
	private ArrayList<Date> eventDates;	// not an array
	private ArrayList<Date> eventAlarms; // not an array
	private String eventColor;	// char or string??
	
	private final String NONE = "null";
	// constructors
	
	// accessors
	public String getName() {
		return eventName;
	}
	
	public String getDescription() {
		return eventDesc;
	}
	
	// allows you to get event by date or individual time, if time is needed??
	public Date getEvent(int givenDate) {
		// search for event on specific date			// what happens if there's two in one day?
														// make it an arraylist if two in one day?
		for(int i = 0; i < eventDates.size(); i++) {
			if(eventDates.get(i).getDay() == givenDate)
				return eventDates.get(i);
		}
		
		Date noneFound = new Date();
		return noneFound;
		
	}
	
	public Date getEvent(int givenDate, int givenTime) {
		// search for events on given day AND time
		for(int i = 0; i < eventDates.size(); i++) {
			if(eventDates.get(i).getDay() == givenDate) {	// if date matches, check time
				Date possibleFind = eventDates.get(i);
				if(givenTime > possibleFind.getStartTime() && givenTime < possibleFind.getEndTime())
					return possibleFind;
			}
		}
		Date noneFound = new Date();
		return noneFound;
		
	}
	
	public Date getAlarm() {
		return eventAlarms.get(0);
	}
	
	public String getColor() {
		return eventColor;
	}
	
	// mutators
	public void setName(String givenName) {
		eventName = givenName;
	}
	
	public void setDescription(String givenDesc) {
		eventDesc = givenDesc;
	}
	
	public void addDate(Date givenDate) {
		eventDates.add(givenDate);
	}
	
	public void addAlarm(Date givenAlarm) {
		eventAlarms.add(givenAlarm);
	}
	
	public void setColor(String givenColor) {
		eventColor = givenColor;
	}
	
	// deleters
	public void rmName() {
		eventName = NONE;
	}
	
	// can't have description without a name?
	public boolean rmDescription() {
		if(eventName != NONE)
			return false;
		
		eventDesc = NONE;
		return true;
	}
	
	public void rmDate() {
		eventDates.get(0).rmTimes();
		eventDates.get(0).rmDay();
	}
	
	public void rmAlarm() {
		eventAlarms.get(0).rmTimes();
		eventAlarms.get(0).rmDay();
	}
	
	public void rmColor() {
		eventColor = NONE;
	}
}
