package com.example.scheduler;

import java.util.ArrayList;

/**
 * A Container object manages all events, and has the ability to repeat them.
 * 
 * Still needs: 
 * How are we getting event's index number?
 * 
 * @author BP
 *
 */

public class EventList{
	
	private ArrayList<Event> allEvents;
	
//	public void repeatEvents(int eventNum, ArrayList<Date> givenDates) {
//		for(int i = 0; i < givenDates.size(); i++) {
//			Date nextDate = givenDates.get(i);			// add each date to the event individually
//			allEvents.get(eventNum).addDate(nextDate);
//		}
//	}
	
	public void getEvent(int eventNum) {
		allEvents.get(eventNum);
	}
	
	public void addEvent(Event givenEvent) {
		allEvents.add(givenEvent);
	}
	
	public void deleteEvent(int eventNum) {
		allEvents.remove(eventNum);
	}
}
