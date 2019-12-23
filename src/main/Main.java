package main;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;

import com.sun.management.DiagnosticCommandMBean;


/* mots actuellement dans le fichier relations : 
 * - hyène
 */

public class Main {

	public ArrayList<graph.Edge> relations;
	public ArrayList<graph.Node> nodes;
	
	public ArrayList<String> dicoMots;
	
	public static HashMap<Integer,graph.Edge> relationsH = new HashMap<Integer,graph.Edge>();
	public static HashMap<Integer,graph.Node> nodesH = new HashMap<Integer,graph.Node>();
	
	public Main() {
		relations = new ArrayList<graph.Edge>();
		nodes = new ArrayList<graph.Node>();
		dicoMots = new ArrayList<String>();
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) throws IOException {
		Main m = new Main();
		String mot =  "viande";
		String phrase = "La moto est une voiture";
		
		m.readWordsList();
		//m.extractPage(mot);
		m.fillingLists();
		
		//m.printNodesRelationsDico();

		
		String nodemot = "e;22842;'hyène';1;120";
		
		graph.Node node = new graph.Node(nodemot);
		
		//System.out.println(node.toString());

	}
	
	public void extractSentence(String sentence) {
		String[] mots = sentence.split(" ");
		
		for ()
		extractPage(sentence);
	}
	
	public void printNodesRelationsDico(){

		for (graph.Node n : nodes){
			System.out.println(n);
		}
		for (graph.Edge n : relations){
			System.out.println(n);
		}
		for (String s : dicoMots) {
			System.out.println(s);
		}
		
	}
	
	public void readWordsList() throws IOException {
		File directory = new File("./");
		String wordsFile = directory.getAbsolutePath().substring(0, directory.getAbsolutePath().length()-1)+"Relations/" + "wordsList.txt";
	
	    BufferedReader br = null;
	    String line = "";
	    
	    br = new BufferedReader(new FileReader(wordsFile));
	    while ((line = br.readLine()) != null){
	    	dicoMots.add(line);
	    }
	    
	}
	
	public void fillingLists() throws IOException {
		File directory = new File("./");
		String relationsFile = directory.getAbsolutePath().substring(0, directory.getAbsolutePath().length()-1)+"Relations/" + "Relations.txt";
	
		/* On splite le fichier en relations */
	    BufferedReader br = null;
	    String line = "";
	    String lineSplitBy = "\n";
	    
	    br = new BufferedReader(new FileReader(relationsFile));
	    while ((line = br.readLine()) != null){
	    	if (line.startsWith("e;")) {
	    		graph.Node temp = new graph.Node(line);
		    	nodes.add(temp);
		    	nodesH.putIfAbsent(temp.getId(),temp);
	    	}
	    	if (line.startsWith("r;")) {
	    		graph.Edge temp = new graph.Edge(line);
		    	relations.add(new graph.Edge(line));
		    	relationsH.put(temp.getIdRelation(),temp);
	    	}
	    }
	    
	    //String[] rules = line.split(lineSplitBy);
	}
	
	public String getUrl(String mot) throws UnsupportedEncodingException{
		return "http://www.jeuxdemots.org/rezo-dump.php?gotermsubmit=Chercher&gotermrel="+URLEncoder.encode(mot, "ISO-8859-1")+"&rel=";
	}
	
	public void extractPage(String mot) throws UnsupportedEncodingException, IOException{
		
		String urlName = getUrl(mot);
		
		URL url = new URL(urlName);
		
		File current = new File("./");
		String dir = current.getAbsolutePath().substring(0, current.getAbsolutePath().length()-1) + "Relations";
		File directory = new File(dir);
		directory.mkdirs();
		String filename = mot+"Resultats.txt";
		String filenameGlobal = "Relations.txt";
		String filenameWords = "wordsList.txt";

		File resG = new File(directory + File.separator + filenameGlobal); // Fichier listant les noeuds+relations globales
		File resW = new File(directory + File.separator + filenameWords); // Fichier listant les mots qu'on a déjà
		File res = new File(directory + File.separator + filename); // Fichier listant les noeuds+relations du nouveau mot
		if (res.exists()) return;
		
		FileWriter writerG = new FileWriter(resG, true); 
		res.createNewFile();

		FileWriter writer = new FileWriter(res); 
		
		FileWriter writerW = new FileWriter(resW, true); 
		writerW.write(mot+"\n");
		dicoMots.add(mot);
		writerW.close();

		try (BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream(), "ISO-8859-1"))) {
		    for (String line; (line = reader.readLine()) != null;) {
		    	if (( line.startsWith("nt;"))||(line.startsWith("e;"))||(line.startsWith("r;"))||(line.startsWith("rt;"))) {
		    		writer.write(line);
		    		writer.write("\n");
		    		writerG.write(line);
		    		writerG.write("\n");
		    	}
		    }
		}

		writer.close();
		writerG.close();

	}
	
	public ArrayList<String> pruningSentence(ArrayList<String> s)
	{
		
		
		
		return s;
	}

}

