package com.example.scheduler;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Window;

import edu.calpoly.android.lab3.Joke;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;

public class Add_activity  extends SherlockFragmentActivity {
	/* Necessary Data for Resources */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initalizeLayout();
		
	}
	
	/* Configures everything Visual*/
	protected void initalizeLayout()
	{
		//config_actionbar();
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.creation_activity);
	}
	
	/* ActionBar Configuration */
	protected void config_actionbar()
	{
//		ActionBar ab = getSupportActionBar();
//		ab.setDisplayShowTitleEnabled(false); 
//		ab.setDisplayShowHomeEnabled(false);
		
		/* Will re-implement last
		LayoutInflater inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    View v =inflater.inflate(R.layout.top_bar_view, null, true);
	    ab.setCustomView(v);
		ab.setDisplayShowCustomEnabled(true);
		
		*/
	}
	
	protected void initAddJokeListeners() {
		button.setOnClickListener(new OnClickListener() {
		
			@Override
			public void onClick(View view) {
			if(edittext.getText().toString().trim().length() > 0)
			{
				Event temp = new Event();
//				temp.setJoke(m_vwJokeEditText.getText().toString());
//				temp.setAuthor(m_strAuthorName);
				add_event(temp);
			}
		  } 
		}
	); 
	}
	

	protected void add_event(Event ev)
	{

	}


}
