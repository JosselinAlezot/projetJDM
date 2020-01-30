package graph;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class Lemmatisation {

	private HashMap<String,ArrayList<String>> allLema = new HashMap<String,ArrayList<String>>();
	
	public Lemmatisation() throws IOException {
		this.getAllLema();
	}
	
	//Fonction qui a servi a recuperer seulement les mots et non groupe de mots du fichier de relation r_lemma
	/*
	public static void cleanFileLemma() throws IOException {
		File directory = new File("./");
		String wordsLemmaFile = directory.getAbsolutePath().substring(0, directory.getAbsolutePath().length()-1)+"Relations" + File.separator + "Lemmatisation" + File.separator + "AllLemmatisation.txt";
		
		String fileNameCleanLemma = "cleanLemma.txt";
		File resCleanLemma = new File(directory + File.separator + "Relations" + File.separator + "Lemmatisation" + File.separator + fileNameCleanLemma);
		FileWriter writer = new FileWriter(resCleanLemma);
		
		
		BufferedReader br = null;
	    String line = "";
	    String actualWord[];    
	    br = new BufferedReader(new FileReader(wordsLemmaFile));
	    while ((line = br.readLine()) != null){
	    	actualWord = line.split(";");
	    	if(!actualWord[0].contains(" "))
	    		writer.write(line +"\n");
	    		
	    }
	    writer.close();
	    br.close();
	}
	*/
	
	/**
	 * 
	 * @throws IOException
	 */
	public void getAllLema() throws IOException {
		File directory = new File("./");
		String wordsLemmaFile = directory.getAbsolutePath().substring(0, directory.getAbsolutePath().length()-1)+"Relations" + File.separator + "Lemmatisation" + File.separator + "cleanLemma.txt";
		BufferedReader br = null;
	    String line = "";
	    String actualWord[];    
	    br = new BufferedReader(new FileReader(wordsLemmaFile));
	    //Liste qui recupere les lemmatisations d'un mot
	    ArrayList<String> listLemma = new ArrayList<String>();
	    //pour toutes les lignes
	    while ((line = br.readLine()) != null){
	    	actualWord = line.split(";");
//	    	System.out.println(actualWord.length);
//	    	System.out.println(actualWord[0]);
	    	//Si le mot a lemmatiser n'est pas un groupe de mot
	    	if(!actualWord[0].contains(" ")) {
	    		//On recupere la liste des lemmatisations deja stockees du mot courant
	    		if(this.allLema.containsKey(actualWord[0]))
	    			//System.out.println("bite");
	    			listLemma = this.allLema.get(actualWord[0]);
	    		//On ajoute sa nouvelle lemmatisation
	    		listLemma.add(actualWord[1]);
	    		//on insere dans la HashMap le mot original avec toutes ses lemmatisations (anciennes + nouvelle)
	    		this.allLema.put(actualWord[0],listLemma);
	    	}
	    }
	    br.close();
	}
	
	/**
	 * 
	 * @param word Mot dont on cherche la/les lemmatisation(s)
	 * @return La liste des lemmatisations ou de la lemmatisation
	 */
	public ArrayList<String> getLemmaFromWord(String word) {
		ArrayList<String> res = new ArrayList<String>();
		//S'il n'existe pas de lemmatisation pour l'argument word alors on renvoie word (version du texte)
		res.add(word);
		for(String s: this.allLema.keySet()) {
			//S'il existe une ou des lemmatisation(s) alors on les renvoie
			if(s.equals(word))
				return this.allLema.get(s);
		}
		return res;
	}
	
	public static void main(String args[]) throws IOException {
		Lemmatisation test = new Lemmatisation();
		test.getAllLema();
		System.out.println("taille allLemma:" + test.getallLemma().size());
	}

	public HashMap<String,ArrayList<String>> getallLemma(){
		return this.allLema;
	}
	
	public void setAllLema(HashMap<String, ArrayList<String>> allLema) {
		this.allLema = allLema;
	}
}
