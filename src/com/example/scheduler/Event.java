package com.example.scheduler;

public class Event{
	private final String NONE = null;
	private final String HEX_WHITE = "FFFFFF";
	private final int EMPTY = 0;
	
	private int eventID;
	private String eventName;
	private String eventDesc;
	private String eventColor;
	private Date eventDate;
	private boolean alarm;

	/* Constructor */
	public Event()
	{
		eventID = EMPTY;
		eventName = null;
		eventDesc = null;
		eventColor = HEX_WHITE;
		eventDate = new Date();
		alarm = false;
	}

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
	

