package com.googlecode.simplex4android;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * Datenhaltungklasse zur Repräsentation von Nebenbedingungen.
 * Nebenbedingungen können in Gleichheits- oder Ungleichheitsform ("<=" und ">=") eingegeben werden. 
 * @author Simplex4Android
 *
 */
@SuppressWarnings("serial")
public class Constraint extends Input implements Serializable{
	private double targetValue;
	private int sign;

	/**
	 * Standardkonstruktor
	 */
	public Constraint() {
		super();
	}

	/**
	 * Gibt den Vergleichsoperator zurück.
	 * @return Vergleichsoperator ("-1" enspricht "<=", "0" entspricht "=" und "1" entspricht ">=")
	 */
	public int getSign(){
		return sign;
	}

	/**
	 * Gibt den Zielfunktionswert zurück.
	 * @return Zielfunktionswert
	 */
	public double getTargetValue(){
		return this.targetValue;
	}

	/**
	 * Setzt das Vergleichssymbol und daraufhin die ggf. benötigte Schlupfvariable, um die Nebenbedingung in Standardform zu bringen.
	 * @param s "-1" enspricht "<=", "0" entspricht "=" und "1" entspricht ">="
	 */
	public void setSign(int s){
		this.sign = s;
	}

	/**
	 * Setzt den Zielfunktionswert.
	 * @param value zu setzender Zielfunktionswert
	 */
	public void setTargetValue(double value){
		this.targetValue = value;
	}

	/**
	 * Gibt die Nebenbedingung (komplett) als String aus.
	 */
	@Override
	public String toString(){
		String s = this.valuesToString();
		if(this.sign==-1){
			s += " \u2264 ";
		}else if(this.sign==0){
			s += " = ";
		}else if(this.sign==1){
			s += " \u2265 ";
		}
		s += String.valueOf(Math.round(this.targetValue*100.)/100.);
		return s;
	}


	/**
	  * Always treat de-serialization as a full-blown constructor, by
	  * validating the final state of the de-serialized object.
	  */
	   private void readObject(
	     ObjectInputStream aInputStream
	   ) throws ClassNotFoundException, IOException {
	     //always perform the default de-serialization first
	     aInputStream.defaultReadObject();
	     //make defensive copy of the mutable Date field
	     //fDateOpened = new Date( fDateOpened.getTime() );
	  }

	    /**
	    * This is the default implementation of writeObject.
	    * Customise if necessary.
	    */
	    private void writeObject(
	      ObjectOutputStream aOutputStream
	    ) throws IOException {
	      //perform the default serialization for all non-transient, non-static fields
	      aOutputStream.defaultWriteObject();
	    }
}
