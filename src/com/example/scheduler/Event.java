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
	
	private int eventID;
	private String eventName;
	private String eventDesc;
	private String eventColor;
	private Date eventDate;
	private boolean alarm;

	
	private final String NONE = "null";

	public String getName() 
	{
		return eventName;
	}
	
	public String getDescription() 
	{
		return eventDesc;
	}
	
	public Boolean getAlarm() 
	{
		return alarm;
	}
	
	public String getColor() 
	{
		return eventColor;
	}
	
	public Date GetDate()
	{
		return eventDate;
	}
	
	public int GetStart()
	{
		return eventDate.getStartTime();
	}
	
	public int GetEnd()
	{
		return eventDate.getEndTime();
	}
	
	public int GetDay()
	{
		return eventDate.getDay();
	}
	
	public int GetID()
	{
		return eventID;
	}
	
	
	// mutators
	public void setName(String givenName) {
		eventName = givenName;
	}
	
	public void setDescription(String givenDesc) {
		eventDesc = givenDesc;
	}
	
	public void setDate(Date input)
	{
		eventDate.setDay(input.getDay());
		eventDate.setStartTime(input.getStartTime());
		eventDate.setEndTime(input.getEndTime());
	}
	
	public void setAlarm(Boolean flag) {
		alarm = flag;
	}
	
	public void setColor(String givenColor) {
		eventColor = givenColor;
	}
	
	public void setID(int id)
	{
		eventID = id;
	}
	
}
	

