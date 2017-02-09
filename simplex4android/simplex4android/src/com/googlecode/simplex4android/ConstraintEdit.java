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
 * Activity zum Erzeugen/Bearbeiten von Nebenbedingungen (Constraints),
 * @params CONSTRAINT_EDIT_INTENT (mit zu bearbeitender Nebenbedingung) oder CONSTRAINT_CREATE_INTENT
 * @return CONSTRAINT_EDIT_RESULT oder CONSTRAINT_CREATE_RESULT mit der erstellten/bearbeiteten Nebenbedingung
 * @author simplex4android: Sebastian Hanschke
 *
 */
public class ConstraintEdit extends Activity {

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
	private static Constraint constraint;
	private static EditText addto;
	
	/**
	 * Wird aufgerufen, wenn die Activity gestartet wird
	 * Hier werden alle Initialisierungen und UI Settings vorgenommen. 
	 * @param savedInstanceState 
	 */
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.constraint_edit);

	    //Ressourcen
		final Spinner gtltoreq = (Spinner) findViewById(R.id.spinner_gtltoreq);
	    final EditText target_element = (EditText) findViewById(R.id.edittext_target_element);
	    final EditText constraint_target_value = (EditText) findViewById(R.id.edittext_constraint_target_value);
	    final EditText target = (EditText) findViewById(R.id.edittext_target);
	    final int[] keyboardButtons = {	R.id.btn_0, R.id.btn_1, R.id.btn_2, R.id.btn_3, R.id.btn_4, 
				R.id.btn_5, R.id.btn_6, R.id.btn_7, R.id.btn_8, R.id.btn_9,
				R.id.btn_minus, R.id.btn_divide, R.id.btn_decimal, R.id.btn_backspace};
	    final Button x_plus = (Button) findViewById(R.id.btn_x_plus);
	    final Button x_minus = (Button) findViewById(R.id.btn_x_minus);
	    final Button add_target_element = (Button) findViewById(R.id.btn_add_target_element);
    	final Button add = (Button) findViewById(R.id.btn_add);
    	final Button back = (Button) findViewById(R.id.btn_cancel);

	    //Behandlung der verschiedenen Intents
	    if(this.getIntent().getBooleanExtra("create", false) ==  true){
	    	constraint = new Constraint();
	    }
	    else if(this.getIntent().getBooleanExtra("edit", false) == true){
	    	constraint = (Constraint) this.getIntent().getSerializableExtra("constraint"); //Constraint laden
	    }
	    
	    //TextFeld, zu dem hinzugefügt werden soll
	    addto = (EditText) findViewById(R.id.edittext_target_element);
	    addto.requestFocus();

	    //Spinner gtltoreq
		ArrayAdapter<CharSequence> adapter_gtltoreq = ArrayAdapter.createFromResource(this, R.array.spinner_gtltoreq_values, android.R.layout.simple_spinner_item); 
		adapter_gtltoreq.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		gtltoreq.setAdapter(adapter_gtltoreq);

	    gtltoreq.setOnItemSelectedListener(new OnItemSelectedListener() {
		    public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		    	//Index "0" enspricht "<=", "2" entspricht "=" und "1" entspricht ">="
		    	//Sign "-1" enspricht "<=", "0" entspricht "=" und "1" entspricht ">="
		    	if(arg3 == 0){
		    		ConstraintEdit.constraint.setSign(-1);
		    	}
		    	else if(arg3 == 2){
		    		ConstraintEdit.constraint.setSign(0);
		    	}
		    	else{
		    		ConstraintEdit.constraint.setSign(1);
		    	}
		    }
		    	 
		    public void onNothingSelected(AdapterView<?> arg0) {}
	    });
	    
	    //Textfeld Target-Element
	    target_element.setRawInputType(android.text.InputType.TYPE_NULL);
	    target_element.setOnFocusChangeListener(new OnFocusChangeListener(){
	    	public void onFocusChange(View v, boolean b){
	    		if(b==true){
	    		    EditText text = (EditText) findViewById(R.id.edittext_target_element);
	    		    findViewById(R.id.btn_add_target_element).setEnabled(true);
	    		    findViewById(R.id.keyboard).setVisibility(View.VISIBLE);
		    		addto = (EditText) findViewById(R.id.edittext_target_element);
	        		Selection.setSelection(text.getText(), text.length());
	    		}
	    		else{
		    		findViewById(R.id.keyboard).setVisibility(View.INVISIBLE);
	    		}
	    	}
		});
	    
	    //Textfeld Constraint-Target-Value
	    constraint_target_value.setRawInputType(android.text.InputType.TYPE_NULL);
	    constraint_target_value.setOnFocusChangeListener(new OnFocusChangeListener(){
	    	public void onFocusChange(View v, boolean b){
	    		if(b==true){
	    		    EditText text = (EditText) findViewById(R.id.edittext_target_element);
	    		    findViewById(R.id.btn_add_target_element).setEnabled(false);
	    		    findViewById(R.id.keyboard).setVisibility(View.VISIBLE);
		    		addto = (EditText) findViewById(R.id.edittext_constraint_target_value);
	        		Selection.setSelection(text.getText(), text.length());
	        		
	    		}
	    		else{
		    		findViewById(R.id.keyboard).setVisibility(View.INVISIBLE);
	    		}
	    	}
		});
	    
	    //Keyboard-Buttons
		for(int i=0; i<keyboardButtons.length; i++){
			Button[] btns = new Button[keyboardButtons.length];
			btns[i] = (Button) findViewById(keyboardButtons[i]);
		    btns[i].setOnClickListener(new OnClickListener() {
		        public void onClick(View v) {
	        		EditText text = addto;
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
		        }});
		}
		
	    //Hinzufügen-Button
	    add_target_element.setOnClickListener (new OnClickListener(){
	    	public void onClick(View V){
	    		addTargetElement();
	    	}
		});
	    
	    
	    //"Xi erhöhen"-Button
	    x_plus.setOnClickListener (new OnClickListener(){
	    	public void onClick(View V){
	    		EditText target_element = (EditText) findViewById(R.id.edittext_target_element);
	    		increment_xi();
	    		try{
	    			target_element.requestFocus();
	    		}
	    		catch(Exception ex){
	    			Toast.makeText(ConstraintEdit.this,ex.getMessage(),Toast.LENGTH_LONG).show();
	    		}
	    	}
	    });

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
		    			String target_value =String.valueOf(constraint.getValue(edittext_x_value-1));
		    			if(target_value.equals("0.0")){
		    				target_element.setText("");
		    				target_element.setHint(0);
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
	    			Toast.makeText(ConstraintEdit.this,ex.getMessage(),Toast.LENGTH_LONG).show();
	    		}
	    	}
	    });
	    
	    //Fertig-Button
	    add.setOnClickListener(new OnClickListener() {
	        public void onClick(View v) {
	    	    EditText target_value = (EditText) findViewById(R.id.edittext_constraint_target_value);
	    	    EditText target_element = (EditText) findViewById(R.id.edittext_target_element);
	    	    if(target_value.getText().toString().equals("")){
	    			Toast.makeText(ConstraintEdit.this,"Eingabe unvollständig! Bitte Zielwert eingeben!",Toast.LENGTH_LONG).show();
        			target_value.setBackgroundResource(R.drawable.textfield_pressed_red);//Hintergrund rot
        			target_value.requestFocus();
	    			return;
	        	}
	        	else if(constraint.getValues().isEmpty()){
	    			Toast.makeText(ConstraintEdit.this,"Eingabe unvollständig! Bitte mind. ein xi hinzufügen!",Toast.LENGTH_LONG).show();
        			target_element.setBackgroundResource(R.drawable.textfield_pressed_red);//Hintergrund rot
        			target_element.requestFocus();
	        	}
	        	else{
//	        		if(!target_element.getText().toString().equals("")){
//	        			//Dialog
//	        			
//	    				AlertDialog.Builder builder = new AlertDialog.Builder(ConstraintEdit.this);
//	    				builder.setMessage("Aktuelle Eingabe hinzufügen?")
//	    				.setCancelable(false)
//	    				.setPositiveButton("Ja", new DialogInterface.OnClickListener() {
//	    					public void onClick(DialogInterface dialog, int id) {
//	    						addTargetElement();
//	    					}       
//	    				})
//	    				.setNegativeButton("Abbruch", new DialogInterface.OnClickListener() {
//	    					public void onClick(DialogInterface dialog, int id) {
//	    						dialog.cancel(); //Dialog schließen
//	    						return;
//	    					}
//	    				})
//	    				.setNeutralButton("Nein", new DialogInterface.OnClickListener() {
//	    					public void onClick(DialogInterface dialog, int id) {
//	    					}
//	    				});
//	    				AlertDialog alert = builder.create();
//	    				alert.show();
//	        		}
	        		try{
	        			constraint.setTargetValue(Double.valueOf(target_value.getText().toString()));
	        		}
	        		catch(Exception ex){
	        			try{
		        			constraint.setTargetValue(Input.fractionToDbl(target_value.getText().toString()));
	        			}
	        			catch(Exception ex2){
	        				Toast.makeText(ConstraintEdit.this,"Fehler beim Typecast!",Toast.LENGTH_LONG).show();
	        				return;
	        			}
	        		}
	        		Intent ConstraintEditIntent = new Intent().putExtra("constraint", constraint);
	        		if(ConstraintEdit.this.getIntent().getBooleanExtra("edit", false)){
		        		ConstraintEditIntent.putExtra("id", ConstraintEdit.this.getIntent().getIntExtra("id", -1));
	        			setResult(CONSTRAINT_EDIT_RESULT,ConstraintEditIntent);
	        		}
	        		else{
	        			setResult(CONSTRAINT_CREATE_RESULT, ConstraintEditIntent);
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
	    
	    //Constraint laden, falls "edit" == true
	    if(this.getIntent().getBooleanExtra("edit", false) == true){
	    	//Textfeld Target
	    	target.setText(constraint.valuesToString());
	    	//Textfeld Target-Element
	    	try{
	    		String target_element_string = String.valueOf(Math.round(constraint.getValue(0)*100.)/100.);
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
	    	//Textfeld Target-Value
	    	constraint_target_value.setText(String.valueOf(Math.round(constraint.getTargetValue()*100)/100));
	    	//Spinner gtltoreq
	    	//Index "0" enspricht "<=", "2" entspricht "=" und "1" entspricht ">="
	    	//Sign "-1" enspricht "<=", "0" entspricht "=" und "1" entspricht ">="
	    	if(constraint.getSign() == -1){
	    		gtltoreq.setSelection(0);
	    	}
	    	else if(constraint.getSign() == 0){
	    		gtltoreq.setSelection(2);
	    	}
	    	else{
	    		gtltoreq.setSelection(1);
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
			String target_value = String.valueOf(constraint.getValue(edittext_x_value-1));
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
	
	/**
	 *TODO: KOMMENTAR HINZUFÜGEN!
	 */
	public void addTargetElement(){
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
						Toast.makeText(ConstraintEdit.this,"Fehler beim Typecast!",Toast.LENGTH_LONG).show();
						return;
					}
				}
			}
			int i = Integer.valueOf(((EditText) findViewById(R.id.edittext_x)).getText().toString().substring(1)).intValue()-1;
			constraint.setValue(i, value);
			target.setText(constraint.valuesToString());
			increment_xi();
		}
		else{
			Toast.makeText(ConstraintEdit.this,"Fehlerhafte Eingabe! Bitte korrigieren!",Toast.LENGTH_LONG).show();
		}
	}
}

	
	
	

