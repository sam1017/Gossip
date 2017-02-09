package com.googlecode.simplex4android;

import java.util.ArrayList;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

/**	
 * Startet entweder 
 *		a) Activity um neues Problem anzulegen, oder 
 *		b) ListView um Probleme zu laden
 *		c) Tutorial (WebView)
 *	
 */	
public class simplex4android extends Activity {
	
	//Ressourcen
	private static ArrayList<Input> inputs;
	private static int id;
    
	/**
	 * Wird aufgerufen, wenn die Activity gestartet wird
	 * Hier werden alle Initialisierungen und UI Settings vorgenommen. 
	 * @param savedInstanceState 
	 */
    @SuppressWarnings("unchecked")
	@Override
    public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.main);
	    //this.getFileStreamPath("simplexProblems.dat").delete(); //Auskommentieren und Programm starten, um die "Datenbank" zu löschen
	    
	    //Ressourcen
	    final Button btn_current = (Button) findViewById(R.id.btn_current);
	    final Button btn_new = (Button) findViewById(R.id.btn_new);
    	final Button btn_load = (Button) findViewById(R.id.btn_load);
    	final Button btn_tutorial = (Button) findViewById(R.id.btn_tutorial);

	    //Behandlung der verschiedenen Intents
	    if(this.getIntent().getBooleanExtra("edit", false) == true){
		    inputs = (ArrayList<Input>) this.getIntent().getSerializableExtra("inputs");
		    id = this.getIntent().getIntExtra("id", -1);
	    	btn_current.setEnabled(true);
	    }
	    
	    //Aktuelles Problem - Button
	    btn_current.setOnClickListener(new OnClickListener() {
	    	public void onClick(View v){
	    		Intent InputCreateIntent = new Intent().setClassName("com.googlecode.simplex4android", "com.googlecode.simplex4android.InputsShow");
	        	InputCreateIntent.putExtra("edit", true);
	    		InputCreateIntent.putExtra("inputs", inputs);
	    		InputCreateIntent.putExtra("id", id);
	    		startActivity(InputCreateIntent);
	    	}
	    });
	    
	    //Neues Problem anlegen - Button
	    btn_new.setOnClickListener(new OnClickListener() {
	    	public void onClick(View v){
	        	Intent InputCreateIntent = new Intent().setClassName("com.googlecode.simplex4android", "com.googlecode.simplex4android.InputsShow");
	        	InputCreateIntent.putExtra("create", true);
	        	startActivity(InputCreateIntent);
	    	}
	    });
	    	    
	    //Problem laden - Button
	    btn_load.setOnClickListener(new OnClickListener() {
	        public void onClick(View v) {
	        	Intent InputsLoadIntent = new Intent().setClassName("com.googlecode.simplex4android", "com.googlecode.simplex4android.ProblemsLoad");
	        	startActivity(InputsLoadIntent);
	        }
	    });
	    
	    //Tutorial - Button
	    btn_tutorial.setOnClickListener(new OnClickListener() {
	        public void onClick(View v) {
	        	Intent TutorialIntent = new Intent();
	            TutorialIntent.setClassName("com.googlecode.simplex4android", "com.googlecode.simplex4android.TutorialShow");
	        	startActivity(TutorialIntent);
	        }
	    });
    }    
}
	    
