package com.example.testplayer;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.view.View;

/**
 *
 *@author  Create by liangchangwei   
 *@data    2016年11月29日 --- 下午3:53:30
 *
 */
public class GrabIt {

	  private GrabIt() {
	  }

	  public static Bitmap takeScreenshot(View view, Bitmap.Config config) {
	    int width = view.getWidth();
	    int height = view.getHeight();

	    if (view != null && width > 0 && height > 0) {
	      Bitmap bitmap = Bitmap.createBitmap(width, height, config);
	      Canvas canvas = new Canvas(bitmap);
	      view.draw(canvas);
	      //canvas.drawColor(Color.RED, PorterDuff.Mode.DARKEN); //NOTES: debug option
	      return bitmap;
	    } else {
	      return null;
	    }
	  }
	}

