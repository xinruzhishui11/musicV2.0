package com.example.music2;

import java.util.List;
import java.util.zip.Inflater;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class MusicAdapter extends MyBaseAdapter<Music> {

	public MusicAdapter(Context context, List<Music> data) {
		super(context, data);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Music music=getData().get(position);
		
		ViewHolder holder;
		if(convertView == null){
			convertView = getLayoutInflater().inflate(R.layout.music_item, null);
			
			holder =new ViewHolder();
			holder.tvTitle=(TextView) convertView.findViewById(R.id.tv_title);
			holder.tvPath=(TextView) convertView.findViewById(R.id.tv_path);
			convertView.setTag(holder);
			
		}else{
			holder=(ViewHolder) convertView.getTag();
		}
		
		holder.tvTitle.setText(music.getTitle());
		holder.tvPath.setText(music.getPath());
		Log.d("edu", "position---"+convertView.hashCode());
		return convertView;
	}
	
	class ViewHolder{
		TextView tvTitle;
		TextView tvPath;
	}

}
