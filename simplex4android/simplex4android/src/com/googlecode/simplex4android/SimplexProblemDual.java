package com.googlecode.simplex4android;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Datenhaltungsklasse SimplexProblem zur Repräsentation von dualen SimplexProblemen
 * Inhalt: SimplexTableau, Zielfunktion, Pivotspalten, deltaByF-Zeile und ein boolean, ob es optimal ist.
 * @author Simplex4Android
 */
@SuppressWarnings("serial")
public class SimplexProblemDual extends SimplexProblem implements Serializable{

	private ArrayList<Double> deltaByF; //Zeile unter dem Tableau für den dualen Simplex

	/**
	 * Standardkonstruktor für ein leeres SimplexProblem zum anschließenden Hinzufügen der Zielfunktion und Nebenbedingungen.
	 * Die Zeile der delta-Werte ist bereits enthalten.
	 */
	public SimplexProblemDual(){
		super();
		this.deltaByF = new ArrayList<Double>();
	}

	/**
	 * Konstruktor, der eine ArrayList mit Input-Objekten übergeben bekommt. 
	 * An erster Stellte muss dabei stehts die Zielfunktion vom Typ Target stehen.
	 * @param input ArrayList mit Input-Objekten (Index 0 muss ein Target-Objekt enthalten)
	 */
	public SimplexProblemDual(ArrayList<Input> input){
		super(input);
		this.deltaByF = new ArrayList<Double>();
	}

	/**
	 * Stellt ein SimplexTableau inklusive Zielfunktion zur Verfügung.
	 * @param tableau
	 * @param target
	 */
	public SimplexProblemDual(double[][] tableau, double[] target){ 
		super(tableau, target);
		SimplexLogic.findPivots(this);
	}

	/**
	 * Klont das aktuelle Problem. 
	 */
	@Override
	public SimplexProblem clone() {
		SimplexProblemDual clone = new SimplexProblemDual();
		clone.setDeltaByF(this.getDeltaByF());
		clone.setTableau(this.getTableau());
		clone.setTarget(this.getTarget());
		clone.setPivots(this.clonePivots());
		return clone;

	}

	/**
	 * Gibt ein Array mit den x/f-Werten für jede Zeile zurück.
	 * @return Array mit den x/f-Werten für jede Zeile
	 */
	public double[] getDeltaByF() {
		return this.convertToDblArray(this.deltaByF);
	}

	/**
	 * Überschreibt die delta/f-Werte.
	 * @param deltaByF neue delta/f-Werte
	 */
	public void setDeltaByF(double[] deltaByF) {
		this.deltaByF = this.convertToDblArrayList(deltaByF);
	}

	/**
	 * Methode, die zusätzlich deltaByF mit Nullen auffüllt. Dies führt dazu, dass im Optimum nur - angezeigt werden.
	 * Wird auch benötigt, falls z.B. das Tableau in der zweiten Phase direkt optimal ist und die tableauToHtml genutzt werden soll.
	 */
	public void setOptimal(){
		super.setOptimal();
		if(deltaByF!=null)deltaByF.clear();
		else{
			deltaByF = new ArrayList<Double>();
		}
		for(int i=0;i<super.getNoColumns()-1;i++){
			deltaByF.add(0.0);
		}
	}

	/**
	 * Methode, die das komplette Simplex-Tableau für den dualen Simplex als HTML-String zurückgibt.
	 * @return komplettes Tableau als duales Problem als String in Html 
	 */
	@Override
	public String tableauToHtml(){
		if(!SimplexLogic.solveableDual(this))deltaByF=null;
		int[] pivots =this.getPivots();
		String html = "\n<html>\n<body>\n<table border=1 cellspacing=0 width=100%25;>\n";
		//Spaltenbreite
		html = html + "<colgroup><col width=13> <col width=13>";
		for(int i=0;i<this.getTarget().length;i++){
			html = html + "<col width=30>";
		}
		html = html +"</colgroup>";
		//1. Zeile: Zielfunktion
		html = html + "<tr align=right>\n<td></td><td></td>";		// direkt inkl. zwei leeren Einträgen 
		for(int i=0;i<this.getTarget().length-1;i++){
			html = html + "<td nowrap>" + (Math.round(this.getTarget()[i]*100.)/100.) + "</td>";
		}
		html = html + "<td></td></tr>\n";
		//2. Zeile: zwei Zeilen frei Durchnummerierung der Spalten + x +x/f
		html = html + "<tr align=right><td></td><td></td>";		// direkt inkl. zwei leeren Einträgen
		for(int i=0;i<this.getTarget().length-1;i++){
			html = html +"<td nowrap>"+ (i+1) +"</td>";
		}
		html = html + "<td nowrap>x</td>";
		//ab der 3. Zeile: das eigentliche Tableau, die ersten beiden Spalten auch wie im Tableau + x/f
		for(int i=0;i<this.getTableau().length-1;i++){			//so oft ausführen wie es Zeilen-1 im Tableau gibt
			if(pivots.length <= this.getNoPivots())html = html + "<tr align=right><td nowrap>"+ + Math.round(this.getTarget()[pivots[i]]*100.)/100.+"</td><td nowrap>" +(pivots[i]+1) +"</td>";
			else html = html + "<tr align=right nowrap><td>"+"&#8211;"+"</td><td nowrap>" +"&#8211;"+"</td>";
			for(int j=0;j<this.getTableau()[0].length;j++){
				if(SimplexLogic.choosePivotRowDual(this)==i && SimplexLogic.choosePivotColumnDual(this)==j){
					html = html  + "<td nowrap bgcolor=#CC0000>";
				}else{
					html = html  + "<td nowrap>";
				}
				html = html + (Math.round((this.getTableau()[i][j])*100.)/100.)+"</td>";
			}
			html = html + "</tr>\n";
		}
		// Letzte Zeile: extra behandlung für delta-Wert
		html = html + "<tr align=right><td></td><td>&#x3b4;</td>"; //inkl. zwei leerfelder
		for(int i=0;i<this.getTableau()[0].length;i++){
			if(pivots.length <= this.getNoPivots())html = html + "<td nowrap>" + (Math.round((this.getTableau()[(this.getTableau().length-1)][i])*100.)/100.) +"</td>";
			else html = html + "<td nowrap>" + "&#8211;" +"</td>";
		}
		html = html + "</tr>\n";
		// allerletzte Zeile mit den delta/f-Werten
		if(deltaByF!=null){
			html = html + "<tr align=right><td></td><td nowrap>&#x3b4;/f</td>"; //inkl. zwei leerfelder
			for(int i=0;i<deltaByF.size();i++){
				if(deltaByF.get(i)>0)html = html + "<td nowrap>" + (Math.round(deltaByF.get(i)*100.)/100.) +"</td>";
				else html = html + "<td nowrap>&#8211;</td>";
			}
			html = html + "<td></td></tr>\n";
		}else{
			html = html + "<tr align=right><td></td><td nowrap>&#x3b4;/f</td>"; //inkl. zwei leerfelder
			for(int i=0;i<this.getTarget().length-1;i++){
				html = html +"<td> &#8211; </td>";
			}
			html = html + "<td></td></tr>\n";
		}
		html = html + "</table>\n</body>\n</html>";
		return html;
	}
}
