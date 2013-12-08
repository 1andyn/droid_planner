package com.example.scheduler;

/* Parse Cloud Imports */
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;
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

/* Basic Android Imports*/
import android.os.Bundle;
import android.os.Handler;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import java.util.List;
import android.net.Uri;

public class Schedule extends SherlockFragmentActivity implements Parse_Interface, Intent_Interface,
	PrefKey_Interface {
	
	/** Get email for SQLite DB name */
    private final static Email_Module email_MODULE = new Email_Module();
    private String identifier;
    
    private final static String BUGFIX = "1337";
    private SharedPreferences UserPrefs;
    
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
    private final static int WEEK_CASE = 5;
    
    private final static int TodoPanelHeight = 75;
    private static int FORCED_DELAY_ANIMA = 175;
    
    /* TO DO SIZE CASES */
    private final static int EMPTY = 0;
    private final static int NONE = -1;
    private final static long NONE_L = -1;

	/* Data Structures */
	protected ArrayList<Event> events_visible;
	protected EventListAdapter e_adapter;
	
	private TextView sliderText;
	
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
	protected Intent week_INTENT;
	
	/* Data for Storing Selected Date */ 
	protected Cal_Date selected_CD;
	protected Cal_Module selected_CM;
	
	private final static String NO_REP = "NNNNNNN";
	
	/* Callback Variables */
	private Event temp_event;
	private ParseObject db_event;
	private ParseObject cntrl_v;
	private long temp_id;
	private int temp_version;
	
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
	    	} case R.id.tb_week:{
	    		switch_activity(WEEK_CASE, NONE);
	    		return false;
	    	} case R.id.full_clear:{
	    		CLEAR_EVERYTHING();
	    		Toast.makeText(Schedule.this, "All events have been cleared!", Toast.LENGTH_LONG).show();
	    		return false;
	    	} case R.id.force_sync:{
	    		push_to_parse();
	    		Toast.makeText(Schedule.this, "Updating data stored in Cloud!", Toast.LENGTH_LONG).show();
	    	} case R.id.ret_sync:{
	    		retrieve_parse_events();
	    		Toast.makeText(Schedule.this, "Pulling data stored in Cloud!", Toast.LENGTH_LONG).show();
	    	} default: {
		        return super.onOptionsItemSelected(item);
		    }
	    }
	}
	
	@SuppressWarnings("static-access")
	private void acquireEmail()
	{
		/* Saves user email to local preferences */
		identifier = email_MODULE.getEmail(this);
		UserPrefs.edit().putString(usr_email, identifier).commit();
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initalizeLayout();
		
		/* SQL Source */
		datasource = new SQL_DataSource(this);
		datasource.open();
		
		/* Setup User Preferences */
		UserPrefs = this.getSharedPreferences(app_id, Context.MODE_PRIVATE);
		
		/* Acquire Email for ClientID */
		acquireEmail();
		
		/* Check for First Run*/
		check_first_run();
		
		/* Set Up Metrics */
		setUpMetrics();
		
		/* Initialize Cloud */
		parse_cloud_init(); // Set up cloud
		
		/* Initialize Selected Date to Today */
		selected_CD = new Cal_Date();
		selected_CM = new Cal_Module();
		init_SelectedCD();
		
		/* Primary List */
		events_visible = new ArrayList<Event>();
		
		e_adapter = new EventListAdapter(this, events_visible);
		e_listview.setLongClickable(true);
		e_listview.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		
		final GestureDetector gdt = new GestureDetector(this, new Gest_Module());
		View.OnTouchListener glt = new View.OnTouchListener(){
			public boolean onTouch(View v, MotionEvent event){
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
	
	protected void parse_cloud_init()
	{
		Parse.initialize(getApplicationContext(), appid, clientid);
		ParseAnalytics.trackAppOpened(getIntent());
	}
	
	private void push_to_parse()
	{
		parse_clean(); //Cleans Cloud
		datasource.clear_object_table(); //Cleans original object table
		ArrayList<Event> temp = datasource.getAllEvents();
		System.out.println("Size of Current Database: " + temp.size());
		for(int INDEX = 0; INDEX < temp.size(); INDEX++){
			/*Pushes each Event into Cloud*/
			temp_event = temp.get(INDEX);
			construct_parse_event(temp_event);
		}
		sync_version(current_version());
	}

	private void check_first_run()
	{
		String first = UserPrefs.getString(first_run, user_first_run);
		if(first.equals(user_first_run)){
			/* Change to not first run*/
			UserPrefs.edit().putString(first_run, not_first_run).commit();
			
			/* Attempt to get ControlVersion from CLOUD */
			ParseQuery<ParseObject> query = ParseQuery.getQuery(ver_class);
			query.whereEqualTo(email, identifier);
			query.getFirstInBackground(new GetCallback<ParseObject>() {
				@Override
				public void done(ParseObject object, ParseException e) {
			        if (object == null) {
			        	System.out.println("Couldn't find VersionControl..");
						UserPrefs.edit().putInt(db_version, initial_version).commit();
						cntrl_v = new ParseObject(ver_class);
						cntrl_v.put(db_ver, String.valueOf(initial_version));
						cntrl_v.put(email, identifier);
						cntrl_v.saveEventually(new SaveCallback() {
				            public void done(ParseException e) {
				                if (e == null) {
				                	System.out.println("Sucessfully reached callback..");
				                	UserPrefs.edit().putString(cntrl_key, cntrl_v.getObjectId()).commit();
				                	System.out.println("Saved version ObjectID: " + cntrl_v.getObjectId());
				                } else {
				                }
				            }
				        });
			        } else {
						 System.out.println("Detected Control Version on Cloud");
						 UserPrefs.edit().putString(cntrl_key, object.getObjectId()).commit();
						 UserPrefs.edit().putInt(db_version, initial_version).commit();
						 retrieve_parse_events();
			        }
			    }
			});
		}
		
		int end = UserPrefs.getInt(db_version, initial_version);
		System.out.println("Current version is: " + end);
	}
	
	private void retrieve_parse_events()
	{
		/* Drop original tables */
		datasource.clear_table();
		ParseQuery<ParseObject> query = ParseQuery.getQuery(parse_class);
		query.whereEqualTo(email, identifier);
		query.findInBackground(new FindCallback<ParseObject>() {
			@Override
			public void done(List<ParseObject> eventList, ParseException e) {
		        if (e == null) {
		    		for(int x = 0; x < eventList.size(); x++) {
		    			Event temp = new Event();
		    			Date temp_time = new Date();
						temp.setName(eventList.get(x).getString(name));
						temp.setDescription(eventList.get(x).getString(desc));
						temp.setAlarm(eventList.get(x).getString(alarm));
						temp.setColor(Integer.parseInt(eventList.get(x).getString(color)));
						temp_time.setMth(Integer.parseInt(eventList.get(x).getString(month)));
						temp_time.setDay(Integer.parseInt(eventList.get(x).getString(day)));
						temp_time.setYr(Integer.parseInt(eventList.get(x).getString(year)));
						temp_time.setStartTime(Integer.parseInt(eventList.get(x).getString(start)));
						temp_time.setEndTime(Integer.parseInt(eventList.get(x).getString(end)));
						temp.set_Rep(eventList.get(x).getString(rep));
						temp.setDate(temp_time);
						temp.set_Asec(Integer.parseInt(eventList.get(x).getString(asec)));	
						temp_id = datasource.createEvent(temp).GetID();
						increment_version();
						eventList.get(x).put(id, String.valueOf(temp_id));
						eventList.get(x);
						eventList.get(x).saveInBackground();
						datasource.saveObjectID(temp_id, eventList.get(x).getObjectId());
		    		}
		        } else {
		        	System.out.println("Cannot reach cloud, or empty Cloud DB");
		        	Toast.makeText(Schedule.this, "Unable to reach cloud...", Toast.LENGTH_LONG).show();
		        }
		    }
		});
    	load_from_database(selected_CD);
	}
	
	private void construct_parse_event(Event ev)
	{
		db_event = new ParseObject(parse_class);
		db_event.put(id, String.valueOf(ev.GetID()));
		db_event.put(email, identifier);
		db_event.put(name, String.valueOf(ev.getName()));
		db_event.put(desc, String.valueOf(ev.getDescription()));
		db_event.put(alarm, String.valueOf(ev.getAlarm()));
		db_event.put(month, String.valueOf(ev.GetMonth()));
		db_event.put(day, String.valueOf(ev.GetDay()));
		db_event.put(year, String.valueOf(ev.GetYear()));
		db_event.put(start, String.valueOf(ev.GetStart()));
		db_event.put(end, String.valueOf(ev.GetEnd()));
		db_event.put(color, String.valueOf(ev.getColor()));
		db_event.put(rep, String.valueOf(ev.getRep()));
		db_event.put(asec, String.valueOf(ev.get_Asec()));		
		db_event.saveInBackground(new SaveCallback() {
            public void done(ParseException e) {
                if (e == null) {
                	SQL_DataSource ds = new SQL_DataSource(getApplicationContext());
                	ds.open();
                	ds.saveObjectID(temp_event.GetID(), db_event.getObjectId());
                	ds.close();
                } else {
                	Toast.makeText(Schedule.this, "Unable to reach cloud...", Toast.LENGTH_LONG).show();
                }
            }
        });
	}
	
	private void setUpMetrics()
	{
	    DisplayMetrics dm = getResources().getDisplayMetrics();
	    REL_SWIPE_MIN_DISTANCE = (int)(120.0f * dm.densityDpi / 160.0f + 0.5); 
	    REL_SWIPE_MAX_OFF_PATH = (int)(250.0f * dm.densityDpi / 160.0f + 0.5);
	    REL_SWIPE_THRESHOLD_VELOCITY = (int)(200.0f * dm.densityDpi / 160.0f + 0.5);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
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
			if(!e.isEqual(empty_event)){
				// Remove Event from Visible List
				for(int x = 0; x < events_visible.size(); x++){
					if(events_visible.get(x).equals(e)){
						events_visible.remove(x);
					}
				}
				datasource.deleteEvent(e);
				ParseObject.createWithoutData(parse_class, 
						datasource.acquireObjectId(e.GetID())).deleteEventually();
				datasource.deleteEventObj(e.GetID());
				
				// Update View List
				e_adapter.notifyDataSetChanged();
				// Set Selection back to Null Event
				selected_event = empty_event;
				increment_version();
			}
	}
	
	protected void remove_todo(Event e)
	{
		// Check if Selected Event is not an Empty Event
			if(!e.isEqual(empty_event)){
				// Remove Event from Visible List
				for(int x = 0; x < todos_visible.size(); x++){
					if(todos_visible.get(x).equals(e)){
						todos_visible.remove(x);
					}
				}
				datasource.deleteEvent(e);
				ParseObject.createWithoutData(parse_class, 
						datasource.acquireObjectId(e.GetID())).deleteEventually();
				datasource.deleteEventObj(e.GetID());
				// Update View List
				t_adapter.notifyDataSetChanged();
				// Set Selection back to Null Event
				selected_event = empty_event;
				increment_version();
			}
	}
	
	private void parse_clean()
	{
		ParseQuery<ParseObject> query = ParseQuery.getQuery(parse_class);
		query.whereEqualTo(email, identifier);
		query.findInBackground(new FindCallback<ParseObject>() {
			@Override
			public void done(List<ParseObject> eventList, ParseException e) {
		        if (e == null) {
		           System.out.println("PPLUS" + "Removing " + eventList.size() + " events");
		    		for(int x = 0; x < eventList.size(); x++) {
						ParseObject.createWithoutData(parse_class, 
								eventList.get(x).getObjectId()).deleteEventually();
		    		}
		        } else {
		        	System.out.println("Error: " + e.getMessage());
		        }
		    }
		});
		sync_version(initial_version);
	}
	
	protected void switch_activity(int USR_CASE, long id)
	{
		switch(USR_CASE){
			case EDT_EVENT_CASE:{
				event_INTENT = new Intent(this, Add_Activity.class);
				event_INTENT.putExtra(SELECT_KEY, selected_CD);
				event_INTENT.putExtra(SELECT_ID_KEY, (long)id);
				startActivity(event_INTENT);
				sync_version(current_version());
				load_from_database(selected_CD);
				break;
			} case EDT_TODO_CASE: {
				todo_INTENT = new Intent(this, TD_Add_Activity.class);
				todo_INTENT.putExtra(SELECT_KEY, selected_CD);
				todo_INTENT.putExtra(SELECT_ID_KEY, (long)id);
				startActivity(todo_INTENT);
				sync_version(current_version());
				load_from_database(selected_CD);
				break;
			} case EVENT_CASE: {
				event_INTENT = new Intent(this, Add_Activity.class);
				event_INTENT.putExtra(SELECT_KEY, selected_CD);
				event_INTENT.putExtra(SELECT_ID_KEY, (long)NONE_L);
				startActivity(event_INTENT);
				sync_version(current_version());
				load_from_database(selected_CD);
				break;
			} case TODO_CASE: {
				todo_INTENT = new Intent(this, TD_Add_Activity.class);
				todo_INTENT.putExtra(SELECT_KEY, selected_CD);
				todo_INTENT.putExtra(SELECT_ID_KEY, (long)NONE_L);
				startActivity(todo_INTENT);
				sync_version(current_version());
				load_from_database(selected_CD);
				break;
			} case MONTH_CASE: {
				month_INTENT = new Intent(this, Month_Activity.class);
				month_INTENT.putExtra(SELECT_KEY, selected_CD);
				startActivityForResult(month_INTENT, month_REQUESTCODE);
				break;
			} case WEEK_CASE: {
               week_INTENT = new Intent(this, Weekly_Activity.class);
               week_INTENT.putExtra(SELECT_KEY, selected_CD);
               startActivityForResult(week_INTENT, week_REQUESTCODE);
               break;
			}
		}
	}
	
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		if(resultCode != REQUEST_CANCELLED && resultCode == RESULT_OKAY){
			if(requestCode == month_REQUESTCODE || requestCode == week_REQUESTCODE){
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
		
		for(int INDEX = 0; INDEX < temp.size(); INDEX++){
			if(temp.get(INDEX).GetStart() == NONE) {
				todos_visible.add(temp.get(INDEX));
			}
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
			remove_Alarm(temp.get(INDEX).GetID());
			if(!temp.get(INDEX).getRep().equals(NO_REP)){
				cancel_repAlarm(temp.get(INDEX));
			}
		}
		parse_clean();
		int initial_version = 0;
		UserPrefs.edit().putString(db_version, String.valueOf(initial_version)).commit();
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
		Bundle weeklyDate = getIntent().getExtras();
        if(weeklyDate == null) { 
            Cal_Date temp_cd = selected_CM.getCurrentDate();
            selected_CD.set_day(temp_cd.get_day());
            selected_CD.set_month(temp_cd.get_month());
            selected_CD.set_year(temp_cd.get_year());
        } else {
            selected_CD.set_day((Integer) weeklyDate.get(SCHEDULE_DAY));
            selected_CD.set_month((Integer) weeklyDate.get(SCHEDULE_MONTH));
            selected_CD.set_year((Integer) weeklyDate.get(SCHEDULE_YEAR));
        }
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
	
	/* NON DEBUG CANCEL, DON'T DELETE*/
	private void remove_Alarm(long id)
	{
		int newid = safeLongToInt(id);
		/* Recreate the alarm creation data */
		Intent AlarmIntent = new Intent(this, Receiver_Module.class);
		AlarmIntent.setData(Uri.parse("custom://" + newid));
		AlarmIntent.setAction(String.valueOf(newid));
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
	
	private void increment_version()
	{
		temp_version = current_version();
		temp_version++; // Increment Version
		/* Save new version locally */
		UserPrefs.edit().putInt(db_version, temp_version).commit();
		sync_version(temp_version);
	}
	
	private int current_version()
	{
		return UserPrefs.getInt(db_version, initial_version);
	}
	
	private void sync_version(int version) 
	{
		temp_version = version; 
		String saved_key = UserPrefs.getString(cntrl_key, still_missing_key);
		ParseQuery<ParseObject> query = ParseQuery.getQuery(ver_class);
		query.getInBackground(saved_key, new GetCallback<ParseObject>() {
			  public void done(ParseObject control, ParseException e) {
			    if (e == null) {
			      control.put(db_ver, String.valueOf(temp_version));
			      control.saveEventually();
			    }
			  }
			});
	}

	private static int safeLongToInt(long l) {
	    if (l < Integer.MIN_VALUE || l > Integer.MAX_VALUE) {
	        throw new IllegalArgumentException
	            (l + " cannot be cast to int without changing its value.");
	    }
	    return (int) l;
	}
}
