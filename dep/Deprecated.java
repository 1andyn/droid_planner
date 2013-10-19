	/** DEBUG VALUES */
	final int STR_TIME = 700;
	final int END_TIME = 1200;
	final int D_M = 9; // Test Month
	final int D_D = 28; //Test Day
	final int D_Y = 2013; //Test Year
	/** END DEBUG VALUES */

	
//	/** Debug Code *************************************************************/
//	
//	protected Event debug_fake_event()
//	{
//		Event temp = new Event();
//		temp.setAlarm("N");
//		//temp.setID(events.size());
//		temp.setID(System.currentTimeMillis()/1000);
//		temp.setName("Test Event " + events.size());
//		temp.setDescription(this.getString(R.string.test_desc));
//		temp.setDate(debug_fake_date());
//		return temp;
//	}
//	
//	protected Event debug_fake_todo()
//	{
//		Event temp = new Event();
//		temp.setAlarm("N");
//		//temp.setID(events.size());
//		temp.setID(System.currentTimeMillis()/1000);
//		temp.setName("Test Event " + todos.size());
//		temp.setDescription(this.getString(R.string.test_desc));
//		temp.setDate(debug_fake_tododate());
//		return temp;
//	}
//
//	protected Date debug_fake_date()
//	{
//		Random generator = new Random();
//		int start, end, dm, dd, dy;
//		start = generator.nextInt(2400);
//		end = generator.nextInt(2400);
//		dm = generator.nextInt(11)+1;
//		dd = generator.nextInt(30)+1;
//		dy = generator.nextInt(9999);
//		Date temp = new Date(start, end, dm, dd, dy);
//		return temp;
//	}
//	
//	protected Date debug_fake_tododate()
//	{
//		Random generator = new Random();
//		int start, dm, dd, dy;
//		start = generator.nextInt(2400);
//		dm = generator.nextInt(11)+1;
//		dd = generator.nextInt(30)+1;
//		dy = generator.nextInt(9999);
//		Date temp = new Date(start, NONE, dm, dd, dy);
//		return temp;
//	}
//	/** Debug Code */
//	
//	/** Debug Code */ // Needs to be rewritten for final version
//	protected void add_event()
//	{
//		events.add(debug_fake_event());
//		events_visible.add(debug_fake_event());
//		e_adapter.notifyDataSetChanged();
//		//Toast.makeText(Schedule.this,"Size of events Array: " + events.size(), Toast.LENGTH_SHORT).show();
//	}
//	/** Debug Code */
//	
//	/** Debug Code */ // Needs to be rewritten for final version
//	protected void add_todo()
//	{
//		todos.add(debug_fake_todo());
//		todos_visible.add(debug_fake_todo());
//		t_adapter.notifyDataSetChanged();
//		//Toast.makeText(Schedule.this,"Size of todo Array: " + events.size(), Toast.LENGTH_SHORT).show();
//	}
//	/** Debug Code ****************************************************/