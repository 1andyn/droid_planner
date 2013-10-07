package com.example.scheduler;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Window;
import android.os.Bundle;

public class Add_activity  extends SherlockFragmentActivity {

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
	

	protected void add_event()
	{

	}


}
