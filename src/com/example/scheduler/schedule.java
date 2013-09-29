package com.example.scheduler;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.ActionMode;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;

import android.content.Context;
/* Basic Android Imports*/
import android.os.Bundle;
//import android.view.LayoutInflater; required library later
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

/* Java Utils Imports */
import java.util.Vector;

public class schedule extends SherlockFragmentActivity {
	
	/** DEBUG VALUES */
	final int STR_TIME = 700;
	final int END_TIME = 1200;
	final int D_M = 9; // Test Month
	final int D_D = 28; //Test Day
	final int D_Y = 2013; //Test Year
	/** END DEBUG VALUES */
	
	/* Application context */
	final Context context=this;

	/* Data Structures */
	protected Vector<Event> events;
	protected Vector<Event> events_visible;
	protected eventListAdapter e_adapter;
	protected ListView e_listview; /* Contains list of Views that displays each Event */
	
	/* Corresponding View and Events for selection */
	protected View selection_view;
	protected Event selection_event;
	
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
            	Toast.makeText(schedule.this, "Deleting selection", Toast.LENGTH_SHORT).show();
				mode.finish();
				return true;
			case R.id.menu_edit:
				/* Some Code to Edit Event */
				/* This toast is for testing purposes only since view will be swapped for editing Events*/
            	Toast.makeText(schedule.this, "Editing selection", Toast.LENGTH_SHORT).show();
				mode.finish();
				return true;
			default:
				Toast.makeText(schedule.this, "Canceling..", Toast.LENGTH_SHORT).show();
				return false;
			}
		}

		@Override
		public void onDestroyActionMode(ActionMode mode) {
			m_Action = null;
			selection_view.setBackgroundResource(android.R.color.transparent);	
		}
		
	};
	/* End Contextual menu code */
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) 
	{
	    int id = item.getItemId();

	    switch(id)
	    {
		    case R.id.tb_add:
		    {
		    	Toast.makeText(schedule.this, "Event Add button was pressed!", Toast.LENGTH_SHORT).show();
		    	return false;
		    }
		    case R.id.tb_month:
		    {
		    	Toast.makeText(schedule.this, "Month was pressed!", Toast.LENGTH_SHORT).show();
		    	return false;

		    }
		    case R.id.tb_date:
		    {
		    	Toast.makeText(schedule.this, "Date was pressed!", Toast.LENGTH_SHORT).show();
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
		initalizeLayout();
		
		events = new Vector<Event>();
		events_visible = new Vector<Event>();
		
		
		e_adapter = new eventListAdapter(this, events_visible);
		e_listview.setAdapter(e_adapter);
		e_listview.setLongClickable(true);
		e_listview.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		e_listview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
			
            public boolean onItemLongClick(AdapterView<?> adv, View v, int pos, long id) 
            {
            	ListView myList = (ListView)findViewById(R.id.eventViewGroup);
            	Toast.makeText(schedule.this,"Test:" + myList.getItemAtPosition(pos).toString(), Toast.LENGTH_SHORT).show();
            	
            	selection_view = v;
                if(m_Action != null)
                {
                	return false;
                }
                
                m_Action = schedule.this.startActionMode(m_ActionCall);
                //v.setBackgroundResource(color.highlight);
                selection_event = (Event) e_adapter.getItem(pos);
                adv.setSelection(pos);
                e_adapter.notifyDataSetChanged();
                return true;                
            }
		});
		
		events.add(debug_fake_event());
		Toast.makeText(schedule.this,"Size of events Array: " + events.size(), Toast.LENGTH_SHORT).show();
		
		
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
		config_actionbar();
	}
	
	/* ActionBar Configuration */
	protected void config_actionbar()
	{
		ActionBar ab = getSupportActionBar();
		ab.setDisplayShowTitleEnabled(false); 
		ab.setDisplayShowHomeEnabled(false);
		
		/* Will re-implement last
		LayoutInflater inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    View v =inflater.inflate(R.layout.top_bar_view, null, true);
	    ab.setCustomView(v);
		ab.setDisplayShowCustomEnabled(true);
		
		*/
	}
	
	protected Event debug_fake_event()
	{
		Event temp = new Event();
		temp.setAlarm(false);
		temp.setID(events.size());
		temp.setName("Test Event");
		temp.setDescription("Description for test Event");
		temp.setDate(debug_fake_date());
		return temp;
	}
	
	protected Date debug_fake_date()
	{
		Date temp = new Date(STR_TIME, END_TIME, D_M, D_D, D_Y);
		return temp;
	}
	
	
	
	

}
