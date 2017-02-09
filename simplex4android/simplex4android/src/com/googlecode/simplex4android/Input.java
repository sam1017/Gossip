package com.googlecode.simplex4android;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * Oberklasse für Input-Objekte, das erste Methoden defintiert und implementiert.
 * @author Simplex4Android
 *
 */
@SuppressWarnings("serial")
public abstract class Input implements Serializable{

	protected ArrayList<Double> values;
	
	/**
	 * Standardkonstruktor
	 */
	public Input(){
		values = new ArrayList<Double>();
	}

	/**
	 * Fügt der Nebenbedingung an letzter Stelle ein neues Element hinzu.
	 * @param value hinzuzufügendes Element
	 */
	public void add(Double value){
		this.values.add(value);
	}
	
	/**
	 * Überführt die übergebene ArrayList<Double> in ein double[].
	 * @param arrayList zu überführende ArrayList
	 * @return überführtes Array
	 */
	private double[] convertToDblArray(ArrayList<Double> arrayList){
		double[] array = new double[arrayList.size()];
		for(int i=0;i<array.length;i++){
			array[i] = arrayList.get(i).doubleValue();
		}
		return array;
	}

	/**
	 * Überführt das übergebene Array in eine ArrayList<Double>.
	 * @param array zu überführendes Array
	 * @return überführte ArrayList
	 */
	protected ArrayList<Double> convertToDblArrayList(double[] array){
		ArrayList<Double> arrayList = new ArrayList<Double>();
		for(int i=0;i<array.length;i++){
			arrayList.add(i,new Double(array[i]));
		}
		return arrayList;
	}
	
	/**
	 * Liest einen als String übergebenen Bruch aus und gibt den dazugehörigen double-Wert zurück
	 * @param s zu bearbeitender Bruch
	 * @return double-Wert des Bruchs
	 * @throws IOException falls kein "/" im übergebenen String enthalten
	 */
	public static double fractionToDbl(String s) throws IOException{
		int slash = s.indexOf("/");
		int firstDigit = 0;
		int lastDigit = s.length();
		if(slash==-1){
			throw new IOException("Kein '/' enthalten");
		}
		if(s.startsWith("-")){
			firstDigit = 1;
			double numerator = Integer.valueOf(s.substring(firstDigit, slash));
			double denomerator = Integer.valueOf(s.substring(slash+1, lastDigit));
			double dbl = -numerator/denomerator;
			return dbl;
		}else{
			double numerator = Integer.valueOf(s.substring(firstDigit, slash));
			double denomerator = Integer.valueOf(s.substring(slash+1, lastDigit));
			double dbl = numerator/denomerator;
			return dbl;
		}		
	}
	
	/**
	 * Gibt die Faktoren der x als neue ArrayList<Double> (deep copy!) zurück.
	 * @return  Faktoren der x als neue ArrayList<Double> (deep copy!)
	 */
	public ArrayList<Double> getClonedValues(){
		double[] vArray = convertToDblArray(this.values);
		ArrayList<Double> vArrayList = convertToDblArrayList(vArray);
		return vArrayList;
	}
	
	/**
	 * Gibt den Wert der x-Variable an Index j zurück.
	 * @return x-Variable an Index j
	 * @throws IndexOutOfBoundsException falls (i<0 || i>(size()-1))
	 */
	public double getValue(int j) throws IndexOutOfBoundsException{
		return this.values.get(j);
	}
	
	/**
	 * Gibt die Faktoren der x zurück.
	 * @return Faktoren der x
	 */
	public ArrayList<Double> getValues() {
		return this.values;
	}
	
	/**
	 * Setzt den Wert an Index j auf den übergebenen double-Wert. Ist der Index nicht vorhanden, wird passend aufgefüllt.
	 * Wird auf Null gesetzt, prüft die Methode, ob es sich um das letzte Element handelt und schneidet entsprechend am Ende ab.
	 * @param j Index des zu setzenden Elements
	 * @param value Wert, auf den das Element gestetzt werden soll.
	 */
	public void setValue(int i, double value){
		if(i>=this.values.size()){
			for(int j=this.values.size();j<=i;j++){
				this.values.add(new Double(0));
			}
		}
		this.values.set(i, value);
		if(i==this.values.size()-1){
			if(value==0.0){ // Das letzte Element soll auf Null gesetzt werden
				this.values.set(i, value);
				while(this.values.size()>0 && this.getValue(this.values.size()-1)==0.0){ // Solange letztes Element gleich Null und Element in der Liste 
					this.values.remove(this.values.size()-1); // Element entfernen
				}
			}
		}
	}
	
	/**
	 * set-Methode für die Zielfunktion
	 * @param values Zielfunktion als Arraylist mit Doublen
	 */
	public void setValues(ArrayList<Double> values) {
		this.values = values;
	}

	/**
	 * Gibt eine Stringdarstellung der x1..xn aus.
	 * @return Stringdarstellung der x1..xn
	 */
	public String valueToString(int i) throws IndexOutOfBoundsException{
		String re = Math.round(this.values.get(i)*100)/100 +"x" +(i+1);
		return re;
	}
	
	/**
	 * Gibt eine Stringdarstellung der x1..xn aus. Es werden nur x-Werte mit Faktor != 0 angezeigt.
	 * @return Stringdarstellung der x1..xn
	 */
	public String valuesToString(){
		String re = "";
		try{
			if(!(this.values.get(0)==0.0)){
				re += String.valueOf(Math.round(this.values.get(0)*100.)/100.) +"x1";
			}
			for(int i=1;i<this.values.size();i++){
				if(!(this.values.get(i)==0)){
					if(re.equals("")){
						if(this.values.get(i)<0){
							re += String.valueOf(Math.round(this.values.get(i)*100.)/100.) +"x" +(i+1);
						}else{
							re += String.valueOf(Math.round(this.values.get(i)*100.)/100.) +"x" +(i+1);
						}
					}else{
						if(this.values.get(i)<0){
							re += " " + String.valueOf(Math.round(this.values.get(i)*100.)/100.) +"x" +(i+1);
						}else{
							re += " + " + String.valueOf(Math.round(this.values.get(i)*100.)/100.) +"x" +(i+1);
						}
					}
				}
							
			}
		}catch(IndexOutOfBoundsException e){
			// keine Aktion
		}
		
		return re;
	}
}
