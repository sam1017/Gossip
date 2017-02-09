package com.googlecode.simplex4android;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.util.ArrayList;
import android.content.Context;


/**
 * Klasse zum Speichern und Laden alles bisher erstellen SimplexProbleme in Simplex4Android.
 */
public class ProblemsDb {

	private ArrayList<ArrayList<Input>> listOfInputs;//Liste zum Speichern von Problemen

	/**
	 * Konstruktor zum Anlegen einer InputsDb für einen übergebenen Kontext.
	 * @param context aktueller Kontext
	 * @throws StreamCorruptedException
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public ProblemsDb(Context context) throws StreamCorruptedException, IOException, ClassNotFoundException{
		listOfInputs = new ArrayList<ArrayList<Input>>();
		this.readFile(context);
	}
	
	/**
	 * Setzt die gespeicherten Probleme.
	 * @param listOfInputs zu speichernde Probleme
	 */
	public void setListOfInputs(ArrayList<ArrayList<Input>> listOfInputs){
		this.listOfInputs = listOfInputs;
	}
	
	/**
	 * Gibt die gespeicherten Probleme zurück.
	 * @return gespeicherte Probleme
	 */
	public ArrayList<ArrayList<Input>> getListOfInputs(){
		return this.listOfInputs;
	}
	/**
	 * Fügt den gespeicherten Problemen ein weiteres hinzu und speichert direkt alle in eine Datei.
	 * @param context aktueller Kontext
	 * @param input zu speicherndes Problem (an Index i muss ein Objekt der Klasse Target stehen)
	 * @throws Exception
	 */
	public void addProblem(Context context, ArrayList<Input> input) throws Exception{
		listOfInputs.add(input);
		this.writeFile(context);
	}

	/**
	 * Überschreibt das Problem an Index i der gespeicherten Probleme.
	 * @param context aktueller Kontext
	 * @param i Index i
	 * @param input zu setzendes Problem
	 * @throws Exception
	 */
	public void setProblem(Context context, int i, ArrayList<Input> input) throws Exception{
		this.listOfInputs.set(i, input);
		this.writeFile(context);
	}
	
	/**
	 * Enternt das Problem an Index i
	 * @param context aktueller Kontext
	 * @param position Index i des zu entfernenden Problems
	 * @throws Exception
	 */
	public void removeProblem(Context context, int position) throws Exception{
		listOfInputs.remove(position);
		this.writeFile(context);
	}
	
	/**
	 * Gibt das Problem an Index i zurück.
	 * @param i 
	 * @return Problem an Index i
	 */
	public ArrayList<Input> getProblem(int i){
		return listOfInputs.get(i);	
	}
	
	/**
	 * Gibt die Namen (Stringrepräsentationen der Zielfunktionen) der Probleme zurück.
	 * @return Stringrepräsentationen der Zielfunktionen der Probleme
	 * @throws NegativeArraySizeException
	 */
	public String[] getNames() throws NegativeArraySizeException{
		String[] s = new String[listOfInputs.size()-1];
		for(int i=0;i<listOfInputs.size();i++){
			s[i] = listOfInputs.get(i).get(0).toString();
		}
		return s;
	}
	
	/**
	 * Schreibt die aktuelle Liste der Probleme in die Datei simplexProblems.dat.
	 * @param context aktueller Kontext
	 * @throws Exception 
	 */
	public void writeFile(Context context) throws Exception {
		context.getFileStreamPath("simplexProblems.dat").delete();
		FileOutputStream fos = context.openFileOutput("simplexProblems.dat", Context.MODE_PRIVATE);
		ObjectOutputStream oos = new ObjectOutputStream(fos);
		oos.writeObject(listOfInputs);
		oos.flush();
		oos.close();
	}

	/**
	 * Liest eine Liste von Problemen aus der Datei simplexProblems.dat.
	 * @param context aktueller Kontext
	 * @throws StreamCorruptedException
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	@SuppressWarnings("unchecked")
	public void readFile(Context context) throws StreamCorruptedException, IOException, ClassNotFoundException{
		FileInputStream fis = null;
		try{
			fis = context.openFileInput("simplexProblems.dat");
		}
		catch(FileNotFoundException e){
			return; // Abbruch, keine Datei vorhanden.
		}
		ObjectInputStream ois = new ObjectInputStream(fis);
		listOfInputs = (ArrayList<ArrayList<Input>>) ois.readObject();
		ois.close();
	}
}
