package com.example.scheduler;

/* Parse Cloud Imports */
import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.parse.ParseObject;

/* ActionBarSherlock Imports */
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.ActionMode;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;

/* Basic Android Imports*/
import android.os.Bundle;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;
//import android.view.LayoutInflater; required library later

import java.util.ArrayList;
import java.util.Random;

public class Schedule extends SherlockFragmentActivity {
	
	/** Get email for SQLite DB name */
    private final Email_Module email_MODULE = new Email_Module();
    private String identifier;
	
    /* Final Values for Integers */
    final static int NONE = -1;
    final static float NO_SHOW = 0;
    final static float SMALL_SHOW = .3f;
    final static float FULL_SHOW = .4f;
    
    /* ENUMERATION FOR TD/EVENT */
    final static int EVENT_CASE = 0;
    final static int TODO_CASE = 1;
    final static int MONTH_CASE = 2;
    
    /* TODO SIZE CASES */
    final static int EMPTY = 0;
    final static int SINGLE = 1;
    final static int DOUBLE = 2;
    final static int MULTI = 3;
	
	/* Application context */
	final Context main_context = this;

	/* Data Structures */
	protected ArrayList<Event> events_visible;
	protected EventListAdapter e_adapter;
	
	protected LinearLayout sub_layout; /* adjustable layout for event*/
	protected LinearLayout sub_layout_todo; /* adjustable layout fot todo*/
	protected ListView e_listview; /* Contains list of Views that displays each Event */
	protected ListView t_listview; /* Contains list of Views that displays each ToDo */
	
	protected ArrayList<Event> todos_visible;
	protected ToDoListAdapter t_adapter;
	
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
				/* Some Code to Delete Event */
            	Toast.makeText(Schedule.this, "Deleting selection", Toast.LENGTH_SHORT).show();
            	remove_event(selected_event);
				mode.finish();
				return true;
			case R.id.menu_edit:
				/* Some Code to Edit Event */
				/* This toast is for testing purposes only since view will be swapped for editing Events*/
            	Toast.makeText(Schedule.this, "Editing selection", Toast.LENGTH_SHORT).show();
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
			//selected_view.setBackgroundResource(android.R.color.transparent);	
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
				/* Some Code to Delete Event */
            	Toast.makeText(Schedule.this, "Deleting selection", Toast.LENGTH_SHORT).show();
            	remove_todo(selected_event);
            	//weight_adjustment();
				mode.finish();
				return true;
			case R.id.menu_edit:
				/* Some Code to Edit Event */
				/* This toast is for testing purposes only since view will be swapped for editing Events*/
            	Toast.makeText(Schedule.this, "Editing selection", Toast.LENGTH_SHORT).show();
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
		return true;
	}
	
	
	
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) 
	{
	    int id = item.getItemId();

	    switch(id)
	    {		    

		    case R.id.tb_month:
		    {
		    	switch_activity(MONTH_CASE);
		    	return false;

		    }
		    case R.id.tb_date:
		    {
		    	Toast.makeText(Schedule.this, "Date was pressed! Using as Clear ALL for now", Toast.LENGTH_SHORT).show();
		    	CLEAR_EVERYTHING();
		    	return false;
		    }
	    	case R.id.tb_sub_ev:
	    	{
	    		switch_activity(EVENT_CASE);
	    		return false;
	    	}
	    	
	    	case R.id.tb_sub_td:
	    	{
	    		switch_activity(TODO_CASE);
	    		return false;
	    	}
		    default:
		    {
		        return super.onOptionsItemSelected(item);
		    }
	    }
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		parse_cloud_init();
		initalizeLayout();
		
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
		e_listview.setAdapter(e_adapter);
		e_listview.setLongClickable(true);
		e_listview.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		e_listview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            public boolean onItemLongClick(AdapterView<?> adv, View v, int pos, long id) 
            {
            	ListView event_list = (ListView)findViewById(R.id.eventViewGroup);
            	
            	/** Debug Toast*/
            	Toast.makeText(Schedule.this, "Position is:" + pos + " ID is: " + id, Toast.LENGTH_SHORT).show();
            	/** Debug Toast*/
            	
            	selected_view = v;
                if(m_Action != null)
                {
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
		t_listview.setAdapter(t_adapter);
		t_listview.setLongClickable(true);
		t_listview.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		t_listview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            public boolean onItemLongClick(AdapterView<?> adv, View v, int pos, long id) 
            {
            	ListView todo_list = (ListView)findViewById(R.id.todoViewGroup);
            	
            	/** Debug Toast*/
            	//Toast.makeText(Schedule.this, "Position is:" + pos + " ID is: " + id, Toast.LENGTH_SHORT).show();
            	/** Debug Toast*/
            	
            	selected_view = v;
                if(m_Action2 != null)
                {
                	return false;
                }
                
                m_Action2 = Schedule.this.startActionMode(m_ActionCall2);
                selected_event = (Event) t_adapter.getItem(pos);
                adv.setSelection(pos);
                t_adapter.notifyDataSetChanged();
                return true;                
            }
		});	
		
		load_from_database(selected_CD);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getSupportMenuInflater();
		inflater.inflate(R.menu.topbar, menu);
		return true;
	}
	/* Configures everything Visual*/
	protected void initalizeLayout()
	{
		setContentView(R.layout.schedule_view);
		e_listview = (ListView)findViewById(R.id.eventViewGroup);
		t_listview = (ListView)findViewById(R.id.todoViewGroup);
		sub_layout = (LinearLayout)findViewById(R.id.adjustableLayout);
		sub_layout_todo = (LinearLayout)findViewById(R.id.adjustableTodo);
		//load_from_database();
		config_actionbar();
	}
	
	/* ActionBar Configuration */
	protected void config_actionbar()
	{
		ActionBar ab = getSupportActionBar();
		ab.setDisplayShowTitleEnabled(false); 
		ab.setDisplayShowHomeEnabled(false);
		
		/* Will re-implement last
		LayoutInflater inflater=(LayoutInflater) main_context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    View v =inflater.inflate(R.layout.top_bar_view, null, true);
	    ab.setCustomView(v);
		ab.setDisplayShowCustomEnabled(true);
		
		*/
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
	
	protected void switch_activity(int USR_CASE)
	{
		switch(USR_CASE)
		{
			case EVENT_CASE:
			{
				event_INTENT = new Intent(this, Add_Activity.class);
				startActivity(event_INTENT);
				load_from_database(selected_CD);
				break;
			}
			case TODO_CASE:
			{
				todo_INTENT = new Intent(this, TD_Add_Activity.class);
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
		e_adapter.notifyDataSetChanged();
		t_adapter.notifyDataSetChanged();
	}
	
	/* Changes Weight of Todo Layout Depending on Size of ArrayList */
	protected void weight_adjustment()
	{
		int SIZE = todos_visible.size();
		if(SIZE == EMPTY)
		{
			sub_layout.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, 
				LayoutParams.WRAP_CONTENT, NO_SHOW));
		}
		else if (SIZE == DOUBLE || SIZE == SINGLE)
		{
			sub_layout.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, 
					LayoutParams.WRAP_CONTENT, SMALL_SHOW));
		}
		else
		{
			sub_layout.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, 
					LayoutParams.WRAP_CONTENT, FULL_SHOW));
		}
	}
	
	protected void CLEAR_EVERYTHING()
	{
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

	
}
