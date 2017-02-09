package com.android.gossip;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 *
 *@author  Create by liangchangwei   
 *@data    2017年1月17日 --- 上午10:56:51
 *
 */
public class GossipView extends View{

	private static final String TAG = "GossipView";
	private Paint mPaint;

	public GossipView(Context context) {
		this(context, null);
		// TODO Auto-generated constructor stub
	}

	public GossipView(Context context, AttributeSet attrs)
	{
		this(context, attrs, 0);
	}

	public GossipView(Context context, AttributeSet attrs, int defStyle){
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
        int canvasWidth = canvas.getWidth();
        int canvasHeight = canvas.getHeight();
        RectF rectF = new RectF(0, 0, canvasWidth, canvasHeight);
        Log.w(TAG,"canvasWidth = " + canvasWidth + " canvasHeight = " + canvasHeight);
        Log.w(TAG,"getMeasuredWidth() = " + getMeasuredWidth() + " getMeasuredHeight() = " + getMeasuredHeight());
        //mPaint.setStyle(Paint.Style.STROKE);//设置画笔为画线条模式
        //mPaint.setStrokeWidth(1);//设置线宽
        //mPaint.setColor(Color.BLACK);//设置线条颜色
        //canvas.translate(0, quarter / 4);
        //canvas.drawOval(rectF, mPaint);
        //canvas.drawRect(0, 0, getMeasuredWidth(), getMeasuredHeight(), mPaint);

		mPaint.setColor(Color.BLACK);
		mPaint.setStyle(Paint.Style.FILL);
		canvas.drawArc(rectF, 0, 180, true, mPaint);
		mPaint.setColor(Color.WHITE);
		mPaint.setStyle(Paint.Style.FILL);
		canvas.drawArc(rectF, 180, 180, true, mPaint);
		
		canvas.save();
		canvas.translate(canvasWidth/2, canvasWidth/4);
		rectF = new RectF(0, 0, canvasWidth/2, canvasWidth/2);
		mPaint.setColor(Color.WHITE);
		mPaint.setStyle(Paint.Style.FILL);
		canvas.drawArc(rectF, 0, 180, true, mPaint);
		canvas.restore();
		
		canvas.save();
		canvas.translate(canvasWidth*2/3, canvasWidth*5/12);
		rectF = new RectF(0, 0, canvasWidth/6, canvasWidth/6);
		mPaint.setColor(Color.BLACK);
		mPaint.setStyle(Paint.Style.FILL);
        canvas.drawOval(rectF, mPaint);
		canvas.restore();
		
		canvas.save();
		canvas.translate(0, canvasWidth/4);
		rectF = new RectF(0, 0, canvasWidth/2, canvasWidth/2);
		mPaint.setColor(Color.BLACK);
		mPaint.setStyle(Paint.Style.FILL);
		canvas.drawArc(rectF, 180, 180, true, mPaint);
		canvas.restore();

		canvas.save();
		canvas.translate(canvasWidth/6, canvasWidth*5/12);
		rectF = new RectF(0, 0, canvasWidth/6, canvasWidth/6);
		mPaint.setColor(Color.WHITE);
		mPaint.setStyle(Paint.Style.FILL);
        canvas.drawOval(rectF, mPaint);
		canvas.restore();

	}

}
