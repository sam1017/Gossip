package com.googlecode.simplex4android;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Datenhaltungsklasse SimplexProblem zur Repräsentation von SimplexProblemen, Inhalt: SimplexTableau und Zielfunktion.
 * Inhalt: SimplexTableau, Zielfunktion, Pivotspalten, xByF-Spalte und ein boolean, ob es optimal ist.
 * @author Simplex4Android
 */
@SuppressWarnings("serial")
public class SimplexProblemPrimal extends SimplexProblem implements Serializable{

	private ArrayList<Double> xByF; //

	/**
	 * Standardkonstruktor für ein leeres SimplexProblem zum anschließenden Hinzufügen der Zielfunktion und Nebenbedingungen.
	 */
	public SimplexProblemPrimal(){
		super();
		this.xByF = new ArrayList<Double>();
	}

	/**
	 * Konstruktor, der eine ArrayList mit Input-Objekten übergeben bekommt. 
	 * An erster Stellte muss dabei stehts die Zielfunktion vom Typ Target stehen.
	 * @param input ArrayList mit Input-Objekten (Index 0 muss ein Target-Objekt enthalten)
	 */
	public SimplexProblemPrimal(ArrayList<Input> input){
		super(input);
		this.xByF = new ArrayList<Double>();
	}

	/**
	 * Stellt ein SimplexTableau inklusive Zielfunktion zur Verfügung.
	 * @param tableau
	 * @param target
	 */
	public SimplexProblemPrimal(double[][] tableau, double[] target){ 
		super(tableau, target);
		SimplexLogic.findPivots(this);
	}

	/**
	 * Gibt ein Klon vom aktuellen Objekt zurück.
	 * @return Klon vom aktuellen Objekt
	 */
	@Override
	public SimplexProblem clone() {
		SimplexProblemPrimal clone = new SimplexProblemPrimal();
		clone.setXByF(this.getXByF());
		clone.setTableau(this.getTableau());
		clone.setTarget(this.getTarget());
		clone.setPivots(this.clonePivots());
		return clone;
	}

	/**
	 * Gibt ein Array mit den x/f-Werten für jede Zeile zurück.
	 * @return Array mit den x/f-Werten für jede Zeile
	 */
	public double[] getXByF() {
		return this.convertToDblArray(this.xByF);
	}

	/**
	 * Methode, die zusätzlich xByF mit Nullen auffült. Dies führt dazu, dass im Optimum nur - angezeigt werden.
	 */
	public void setOptimal(){
		super.setOptimal();
		if(xByF!=null)xByF.clear();
		else{
			xByF = new ArrayList<Double>();
		}
		for(int i=0;i<super.getNoColumns()-1;i++){
			xByF.add(0.0);
		}
	}
	/**
	 * Überschreibt die x/f-Werte.
	 * @param xByF neue x/f-Werte
	 */
	public void setXByF(double[] xByF) {
		this.xByF = this.convertToDblArrayList(xByF);
	}

	/**
	 * Erstellt HTML-Code als String für das primale Problem.
	 * @return komplettes Tableau als String in Html 
	 */
	@Override
	public String tableauToHtml(){
		int[] pivots =this.getPivots();
		String html = "\n<html>\n<body>\n<table border=1 cellspacing=0 width=100%25;>\n";
		//Spaltenbreite
		html = html + "<colgroup><col width=13> <col width=13>";
		for(int i=0;i<this.getTarget().length+1;i++){
			html = html + "<col width=30>";
		}
		html = html +"</colgroup>";
		//1. Zeile: Zielfunktion
		html = html + "<tr align=right>\n<td></td><td></td>";		// direkt inkl. zwei leeren Einträgen 
		for(int i=0;i<this.getTarget().length-1;i++){
			html = html + "<td nowrap>" + (Math.round(this.getTarget()[i]*100.)/100.) + "</td>";
		}
		html = html + "<td></td><td></td></tr>\n";
		//2. Zeile: zwei Zeilen frei Durchnummerierung der Spalten + x +x/f
		html = html + "<tr align=right><td></td><td></td>";		// direkt inkl. zwei leeren Einträgen
		for(int i=0;i<this.getTarget().length-1;i++){
			html = html +"<td nowrap>"+ (i+1) +"</td>";
		}
		html = html + "<td nowrap>x</td><td nowrap>x/f</td>";
		//ab der 3. Zeile: das eigentliche Tableau, die ersten beiden Spalten auch wie im Tableau + x/f
		for(int i=0;i<this.getTableau().length-1;i++){			//so oft ausführen wie es Zeilen-1 im Tableau gibt
			if(pivots.length <= this.getNoPivots())html = html + "<tr align=right><td nowrap>"+ Math.round(this.getTarget()[pivots[i]]*100.)/100.+"</td><td nowrap>" +(pivots[i]+1) +"</td>";
			else html = html + "<tr align=right><td>"+"&#8211;"+"</td><td>" +"&#8211;"+"</td>";
			for(int j=0;j<this.getTableau()[0].length;j++){
				if(SimplexLogic.choosePivotRow(this)==i && SimplexLogic.choosePivotColumn(this)==j){
					html = html  + "<td nowrap bgcolor=#CC0000>";

				}else{
					html = html  + "<td nowrap>";
				}
				html = html + (Math.round((this.getTableau()[i][j])*100.)/100.)+"</td>";
			}
			//x/f noch hinten dran hängen
			if(xByF!=null){
				if((xByF.get(i)<=0) || (xByF.get(i)== Double.POSITIVE_INFINITY)){
					html = html + "<td nowrap> &#8211; </td>";
				}
				else{
					html = html + "<td nowrap>"+ (Math.round(xByF.get(i)*100.)/100.)+"</td>";
				}
			}else{
				html = html + "<td nowrap> &#8211; </td>";
			}
			html = html + "</tr>\n";
		}
		// Letzte Zeile: extra behandlung für delta-Wert
		html = html + "<tr align=right><td></td><td>&#x3b4;</td>"; //inkl. zwei leerfelder
		for(int i=0;i<this.getTableau()[0].length;i++){
			if(pivots.length <= this.getNoPivots())html = html + "<td nowrap>" + (Math.round((this.getTableau()[(this.getTableau().length-1)][i])*100.)/100.) +"</td>";
			else html = html + "<td nowrap>" + "&#8211;" +"</td>";
		}
		html = html + "<td></td></tr>\n";
		html = html + "</table>\n</body>\n</html>";
		return html;
	}
}
