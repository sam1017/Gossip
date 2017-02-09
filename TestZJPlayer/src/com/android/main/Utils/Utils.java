package com.android.main.Utils;

import java.util.HashMap;

import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;

/**
 *
 *@author  Create by liangchangwei   
 *@data    2016年10月13日 --- 下午3:46:55
 *
 */
public class Utils {
	
	public static final int DEFAULT = 1;
	public static final int FOURPICTURE = 4;
	
	private static int DisplayType = DEFAULT;
    private static HashMap<Integer, Object> map = new HashMap<>();
	private static int mNumberOfPicture = 8;

	public static void setDisplayType(int type){
		if((type == DEFAULT)||(type == FOURPICTURE)){
			DisplayType = type;
		}
	}
	
	public static int getDisplayType(){
		return DisplayType;
	}
	
    public static void initCfgData() {
        ScaleAnimation scaleAnimation0 = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        scaleAnimation0.setDuration(500);
        map.put(0,scaleAnimation0);

        ScaleAnimation scaleAnimation1 = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f);
        scaleAnimation1.setDuration(500);
        map.put(1,scaleAnimation1);

        ScaleAnimation scaleAnimation2 = new ScaleAnimation(4.0f, 1.0f, 4.0f, 1.0f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        scaleAnimation2.setDuration(500);
        map.put(2,scaleAnimation2);

        ScaleAnimation scaleAnimation3 = new ScaleAnimation(4.0f, 1.0f, 4.0f, 1.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f);
        scaleAnimation3.setDuration(500);
        map.put(3,scaleAnimation3);

        ScaleAnimation scaleAnimation4 = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f,
                Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF, 1.0f);
        scaleAnimation4.setDuration(500);
        map.put(4,scaleAnimation4);

        ScaleAnimation scaleAnimation5 = new ScaleAnimation(4.0f, 1.0f, 4.0f, 1.0f,
                Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF, 1.0f);
        scaleAnimation5.setDuration(500);
        map.put(5,scaleAnimation5);

        TranslateAnimation animation_translate0 = new TranslateAnimation(-480f,0f,-854f,0f);
        animation_translate0.setDuration(500);
        map.put(6,animation_translate0);

        TranslateAnimation animation_translate1 = new TranslateAnimation(480f,0f,854f,0f);
        animation_translate1.setDuration(500);
        map.put(7,animation_translate1);

        TranslateAnimation animation_translate2 = new TranslateAnimation(-480f,0f,854f,0f);
        animation_translate2.setDuration(500);
        map.put(8,animation_translate2);

        TranslateAnimation animation_translate3 = new TranslateAnimation(480f,0f,-854f,0f);
        animation_translate3.setDuration(500);
        map.put(9,animation_translate3);
    }
    
    public static int getTotalAnimaNum(){
		return map.size();
    }
    public static int getTotalPicture(){
		return mNumberOfPicture ;
    }
    public static HashMap<Integer, Object> getmap(){
    	return map;
    }

}
