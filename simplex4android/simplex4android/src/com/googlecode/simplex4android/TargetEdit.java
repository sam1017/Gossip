package com.googlecode.simplex4android;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Selection;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

/**
 * Activity zum Erzeugen/Bearbeiten von Zielfunktionen (Targets),
 * @params TARGET_EDIT_INTENT (mit zu bearbeitender Zielfunktion) oder TARGET_CREATE_INTENT
 * @return TARGET_EDIT_RESULT oder TARGET_CREATE_RESULT mit der erstellten/bearbeiteten Zielfunktion
 * @author simplex4android: Sebastian Hanschke
 *
 */
public class TargetEdit extends Activity {

	//ResultCodes
	static final int CONSTRAINT_EDIT_RESULT = 1;
	static final int CONSTRAINT_CREATE_RESULT = 2;
	static final int TARGET_EDIT_RESULT = 3;
	static final int TARGET_CREATE_RESULT = 4;

	//RequestCodes
	static final int CONSTRAINT_EDIT_REQUEST = 1;
	static final int CONSTRAINT_CREATE_REQUEST = 2;
	static final int TARGET_EDIT_REQUEST = 3;
	static final int TARGET_CREATE_REQUEST = 4;
	
	//Ressourcen
	private static Target target = new Target();
	
	/**
	 * Wird aufgerufen, wenn die Activity gestartet wird
	 * Hier werden alle Initialisierungen und UI Settings vorgenommen. 
	 * @param savedInstanceState 
	 */
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.target_edit);

	    //Ressources
		final Spinner minmax = (Spinner) findViewById(R.id.spinner_minmax);
	    final EditText target_element = (EditText) findViewById(R.id.edittext_target_element);
	    final int[] keyboardButtons = {	R.id.btn_0, R.id.btn_1, R.id.btn_2, R.id.btn_3, R.id.btn_4, 
				R.id.btn_5, R.id.btn_6, R.id.btn_7, R.id.btn_8, R.id.btn_9,
				R.id.btn_minus, R.id.btn_divide, R.id.btn_decimal, R.id.btn_backspace};
	    final Button x_plus = (Button) findViewById(R.id.btn_x_plus);
	    final Button x_minus = (Button) findViewById(R.id.btn_x_minus);
	    final Button add_target_element = (Button) findViewById(R.id.btn_add_target_element);
    	final Button add = (Button) findViewById(R.id.btn_add);
	    final Button back = (Button) findViewById(R.id.btn_back);

	    
	    //Behandlung der verschiedenen Intents
	    if(this.getIntent().getBooleanExtra("create", false) ==  true){
	    	target = new Target();
	    }
	    else if(this.getIntent().getBooleanExtra("edit", false) == true){
	    	target = (Target) this.getIntent().getSerializableExtra("target"); //Target laden
	    }
    
		//Spinner minmax
		ArrayAdapter<CharSequence> adapter_minmax = ArrayAdapter.createFromResource(this, R.array.spinner_minmax_values, android.R.layout.simple_spinner_item); 
		adapter_minmax.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		minmax.setAdapter(adapter_minmax);
	    minmax.setOnItemSelectedListener(new OnItemSelectedListener() {
		    public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		    	//Index "0" enspricht "min", "1" entspricht "max"
		    	////true bedeutet Minimierung, false bedeutet Maximierung
		    	if(arg3 == 0){
		    		target.setMinOrMax(true);
		    	}
		    	else{
		    		target.setMinOrMax(false);
		    	}
		    }
		    	 
		    public void onNothingSelected(AdapterView<?> arg0) {}
	    });
	    
	    //Textfeld Target-Element
	    target_element.setRawInputType(android.text.InputType.TYPE_NULL);
	    target_element.setOnFocusChangeListener(new OnFocusChangeListener(){
	    	public void onFocusChange(View v, boolean b){
	        	EditText text = (EditText) findViewById(R.id.edittext_target_element);
	    		if(b==true){
		    		findViewById(R.id.keyboard).setVisibility(View.VISIBLE);
	        		Selection.setSelection(text.getText(), text.length());
	    		}
	    		else{
		    		findViewById(R.id.keyboard).setVisibility(View.INVISIBLE);
	    		}
	    	}
		});
	    
	    //Keyboard-Button
		for(int i=0; i<keyboardButtons.length; i++){
			Button[] btns = new Button[keyboardButtons.length];
			btns[i] = (Button) findViewById(keyboardButtons[i]);
		    btns[i].setOnClickListener(new OnClickListener() {
		        public void onClick(View v) {
		        	EditText text = (EditText) findViewById(R.id.edittext_target_element);
		        	String newtext = text.getText().toString(); 
		        	if(v.getTag().equals("backspace")){
		        		if(newtext.length() > 0){
		        			newtext = newtext.substring(0, newtext.length()-1);
		        		}
		        	}
		        	else{
		        		newtext += v.getTag();
		        	}
		        	if(!SimplexLogic.checkInput(newtext)){
		        		text.setBackgroundResource(R.drawable.textfield_pressed_red);//Hintergrund rot
		        	}
		        	else{
		        		text.setBackgroundResource(R.drawable.textfield_default);
		        	}
	        		text.setText(newtext);
	        		Selection.setSelection(text.getText(), text.length());
		        }
		    });
		}
		
	    //"Xi erhöhen"-Button
	    OnClickListener x_plus_action = new OnClickListener(){
	    	public void onClick(View V){
	    		EditText target_element = (EditText) findViewById(R.id.edittext_target_element);
	    		increment_xi();
	    		try{
	    			target_element.requestFocus();
	    		}
	    		catch(Exception ex){
	    			Toast.makeText(TargetEdit.this,ex.getMessage(),Toast.LENGTH_LONG).show();
	    		}
	    	}
	    };
	    x_plus.setOnClickListener (x_plus_action);
	    
	    //"Xi verringern"-Button
	    x_minus.setOnClickListener (new OnClickListener(){
	    	public void onClick(View V){
	    		EditText edittext_x = (EditText) findViewById(R.id.edittext_x); 
	    		EditText target_element = (EditText) findViewById(R.id.edittext_target_element);
	    		int edittext_x_value = Integer.valueOf(edittext_x.getText().toString().substring(1)).intValue();
	    		if(edittext_x_value>1){
	    			edittext_x_value--;
	    			edittext_x.setText("x" + edittext_x_value);
		    		try{
		    			String target_value =String.valueOf(target.getValue(edittext_x_value-1));
		    			if(target_value.equals("0.0")){
		    				target_element.setText("");
		    				target_element.setHint("0");
		    			}
		    			else{
		    				target_element.setText(target_value);
			        		Selection.setSelection(target_element.getText(), target_element.length());
		    			}
		    		}
		    		catch(IndexOutOfBoundsException e){
		    			target_element.setHint("0");
		    		}
		    		catch(Exception e){
		    			
		    		}
	    		}
	    		try{
	    			target_element.requestFocus();
	    		}
	    		catch(Exception ex){
	    			Toast.makeText(TargetEdit.this,ex.getMessage(),Toast.LENGTH_LONG).show();
	    		}
	    	}
	    });

		//Hinzufügen-Button
	    add_target_element.setOnClickListener (new OnClickListener(){
	    	public void onClick(View v){
	        	EditText target_element = (EditText) findViewById(R.id.edittext_target_element);
	        	EditText target = (EditText) findViewById(R.id.edittext_target);
	        	if(SimplexLogic.checkInput(target_element.getText().toString())){
	        		double value;
	        		if(target_element.getText().toString().equals("")){
	        			value = 0.0;
	        		}
	        		else{
	        			try{
	        				value = Double.valueOf(target_element.getText().toString());
	        			}
	        			catch(Exception ex){
	        				try{
	        					value = Input.fractionToDbl(target_element.getText().toString());
	        				}
	        				catch(Exception ex2){
	        	    			Toast.makeText(TargetEdit.this,"Fehler beim Typecast!",Toast.LENGTH_LONG).show();
	        	    			return;
	        				}
	        			}
	        		}
	        		int i = Integer.valueOf(((EditText) findViewById(R.id.edittext_x)).getText().toString().substring(1)).intValue()-1;
		        	TargetEdit.target.setValue(i, value);
	    			target.setText(TargetEdit.target.valuesToString());
	    			increment_xi();
	        	}
	    		else{
	    			Toast.makeText(TargetEdit.this,"Fehlerhafte Eingabe! Bitte korrigieren!",Toast.LENGTH_LONG).show();
	    		}
	    	}
		});
	    
	    //Fertig-Button
	    add.setOnClickListener(new OnClickListener() {
	        public void onClick(View v) {
	    	    EditText target_element = (EditText) findViewById(R.id.edittext_target_element);
	        	if(target.getValues().isEmpty()){
	    			Toast.makeText(TargetEdit.this,"Eingabe unvollständig! Bitte mind. ein xi hinzufügen!",Toast.LENGTH_LONG).show();
        			target_element.setBackgroundResource(R.drawable.textfield_pressed_red);//Hintergrund rot
	        	}
	        	else{
	        		Intent TargetEditIntent = new Intent().putExtra("target", target);
	        		if(TargetEdit.this.getIntent().getBooleanExtra("edit", false)){
	        			setResult(TARGET_EDIT_RESULT, TargetEditIntent);
	        		}
	        		else{
	        			setResult(TARGET_CREATE_RESULT, TargetEditIntent);
	        		}
	        		finish();
	        	}
	        }
	    });
	    
	    //Zurück-Button
	    back.setOnClickListener(new OnClickListener() {
	        public void onClick(View v) {
	        	setResult(RESULT_CANCELED);
        		finish();
	        }
	    });
	    
	    //Target laden, falls "edit" == true
	    if(this.getIntent().getBooleanExtra("edit", false) == true){

	    //Textfeld Target
	    EditText edittext_target = (EditText) findViewById(R.id.edittext_target);
	    edittext_target.setText(target.valuesToString());

    	//Textfeld Target-Element
	    try{
	    	String target_element_string = String.valueOf(Math.round(target.getValue(0)*100.)/100.);
	    	if(target_element_string.equals("0.0")){
	    		target_element.setText("");
	    		target_element.setHint("0");
	    	}
	    	else{
	    		target_element.setText(target_element_string);
	    		Selection.setSelection(target_element.getText(), target_element.length());
	    	}
	    }
	    catch(Exception e){
	    	target_element.setText("");
	    	target_element.setHint("0");
	    }
	    
	    //Spinner minmax
    	//Index "0" enspricht "min", "1" entspricht "max"
    	////true bedeutet Minimierung, false bedeutet Maximierung
	    if(target.getMinOrMax()){
	    	minmax.setSelection(0);
	    }
	    else{
	    	minmax.setSelection(1);
	    }
	    }
	}
	
	/**
	 * Methode zum Erhöhen des xi-Wertes abhängig vom Inhalt des aktuellen Feldes 
	 * (Beachte: Eingabe in dieses Feld zur Zeit deaktivert)
	 */
	public void increment_xi(){
		EditText edittext_x = (EditText) findViewById(R.id.edittext_x); 
		EditText target_element = (EditText) findViewById(R.id.edittext_target_element);
		int edittext_x_value = Integer.valueOf(edittext_x.getText().toString().substring(1)).intValue();
		edittext_x_value++;//inkrementieren
		edittext_x.setText("x" + edittext_x_value);
		try{
			String target_value =String.valueOf(target.getValue(edittext_x_value-1));
			if(target_value.equals("0.0")){
				target_element.setText("");
				target_element.setHint("0");
			}
			else{
				target_element.setText(target_value);
        		Selection.setSelection(target_element.getText(), target_element.length());
			}
		}
		catch(IndexOutOfBoundsException e){
    		target_element.setText("");
			target_element.setHint("0");
		}
		catch(Exception e){
		}
	}
}
	
	
	

