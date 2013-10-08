package com.example.scheduler;

public class SQL_Data {
        
		/* Unsure if this coded is even needed */
		/* This is the data structure used by SQLite DB*/
		
        /* Class used for Getting Email Data*/
        
        private long counter; /* Used as Index of Database */
        private long id;
        private String name;
        private String desc;
        private int alarm;
        private int month;
        private int day;
        private int year;
        private int start_time;
        private int end_time;
		
		
		/* Mutators */
		public void setCounter (long c){
			this.counter = c;
		}	
		
		public void setId(long id){
			this.id = id;
		}
		
		public void setName (String name){
			this.name = name;
		}
		
		public void setDesc (String desc){
			this.desc = desc;
		}
		
		public void setAlarm (int a){
			this.alarm = a;
		}
		
		public void setMonth (int m){
			this.month = m;
		}
		
		public void setDay (int d){
			this.day = d;
		}
		
		public void setYear (int y){
			this.year = y;
		}
		
		public void setST (int st){
			this.start_time = st;
		}
		
		public void setET (int et){
			this.end_time = et;
		}
		

}