package com.android.gossip;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 *
 *@author  Create by liangchangwei   
 *@data    2017年1月18日 --- 上午10:06:03
 *
 */
public class DisplayView extends View{

	private static final String TAG = "DisplayView";
	private Paint mPaint;

	public DisplayView(Context context) {
		this(context, null);
		// TODO Auto-generated constructor stub
	}

	public DisplayView(Context context, AttributeSet attrs)
	{
		this(context, attrs, 0);
	}
	
	public DisplayView(Context context, AttributeSet attrs, int defStyle){
		// TODO Auto-generated constructor stub
		super(context, attrs, defStyle);
		mPaint = new Paint();
	}
	
	/* (non-Javadoc)
	 * @see android.view.View#onDraw(android.graphics.Canvas)
	 */
	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
        mPaint.setColor(Color.BLACK);//设置线条颜色
        canvas.drawRect(0, 0, getMeasuredWidth(), getMeasuredHeight(), mPaint);
	}

	
}
