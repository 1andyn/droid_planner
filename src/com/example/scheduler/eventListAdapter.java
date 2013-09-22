package com.example.scheduler;

import java.util.List;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class eventListAdapter extends BaseAdapter {

	//Context in which eventListAdapter is being used
	private Context app_Context;
	private List<Event> event_list;
	
	public eventListAdapter(Context c, List<Event> e_list)
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
		return pos; //modify event IDs to have a certain unique ID
	}

	@Override
	public View getView(int apos, View convertView, ViewGroup parent) {
		if(convertView == null)
		{
			
		}
		return null;
	}


	
}
