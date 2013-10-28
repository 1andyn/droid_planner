package com.example.scheduler;

public class Event{
	private final int INT_WHITE = 16777215;
	private final int EMPTY = 0;
	private final String NO_REP = "NNNNNNN";
	
	private long eventID;
	private String eventName;
	private String eventDesc;
	private int eventColor;
	private Date eventDate;
	private String alarm;
	private String rep_STRING;

	/* Constructor */
	public Event(long id, String name)
	{
		eventID = id;
		eventName = name;
		eventDesc = null;
		eventColor = INT_WHITE;
		eventDate = new Date();
		rep_STRING = NO_REP;
		alarm = "N";
	}
	
	
	public Event()
	{
		eventID = EMPTY;
		eventName = null;
		eventDesc = null;
		eventColor = INT_WHITE;
		eventDate = new Date();
		rep_STRING = NO_REP;
		alarm = "N";
	}

	public String getName() 
	{
		return eventName;
	}
	
	public String getDescription() 
	{
		return eventDesc;
	}
	
	public String getAlarm() 
	{
		return alarm;
	}
	
	public int getColor() 
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
	
	public int GetMonth()
	{
		return eventDate.getMonth();
	}
	
	public int GetYear()
	{
		return eventDate.getYear();
	}
	
	public long GetID()
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
		eventDate.setMth(input.getMonth());
		eventDate.setDay(input.getDay());
		eventDate.setYr(input.getYear());
		eventDate.setStartTime(input.getStartTime());
		eventDate.setEndTime(input.getEndTime());
	}
	
	public void setAlarm(String flag) {
		alarm = flag;
	}
	
	public void setColor(int givenColor) {
		eventColor = givenColor;
	}
	
	public void setID(long id)
	{
		eventID = id;
	}

	public boolean isEqual(Event ev)
	{
		return(ev.GetID() == this.GetID());
	}
	
	public String getRep()
	{
		return rep_STRING;
	}
	
	public void set_Rep(String rep_string)
	{
		rep_STRING = rep_string;
	}
	
}
	

