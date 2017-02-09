package com.android.main.DataBaseHelper;

import com.android.main.Utils.ContentPlayedInfo;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 *
 *@author  Create by liangchangwei   
 *@data    2016年10月13日 --- 下午3:17:51
 *
 */
public class SqliteHelper extends SQLiteOpenHelper{

	private static final String TAG = "SqliteHelper";
	static final String TB_NAME = "content_played";

	public SqliteHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
        db.execSQL("CREATE TABLE IF NOT EXISTS " +
                TB_NAME + "(" +
                ContentPlayedInfo.ID + " integer primary key," +
                ContentPlayedInfo.USERNAME + " varchar,"+
                ContentPlayedInfo.PICPATH + " varchar,"+
                ContentPlayedInfo.MP3PATH + " varchar,"+
                ContentPlayedInfo.STARTTIME + " long"+
                ContentPlayedInfo.DURATION + " long"+
                ")"
        );
        Log.w(TAG,"Database onCreate");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		
	}

}
