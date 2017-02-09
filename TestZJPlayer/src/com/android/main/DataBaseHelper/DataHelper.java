package com.android.main.DataBaseHelper;

import java.util.ArrayList;
import java.util.List;

import com.android.main.Utils.ContentPlayedInfo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 *
 *@author  Create by liangchangwei   
 *@data    2016年10月13日 --- 下午3:47:34
 *
 */
public class DataHelper {
    private static final String TAG ="DataHelper" ;
    private static String DB_NAME = "userdata.db";
    private static int DB_VERSION = 2;
    private SQLiteDatabase db;
    private SqliteHelper dbHelper;

    public DataHelper(Context context){
        dbHelper = new SqliteHelper(context,DB_NAME,null,DB_VERSION);
        db = dbHelper.getWritableDatabase();
    }

    public void close(){
        db.close();
        dbHelper.close();
    }

    public List<ContentPlayedInfo> GetUserInfoList(){
        List<ContentPlayedInfo> userList = new ArrayList<ContentPlayedInfo>();
        Cursor cursor = db.query(SqliteHelper.TB_NAME,null,null,null,null,null,ContentPlayedInfo.ID + " DESC");
        cursor.moveToFirst();
        while(!cursor.isAfterLast()&&(cursor.getString(3)!=null)){
        	ContentPlayedInfo user = new ContentPlayedInfo();
            user.setUsername(cursor.getString(1));
            user.setPic_path(cursor.getString(2));
            user.setMp3_path(cursor.getString(3));
            user.setStart_time(cursor.getLong(4));
            user.setDuration(cursor.getLong(5));
            userList.add(user);
            cursor.moveToNext();
        }
        cursor.close();
        return userList;
    }

    public long SaveUserInfo(ContentPlayedInfo user){
        ContentValues values = new ContentValues();
        values.put(ContentPlayedInfo.USERNAME, user.getUsername());
        values.put(ContentPlayedInfo.PICPATH,user.getPic_path());
        values.put(ContentPlayedInfo.MP3PATH,user.getMp3_path());
        values.put(ContentPlayedInfo.STARTTIME,user.getStart_time());
        values.put(ContentPlayedInfo.DURATION,user.getDuration());
        Log.w(TAG,"UserInfo = " + user);
        long uid = db.insert(SqliteHelper.TB_NAME,ContentPlayedInfo.ID,values);
        return uid;
    }

    public int DelUserInfo(String username) {
        int id = db.delete(SqliteHelper.TB_NAME,
        		ContentPlayedInfo.USERNAME + "=?", new String[]{username});
        Log.e(TAG, id + "");
        return id;
    }

    public static ContentPlayedInfo getUserByName(String userName,List<ContentPlayedInfo> userList){
    	ContentPlayedInfo userInfo = null;
        int size = userList.size();
        Log.w(TAG,"userList.size() = " + userList.size());
        for( int i=0;i<size;i++){
            if(userName.equals(userList.get(i).getUsername())){
                userInfo = userList.get(i);
                break;
            }
        }
        Log.w(TAG,"getUserByName userName = " + userName + " userInfo = " + userInfo);
        return userInfo;
    }

    public Boolean HaveUserInfo(String userName) {
        Boolean b = false;
        Cursor cursor = db.query(SqliteHelper. TB_NAME, null, ContentPlayedInfo.USERNAME
                + "=?", new String[]{userName}, null, null, null );
        b = cursor.moveToFirst();
        Log. e("HaveUserInfo", b.toString());
        cursor.close();
        return b;
    }

    public Boolean HavePicInfo(String picpath) {
        Boolean b = false;
        Cursor cursor = db.query(SqliteHelper. TB_NAME, null, ContentPlayedInfo.PICPATH
                + "=?", new String[]{picpath}, null, null, null );
        b = cursor.moveToFirst();
        Log. e("HaveUserInfo", b.toString());
        cursor.close();
        return b;
    }

}

