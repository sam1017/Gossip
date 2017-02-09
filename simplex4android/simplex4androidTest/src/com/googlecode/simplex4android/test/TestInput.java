package com.googlecode.simplex4android.test;

import java.io.IOException;
import com.googlecode.simplex4android.Constraint;
import com.googlecode.simplex4android.Input;
import junit.framework.TestCase;

public class TestInput extends TestCase {
	Input c;
	public static void setUpClass() throws Exception{
	}
	public static void tearDownClass() throws Exception{
	}
	public void setUp(){
		c = new Constraint();
	}
	public void tearDown(){
	}
	
	/**
	 * Testet, ob das Hinzufügen eines neuen Elementes an einem noch nicht existenten Index funktioniert.
	 */
	public void testSetValue(){
		c.setValue(5, 5.0);
		assertEquals(c.getValue(5), new Double(5).doubleValue());
	}
	
	public void testSetValueDelete(){
		c.setValue(1, 5.0);
		c.setValue(5, 5.0);
		c.setValue(5, 0);
		assertEquals(2, c.getValues().size());
		c.setValue(1, 0);
		assertEquals(0, c.getValues().size());
	}
	
	/**
	 * Testet, ob ein der Methode fractionToDbl(String s) übergebener Bruch-String fehlerfrei in ein double überführt wird.
	 */
	public void testFractionToDblCorrect(){
		String s1 = "24/12";
		String s2 = "-1/2";
		double d1 = 24.0/12.0;
		double d2 = -0.5;
		try{
			assertEquals(d1,Constraint.fractionToDbl(s1));
			assertEquals(d2,Constraint.fractionToDbl(s2));
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	
	/**
	 * Testet, ob die Methode fractionToDbl(String s) bei falscher Eingabe die passende Exception wirft.
	 */
	public void testFractionToDblFalse(){
		String s = "24";
		try{
			Constraint.fractionToDbl(s);
		}catch(IOException e){
			assertEquals("java.io.IOException: Kein '/' enthalten",e.toString());
		}
	}

}
