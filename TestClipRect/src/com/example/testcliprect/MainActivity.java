package com.example.testcliprect;

import java.io.IOException;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Region.Op;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

public class MainActivity extends Activity {

	public Bitmap mBitmap;
	public Bitmap mNextBitmap;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(new LineView(this));
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onResume()
	 */
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onPause()
	 */
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}
	
	public class LineView extends View {
	    protected static final String TAG = "LineView";
		private int width = 0;  
	    private int height = 0;  
	    private int index = 0;
	    private boolean isRun = false;  
	    private boolean isFirstRun = false;  
	    private Rect leftRect = null;
	    private Rect rightRect = null;
		private Handler mHandler;
		private int pointWidth = 0;  
		private int pointHeight = 0;
		private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		private Paint mNextPaint = new Paint();
		private Path mPath ;
		private Path mNextPath;
		private float radius = 0.0f;
		
		public LineView(Context context) {
			super(context);
			// TODO Auto-generated constructor stub
			WindowManager wm = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
	        width = wm.getDefaultDisplay().getWidth();
	        height = wm.getDefaultDisplay().getHeight();	
	        pointWidth = width / 2;  
	        pointHeight = height / 2;
	        radius = (float) Math.sqrt(pointWidth*pointWidth + pointHeight*pointHeight);
	        Log.w(TAG,"pointWidth = " + pointWidth + " pointHeight = " + pointHeight + " radius = " + radius);
	        leftRect = new Rect();
	        rightRect = new Rect();
	        mHandler = new Handler();
	        isFirstRun = false;
			try {
				mBitmap = BitmapFactory.decodeStream(getAssets().open("pic1.jpg"));
				mNextBitmap = BitmapFactory.decodeStream(getAssets().open("pic2.jpg"));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    }

		
	    @Override  
	    protected void onDraw(Canvas canvas) {
	    	//mPaint.setColor(Color.rgb(220, 220, 220));  
	        //canvas.drawCircle(pointWidth, pointHeight, pointWidth, mPaint);

	        //mPaint.setColor(Color.RED);
	    	canvas.drawBitmap(mBitmap, null, new Rect(0,0,width,height), new Paint());
	        RectF f = new RectF(pointWidth - radius, pointHeight - radius, pointWidth + radius, pointHeight + radius);
	        //mPath.addArc(f, 0, 180);
	        //canvas.clipPath(mPath);
	        //mPath.addArc(f,135,180);
	        //canvas.clipPath(mPath, Op.REPLACE);
	        canvas.save();
	        mPath = new Path();
	        mPath.addArc(f, index, 180);
	        canvas.clipPath(mPath);
	        mNextPath = new Path();
	        mNextPath.addArc(f,-index,180);
	        canvas.clipPath(mNextPath, Op.XOR);
	        //canvas.drawColor(Color.RED);
	    	canvas.drawBitmap(mNextBitmap, null, new Rect(0,0,width,height), new Paint());
	    	canvas.restore();
	        mPath.close();
	        mNextPath.close();
;	        //canvas.drawArc(f, -90f, 135, true, mPaint);
	        /*
	        canvas.drawColor(Color.BLUE); 
	        canvas.save();
	        leftRect.set(0, 0, index, height);
	        canvas.clipRect(leftRect);
	        canvas.drawColor(Color.GREEN);
	        canvas.restore();
	        canvas.save();
	        rightRect.set(width-index,0,width,height);
	        canvas.clipRect(rightRect);
	        canvas.drawColor(Color.GREEN);
	        canvas.restore();
	        */
	    	Log.w(TAG,"onDraw index = " + index);
	        if(isFirstRun == false){
	            moveView();
	            isFirstRun = true;
	            isRun = isFirstRun;
	        }
	    }  
	    
	    public void moveView(){  
	        mHandler.post(moveThread);
	    }  
	    
	    private final Runnable moveThread = new Runnable() {  
	          
	        @Override  
	        public void run() {  
	        	Log.w(TAG,"moveThread isRun = " + isRun);
	            if(isRun == true){  
	                index +=2;  
	            	Log.w(TAG,"moveThread index = " + index);
	                if(index >= 90){  
	                	isRun = false;  
	                }  
	                postInvalidate();  
	                mHandler.postDelayed(this, 15);
	            }else{
	            	Log.w(TAG,"moveThread isRun = " + isRun + " index = " + index);
	            	index = 0;
	            	isRun = true;
	            	Bitmap tempBitmap = mBitmap;
	            	mBitmap = mNextBitmap;
	            	mNextBitmap = tempBitmap;
	                mHandler.postDelayed(this, 1000);
	            }
	        }  
	    };  
	}
}
