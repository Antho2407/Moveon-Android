package com.applicationmoveon.event;


import java.util.List;

import com.applicationmoveon.R;
import com.applicationmoveon.R.id;
import com.applicationmoveon.R.layout;


import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


public class EventAdapter extends BaseAdapter {


	public static class EventData
	{

		public int eventId;
		public String eventDescription;
		public String eventOwner;
		public int eventState;
		public String eventTitle;
		public String eventLocation;
		public String eventDateStart;
		public String eventDateEnd;
		public String eventHourStart;
		public String eventHourFinish;
		public int numberOfParticipants;
		public int numberOfPlaces;
		public String eventDateCreation;
		public float latitude;
		public float longitude;
		public float distance;
		public float temperature;
		public String url;

		public EventData(int eventId, String eventTitle, String eventLocation,
				String eventDescription, String eventDateStart,
				String eventHourStart, String eventHourFinish,
				int numberOfParticipants, String eventOwner, int eventState, String eventDateCreation, float latitude, float longitude,float temperature,String url) {
			this.eventId = eventId;
			this.eventDescription = eventDescription;	
			this.eventTitle = eventTitle;
			this.eventLocation = eventLocation;
			this.eventDateStart = eventDateStart;
			this.eventHourStart = eventHourStart;
			this.eventHourFinish = eventHourFinish;
			this.numberOfParticipants = numberOfParticipants;
			this.eventState = eventState;
			this.eventOwner = eventOwner;
			this.eventDateCreation = eventDateCreation;
			this.latitude = latitude;
			this.longitude = longitude;
			this.temperature = temperature;
			this.url = url;
		}

	}

	private Context _context;
	private List<EventData> _data;



	@Override
	public int getCount() {
		if(_data != null)
			return _data.size();
		else
			return 0;
	}


	@Override
	public Object getItem(int at) {
		if(_data != null && at >= 0 && at < _data.size())
			return _data.get(at);
		else
			return null;
	}


	@Override
	public long getItemId(int at) {
		return at;
	}


	@Override
	public View getView(int position, View view, ViewGroup parent) {
		EventData data = _data.get(position);

		if(view == null)
		{
			LayoutInflater inflater = (LayoutInflater) _context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = inflater.inflate(R.layout.item_event_list, parent, false);
		}

		TextView title = (TextView)view.findViewById(R.id.event_title);
		TextView date = (TextView)view.findViewById(R.id.event_date);
		TextView description = (TextView)view.findViewById(R.id.event_description);
		TextView part = (TextView)view.findViewById(R.id.event_participants);

		title.setText(data.eventTitle);
		date.setText("Le "+ data.eventDateStart +" ра "+ data.eventLocation + " de "+data.eventHourStart+" р "+data.eventHourFinish);
		//description.setText(data.eventDescription);
		part.setText(data.numberOfParticipants+" participants");

		if(position % 2 == 0)
			view.setBackgroundColor(Color.argb(255, 245, 245, 245));
		else
			view.setBackgroundColor(Color.WHITE);

		return view;
	}

	public EventAdapter(Context context, List<EventData> data)
	{
		_context = context;
		_data = data;
	}
	
}
