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
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
//import android.view.LayoutInflater; required library later

import java.util.ArrayList;
import java.util.Random;

public class Schedule extends SherlockFragmentActivity {
	
	/** Get email for SQLite DB name */
    private final Email_Fetcher e_f = new Email_Fetcher();
    private String identifier;
	
	/** DEBUG VALUES */
	final int STR_TIME = 700;
	final int END_TIME = 1200;
	final int D_M = 9; // Test Month
	final int D_D = 28; //Test Day
	final int D_Y = 2013; //Test Year
	/** END DEBUG VALUES */
	
	/* Application context */
	final Context main_context = this;

	/* Data Structures */
	protected ArrayList<Event> events;
	protected ArrayList<Event> events_visible;
	protected EventListAdapter e_adapter;
	
	protected ListView e_listview; /* Contains list of Views that displays each Event */
	protected ListView t_listview; /* Contains list of Views that displays each ToDo */
	
	protected ArrayList<Event> todos;
	protected ArrayList<Event> todos_visible;
	protected ToDoListAdapter t_adapter;
	
	/* Corresponding View and Events for selection */
	protected View selected_view;
	protected Event selected_event;
	protected Event empty_event = new Event(); // Empty event for null purposes
	
	/* Intents for separate Activities*/
	protected Intent event_creation;
	protected Intent todo_creation;
	
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
			//selected_view.setBackgroundResource(android.R.color.transparent);	
		}
		
	};
	
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) 
	{
	    int id = item.getItemId();

	    switch(id)
	    {		    

		    case R.id.tb_month:
		    {
		    	Toast.makeText(Schedule.this, "Month was pressed!", Toast.LENGTH_SHORT).show();
		    	return false;

		    }
		    case R.id.tb_date:
		    {
		    	Toast.makeText(Schedule.this, "Date was pressed!", Toast.LENGTH_SHORT).show();
		    	return false;
		    }
	    	case R.id.tb_sub_ev:
	    	{
	    		switch_add_activity();
	    		return false;
	    	}
	    	case R.id.tb_sub_qt:
	    		add_todo();
	    		return false;
	    	
	    	case R.id.tb_sub_td:
	    	{
	    		/*
	    		todo_creation = new Intent(this, TD_Add_Activity.class);
	    		startActivity(todo_creation);
	    		t_adapter.notifyDataSetChanged();
	    		*/
		    	Toast.makeText(Schedule.this, "Create Todo was pressed!", Toast.LENGTH_SHORT).show();
	    		return false;
	    	}
	    	case R.id.tb_sub_qe:
	    	{
	    		add_event();
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
		
		events = new ArrayList<Event>();
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
               // v.setBackgroundResource(color.highlight);
                selected_event = (Event) e_adapter.getItem(pos);
                adv.setSelection(pos);
                e_adapter.notifyDataSetChanged();
                return true;                
            }
		});		
		
		todos = new ArrayList<Event>();
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
            	Toast.makeText(Schedule.this, "Position is:" + pos + " ID is: " + id, Toast.LENGTH_SHORT).show();
            	/** Debug Toast*/
            	
            	selected_view = v;
                if(m_Action2 != null)
                {
                	return false;
                }
                
                m_Action2 = Schedule.this.startActionMode(m_ActionCall2);
               // v.setBackgroundResource(color.highlight);
                selected_event = (Event) t_adapter.getItem(pos);
                adv.setSelection(pos);
                t_adapter.notifyDataSetChanged();
                return true;                
            }
		});	
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getSupportMenuInflater();
		inflater.inflate(R.menu.topbar, menu);
//		return super.onCreateOptionsMenu(menu);
		return true;
	}
	
	/* Configures everything Visual*/
	protected void initalizeLayout()
	{
		setContentView(R.layout.schedule_view);
		e_listview = (ListView)findViewById(R.id.eventViewGroup);
		t_listview = (ListView)findViewById(R.id.todoViewGroup);
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
	
	protected Event debug_fake_event()
	{
		Event temp = new Event();
		temp.setAlarm(false);
		//temp.setID(events.size());
		temp.setID(System.currentTimeMillis()/1000);
		temp.setName("Test Event " + events.size());
		temp.setDescription(this.getString(R.string.test_desc));
		temp.setDate(debug_fake_date());
		return temp;
	}
	
	/** Debug Code */
	protected Date debug_fake_date()
	{
		Random generator = new Random();
		int start, end, dm, dd, dy;
		start = generator.nextInt(2400);
		end = generator.nextInt(2400);
		dm = generator.nextInt(11)+1;
		dd = generator.nextInt(30)+1;
		dy = generator.nextInt(9999);
		Date temp = new Date(start, end, dm, dd, dy);
		return temp;
	}
	/** Debug Code */
	
	/** Debug Code */ // Needs to be rewritten for final version
	protected void add_event()
	{
		events.add(debug_fake_event());
		events_visible.add(debug_fake_event());
		e_adapter.notifyDataSetChanged();
		//Toast.makeText(Schedule.this,"Size of events Array: " + events.size(), Toast.LENGTH_SHORT).show();
	}
	/** Debug Code */
	
	/** Debug Code */ // Needs to be rewritten for final version
	protected void add_todo()
	{
		todos.add(debug_fake_event());
		todos_visible.add(debug_fake_event());
		t_adapter.notifyDataSetChanged();
		//Toast.makeText(Schedule.this,"Size of todo Array: " + events.size(), Toast.LENGTH_SHORT).show();
	}
	/** Debug Code */
	

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
				
				// Remove Event from Primary List
				for(int x = 0; x < events.size(); x++)
				{
					if(events.get(x).equals(e))
					{
						events.remove(x);
					}
				}
				
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
				
				// Remove Event from Primary List
				for(int x = 0; x < todos.size(); x++)
				{
					if(todos.get(x).equals(e))
					{
						todos.remove(x);
					}
				}
				
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
	
	protected void switch_add_activity()
	{
		event_creation = new Intent(this, Add_Activity.class);
		startActivity(event_creation);
		e_adapter.notifyDataSetChanged();
	
	}
	
}
