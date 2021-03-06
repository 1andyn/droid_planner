package com.example.scheduler;

import java.util.ArrayList;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class ToDoListAdapter extends BaseAdapter {

	//Context in which eventListAdapter is being used
	private Context app_Context;
	private ArrayList<Event> event_list;
	
	public ToDoListAdapter(Context c, ArrayList<Event> e_list)
	{
		app_Context = c;
		event_list = e_list;
	}
	//returns size of eventlist
	@Override
	public int getCount() {
		return event_list.size();
	}

	@Override
	public Object getItem(int pos) {
		return event_list.get(pos);
	}

	@Override
	public long getItemId(int pos) {
		return event_list.get(pos).GetID(); 
	}

	@Override
	public View getView(int pos, View convertView, ViewGroup parent) {
		if(convertView == null)
		{
			ToDoView eventer = new ToDoView(app_Context, (Event)getItem(pos));
			eventer.setLongClickable(true);
			eventer.setFocusable(true);
			eventer.setBackground(app_Context.getResources().getDrawable(R.drawable.rounded_item));
			eventer.setBackgroundColor(event_list.get(pos).getColor());
			return eventer;
		}
		else
		{
			convertView.setLongClickable(true);
			convertView.setBackground(app_Context.getResources().getDrawable(R.drawable.rounded_item));
			convertView.setBackgroundColor(event_list.get(pos).getColor());
			convertView.setFocusable(true);
			ToDoView t_view = (ToDoView)convertView;
			t_view.setEvent((Event)getItem(pos));
			return convertView;
		}
	}


	
}