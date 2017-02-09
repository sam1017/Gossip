package com.example.testimagecliprect;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Region.Op;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

public class MainActivity extends Activity {
	
	protected static final String TAG = "MainActivity";
	protected static final int MAX_ALPHA_VALUE = 255;
	private int ScreenWidth = 0;
	private int ScreenHeight = 0;
	private int HalfWidth = 0;
	private int HalfHeight = 0;
    private Rect leftRect = null;
    private Rect rightRect = null;
    private Rect midRect = null;
    private int index = 0;
    private int mxindex = 0;
    private int myindex = 0;
    private int step = 0;
	private Handler mHandler;
	private int mAnimationtype = 0;
    private boolean isRun = false;  
    private boolean isFirstRun = false;  
	ImageView mImageView = null;
	private List<String>mPath = new ArrayList();
	private int mPicNumber = 9;
	private int mPicStart = 0;
	private RectF rectf = null;
	private Bitmap mBitmap = null;
	private Bitmap mNextBitmap = null;
	private Paint mPaint = null;
	private Paint mNextPaint = null;
	private int alphavalue = 0;
	
	
	private Runnable doStepToNextPicRunnable = new Runnable(){

		@Override
		public void run() {
			// TODO Auto-generated method stub
            if(isRun == true){  
                alphavalue += (int)(MAX_ALPHA_VALUE/30);
                if(alphavalue > MAX_ALPHA_VALUE){
                	alphavalue = MAX_ALPHA_VALUE;
                }
                mNextPaint.setAlpha(alphavalue);
            	Log.w(TAG,"doStepToNextPicRunnable mNextPaint = " + alphavalue);
                //mPaint.setAlpha(MAX_ALPHA_VALUE - alphavalue);
            	Log.w(TAG,"doStepToNextPicRunnable index = " + index);
            	if((mAnimationtype == 0)||(mAnimationtype == 1)){
                    index += step;  
                    if(index >= HalfWidth){  
                    	isRun = false;  
                    }  
            	}else if((mAnimationtype == 2)||(mAnimationtype == 3)){
                    index += HalfHeight/30;  
                    if(index >= HalfHeight){  
                    	isRun = false;  
                    }  
            	}else if(mAnimationtype == 4){
            		mxindex += step;
            		myindex += HalfHeight/30;
            		if((mxindex >= HalfWidth)||(myindex >= HalfHeight)){
                    	isRun = false;  
            		}
            	}else if(mAnimationtype == 5){
            		index += ScreenWidth/30;
            		if(index >= ScreenWidth){
                    	isRun = false;  
            		}
            	}else if(mAnimationtype == 6){
            		index += HalfHeight/30;
            		if(index >= ScreenHeight){
                    	isRun = false;  
            		}
            	}
                mImageView.postInvalidate();  
                //mHandler.postDelayed(this, 10);
                mHandler.post(this);
            }else{
            	Log.w(TAG,"1 doStepToNextPicRunnable isRun = " + isRun + " index = " + index + " mAnimationtype = " + mAnimationtype);
            	index = 0;
            	mxindex = 0;
            	myindex = 0;
            	alphavalue = 0;
            	isRun = true;
            	//isFirstRun = true;
                double num = Math.random()*1000;
                mAnimationtype = (int)num%7;
            	Log.w(TAG,"doStepToNextPicRunnable: mAnimationtype = " + mAnimationtype);

            	try {
					mBitmap = BitmapFactory.decodeStream(getAssets().open(mPath.get(mPicStart%mPicNumber)));
					mNextBitmap = BitmapFactory.decodeStream(getAssets().open(mPath.get((mPicStart+1)%mPicNumber)));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            	Log.w(TAG,"2 doStepToNextPicRunnable isRun = " + isRun + " index = " + index + " mAnimationtype = " + mAnimationtype);
            	mPicStart++;
                mHandler.postDelayed(this, 3000);
            }
		}
		
	};

	
	public class ImageClipRect extends ImageView {

		public ImageClipRect(Context context) {
			super(context);
			// TODO Auto-generated constructor stub
		}

		/* (non-Javadoc)
		 * @see android.widget.ImageView#onDraw(android.graphics.Canvas)
		 */
		@SuppressLint("DrawAllocation")
		@Override
		protected void onDraw(Canvas canvas) {
			// TODO Auto-generated method stub
			if(isFirstRun){
				isFirstRun = false;
				return;
			}
			if(mAnimationtype == 0){
				canvas.drawBitmap(mBitmap, null, rectf, mPaint);
				canvas.save();
				leftRect.set(0, 0, index, ScreenHeight);
				canvas.clipRect(leftRect);
				rightRect.set(ScreenWidth-index, 0, ScreenWidth, ScreenHeight);
				canvas.clipRect(rightRect, Op.UNION);
				canvas.drawBitmap(mNextBitmap, null, rectf, mNextPaint);
				canvas.restore();
			}else if(mAnimationtype == 1){
				canvas.drawBitmap(mBitmap, null, rectf, mPaint);
				canvas.save();
				midRect.set(HalfWidth-index, 0, HalfWidth + index, ScreenHeight);
				canvas.clipRect(midRect);
				canvas.drawBitmap(mNextBitmap, null, rectf, mNextPaint);
				canvas.restore();
			}else if(mAnimationtype == 2){
				canvas.drawBitmap(mBitmap, null, rectf, mPaint);
				canvas.save();
				leftRect.set(0, 0, ScreenWidth, index);
				canvas.clipRect(leftRect);
				rightRect.set(0, ScreenHeight - index, ScreenWidth, ScreenHeight);
				canvas.clipRect(rightRect, Op.UNION);
				canvas.drawBitmap(mNextBitmap, null, rectf, mNextPaint);
				canvas.restore();
			}else if(mAnimationtype == 3){
				canvas.drawBitmap(mBitmap, null, rectf, mPaint);
				canvas.save();
				midRect.set(0, HalfHeight-index, ScreenWidth, HalfHeight + index);
				canvas.clipRect(midRect);
				canvas.drawBitmap(mNextBitmap, null, rectf, mNextPaint);
				canvas.restore();
			}else if(mAnimationtype == 4){
				canvas.drawBitmap(mBitmap, null, rectf, mPaint);
				canvas.save();
				midRect.set(HalfWidth - mxindex, HalfHeight-myindex, HalfWidth + mxindex, HalfHeight + myindex);
				canvas.clipRect(midRect);
				canvas.drawBitmap(mNextBitmap, null, rectf, mNextPaint);
				canvas.restore();
			}else if(mAnimationtype == 5){
				canvas.drawBitmap(mBitmap, null, rectf, mPaint);
				canvas.save();
				int tempheight = ScreenHeight/16;
				leftRect.set(0, 0, index, tempheight);
				canvas.clipRect(leftRect);
				rightRect.set(ScreenWidth-index, tempheight, ScreenWidth, 2*tempheight);
				canvas.clipRect(rightRect, Op.UNION);
				for(int i = 1; i < 8; i ++){
					leftRect.set(0, 2*i*tempheight, index, (2*i+1)*tempheight);
					canvas.clipRect(leftRect,Op.UNION);
					rightRect.set(ScreenWidth-index, (2*i+1)*tempheight, ScreenWidth, (2*i+2)*tempheight);
					canvas.clipRect(rightRect, Op.UNION);
				}
				canvas.drawBitmap(mNextBitmap, null, rectf, mNextPaint);
				canvas.restore();
			}else if(mAnimationtype == 6){
				canvas.drawBitmap(mBitmap, null, rectf, mPaint);
				canvas.save();
				int tempheight = ScreenWidth/16;
				leftRect.set(0, 0, tempheight, index);
				canvas.clipRect(leftRect);
				rightRect.set(tempheight, ScreenHeight - index, 2*tempheight, ScreenHeight);
				canvas.clipRect(rightRect, Op.UNION);
				for(int i = 1; i < 8; i ++){
					leftRect.set(2*i*tempheight, 0, (2*i+1)*tempheight, index);
					canvas.clipRect(leftRect,Op.UNION);
					rightRect.set((2*i+1)*tempheight, ScreenHeight - index,(2*i+2)*tempheight,ScreenHeight);
					canvas.clipRect(rightRect, Op.UNION);
				}
				canvas.drawBitmap(mNextBitmap, null, rectf, mNextPaint);
				canvas.restore();
			}
		}
		
		

	}


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        mImageView = new ImageClipRect(this);
		WindowManager wm = (WindowManager)getSystemService(Context.WINDOW_SERVICE);
		ScreenWidth = wm.getDefaultDisplay().getWidth();
		ScreenHeight = wm.getDefaultDisplay().getHeight();
		rectf = new RectF(0, 0, ScreenWidth, ScreenHeight);
		if(ScreenWidth != 0){
			HalfWidth = ScreenWidth/2;
			step = HalfWidth/30;
		}
		if(ScreenHeight != 0){
			HalfHeight = ScreenHeight/2;
		}
        leftRect = new Rect();
        rightRect = new Rect();
        midRect = new Rect();
        mHandler = new Handler();
        isFirstRun = true;
        mPaint = new Paint();
        mNextPaint = new Paint();
        initpath();
        
        setContentView(mImageView);
		//setContentView(R.layout.activity_main);
		//mImageView = (ImageView)this.findViewById(R.id.myimageview);
		//try {
		//	mImageView.setImageBitmap(BitmapFactory.decodeStream(getAssets().open("pic2.jpg")));
		//} catch (IOException e) {
		//	// TODO Auto-generated catch block
		//	e.printStackTrace();
		//}
	}

	
	private void initpath() {
		// TODO Auto-generated method stub
		for(int i = 1; i <= mPicNumber; i++){
			mPath.add("pic" + i + ".jpg");
		}
		mPicStart = 0;
	}


	/* (non-Javadoc)
	 * @see android.app.Activity#onResume()
	 */
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		isFirstRun = false;
	    isRun = false;  
        mHandler.removeCallbacks(doStepToNextPicRunnable);
        mHandler.post(doStepToNextPicRunnable);
	}


	/* (non-Javadoc)
	 * @see android.app.Activity#onPause()
	 */
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
        mHandler.removeCallbacks(doStepToNextPicRunnable);
        Log.d(TAG,"onPause");
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
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
