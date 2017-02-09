package com.example.testreadexcel;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.app.Activity;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.widget.TextView;
import jxl.*;

public class MainActivity extends Activity {
	private static final String TAG = "MainActivity";
	private TextView txt = null;
	private MediaPlayer mp;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.w(TAG,"onCreate");
		setContentView(R.layout.activity_main);
		txt = (TextView)findViewById(R.id.txt_show);
		txt.setMovementMethod(ScrollingMovementMethod.getInstance());
		readExcel();
		preparePlayMp3();
	}

	private void preparePlayMp3() {
		// TODO Auto-generated method stub
		Log.w(TAG,"preparePlayMp3");
		mp = new MediaPlayer();
		try {
			mp.setDataSource(this.getAssets().openFd("MP3/linjunjie_0.mp3").getFileDescriptor());
			mp.prepare();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		Log.w(TAG,"onResume");
		super.onResume();
		mp.start();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		Log.w(TAG,"onPause");
		super.onPause();
		mp.pause();
	}

	
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		Log.w(TAG,"onStop");
		super.onStop();
		//mp.stop();
	}

	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		Log.w(TAG,"onDestroy");
		super.onDestroy();
		mp.stop();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	public void readExcel() {
		  try {
		   //InputStream is = new FileInputStream("mnt/sdcard/test.xls");
		   Log.w(TAG,"readExcel");
		   InputStream is = this.getAssets().open("Test.xls");
		   //Log.w(TAG,"readExcel 1 Path = " + Path);
		   //InputStream is = getClass().getResourceAsStream(Path);
		   //Workbook book = Workbook.getWorkbook(new File("mnt/sdcard/test.xls"));
		   Log.w(TAG,"readExcel 1");
		   if(is == null){
    		   Log.w(TAG,"readExcel error");
    		   txt.setText("Can't not read file !");
    		   String fileNames[] =this.getAssets().list("");
    		   if(fileNames.length > 0){
    	            for (String fileName : fileNames) {
    	     		   Log.w(TAG,"readExcel fileName = " + fileName);
    	             }
    		   }
		   }else{
    		   Workbook book = Workbook.getWorkbook(is);
    		   Log.w(TAG,"readExcel 2");
    		   int num = book.getNumberOfSheets();
    		   Log.w(TAG,"readExcel num = " + num);
    		   txt.setText("the num of sheets is " + num+ "\n");
    		   // 获得第一个工作表对象
    		   Sheet sheet = book.getSheet(0);
    		   int Rows = sheet.getRows();
    		   int Cols = sheet.getColumns();
    		   Log.w(TAG,"readExcel Rows = " + Rows);
    		   Log.w(TAG,"readExcel Cols = " + Cols);
    		   txt.append("the name of sheet is " + sheet.getName() + "\n");
    		   txt.append("total rows is " + Rows + "\n");
    		   txt.append("total cols is " + Cols + "\n");
    		   for (int i = 0; i < Cols; ++i) { 
    			   for (int j = 0; j < Rows; ++j) {
    				   // getCell(Col,Row)获得单元格的值
    				   txt.append("contents:" + sheet.getCell(i,j).getContents() + "\n");
    			   }
    		   }
    		   book.close();
		  }
		  } catch (Exception e) {
		   System.out.println(e);
		  }
		}

}


