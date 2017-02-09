package com.googlecode.simplex4android;

import java.util.ArrayList;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Activity zum Anzeigen einer Liste von Problemen (anhand ihrer Zielfunktionen), die aus einer ProblemsDb geladen wurden.
 * Probleme können per Klick auf die entsprechenden Buttons geladen und gelöscht werden.
 * @author Sebastian Hanschke
 *
 */
public class ProblemsLoad extends Activity {
	
	//Ressourcen
	private static ArrayAdapter<String> adapter_list_problems;
	private ArrayList<ArrayList<Input>> listOfInputs;
	private ProblemsDb data;

	/**
	 * Wird aufgerufen, wenn die Activity gestartet wird
	 * Hier werden alle Initialisierungen und UI Settings vorgenommen. 
	 * @param savedInstanceState 
	 */
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	setContentView(R.layout.problems_load);

    	//Ressourcen
    	final ListView lv_problems = (ListView) findViewById(R.id.list_problems);
    	final Button btn_cancel = (Button) findViewById(R.id.btn_cancel);
    	final Button btn_new_problem= (Button) findViewById(R.id.btn_new_problem);

    	try {
    		data = new ProblemsDb(ProblemsLoad.this);
    	} catch (Exception ex) {
    		ex.printStackTrace();
    	}

    	try{
    		adapter_list_problems = new ArrayAdapter<String>(this, R.layout.listview_problems, R.id.tv_row);
    		listOfInputs = data.getListOfInputs();
    		for(int i=0;i<listOfInputs.size();i++){ // durch alle Inputs
    			adapter_list_problems.add(listOfInputs.get(i).get(0).toString());
    		}
    	}catch(Exception ex){

    	}
    	lv_problems.setAdapter(adapter_list_problems);
    	lv_problems.refreshDrawableState();
    	hideOrShowEmptyText();

    	//Zurück-Button
    	btn_cancel.setOnClickListener(new OnClickListener() {
    		public void onClick(View v) {
    			finish();
    		}
    	});

    	//Neues-Problem-Button
    	btn_new_problem.setOnClickListener(new OnClickListener() {
    		public void onClick(View v) {
    			Intent InputCreateIntent = new Intent().setClassName("com.googlecode.simplex4android", "com.googlecode.simplex4android.InputsShow");
    			InputCreateIntent.putExtra("create", true);
    			startActivity(InputCreateIntent);
    			finish();
    		}
    	});

    }

    /**
     * Handelt Klicks auf den Löschen-Button eines Problems
     * @param v
     */
    public void DeleteClickHandler(View v){
    	ListView lv_problems = (ListView) findViewById(R.id.list_problems);
    	RelativeLayout rl_row = (RelativeLayout)v.getParent();
    	int position = lv_problems.indexOfChild(rl_row);
    	adapter_list_problems.remove(adapter_list_problems.getItem(position));
    	try{
    		data.removeProblem(ProblemsLoad.this, position);
    	}
    	catch(Exception ex){
    	}	
    	hideOrShowEmptyText();
    }

    /**
     * Handelt Klicks auf den Edit-Button eines Problems (das Problem wird in InputsShow geöffnet)
     * @param v
     */
    public void EditClickHandler(View v)
    {
    	ListView lv_problems = (ListView) findViewById(R.id.list_problems);
    	RelativeLayout rl_row = (RelativeLayout)v.getParent();
    	int position = lv_problems.indexOfChild(rl_row);
    	ArrayList<Input> inputs = data.getProblem(position);
    	Intent InputsEditIntent = new Intent().setClassName("com.googlecode.simplex4android", "com.googlecode.simplex4android.InputsShow");
    	InputsEditIntent.putExtra("inputs", inputs);
    	InputsEditIntent.putExtra("edit", true);
    	InputsEditIntent.putExtra("id", position);
    	startActivity(InputsEditIntent);
    }

	/**
	 * Blendet die Meldung "Keine Probleme vorhanden. Bitte anlegen!" (text_list_empty) ein oder aus.
	 */
    private void hideOrShowEmptyText(){
    	TextView text_list_empty = (TextView) findViewById(R.id.text_list_empty);
    	ViewGroup.LayoutParams params_text_list_empty = text_list_empty.getLayoutParams();
    	if(!data.getListOfInputs().isEmpty()){
    		params_text_list_empty.height = 0;
    	}
    	else{
    		params_text_list_empty.height = ViewGroup.LayoutParams.WRAP_CONTENT;
    	}
    	text_list_empty.requestLayout();
    }
}
