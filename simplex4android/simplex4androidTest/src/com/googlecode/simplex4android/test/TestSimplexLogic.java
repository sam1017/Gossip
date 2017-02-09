package com.googlecode.simplex4android.test;

import java.io.IOException;
import java.util.Arrays;
import java.util.zip.DataFormatException;
import com.googlecode.simplex4android.SimplexHistory;
import com.googlecode.simplex4android.SimplexLogic;
import com.googlecode.simplex4android.SimplexProblemDual;
import com.googlecode.simplex4android.SimplexProblemPrimal;

import junit.framework.TestCase;

public class TestSimplexLogic extends TestCase {
	private SimplexProblemPrimal problemPrimal;
	private SimplexProblemDual problemDual;

	public static void setUpClass() throws Exception{
	}
	public static void tearDownClass() throws Exception{
	}
	public void setUp(){
		double[][] tableauPrimal = {{-1.5,3,0,0,1,-1,6},{0,1,0,1,0,-1,3},{0.5,-1,1,0,0,1,1},{0,0,0,0,0,0,0}};
		double[] targetPrimal = {1,2,7,5,0,0,0};
		problemPrimal = new SimplexProblemPrimal(tableauPrimal, targetPrimal);

		double[][] tableauDual = {{-1,-2,-1,1,0,-3},{-2,1,-3,0,1,-4},{0,0,0,0,0,0}};
		double[] targetDual = {2,3,4,0,0,0};
		problemDual = new SimplexProblemDual(tableauDual, targetDual);
	}
	public void tearDown(){
	}

	/**
	 * Testet, ob die benötigten künstlichen Variablen hinzugefügt wurden.
	 */
	public void testAddArtificialVars(){
		double[][] tab = {{-1.5,3,0,0,5,-1,6},{0,1,0,5,0,-1,3},{0.5,-1,5,0,0,1,1},{0,0,0,0,0,0,0}};
		this.problemPrimal.setTableau(tab);
		this.problemPrimal = (SimplexProblemPrimal) SimplexLogic.addArtificialVars(problemPrimal);
		assertEquals(this.problemPrimal.getField(0, 6),1.0);
		assertEquals(this.problemPrimal.getField(1, 7),1.0);
		assertEquals(this.problemPrimal.getField(2, 8),1.0);
	}

	/**
	 * Testet, ob testAddArtificialVars null zurück gibt, falls das Hinzufügen von künstlichen Variablen nicht nötig ist.
	 */
	public void testAddArtificialVarsNull(){
		assertEquals(null,SimplexLogic.addArtificialVars(this.problemPrimal));
	}

	/**
	 * Prüft, ob die deltaByF-Werte korrekt ermitteln werden.
	 */
	public void testCalcDeltaByF(){
		SimplexLogic.calcDeltas(this.problemDual);
		SimplexLogic.calcDeltaByF(this.problemDual);
		double[] deltaByF = {1,-1,(-4.0/-3.0),-1,-1};
		assertTrue(Arrays.equals(deltaByF, this.problemDual.getDeltaByF()));
	}

	/**
	 * Testet, ob die Delta-Werte richtig berechnet werden.
	 */
	public void testCalcDeltas(){
		SimplexLogic.calcDeltas(this.problemPrimal);
		SimplexLogic.calcDeltas(this.problemDual);
		double[] deltasPrimal = {2.5,-4,0,0,0,2,22};
		double[] deltasDual = {-2,-3,-4,0,0,0};
		assertTrue(Arrays.equals(deltasPrimal, this.problemPrimal.getRow(this.problemPrimal.getNoRows()-1)));
		assertTrue(Arrays.equals(deltasDual, this.problemDual.getRow(this.problemDual.getNoRows()-1)));
	}

	/**
	 * Prüft, ob die x/f-Werte korrekt berechnet wurden.
	 */
	public void testCalcXByF(){
		SimplexLogic.calcDeltas(this.problemPrimal);
		SimplexLogic.calcXByF(this.problemPrimal);
		assertEquals(-4.0, this.problemPrimal.getXByF()[0]);
		assertTrue(Double.isInfinite(this.problemPrimal.getXByF()[1]));
		assertEquals(2.0, this.problemPrimal.getXByF()[2]);
	}

