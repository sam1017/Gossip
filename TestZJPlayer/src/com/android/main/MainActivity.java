package com.android.main;
/**
*
*@author  Create by liangchangwei   
*@data    2016年10月13日 --- 下午3:17:51
*
*/
import java.io.IOException;
import java.io.InputStream;

import com.android.main.Fragment.MyFragment;
import com.android.main.Utils.Utils;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.widget.ImageView;

public class MainActivity extends FragmentActivity {

	private static final String TAG = "MainActivity";
    private MyFragment mainPanel = null;
	private ImageView myImageView;
	private ImageView myImageView1;
	private ImageView myImageView2;
	private ImageView myImageView3;
	private ImageView myImageView4;
	private Handler mHandler;
	protected int currPicture = 0;
	AssetManager assetManager;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//setContentView(R.layout.activity_main);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        Utils.initCfgData();
        Fragment main = getSupportFragmentManager().findFragmentById(R.id.myfragment);
        Log.d(TAG,"onCreate main = " + main);
        mainPanel = (MyFragment)main;
        assetManager=this.getAssets();  
        mHandler = new Handler();

        if(Utils.getDisplayType() == Utils.DEFAULT){
            myImageView = (ImageView)mainPanel.getView().findViewById(R.id.exampleimage);
            //mHandler = new Handler();
            try {  
                InputStream in=assetManager.open("Pic/adv1.png");  
                //InputStream in=assetManager.open("img/main."+rs.get(position).get("moduleID").toString()+".png");  
                Bitmap bmp=BitmapFactory.decodeStream(in);  
                myImageView.setImageBitmap(bmp);  
            } catch (Exception e) {  
                // TODO: handle exception  
            }  
        }else if(Utils.getDisplayType() == Utils.FOURPICTURE){
            myImageView1 = (ImageView)mainPanel.getView().findViewById(R.id.exampleimage1);
            myImageView2 = (ImageView)mainPanel.getView().findViewById(R.id.exampleimage2);
            myImageView3 = (ImageView)mainPanel.getView().findViewById(R.id.exampleimage3);
            myImageView4 = (ImageView)mainPanel.getView().findViewById(R.id.exampleimage4);
            
            try {
				myImageView1.setImageBitmap(BitmapFactory.decodeStream(assetManager.open("Pic/adv1.png")));
				myImageView2.setImageBitmap(BitmapFactory.decodeStream(assetManager.open("Pic/adv2.png")));
				myImageView3.setImageBitmap(BitmapFactory.decodeStream(assetManager.open("Pic/adv3.png")));
				myImageView4.setImageBitmap(BitmapFactory.decodeStream(assetManager.open("Pic/adv4.png")));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
	}
	
    @Override
    protected void onPause() {
        super.onPause();
        mHandler.removeCallbacks(doStepToNextPicRunnable);
        Log.d(TAG,"onPause");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG,"onResume");
        mHandler.removeCallbacks(doStepToNextPicRunnable);
        mHandler.post(doStepToNextPicRunnable);
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG,"onStop");
    }

    private Runnable doStepToNextPicRunnable = new Runnable() {
        @Override
        public void run() {
            if (currPicture  <= Utils.getTotalPicture()) {
                double num = Math.random()*1000;
                int number = (int)num%(Utils.getTotalAnimaNum());
                Log.d(TAG,"mainPanel = " + mainPanel + " currPicture = " + currPicture + " map.size = " + Utils.getmap().size() + " num = " + num + " number = " + number);
                Animation animation = (Animation) Utils.getmap().get(number);
                if(mainPanel != null ){
            		if(Utils.getDisplayType() == Utils.DEFAULT){
                        myImageView.startAnimation((Animation)Utils.getmap().get(number));
                        try{
                            InputStream in=assetManager.open("Pic/adv"+ currPicture +".png");  
                            Bitmap bmp=BitmapFactory.decodeStream(in);  
                            myImageView.setImageBitmap(bmp);  
                        } catch (IOException e) {
            				// TODO Auto-generated catch block
            				e.printStackTrace();
            			}
            		}else if(Utils.getDisplayType() == Utils.FOURPICTURE){
                        myImageView1.startAnimation((Animation)Utils.getmap().get(number));
                        myImageView2.startAnimation((Animation)Utils.getmap().get(number));
                        myImageView3.startAnimation((Animation)Utils.getmap().get(number));
                        myImageView4.startAnimation((Animation)Utils.getmap().get(number));
                        try {
            				myImageView1.setImageBitmap(BitmapFactory.decodeStream(assetManager.open("Pic/adv"+ currPicture +".png")));
            				myImageView2.setImageBitmap(BitmapFactory.decodeStream(assetManager.open("Pic/adv"+ currPicture +".png")));
            				myImageView3.setImageBitmap(BitmapFactory.decodeStream(assetManager.open("Pic/adv"+ currPicture +".png")));
            				myImageView4.setImageBitmap(BitmapFactory.decodeStream(assetManager.open("Pic/adv"+ currPicture +".png")));
            			} catch (IOException e) {
            				// TODO Auto-generated catch block
            				e.printStackTrace();
            			}
            		}
                }
                currPicture ++;
                if(currPicture > Utils.getTotalPicture()){
                    currPicture = 1;
                }
                mHandler.postDelayed(this, 3000);
            }
        }
    };

}
