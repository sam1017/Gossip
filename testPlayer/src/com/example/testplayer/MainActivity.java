package com.example.testplayer;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;

public class MainActivity extends Activity {

    private FlipViewController flipView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        flipView = new FlipViewController(this, FlipViewController.HORIZONTAL);
        flipView.setAdapter(new TravelAdapter(this));
        setContentView(flipView);
    }
	
	@Override
	protected void onResume() {
	    super.onResume();
	    flipView.onResume();
	}

	@Override
	protected void onPause() {
	    super.onPause();
	    flipView.onPause();
	}

}
