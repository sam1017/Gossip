package com.example.testplayer;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.DataSetObserver;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;

/**
 *
 *@author  Create by liangchangwei   
 *@data    2016年11月29日 --- 下午5:58:57
 *
 */
public class TravelAdapter extends BaseAdapter {

	  private static final String TAG = "TravelAdapter";

	private LayoutInflater inflater;

	  private int repeatCount = 100;

	  private List<Travels.Data> travelData;

	  public TravelAdapter(Context context) {
	    inflater = LayoutInflater.from(context);
	    travelData = new ArrayList<Travels.Data>(Travels.IMG_DESCRIPTIONS);
	  }

	  @Override
	  public int getCount() {
	    return travelData.size() * repeatCount;
	  }

	  public int getRepeatCount() {
	    return repeatCount;
	  }

	  public void setRepeatCount(int repeatCount) {
	    this.repeatCount = repeatCount;
	  }

	  @Override
	  public Object getItem(int position) {
	    return position;
	  }

	  @Override
	  public long getItemId(int position) {
	    return position;
	  }

	  @Override
	  public View getView(int position, View convertView, ViewGroup parent) {
	    View layout = convertView;
	    if (convertView == null) {
	      layout = inflater.inflate(R.layout.complex1, null);
	      Log.d(TAG,"created new view from adapter: " + position);
	    }

	    final Travels.Data data = travelData.get(position % travelData.size());


	    UI
	        .<ImageView>findViewById(layout, R.id.photo)
	        .setImageBitmap(IO.readBitmap(inflater.getContext().getAssets(), data.imageFilename));

	    return layout;
	  }

	  public void removeData(int index) {
	    if (travelData.size() > 1) {
	      travelData.remove(index);
	    }
	  }
}