	/**
	 * Testet, ob die Optimalität das primalen SimplexProblems korrekt erkannt wird.
	 */
	public void testCheckDualOptimal(){
		SimplexLogic.calcDeltas(this.problemDual);
		assertTrue(!SimplexLogic.checkDualOptimal(this.problemDual));
		double[][] tab = {{0,1,-0.2,-0.4,0.2,0.4},{1,0,1.4,-0.2,-0.4,2.2},{0,0,-1.8,-1.6,-0.2,5.8}};
		this.problemDual.setTableau(tab);
		assertTrue(SimplexLogic.checkDualOptimal(this.problemDual));
	}

	/**
	 * Testet, ob mit checkInput() geprüfte Stringeingaben richtig erkannt werden.
	 */
	public void testCheckInput(){
		assertTrue(SimplexLogic.checkInput("0"));
		assertTrue(SimplexLogic.checkInput("2"));
		assertTrue(SimplexLogic.checkInput("2.0"));
		assertTrue(SimplexLogic.checkInput("600"));
		assertTrue(SimplexLogic.checkInput("2.005"));
		assertTrue(SimplexLogic.checkInput("-2/5"));
		assertTrue(!SimplexLogic.checkInput("00"));
		assertTrue(!SimplexLogic.checkInput("-00"));
		assertTrue(!SimplexLogic.checkInput("2/0"));
		assertTrue(!SimplexLogic.checkInput("/2"));
		assertTrue(!SimplexLogic.checkInput("-"));
		assertTrue(!SimplexLogic.checkInput("/"));
		assertTrue(!SimplexLogic.checkInput("2/4/"));
		assertTrue(!SimplexLogic.checkInput("-2-"));
		assertTrue(!SimplexLogic.checkInput("2.0."));
	}

	/**
	 * Testet, ob die Optimalität das primalen SimplexProblems korrekt erkannt wird.
	 */
	public void testCheckOptimal(){
		SimplexLogic.calcDeltas(this.problemPrimal);
		assertTrue(!SimplexLogic.checkOptimal(this.problemPrimal));
		double[] row = {-2,-100,0,0,0,-5,12};
		this.problemPrimal.setRow(row, this.problemPrimal.getNoRows()-1);
		assertTrue(SimplexLogic.checkOptimal(this.problemPrimal));
	}

	/**
	 * Testet, ob die korrekte Pivotspalte gewählt wird.
	 */
	public void testChoosePivotColumn(){
		SimplexLogic.calcDeltas(this.problemPrimal);
		int pivotColumn = SimplexLogic.choosePivotColumn(this.problemPrimal);
		assertEquals(0,pivotColumn);
	}

	public void testChoosePivotColumnDual(){
		SimplexLogic.calcDeltas(problemDual);
		SimplexLogic.calcDeltaByF(problemDual);
		assertEquals(0,SimplexLogic.choosePivotColumnDual(this.problemDual));
	}

	/**
	 * Prüft, ob die korrekte Zeile des neuen Pivotelements gefunden wird.
	 */
	public void testChoosePivotRow(){
		SimplexLogic.calcDeltas(this.problemPrimal);
		SimplexLogic.calcXByF(this.problemPrimal);
		assertEquals(2, SimplexLogic.choosePivotRow(this.problemPrimal));
		double[] xByF = {-4,-2,-1};
		this.problemPrimal.setXByF(xByF);
		assertEquals(-1, SimplexLogic.choosePivotRow(this.problemPrimal));
	}

	/**
	 * Testet, ob die Pivotspalte in der dualen Simplex-Methode korrekt gewählt wird.
	 */
	public void testChoosePivotRowDual(){
		assertEquals(-1,SimplexLogic.choosePivotRowDual(problemDual));
	}

	/**
	 * Prüft, ob die Pivotspalten korrekt gefunden werden.
	 */
	public void testFindPivots(){
		SimplexLogic.findPivots(this.problemPrimal);
		int [] pivots = {4,3,2};
		assertTrue(Arrays.equals(pivots, this.problemPrimal.getPivots()));

	}

