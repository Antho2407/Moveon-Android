package com.applicationmoveon.user;

import java.util.List;

import com.applicationmoveon.R;
import com.applicationmoveon.R.id;
import com.applicationmoveon.R.layout;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;


public class UserAdapter extends BaseAdapter{


	public static class UserData
	{

		public String userFirstname;
		public String userName;
		public int eventOwned;
		public Drawable picture;
		public boolean followed;


		public UserData(String userFirstname, String userName,
				int eventOwned, Drawable picture, boolean followed) {
			this.userFirstname = userFirstname;
			this.userName = userName;
			this.eventOwned = eventOwned;
			this.picture = picture;
			this.followed=followed;
		}


	}

	private Context _context;
	private List<UserData> _data;



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
		UserData data = _data.get(position);

		if(view == null)
		{
			LayoutInflater inflater = (LayoutInflater) _context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = inflater.inflate(R.layout.item_user_list, parent, false);
		}

		ImageView picture = (ImageView) view.findViewById(R.id.user_pic);
		TextView login = (TextView) view.findViewById(R.id.user_login);
		TextView eventNb = (TextView) view.findViewById(R.id.user_nb_event);
		CheckBox followed = (CheckBox) view.findViewById(R.id.user_followed);
		
		picture.setImageDrawable(data.picture);
		login.setText(data.userFirstname+ " "+ data.userName);
		eventNb.setText("Nombres d'evenements crées : "+data.eventOwned);
		followed.setChecked(data.followed);

		
		if(position % 2 == 0)
			view.setBackgroundColor(Color.argb(255, 245, 245, 245));
		else
			view.setBackgroundColor(Color.WHITE);

		return view;
	}

	public UserAdapter(Context context, List<UserData> data)
	{
		_context = context;
		_data = data;
	}

}
