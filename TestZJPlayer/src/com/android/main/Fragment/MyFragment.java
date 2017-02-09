package com.android.main.Fragment;

import com.android.main.R;
import com.android.main.Utils.Utils;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 *
 *@author  Create by liangchangwei   
 *@data    2016年10月13日 --- 下午4:52:01
 *
 */
public class MyFragment extends Fragment{

    private static final String TAG = "MyFragment";
	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
        Log.w(TAG,"onCreate");
		super.onCreate(savedInstanceState);
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = null;
		if(Utils.getDisplayType() == Utils.DEFAULT){
			view =  inflater.inflate(R.layout.fragment_layout, container, false);
		}else if(Utils.getDisplayType() == Utils.FOURPICTURE){
			view =  inflater.inflate(R.layout.fragment_layout4, container, false);
		}
		return view;
	}
}
