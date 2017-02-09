package com.googlecode.simplex4android;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Datenhaltungsklasse SimplexProblem zur Repräsentation von SimplexProblemen, Inhalt: SimplexTableau und Zielfunktion.
 * Inhalt: SimplexTableau, Zielfunktion, Pivotspalten und ein boolean, ob es optimal ist.
 * @author Simplex4Android
 */
@SuppressWarnings("serial")
public abstract class SimplexProblem implements Serializable{
	private ArrayList<ArrayList<Double>> tableau; 
	private ArrayList<Double> target; //Zielfunktion mit zusätzlicher 0, um den Zielwert im Tableau berechnen zu können
	private int[] pivots;//Basisspalten
	private boolean optimal;

	//SETTINGS!!!
	//normaler Simplex oder Dualer Simplex
	//automatische/manuelle Wahl der Pivotspalten

	/**
	 * Der leere Konstruktor sollte nur zum klonen eins Objektes benutzt werden!!!
	 * Standardkonstruktor für ein leeres SimplexProblem zum anschließenden Hinzufügen der Zielfunktion und Nebenbedingungen.
	 * Die Zeile der delta-Werte ist bereits enthalten.
	 */
	public SimplexProblem(){
		this.tableau = new ArrayList<ArrayList<Double>>();
		this.tableau.add(new ArrayList<Double>()); // Zeile der delta-Werte hinzufügen
		this.target = new ArrayList<Double>();
		this.optimal = false;
		pivots = new int[this.getNoRows()-1];
		//		this.initializePivotsWithMinusOne();

	}

	/**
	 * Konstruktor, der eine ArrayList mit Input-Objekten übergeben bekommt. 
	 * An erster Stellte muss dabei stehts die Zielfunktion vom Typ Target stehen.
	 * @param input ArrayList mit Input-Objekten (Index 0 muss ein Target-Objekt enthalten)
	 */
	public SimplexProblem(ArrayList<Input> input){
		// Zielfunktion auslesen und anlegen
		Target t = (Target) input.get(0);
		this.target = t.getClonedValues();
		this.target.add(new Double(0));
		if(!t.getMinOrMax()){
			for(int i=0;i<this.target.size()-1;i++){
				this.target.set(i, new Double(this.target.get(i).doubleValue()*(-1)));
			}
		}	
		// Tableau anlegen und mit Zeilen füllen
		this.tableau = new ArrayList<ArrayList<Double>>();
		ArrayList<Double> deltas = new ArrayList<Double>();
		
		//Herausfinden ob die Zielfunktion oder eine Nebenbedingung länger ist
		int longestRow = -1; //-1: Target ist mit am längsten, 0-n Nr der längsten Row (row i ist länger als Target)
		int lengthLR = target.size(); //Länger der längsten Zeile
		for(int i=0;i<input.size();i++){
			if(input.get(i).getValues().size()+1>lengthLR){
				longestRow = i;
				lengthLR = input.get(i).getValues().size()+1;
			}
		}
		if(longestRow!=-1){
			for(int i=target.size()-1;0<lengthLR-target.size();){
				target.add(i,new Double(0)); // target
			}	
		}
		for(int i=0;i<lengthLR;i++){  
			deltas.add(new Double(0)); // delta-Zeile anlegen
		}
		this.tableau.add(deltas);
		for(int i=1; i<input.size();i++){
			Constraint c = (Constraint) input.get(i); // Nebenbedingungen anlegen
			ArrayList<Double> row = new ArrayList<Double>();
			row = c.getClonedValues();
			row.add(new Double(c.getSign()));
			row.add(new Double(c.getTargetValue()));
			this.addRow(row);
		}
		this.optimal = false;
		pivots = new int[this.getNoRows()-1];
	}

	/**
	 * Stellt ein SimplexTableau inklusive Zielfunktion zur Verfügung.
	 * @param tableau
	 * @param target
	 */
	public SimplexProblem(double[][] tableau, double[] target){ 
		this.tableau = this.convertTo2DArrayList(tableau);
		this.target = this.convertToDblArrayList(target);		
		this.optimal = false;
		pivots = new int[this.getNoRows()-1];
	}

