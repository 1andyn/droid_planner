package com.example.scheduler;

/* Parse Cloud Imports */
import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.sothree.slidinguppanel.SlidingUpPanelLayout.PanelSlideListener;

/* ActionBarSherlock Imports */
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.ActionMode;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.Window;
import com.haarman.listviewanimations.swinginadapters.prepared.ScaleInAnimationAdapter;

import android.net.Uri;
/* Basic Android Imports*/
import android.os.Bundle;
import android.os.Handler;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.text.method.LinkMovementMethod;
import android.util.DisplayMetrics;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewStub;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Schedule extends SherlockFragmentActivity {
	
	/** Get email for SQLite DB name */
    private final Email_Module email_MODULE = new Email_Module();
    private String identifier;
    
    private final static String BUGFIX = "1337";
    
    /* DPI Metrics */
    private int REL_SWIPE_MIN_DISTANCE; 
    private int REL_SWIPE_MAX_OFF_PATH;
    private int REL_SWIPE_THRESHOLD_VELOCITY;
    
    /* ENUMERATION FOR TD/EVENT */
    private final static int EVENT_CASE = 0;
    private final static int TODO_CASE = 1;
    private final static int MONTH_CASE = 2;
    private final static int EDT_TODO_CASE = 3;
    private final static int EDT_EVENT_CASE = 4;
    
    private final static int TodoPanelHeight = 75;
    //private final static int CLEAR_CASE = 0;
    
    /* TODO SIZE CASES */
    private final static int EMPTY = 0;
    private final static int NONE = -1;
    private final static long NONE_L = -1;

	/* Data Structures */
	protected ArrayList<Event> events_visible;
	protected EventListAdapter e_adapter;
	
	private TextView sliderText;
	private static int FORCED_DELAY_ANIMA = 175;
	
	protected ListView e_listview; /* Contains list of Views that displays each Event */
	protected ListView t_listview; /* Contains list of Views that displays each ToDo */
	
	protected ArrayList<Event> todos_visible;
	protected ToDoListAdapter t_adapter;
	
	private ScaleInAnimationAdapter e_anima_adapter;
	private ScaleInAnimationAdapter t_anima_adapter;
	
	protected ViewStub empty_todo;
	protected ViewStub empty_events;
	
	protected SlidingUpPanelLayout slidePanel;
	protected RelativeLayout r_Layout;
	
	/*SQL Data Source */
	protected SQL_DataSource datasource;
	
	/* Corresponding View and Events for selection */
	protected View selected_view;
	protected Event selected_event;
	protected Event empty_event = new Event(); // Empty event for null purposes
	
	/* Intents for separate Activities*/
	protected Intent event_INTENT;
	protected Intent todo_INTENT;
	protected Intent month_INTENT;
	
	/* Data for Storing Selected Date */ 
	protected Cal_Date selected_CD;
	protected Cal_Module selected_CM;
	
	/* Intent Codes */
	final static int month_REQUESTCODE = 1;
	final static int REQUEST_CANCELLED = -1;
	final static int RESULT_OK = 1;
	final static String SCHEDULE_DAY = "S_DAY";
	final static String SCHEDULE_MONTH = "S_MON";
	final static String SCHEDULE_YEAR = "S_YR";
	final static String SELECT_KEY = "CURRENT_DATE";
	final static String SELECT_ID_KEY = "SELECT_ID";
	
	private final static String NO_REP = "NNNNNNN";
	
	/* Contextual menu code */
	/** This code is used to open a menu when long-clicking an item */
	protected com.actionbarsherlock.view.ActionMode m_Action; 
	protected com.actionbarsherlock.view.ActionMode.Callback m_ActionCall = new ActionMode.Callback() 
	{
		@Override
		public boolean onCreateActionMode(ActionMode mode, Menu menu) {
			MenuInflater inflater = mode.getMenuInflater();
			inflater.inflate(R.menu.option_menu, menu);
			return true;
		}

		@Override
		public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
			return false;
		}

		@Override
		public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
			switch(item.getItemId()){
			case R.id.menu_remove:
            	Toast.makeText(Schedule.this, "Deleting selection", Toast.LENGTH_SHORT).show();
            	remove_Alarm(selected_event.GetID());
            	if(!selected_event.getRep().equals(NO_REP)){
            		cancel_repAlarm(selected_event);
            	}
            	remove_event(selected_event);
				mode.finish();
				return true;
			case R.id.menu_edit:
            	switch_activity(EDT_EVENT_CASE, selected_event.GetID());
				mode.finish();
				return true;
			default:
				Toast.makeText(Schedule.this, "Canceling..", Toast.LENGTH_SHORT).show();
				return false;
			}
		}

		@Override
		public void onDestroyActionMode(ActionMode mode) {
			m_Action = null;
		}
		
	};
	/* End Contextual menu code */
	
	
	/** This code is used to open a menu when long-clicking an item */
	protected com.actionbarsherlock.view.ActionMode m_Action2; 
	protected com.actionbarsherlock.view.ActionMode.Callback m_ActionCall2 = new ActionMode.Callback() 
	{
		@Override
		public boolean onCreateActionMode(ActionMode mode, Menu menu) {
			MenuInflater inflater = mode.getMenuInflater();
			inflater.inflate(R.menu.option_menu, menu);
			return true;
		}

		@Override
		public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
			return false;
		}

		@Override
		public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
			switch(item.getItemId()){
			case R.id.menu_remove:
            	Toast.makeText(Schedule.this, "Deleting selection", Toast.LENGTH_SHORT).show();
            	remove_Alarm(selected_event.GetID());
            	remove_todo(selected_event);
				mode.finish();
				return true;
			case R.id.menu_edit:
				switch_activity(EDT_TODO_CASE, selected_event.GetID());
				mode.finish();
				return true;
			default:
				Toast.makeText(Schedule.this, "Canceling..", Toast.LENGTH_SHORT).show();
				return false;
			}
		}

		@Override
		public void onDestroyActionMode(ActionMode mode) {
			m_Action2 = null;
		}
		
	};
	
	
	@Override
	public boolean onPrepareOptionsMenu (Menu menu)
	{
		MenuItem date_text = menu.findItem(R.id.tb_date);
		date_text.setTitle(selected_CM.DateString(selected_CD));
		return super.onPrepareOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) 
	{
	    int id = item.getItemId();

	    switch(id){		    
		    case R.id.tb_month:{
		    	switch_activity(MONTH_CASE, NONE);
		    	return false;
		    } case R.id.tb_date:{
		    	init_SelectedCD();
		    	load_from_database(selected_CD);
		    	invalidateOptionsMenu();
		    	Toast.makeText(Schedule.this, "Showing events for today", Toast.LENGTH_SHORT).show();
		    	return false;
		    } case R.id.tb_sub_ev:{
	    		switch_activity(EVENT_CASE, NONE);
	    		return false;
	    	} case R.id.tb_sub_td:{
	    		switch_activity(TODO_CASE, NONE);
	    		return false;
	    	} case R.id.full_clear:{
	    		CLEAR_EVERYTHING();
	    		Toast.makeText(Schedule.this, "All events have been cleared!", Toast.LENGTH_LONG).show();
	    		return false;
	    	} case R.id.test_item:{
		    	Toast.makeText(Schedule.this, "Created test alarm...wait 5 seconds", Toast.LENGTH_SHORT).show();
		    	create_Alarm();
		    	return false;
	    	} case R.id.test_cancel:{
	    		Toast.makeText(Schedule.this, "Cancelled Alarm...", Toast.LENGTH_SHORT).show();
	    		cancel_Alarm();
	    	} case R.id.get_mill:{
	    		show_milli();
	    	} default: {
		        return super.onOptionsItemSelected(item);
		    }
	    }
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		parse_cloud_init();
		initalizeLayout();
		
		/* Set Up Metrics */
		setUpMetrics();
		
		/* SQL Source */
		datasource = new SQL_DataSource(this);
		datasource.open();
		
		/* Initialize Selected Date to Today */
		selected_CD = new Cal_Date();
		selected_CM = new Cal_Module();
		init_SelectedCD();
		
		/* Primary List */
		events_visible = new ArrayList<Event>();
		
		e_adapter = new EventListAdapter(this, events_visible);
		//e_listview.setAdapter(e_adapter);
		e_listview.setLongClickable(true);
		e_listview.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		
//		final GestureDetector gdt = new GestureDetector(this, new Gesture_Module(this, e_listview, 
//				REL_SWIPE_MAX_OFF_PATH, REL_SWIPE_MAX_OFF_PATH, REL_SWIPE_MAX_OFF_PATH));
		
		final GestureDetector gdt = new GestureDetector(this, new Gest_Module());
		View.OnTouchListener glt = new View.OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				return gdt.onTouchEvent(event);
			}
		};
		
		e_listview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            public boolean onItemLongClick(AdapterView<?> adv, View v, int pos, long id) 
            {
            	selected_view = v;
                if(m_Action != null){
                	return false;
                }
                m_Action = Schedule.this.startActionMode(m_ActionCall);
                selected_event = (Event) e_adapter.getItem(pos);
                adv.setSelection(pos);
                e_adapter.notifyDataSetChanged();
                return true;                
            }
		});		
		
		/* Secondary List */
		todos_visible = new ArrayList<Event>();

		t_adapter = new ToDoListAdapter(this, todos_visible);
		t_listview.setLongClickable(true);
		t_listview.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		t_listview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            public boolean onItemLongClick(AdapterView<?> adv, View v, int pos, long id){
            	selected_view = v;
                if(m_Action2 != null) {
                	return false;
                }
                m_Action2 = Schedule.this.startActionMode(m_ActionCall2);
                selected_event = (Event) t_adapter.getItem(pos);
                adv.setSelection(pos);
                t_adapter.notifyDataSetChanged();
                return true;                
            }
		});	
		
		e_anima_adapter = new ScaleInAnimationAdapter(e_adapter);
		e_anima_adapter.setAbsListView(e_listview);
		e_listview.setAdapter(e_anima_adapter);
		t_anima_adapter = new ScaleInAnimationAdapter(t_adapter);
		t_anima_adapter.setAbsListView(t_listview);
		t_listview.setAdapter(t_anima_adapter);
		
		e_listview.setOnTouchListener(glt);
		r_Layout.setOnTouchListener(glt);
		
		load_from_database(selected_CD);
	}

	private void setUpMetrics()
	{
	    DisplayMetrics dm = getResources().getDisplayMetrics();
	    REL_SWIPE_MIN_DISTANCE = (int)(120.0f * dm.densityDpi / 160.0f + 0.5); 
	    REL_SWIPE_MAX_OFF_PATH = (int)(250.0f * dm.densityDpi / 160.0f + 0.5);
	    REL_SWIPE_THRESHOLD_VELOCITY = (int)(200.0f * dm.densityDpi / 160.0f + 0.5);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getSupportMenuInflater();
		inflater.inflate(R.menu.topbar, menu);
		inflater.inflate(R.menu.settings_menu, menu);
		return true;
	}
	/* Configures everything Visual*/
	protected void initalizeLayout()
	{
		requestWindowFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
		getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.color.HoloDark));
		setContentView(R.layout.schedule_view);
		
		empty_todo = (ViewStub) findViewById(R.id.empty_todo);
		empty_events = (ViewStub) findViewById(R.id.empty_event);
		
		e_listview = (ListView)findViewById(R.id.eventViewGroup);
		t_listview = (ListView)findViewById(R.id.todoViewGroup);
        sliderText = (TextView) findViewById(R.id.todo_slider);
        
        r_Layout = (RelativeLayout) findViewById(R.id.topLL);
        
		config_actionbar();
		
        SlidingUpPanelLayout layout = (SlidingUpPanelLayout) findViewById(R.id.sliding_layout);
        layout.setShadowDrawable(getResources().getDrawable(R.drawable.above_shadow));
        layout.setPanelHeight(TodoPanelHeight);
        layout.setAnchorPoint(0.3f);
        layout.setDragView(sliderText);
        layout.setPanelSlideListener(new PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {
                if (slideOffset < 0.3) {
                    if (getActionBar().isShowing()) {
                        getActionBar().hide();
                    }
                } else {
                    if (!getActionBar().isShowing()) {
                        getActionBar().show();
                    }
                }
            }

            @Override
            public void onPanelExpanded(View panel) {
            }

            @Override
            public void onPanelCollapsed(View panel) {
            }

            @Override
            public void onPanelAnchored(View panel) {
            }
        });
        sliderText.setMovementMethod(LinkMovementMethod.getInstance());
		
	}
	
	/* ActionBar Configuration */
	protected void config_actionbar()
	{
		ActionBar ab = getSupportActionBar();
		ab.setDisplayShowTitleEnabled(false); 
		ab.setDisplayShowHomeEnabled(false);
	}
	protected void remove_event(Event e)
	{
		// Check if Selected Event is not an Empty Event
			if(!e.isEqual(empty_event))
			{
				// Remove Event from Visible List
				for(int x = 0; x < events_visible.size(); x++)
				{
					if(events_visible.get(x).equals(e))
					{
						events_visible.remove(x);
					}
				}
				datasource.deleteEvent(e);
				// Update View List
				e_adapter.notifyDataSetChanged();
				// Set Selection back to Null Event
				selected_event = empty_event;
			}
	}
	
	protected void remove_todo(Event e)
	{
		// Check if Selected Event is not an Empty Event
			if(!e.isEqual(empty_event))
			{
				// Remove Event from Visible List
				for(int x = 0; x < todos_visible.size(); x++)
				{
					if(todos_visible.get(x).equals(e))
					{
						todos_visible.remove(x);
					}
				}
				datasource.deleteEvent(e);		
				// Update View List
				t_adapter.notifyDataSetChanged();
				// Set Selection back to Null Event
				selected_event = empty_event;
			}
	}
	
	protected void parse_cloud_init()
	{
		Parse.initialize(this, "oUi6DEolQ95K8EyHni3HlWNJWyUYeQZG7G142RdQ", "9k0t1vS9INswCXDd7EeLpeGWQJ0RMoyPBxnMjsYi");
		ParseAnalytics.trackAppOpened(getIntent());
		//ParseObject testObject = new ParseObject("TestObject");
		//testObject.put("foo", "bar");
		//testObject.saveInBackground();
	}
	
	protected void switch_activity(int USR_CASE, long id)
	{
		switch(USR_CASE)
		{
			case EDT_EVENT_CASE:
			{
				event_INTENT = new Intent(this, Add_Activity.class);
				event_INTENT.putExtra(SELECT_KEY, selected_CD);
				event_INTENT.putExtra(SELECT_ID_KEY, (long)id);
				startActivity(event_INTENT);
				load_from_database(selected_CD);
				break;
			}
			case EDT_TODO_CASE:
			{
				todo_INTENT = new Intent(this, TD_Add_Activity.class);
				todo_INTENT.putExtra(SELECT_KEY, selected_CD);
				todo_INTENT.putExtra(SELECT_ID_KEY, (long)id);
				startActivity(todo_INTENT);
				load_from_database(selected_CD);
				break;
			}
			case EVENT_CASE:
			{
				event_INTENT = new Intent(this, Add_Activity.class);
				event_INTENT.putExtra(SELECT_KEY, selected_CD);
				event_INTENT.putExtra(SELECT_ID_KEY, (long)NONE_L);
				startActivity(event_INTENT);
				load_from_database(selected_CD);
				break;
			}
			case TODO_CASE:
			{
				todo_INTENT = new Intent(this, TD_Add_Activity.class);
				todo_INTENT.putExtra(SELECT_KEY, selected_CD);
				todo_INTENT.putExtra(SELECT_ID_KEY, (long)NONE_L);
				startActivity(todo_INTENT);
				load_from_database(selected_CD);
				break;
			}
			case MONTH_CASE:
			{
				month_INTENT = new Intent(this, Month_Activity.class);
				month_INTENT.putExtra(SELECT_KEY, selected_CD);
				startActivityForResult(month_INTENT, month_REQUESTCODE);
				break;
			}
		}
	}
	
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		if(resultCode != REQUEST_CANCELLED && resultCode == RESULT_OK)
		{
			if(requestCode == month_REQUESTCODE)
			{
				selected_CD.set_day(data.getIntExtra(SCHEDULE_DAY, EMPTY));
				selected_CD.set_month(data.getIntExtra(SCHEDULE_MONTH, EMPTY));
				selected_CD.set_year(data.getIntExtra(SCHEDULE_YEAR, EMPTY));
				invalidateOptionsMenu();
			}
		}
	}
	
	protected void load_from_database(Cal_Date d)
	{
		ArrayList<Event> temp = datasource.getEventsForDate(d);
		events_visible.clear();
		todos_visible.clear();
		
		for(int INDEX = 0; INDEX < temp.size(); INDEX++)
		{
			if(temp.get(INDEX).GetStart() == NONE) todos_visible.add(temp.get(INDEX));
			else events_visible.add(temp.get(INDEX));
		}
		
		trigger_ViewStub();
		
		t_adapter.notifyDataSetChanged();
		e_adapter.notifyDataSetChanged();
		
		delay_Animation();
	}
	
	
	protected void trigger_ViewStub()
	{
		if(events_visible.isEmpty() == true){
			empty_events.setVisibility(View.VISIBLE);
			e_listview.setVisibility(View.INVISIBLE);
		} else {
			empty_events.setVisibility(View.INVISIBLE);
			e_listview.setVisibility(View.VISIBLE);
		}
		if(todos_visible.isEmpty() == true){
			empty_todo.setVisibility(View.VISIBLE);
			t_listview.setVisibility(View.INVISIBLE);
		} else {
			empty_todo.setVisibility(View.INVISIBLE);
			t_listview.setVisibility(View.VISIBLE);
		}
	}
	
	protected void CLEAR_EVERYTHING()
	{
		/* Deletes Repeated Alarms */
		ArrayList<Event> temp = datasource.getAllEvents();
		for(int INDEX = 0; INDEX < temp.size(); INDEX++)
		{
			if(!temp.get(INDEX).getRep().equals(NO_REP)){
				remove_Alarm(temp.get(INDEX).GetID());
				cancel_repAlarm(temp.get(INDEX));
			}
		}
		
		/* Clears ALL Containers, ALL of the DB Table*/
		datasource.clear_table();
    	todos_visible.clear();
    	events_visible.clear();
    	e_adapter.notifyDataSetChanged();
    	t_adapter.notifyDataSetChanged();
    	
    	/* Resets Selected Day */
    	init_SelectedCD();
		invalidateOptionsMenu();
	}
	
	@Override
	protected void onResume()
	{
		datasource.open();
		load_from_database(selected_CD);
		super.onResume();
	}
	
	@Override	
	protected void onPause()
	{
		datasource.close();
		super.onPause();
	}
	
	private void init_SelectedCD()
	{
		Cal_Date temp_cd = selected_CM.getCurrentDate();
		selected_CD.set_day(temp_cd.get_day());
		selected_CD.set_month(temp_cd.get_month());
		selected_CD.set_year(temp_cd.get_year());
	}
	
	private void next_day()
	{
		selected_CM.transitionNextDate(selected_CD);
    	load_from_database(selected_CD);
    	invalidateOptionsMenu();
	}
	
	private void prev_day()
	{
		selected_CM.transitionPrevDate(selected_CD);
    	load_from_database(selected_CD);
    	invalidateOptionsMenu();
	}

	private void delay_Animation()
	{
		new Handler().postDelayed(new Runnable(){
		    @Override
		    public void run(){
		        e_anima_adapter.reset();
		        e_anima_adapter.setAbsListView(e_listview);
//		        t_anima_adapter.reset();
//		        t_anima_adapter.setAbsListView(t_listview);
		    }
		}, FORCED_DELAY_ANIMA);
	}
	
    class Gest_Module extends SimpleOnGestureListener{ 

//        @Override 
//        public boolean onSingleTapUp(MotionEvent e) {
//            //int pos = e_listview.pointToPosition((int)e.getX(), (int)e.getY());
//            System.out.println("Single Tap");
//            return false;
//        }

        @Override 
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) { 
        	try {
	            if (Math.abs(e1.getY() - e2.getY()) > REL_SWIPE_MAX_OFF_PATH) 
	                return false; 
	            if(e1.getX() - e2.getX() > REL_SWIPE_MIN_DISTANCE && 
	                Math.abs(velocityX) > REL_SWIPE_THRESHOLD_VELOCITY) { 
	            	next_day();
	            }  else if (e2.getX() - e1.getX() > REL_SWIPE_MIN_DISTANCE && 
	                Math.abs(velocityX) > REL_SWIPE_THRESHOLD_VELOCITY) { 
	            	prev_day();
	            } 
            } catch (Exception e) {
            	System.out.println("Exception Detected!");
            }
            return false; 
        } 

    } 

    private final int id = 1;
    
    /** DEBUG */
	private int create_Alarm()
	{
		/* Bundle or Extra Keys */
		final String EV_NAME = "event_name";
		final String EV_DESC = "event_desc";
		
		/* Instantiate a Calendar */ 
	    Calendar calendar = Calendar.getInstance();
	    calendar.add(Calendar.SECOND, 5);
		
	    Intent AlarmIntent = new Intent(this, Receiver_Module.class);
	    AlarmIntent.setData(Uri.parse("custom://" + id));
	    AlarmIntent.setAction(String.valueOf(id));

	    AlarmIntent.putExtra(EV_NAME, "This is an event name!");
	    AlarmIntent.putExtra(EV_DESC, "This is a description!");
	    PendingIntent DispIntent = PendingIntent.getBroadcast(this, id, AlarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);

	    /* Scheduling the Alarm to be triggered*/
	    AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
	    alarmManager.set(AlarmManager.RTC, calendar.getTimeInMillis(), DispIntent);
	    
	    return id;
	}
	
	private void cancel_Alarm()
	{
		/* Recreate the alarm creation data */
		Intent AlarmIntent = new Intent(this, Receiver_Module.class);
		AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
		PendingIntent DispIntent = PendingIntent.getBroadcast(this, id, AlarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
		
		/* Instead of setting an alarm, use cancel on the pending Intent*/
		alarmManager.cancel(DispIntent);
	}
	
	private void show_milli()
	{
		Cal_Module temp = new Cal_Module();
		String s = String.valueOf(temp.getMilliseconds());
		Toast.makeText(Schedule.this, s, Toast.LENGTH_SHORT).show();
		temp = null;
	}
	
	/* NON DEBUG CANCEL, DON'T DELETE*/
	private void remove_Alarm(long id)
	{
		int newid = safeLongToInt(id);
		/* Recreate the alarm creation data */
		Intent AlarmIntent = new Intent(this, Receiver_Module.class);
		AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
		PendingIntent DispIntent = PendingIntent.getBroadcast(this, newid, AlarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
		
		/* Instead of setting an alarm, use cancel on the pending Intent*/
		alarmManager.cancel(DispIntent);
	}
	
	private void cancel_repAlarm(Event e)
	{
		int newid = safeLongToInt(e.GetID());
		
		Repetition_Module repmod = new Repetition_Module();
		repmod.set_RepString(e.getRep());
		List<Integer> temp = repmod.get_RepArray();
		
		for(int x = 0; x < temp.size(); x++){
		
		String S = "" + newid + BUGFIX + newid + temp.get(x);	
		int newids = Integer.parseInt(S);
		
		/* Recreate the alarm creation data */
		Intent AlarmIntent = new Intent(this, Receiver_Module.class);    
		AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
		AlarmIntent.setData(Uri.parse("rep://" + newids));
		AlarmIntent.setAction(String.valueOf(newids));
		PendingIntent DispIntent = PendingIntent.getBroadcast(this, newids, 
				AlarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
		
		/* Instead of setting an alarm, use cancel on the pending Intent*/
		alarmManager.cancel(DispIntent);
		}
	}
	
	private static int safeLongToInt(long l) {
	    if (l < Integer.MIN_VALUE || l > Integer.MAX_VALUE) {
	        throw new IllegalArgumentException
	            (l + " cannot be cast to int without changing its value.");
	    }
	    return (int) l;
	}

	
}
