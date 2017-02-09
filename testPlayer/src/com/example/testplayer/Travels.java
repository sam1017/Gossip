package com.example.testplayer;

import java.util.ArrayList;
import java.util.List;

/**
 *
 *@author  Create by liangchangwei   
 *@data    2016年11月29日 --- 下午6:00:39
 *
 */
public class Travels {

	  public static final List<Data> IMG_DESCRIPTIONS = new ArrayList<Data>();

	  static {
	    Travels.IMG_DESCRIPTIONS.add(new Travels.Data("pic_1.jpg"));
	    Travels.IMG_DESCRIPTIONS.add(new Travels.Data("pic_2.jpg"));
	    Travels.IMG_DESCRIPTIONS.add(new Travels.Data("pic_3.jpg"));
	    Travels.IMG_DESCRIPTIONS.add(new Travels.Data("pic_4.jpg"));
	    Travels.IMG_DESCRIPTIONS.add(new Travels.Data("pic_5.jpg"));
	  }

	  public static final class Data {

	    public final String imageFilename;

	    private Data(String imageFilename) {
	      this.imageFilename = imageFilename;
	    }
	  }
	}