	/**
	 * Fügt eine weitere Pivospalte (z.B. als künstliche oder Schlupfvariable) an vorletzter Stelle des Tableaus ein. 
	 * Die Eins befindet sich in der Zeile mit Index c, die neue Variable wird mit Kosten 1 in der Zielfunktion hinzugefügt.
	 * @param row Index der Zeile, für die Eins der neuen Pivotspalte
	 */
	public void addArtificialVar(int row){
		//		System.out.println(row);
		// Pivotspalte ergänzen
		for(int i=0;i<this.tableau.size();i++){
			if(i==row){
				this.tableau.get(row).add(this.tableau.get(row).size()-1,new Double(1));
			}else{
				this.tableau.get(i).add(this.tableau.get(i).size()-1,new Double(0));
			}

		}
		// Einfügen der neuen Variable in die Zielfunktion inkl. Verschiebung des Zielwerts
		this.target.add(this.target.size()-1,new Double(1));
		// Einfügen der neuen Pivotspalte in die Basis
		this.pivots[row] = this.target.size()-2;
		//this.pivots.add(row,new Integer(this.target.size()-2));	
	}

	/**
	 * Fügt dem SimplexProblem eine neue Zeile beliebiger Länge an vorletzter Stelle hinzu (in der letzten Zeile befinden sich stehts die delta-Werte.
	 * Je nach Länge werden in den bereits vorhandenen Zeilen Nullen ergänzt und wenn nötig Schlupfvariablen ergänzt.
	 * @param r neu einzufügenden Zeile, der Faktor der Variablen xi steht an Stelle x(i-1) des Arrays, an vorletzter Stelle der Vergleichsoperator ("-1" enspricht "<=", "0" entspricht "=" und "1" entspricht ">=")) und an letzter Stelle der Zielwert
	 */
	public void addRow(ArrayList<Double> row){
		ArrayList<Double> newRow = new ArrayList<Double>();
		// Alle Variablen vor dem Vergleichsoperator einfügen
		for(int i=0;i<row.size()-2;i++){
			newRow.add(i, new Double(row.get(i)).doubleValue());
		}
		// Auffüllen der restlichen Variablen (fehlend zur Zielfunktion) mit 0
		for(int i=0;i<(this.getRow(0).length-(row.size()-1));i++){
			newRow.add(new Double(0));
		}
		// evtl. Einfügen der Schlupfvariable inkl. Auffüllen von Nullen in anderen Zeilen
		int sign = row.get(row.size()-2).intValue();
		if(sign!=0){
			if(sign==1){
				newRow.add(new Double(-1));
			}else{
				newRow.add(new Double(1));
			}
			this.target.add(this.target.size()-1, new Double(0)); // Einfügen in Zielfunktion
			for(int i=0;i<this.getNoRows();i++){ // Einfügen der Nullen in anderen Zeilen
				this.tableau.get(i).add(this.tableau.get(i).size()-1, new Double(0));
			}
		}
		// Zielwert anfügen
		newRow.add(row.get(row.size()-1));
		// Einfügen der Zeile an vorletzter Stelle ins Tableau (deltaWerte immer unten)
		this.tableau.add(this.tableau.size()-1, newRow);
	}

	/**
	 * Abstrakte Methode, um ein SimplexProblem-Objekt zu klonen und somit eine History abbilden zu können.
	 * @return geklontes SimplexProblem
	 */
	public abstract SimplexProblem clone();

	public int[] clonePivots(){
		int[]tmp = new int[pivots.length];
		for (int i=0;i<pivots.length;i++){
			tmp[i] = pivots[i];
		}
		return tmp;
	}

	/**
	 * Überführt das übergebene zweidimensionale Array in ein ArrayList<ArrayList<Double>>.
	 * @param array zu überführendes zweidimensionales Array
	 * @return überführte ArrayList
	 */
	protected ArrayList<ArrayList<Double>> convertTo2DArrayList(double[][] array){
		ArrayList<ArrayList<Double>> tableau = new ArrayList<ArrayList<Double>>();
		for(int i=0;i<array.length;i++){
			tableau.add(i,new ArrayList<Double>());
			for(int j=0;j<array[0].length;j++){
				tableau.get(i).add(j,new Double(array[i][j]));
			}
		}
		return tableau;
	}

