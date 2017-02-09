package com.googlecode.simplex4android;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

/**
 * Activity zum Anzeigen des Tutorials
 * @author Sebastian Hanschke
 */
public class TutorialShow extends Activity {
	
	//Ressourcen
	WebView mWebView;

	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
    	setContentView(R.layout.tutorial_show);

    	mWebView = (WebView) findViewById(R.id.webview_tutorial);
    	mWebView.setWebViewClient(new WebViewClient());
    	mWebView.getSettings().setJavaScriptEnabled(true);
    	mWebView.getSettings().setBuiltInZoomControls(true);
		mWebView.loadUrl("file:///android_asset/tutorial.html");
    	
    	//mWebView.loadData(tutorial, "text/html", "utf-8");
    	
	    //Zurück-Button
    	final Button btn_back = (Button) findViewById(R.id.btn_back);
	    btn_back.setOnClickListener(new OnClickListener() {
	    	public void onClick(View v) {
	    		finish();
	    	}
	    });
	}	
}