	/**
	 * Prüft, ob der Gauß-Algorithmus korrekt durchgeführt wird.
	 */
	public void testGauss(){
		SimplexLogic.calcDeltas(this.problemPrimal);
		double[][] tableau = {{0,0,3,0,1,2,9},{0,1,0,1,0,-1,3},{1,-2,2,0,0,2,2},{0,1,-5,0,0,-3,17}};
		try{
			SimplexLogic.gauss(this.problemPrimal, 2, 0);
		}catch(IOException e){
			e.toString();
		}
		assertTrue(Arrays.equals(tableau[0], this.problemPrimal.getTableau()[0]));
		assertTrue(Arrays.equals(tableau[1], this.problemPrimal.getTableau()[1]));
		assertTrue(Arrays.equals(tableau[2], this.problemPrimal.getTableau()[2]));
		assertTrue(Arrays.equals(tableau[3], this.problemPrimal.getTableau()[3]));
	}

	/**
	 * Prüft, ob die primale Zulässigkeit eines SimplexProblems korrekt erkannt wird.
	 */
	public void testPrimalValid(){
		SimplexLogic.calcDeltas(this.problemPrimal);
		SimplexLogic.calcXByF(this.problemPrimal);
		assertTrue(SimplexLogic.primalValid(this.problemPrimal));
		double[] c = {4,-2,0};
		this.problemPrimal.setColumn(c, this.problemPrimal.getNoColumns()-1);
		assertTrue(!SimplexLogic.primalValid(this.problemPrimal));
	}

	/**
	 * Testet, ob der Simplex-Schritt richtig durchgeführt wird.
	 */
	public void testSimplexDual(){
		double[][] tableau = {{0,-2.5,0.5,1,-0.5,-1},{1.0,-0.5,1.5,-0.0,-0.5,2.0},{0,-4,-1,0,-1,4}};
		SimplexLogic.calcDeltas(problemDual);
		SimplexLogic.calcDeltaByF(problemDual);
		try{
			SimplexLogic.simplex(this.problemDual);
		}catch(IOException e) {
		}catch(DataFormatException e){
		}
		assertTrue(Arrays.equals(tableau[0], this.problemDual.getRow(0)));
		assertTrue(Arrays.equals(tableau[1], this.problemDual.getRow(1)));
		assertTrue(Arrays.equals(tableau[2], this.problemDual.getRow(2)));
	}

	/**
	 * Testet, ob der Simplex-Schritt richtig durchgeführt wird.
	 */
	public void testSimplexPrimal(){
		double[][] tableau = {{0,0,3,0,1,2,9},{0,1,0,1,0,-1,3},{1,-2,2,0,0,2,2},{0,1,-5,0,0,-3,17}};
		SimplexLogic.calcDeltas(problemPrimal);
		SimplexLogic.calcXByF(problemPrimal);
		try{
			SimplexLogic.simplex(this.problemPrimal);
		}catch(IOException e){
		}catch(DataFormatException e) {
		}
		assertTrue(Arrays.equals(tableau[0], this.problemPrimal.getTableau()[0]));
		assertTrue(Arrays.equals(tableau[1], this.problemPrimal.getTableau()[1]));
		assertTrue(Arrays.equals(tableau[2], this.problemPrimal.getTableau()[2]));
		assertTrue(Arrays.equals(tableau[3], this.problemPrimal.getTableau()[3]));
	}

