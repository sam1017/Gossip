package com.android.gossip;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity {
    protected static final String TAG = "MainActivity";
	public GossipView myView = null;
	public DisplayView mUpDisplayView = null;
    public int mDuration = 1000;
    public int mTimes = 10;
    public Button mStartButton,mStopButton;
    public TextView mUpText, mDownText;
    public String[] mText = {
    "Ç¬","À¤","Õð","Ùã","¿²","Àë","ôÞ","¶Ò"
    };
	private Handler mHandler;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		 setContentView(R.layout.activity_main);
		myView = (GossipView)this.findViewById(R.id.myview);
		
		final RotateAnimation animation =new RotateAnimation(0f,mTimes * 360f,Animation.RELATIVE_TO_SELF, 
				0.5f,Animation.RELATIVE_TO_SELF,0.5f); 		
		animation.setDuration(mTimes * mDuration);
		animation.setFillAfter(true);
		mUpText = (TextView)this.findViewById(R.id.uptext);
		mDownText = (TextView)this.findViewById(R.id.downtext);
		mUpDisplayView = (DisplayView)this.findViewById(R.id.upview);
		mHandler = new Handler();
		
		mStartButton = (Button)this.findViewById(R.id.startbutton);
		mStartButton.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//animation.setStartOffset(0);
				//animation.startNow();
				myView.startAnimation((Animation)animation);
		        Log.w(TAG,"mStartButton onClick animation = " + animation);
		        showText();
			}
			
		});
		
		mStopButton = (Button)this.findViewById(R.id.stopbutton);
		mStopButton.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
		        Log.w(TAG,"mStopButton onClick animation = " + animation);
		        myView.clearAnimation();
		        mUpText.setText("");
		        mDownText.setText("");
		        mHandler.removeCallbacks(showThread);
			}
			
		});
		
	}

	int i = 0;
	int step = 0;
	protected void showText() {
		// TODO Auto-generated method stub
		i = 0;
		step = 100;
		mHandler.postDelayed(showThread, step);
	}

    private final Runnable showThread = new Runnable() {  
        
        @Override  
        public void run() {  
        	Log.w(TAG,"showThread");
        	Log.w(TAG,"showText i = " + i + " step = " + step);
			double num = Math.random()*1000;
            int number = (int)num%(mText.length);
	        mUpText.setText(mText[number]);
			num = Math.random()*1000;
            number = (int)num%(mText.length);
	        mDownText.setText(mText[number]);
			if(i > (mTimes * mDuration)/2){
				step += 100;
			}
			i += step;
			if(i < mTimes * mDuration){
				mHandler.postDelayed(this, step);
			}
        }  
    };  
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onResume()
	 */
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		 if(getRequestedOrientation()!=ActivityInfo.SCREEN_ORIENTATION_PORTRAIT){
			  setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	    }
		super.onResume();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
