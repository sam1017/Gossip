package com.googlecode.simplex4android.test;

import java.util.ArrayList;
import java.util.Arrays;

import com.googlecode.simplex4android.Constraint;
import com.googlecode.simplex4android.Input;
import com.googlecode.simplex4android.SimplexProblem;
import com.googlecode.simplex4android.SimplexProblemPrimal;
import com.googlecode.simplex4android.Target;

import junit.framework.TestCase;

public class TestSimplexProblem extends TestCase {
	public static void setUpClass() throws Exception{
	}
	public static void tearDownClass() throws Exception{
	}
	public void setUp(){
	}
	public void tearDown(){
	}
	
	/**
	 * Testet, ob der Konstruktor, der aus einer ArrayList<Input> das Tableau und die Zielfunktion aufbaut, korrekt funktioniert.
	 */
	public void testSimplexProblemArrayListInput(){
		Target t = new Target();
		t.setValue(0,1);
		t.setValue(1,2);
		t.setValue(2,3);
		t.setMinOrMax(false);
		
		Constraint c1 = new Constraint();
		c1.setValue(0, 1);
		c1.setSign(-1);
		c1.setTargetValue(5);
		Constraint c2 = new Constraint();
		c2.setValue(1, 1);
		c2.setSign(0);
		c2.setTargetValue(10);
		Constraint c3 = new Constraint();
		c3.setValue(2, 1);
		c3.setSign(1);
		c3.setTargetValue(20);
		Constraint c4 = new Constraint();
		c4.setValue(1, 0);
		c4.setValue(2, 1);
		c4.setSign(1);
		c4.setTargetValue(20);
		
		ArrayList<Input> input = new ArrayList<Input>();
		input.add(t);
		input.add(c1);
		input.add(c2);
		input.add(c3);
		
		SimplexProblemPrimal problem = new SimplexProblemPrimal(input);
		double[] target = {-1,-2,-3,0,0,0};
		double[][] tableau = {{1.0,0.0,0.0,1.0,0.0,5.0},{0.0,1.0,0.0,0.0,0.0,10.0},{0.0,0.0,1.0,0.0,-1.0,20.0},{0.0,0.0,0.0,0.0,0.0,0.0}};
		assertTrue(Arrays.equals(target, problem.getTarget()));
		assertTrue(Arrays.equals(tableau[0],problem.getTableau()[0]));
		assertTrue(Arrays.equals(tableau[1],problem.getTableau()[1]));
		assertTrue(Arrays.equals(tableau[2],problem.getTableau()[2]));
		assertTrue(Arrays.equals(tableau[3],problem.getTableau()[3]));
		
	}

}