	/**
	 * Testet, ob die ZweiPhasenSimplexMethode für ein primal zulässiges Problem korrekt durchgeführt wird.
	 * Es werden alle Zwischenschritte kontrolliert.
	 */
	public void testTwoPhaseSimplexPrimal(){
		double[] target = {1,2,7,5,0,0,0};
		double[][] startTableau = {{-1,2,1,0,1,0,7},{0,1,0,1,0,-1,3},{1,0,2,2,0,0,8},{0,0,0,0,0,0,0}};
		problemPrimal = new SimplexProblemPrimal(startTableau, target);
		SimplexHistory[] history = new SimplexHistory[2];
		history = SimplexLogic.twoPhaseSimplex(problemPrimal);


		// Wurden die künstlichen Variablen korrekt hinzugefügt?
		double[][] phaseOne0 = {{-1,2,1,0,1,0,0,0,7},{0,1,0,1,0,-1,1,0,3},{1,0,2,2,0,0,0,1,8},{1,1,2,3,0,-1,0,0,11}};
		assertTrue(Arrays.equals(phaseOne0[0], history[0].getElement(1).getTableau()[0]));
		assertTrue(Arrays.equals(phaseOne0[1], history[0].getElement(1).getTableau()[1]));
		assertTrue(Arrays.equals(phaseOne0[2], history[0].getElement(1).getTableau()[2]));
		assertTrue(Arrays.equals(phaseOne0[3], history[0].getElement(1).getTableau()[3]));

		// Richtiger Simplex-Schritt in Phase I?
		double[][] phaseOne1 = {{0,2,3,2,1,0,0,1,15},{0,1,0,1,0,-1,1,0,3},{1,0,2,2,0,0,0,1,8},{0,1,0,1,0,-1,0,-1,3}};
		assertTrue(Arrays.equals(phaseOne1[0], history[0].getElement(2).getTableau()[0]));
		assertTrue(Arrays.equals(phaseOne1[1], history[0].getElement(2).getTableau()[1]));
		assertTrue(Arrays.equals(phaseOne1[2], history[0].getElement(2).getTableau()[2]));
		assertTrue(Arrays.equals(phaseOne1[3], history[0].getElement(2).getTableau()[3]));

		// Richtiger Simplex-Schritt in Phase I (letzter Schritt in Phase I)?
		double[][] phaseOne2 = {{0,0,3,0,1,2,-2,1,9},{0,1,0,1,0,-1,1,0,3},{1,0,2,2,0,0,0,1,8},{0,0,0,0,0,0,-1,-1,0}};
		assertTrue(Arrays.equals(phaseOne2[0], history[0].getElement(3).getTableau()[0]));
		assertTrue(Arrays.equals(phaseOne2[1], history[0].getElement(3).getTableau()[1]));
		assertTrue(Arrays.equals(phaseOne2[2], history[0].getElement(3).getTableau()[2]));
		assertTrue(Arrays.equals(phaseOne2[3], history[0].getElement(3).getTableau()[3]));

		// Richtiger Simplex-Schritt in Phase II UND damit optimale Lösung gefunden (gleichzeitig Starttableau der Phase II)?
		double[][] phaseTwo2Optimal = {{0,0,3,0,1,2,9},{0,1,0,1,0,-1,3},{1,0,2,2,0,0,8},{0,0,-5,-1,0,-2,14}};
		assertTrue(Arrays.equals(phaseTwo2Optimal[0], history[1].getElement(0).getTableau()[0]));
		assertTrue(Arrays.equals(phaseTwo2Optimal[1], history[1].getElement(0).getTableau()[1]));
		assertTrue(Arrays.equals(phaseTwo2Optimal[2], history[1].getElement(0).getTableau()[2]));
		assertTrue(Arrays.equals(phaseTwo2Optimal[3], history[1].getElement(0).getTableau()[3]));
	}

