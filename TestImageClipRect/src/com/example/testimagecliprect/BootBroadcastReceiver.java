package com.example.testimagecliprect;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/**
 *
 *@author  Create by liangchangwei   
 *@data    2016年12月22日 --- 下午12:10:15
 *
 */
public class BootBroadcastReceiver extends BroadcastReceiver{

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		String action = intent.getAction().toString();
		if(action.equals(Intent.ACTION_BOOT_COMPLETED)){
			//Toast.makeText(context, "boot completed action has got", Toast.LENGTH_LONG).show();
			Intent startintent = new Intent(context, MainActivity.class);
			startintent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(startintent);
		}
	}

}
