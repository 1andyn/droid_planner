package com.example.scheduler;

/* Action Bar Sherlock Imports*/
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.ActionMode;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.SubMenu;
import com.actionbarsherlock.view.Window;

/* Basic Android Imports*/
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class View_schedule extends SherlockActivity {

	/* Buttons */
	protected Button month_button;
	protected Button date_button;
	protected Button event_button;
	
	protected EventList events;
	protected EventList events_visible;
	protected eventListAdapter e_adapter;
	protected ListView e_listview; /* Contains list of Views that displays each Event */
	
	/* Corresponding View and Events for selection */
	protected View selection_view;
	protected Event selection_event;
	
	/* Primary Menu */
	protected Menu primary_menu;
	
	/* Contextual menu code */
	protected com.actionbarsherlock.view.ActionMode m_Action; 
	protected com.actionbarsherlock.view.ActionMode.Callback m_ActionCall = new ActionMode.Callback() 
	{
		@Override
		public boolean onCreateActionMode(ActionMode mode, Menu menu) {
			MenuInflater inflater = mode.getMenuInflater();
			inflater.inflate(R.menu.actionmenu, menu);
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
            	Toast.makeText(View_schedule.this, "Deleting selection", Toast.LENGTH_SHORT).show();
				mode.finish();
				return true;
			case R.id.menu_edit:
				/* Some Code to Edit Event */
				/* This toast is for testing purposes only since view will be swapped for editing Events*/
            	Toast.makeText(View_schedule.this, "Editing selection", Toast.LENGTH_SHORT).show();
				mode.finish();
				return true;
			default:
				Toast.makeText(View_schedule.this, "Canceling..", Toast.LENGTH_SHORT).show();
				return false;
			}
		}

		@Override
		public void onDestroyActionMode(ActionMode mode) {
			m_Action = null;
			selection_view.setBackgroundResource(android.R.color.transparent);	
		}
		
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE); /* Removes Application Title from Top*/
		super.onCreate(savedInstanceState);
		initalizeLayout();
		
//		events = new EventList();
//		events_visible = new EventList();
//		e_adapter = new eventListAdapter(this, events_visible);
//		e_listview.setAdapter(e_adapter);
//		e_listview.setLongClickable(true);
//		e_listview.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
//		e_listview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
//			
//            public boolean onItemLongClick(AdapterView<?> adv, View v, int pos, long id) 
//            {
//            	ListView myList = (ListView)findViewById(R.id.eventViewGroup);
//            	Toast.makeText(View_schedule.this,myList.getItemAtPosition(pos).toString(), Toast.LENGTH_SHORT).show();
//            	
//            	selection_view = v;
//            	
//                if(m_Action != null)
//                {
//                	return false;
//                }
//                
//                m_Action = View_schedule.this.startActionMode(m_ActionCall);
//                //v.setBackgroundResource(color.highlight);
//                selection_event = (Event) e_adapter.getItem(pos);
//                adv.setSelection(pos);
//                e_adapter.notifyDataSetChanged();
//
//                return true;                
//            }
//		});
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		MenuInflater inflater = getSupportMenuInflater();
		primary_menu = menu;
		inflater.inflate(R.menu.actionmenu, menu);
		return super.onCreateOptionsMenu(menu);
	}
	
	/* Sets Layout to be used by primary screen, Connects Buttons from XML to Java */
	protected void initalizeLayout()
	{
		setContentView(R.layout.activity_view_schedule);
		month_button = (Button)findViewById(R.id.MonthViewButton);
		date_button = (Button)findViewById(R.id.DateButton);
		event_button = (Button)findViewById(R.id.addEventButton);
	}
	
	protected void initEventButtonListeners() {
		event_button.setOnClickListener(new OnClickListener() {
		
			@Override
			public void onClick(View view) {
	        Toast.makeText(View_schedule.this, "Add Event Pressed!", Toast.LENGTH_SHORT).show();
		  } 
		}
	); 
	
	}
}