	/**
	 * Testet, ob die ZweiPhasenSimplexMethode für ein dual zulässiges Problem korrekt durchgeführt wird.
	 * Es werden alle Zwischenschritte kontrolliert.
	 */
	public void testTwoPhaseSimplexDual(){
		double[][] tableauDual = {{0,-1,-3,-3,-2,1,0,-1},{-1,-5,-5,5,5,0,1,-1},{0,0,0,0,0,0,0,0}};
		double[] targetDual = {10,55,75,15,5,0,0,0};
		problemDual = new SimplexProblemDual(tableauDual, targetDual);
		SimplexHistory[] history = new SimplexHistory[2];
		history = SimplexLogic.twoPhaseSimplex(problemDual);


		// 1.Tableau
		double[][] phaseTwo0 = {{0.0,-1.0,-3.0,-3.0,-2.0,1.0,0.0,-1.0},{-1.0,-5.0,-5.0,5.0,5.0,0.0,1.0,-1.0},{-10.0,-55.0,-75.0,-15.0,-5.0,0.0,0.0,0.0}};
		assertTrue(Arrays.equals(phaseTwo0[0], history[1].getElement(0).getTableau()[0]));
		assertTrue(Arrays.equals(phaseTwo0[1], history[1].getElement(0).getTableau()[1]));
		assertTrue(Arrays.equals(phaseTwo0[2], history[1].getElement(0).getTableau()[2]));
		

		// 2.Tableau
		double[][] phaseTwo1 = {{-0.0,0.5,1.5,1.5,1.0,-0.5,-0.0,0.5},{-1.0,-7.5,-12.5,-2.5,0.0,2.5,1.0,-3.5},{-10.0,-52.5,-67.5,-7.5,0.0,-2.5,0.0,2.5}};
		assertTrue(Arrays.equals(phaseTwo1[0], history[1].getElement(1).getTableau()[0]));
		assertTrue(Arrays.equals(phaseTwo1[1], history[1].getElement(1).getTableau()[1]));
		assertTrue(Arrays.equals(phaseTwo1[2], history[1].getElement(1).getTableau()[2]));
		
		// 3.Tableau
		double[][] phaseTwo2 = {{-0.6,-4.0,-6.0,0.0,1.0,1.0,0.6,-1.6},{0.4,3.0,5.0,1.0,0.0,-1.0,-0.4,1.4},{-7.0,-30.0,-30.0,0.0,0.0,-10.0,-3.0,13.0}};
		
		// Aufgrund von Rundungsdifferenzen mit double-Werten, wird hier auf 10 Nachkommastellen gerundet
		for(int i=0;i<history[1].getElement(2).getNoRows();i++){
			for(int j=0;j<history[1].getElement(2).getNoColumns();j++){
				history[1].getElement(2).setField(i, j, (Math.round(history[1].getElement(2).getField(i, j)*10000000000.)/10000000000.));
			}
		}
		assertTrue(Arrays.equals(phaseTwo2[0], history[1].getElement(2).getTableau()[0]));
		assertTrue(Arrays.equals(phaseTwo2[1], history[1].getElement(2).getTableau()[1]));
		assertTrue(Arrays.equals(phaseTwo2[2], history[1].getElement(2).getTableau()[2]));
		
		// 4.Tableau
		// Aufgrund von Rundungsdifferenzen mit double-Werten, wird hier auf 10 Nachkommastellen gerundet
		double[][] phaseTwo2Optimal = {{0.1,2./3.,1.0,-0.0,-1./6.,-1./6.,-0.1,4./15.},{-0.1,-1./3.,0.0,1.0,5./6.,-1./6.,0.1,1./15.},{-4.0,-10.0,0.0,0.0,-5.0,-15.0,-6.0,21.0}};
		for(int i=0;i<history[1].getElement(3).getNoRows();i++){
			for(int j=0;j<history[1].getElement(3).getNoColumns();j++){
				history[1].getElement(3).setField(i, j, (Math.round(history[1].getElement(3).getField(i, j)*10000000000.)/10000000000.));
				phaseTwo2Optimal[i][j] = (Math.round(phaseTwo2Optimal[i][j]*10000000000.)/10000000000.);
			}
		}
		//double[][] phaseTwo2Optimal = {{0.1,2/3,1.0,0.0,-1/6,-1/6,-0.1,4/15},{-0.1,-1/3,0.0,1.0,5/6,-1/6,0.1,1/15},{-4.0,-10.0,0.0,0.0,-5.0,-15.0,-6.0,21.0}};
		assertTrue(Arrays.equals(phaseTwo2Optimal[0], history[1].getElement(3).getTableau()[0]));
		assertTrue(Arrays.equals(phaseTwo2Optimal[1], history[1].getElement(3).getTableau()[1]));
		assertTrue(Arrays.equals(phaseTwo2Optimal[2], history[1].getElement(3).getTableau()[2]));
		
	}


}