	/**
	 * Überführt die übergebene ArrayList<Double> in ein double[].
	 * @param arrayList zu überführende ArrayList
	 * @return überführtes Array
	 */
	protected double[] convertToDblArray(ArrayList<Double> arrayList){
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
	 * Überführt die übergebene ArrayList<Integer> in ein int[].
	 * @param arrayList zu überführende ArrayList
	 * @return überführtes Array
	 */
	protected int[] convertToIntArray(ArrayList<Integer> arrayList){
		int[] array = new int[arrayList.size()];
		for(int i=0;i<array.length;i++){
			array[i] = arrayList.get(i).intValue();
		}
		return array;
	}

	/**
	 * Überführt das übergebene Array in eine ArrayList<Integer>.
	 * @param array zu überführendes Array
	 * @return überführte ArrayList
	 */
	protected ArrayList<Integer> convertToIntArrayList(int[] array){
		ArrayList<Integer> arrayList = new ArrayList<Integer>();
		for(int i=0;i<array.length;i++){
			arrayList.add(i,new Integer(array[i]));
		}
		return arrayList;
	}

	/**
	 * Gibt Spalte j aus.
	 * @param j Index der auszugebenen Spalte
	 * @return Spalte j
	 */
	public double[] getColumn(int j){
		double[] r = new double[this.tableau.size()];
		for(int i=0;i<r.length;i++){
			r[i] = this.tableau.get(i).get(j).doubleValue();
		}
		return r;
	}

	/**
	 * Gibt den Inhalt den Feldes in (zeile,spalte) aus.
	 * @param i Index der Zeile im SimplexTableau
	 * @param j Index der Spalte im SimplexTableau
	 * @return Inhalt des Feldes in (zeile,spalte)
	 */
	public double getField(int i, int j){
		return this.tableau.get(i).get(j).doubleValue();
	}

	/**
	 * Gibt die letzte Spalte des Simplex-Tableaus aus.
	 * @return letzte Spalte des Simplex-Tableaus
	 */
	public double[] getLastColumn(){
		return getColumn(this.getNoColumns()-1);
	}

	/**
	 * Gibt die letzte Zeile (delta-Werte) aus.
	 * @return letzte Zeile (delta-Werte)
	 */
	public double[] getLastRow(){
		return getRow(this.getNoRows()-1);
	}

	/**
	 * Gibt die Anzahl der Spalten aus.
	 * @return Anzahl der Spalten
	 */
	public int getNoColumns(){
		return this.tableau.get(0).size();
	}

	/**
	 * Gibt die Anzahl der gespeicherten Pivotspalten zurück (Es kann mehr geben,
	 * es werden aber nur so viele gefunden wie benötigt werden)
	 * @return Anzahl Pivotspalten
	 */
	public int getNoPivots(){
		int noPivots = 0;
		for(int i=0;i<this.pivots.length;i++){
			if(this.pivots[i] != -1)noPivots++;
		}
		return noPivots;
	}

	/**
	 * Gibt die Anzahl der Zeilen aus.
	 * @return Anzahl der Zeilen
	 */
	public int getNoRows(){
		return this.tableau.size();
	}

	/**
	 * Gibt true, wenn Optimaltableau gefunden, sonst false.
	 * @return true, wenn Optimaltableau gefunden, sonst false.
	 */
	public boolean getOptimal(){
		return optimal;
	}

	/**
	 * Gibt die Pivotspaltentabelle (als Indizes) zurück.
	 * @return Indizes der Pivotspalten
	 */
	public int[] getPivots() {
		return this.pivots;
	}

	/**
	 * Gibt Zeile i aus.
	 * @param i Index der auszugebenen Zeile
	 * @return Zeile i
	 */
	public double[] getRow(int i){
		return this.convertToDblArray(this.tableau.get(i));
	}

	/**
	 * Gibt das SimplexTableau aus.
	 * @return SimplexTableau
	 */
	public double[][] getTableau() {
		double[][] tableau = new double[this.tableau.size()][this.tableau.get(0).size()];
		for(int i=0;i<tableau.length;i++){
			for(int j=0;j<tableau[0].length;j++){
				tableau[i][j] = this.getField(i, j);
			}
		}
		return tableau;
	}

	/**
	 * Gibt die Zielfunktion zurück.
	 * @return Zielfunktion
	 */
	public double[] getTarget() {
		return this.convertToDblArray(this.target);
	}

	/**
	 * Setzt Spalte j.
	 * @param c übergebene Spalte
	 * @param j Index der zu verändernden Spalte
	 */
	public void setColumn(double[] c, int j){
		for(int i=0;i<c.length;i++){
			this.tableau.get(i).set(j, new Double(c[i]));
		}
	}

	/**
	 * Setzt den Inhalt in Feld (zeile, spalte) auf den übergebenen double-Wert.
	 * @param i Index der Zeile im SimplexTableau
	 * @param j Index der Spalte im SimplexTableau
	 * @param value übergebener Wert
	 */
	public void setField(int i, int j, double value){
		this.tableau.get(i).set(j, new Double(value));
	}

	/**
	 * Setzt das Tableau als Optimaltableau.
	 */
	public void setOptimal(){
		this.optimal = true;
	}

	/**
	 * Speichert die Pivotspaltentabelle.
	 * @param pivots Indizes der zu setzendenden Pivotspalten
	 */
	public void setPivots(int[] pivots) {
		this.pivots = pivots;
	}

	/**
	 * Setzt Zeile i.
	 * @param r übergebene Zeile
	 * @param i Index der zu ändernden Zeile
	 */
	public void setRow(double[] r, int i){
		for(int a=0;a<r.length;a++){
			this.tableau.get(i).set(a,new Double(r[a]));
		}
	}

	/**
	 * setzt an Stelle row in pivots den Wert column.
	 * Achtung: die Indizes fangen bei null an! 
	 * @param row Zeile des Tableaus in der sich die Pivotspalte ändert
	 * @param column Spalte der neuen Pivotspalte
	 */
	public void setSinglePivot(int row, int column) {
		this.pivots[row] = column;
	}

	/**
	 * Setzt das SimplexTableau und berechnet anschließend die Pivotwerte neu.
	 * @param tableau übergebenes SimplexTableau
	 */
	public void setTableau(ArrayList<ArrayList<Double>> tableau) {
		this.tableau = tableau;
		SimplexLogic.findPivots(this);
	}

	/**
	 * Setzt das SimplexTableau und berechnet anschließend die Pivotwerte neu.
	 * @param tableau übergebenes SimplexTableau
	 */
	public void setTableau(double[][] tableau) {
		this.tableau = this.convertTo2DArrayList(tableau);
		SimplexLogic.findPivots(this);
	}

	/**
	 * Setzt die Zielfunktion.
	 * @param target übergebene Zielfunktion
	 */
	public void setTarget(ArrayList<Double> target) {
		this.target = target;
	}

	/**
	 * Setzt die Zielfunktion.
	 * @param target übergebene Zielfunktion
	 */
	public void setTarget(double[] target) {
		this.target = this.convertToDblArrayList(target);
	}

	/**
	 * Abstrakte Methode, um das Tableau in HTML darzustellen.
	 * @return String mit HTML-Code als Inhalt für eine Tabelle
	 */
	public abstract String tableauToHtml();	

	/**
	 * Gibt eine Stringdarstellung des SimplexTableaus zurück.
	 * @return Stringdarstellung des SimplexTableaus
	 */
	public String tableauToString(){
		String re ="";
		for(int i=0;i<this.tableau.size();i++){
			for(int j=0;j<this.tableau.get(0).size()-1;j++){
				re += " " +this.tableau.get(i).get(j).doubleValue();
			}
			re += " | " +this.tableau.get(i).get(this.tableau.get(0).size()-1) +"\n";
		}		
		return re;
	}

	/**
	 * Gibt eine Stringdarstellung der Zielfunktion zurück.
	 * @return Stringdarstellung der Zielfunktion.
	 */
	public String targetToString(){
		String re = "";
		re += this.target.get(0).intValue() +"x1";
		for(int i=1;i<this.target.size()-1;i++){
			if(this.target.get(i)<0){
				re += " " +this.target.get(i).intValue() +"x" +(i+1);
			}else{
				re += " + " +this.target.get(i).intValue() +"x" +(i+1);
			}			
		}
		re += " = min \n";
		return re;
	}
}
