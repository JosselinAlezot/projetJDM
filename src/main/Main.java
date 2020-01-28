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
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;

import com.sun.management.DiagnosticCommandMBean;
//import com.sun.tools.javac.util.Pair;

import graph.Phrase;


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
	
	public void init(Main m) throws IOException {
		m.readWordsList();
		m.extractSentence("");
		m.fillingLists();
	}
	
	public void initWord(Main m, String phrase) throws IOException {
		System.out.println("phrase : "+phrase);
		//m.readWordsList();
		m.extractSentence(phrase);
		//m.fillingLists();
	}

	public static void main(String[] args) throws IOException {
//		
//		System.out.println("&#305;");
//		String base = "&#305;";
//		
//		String eu = URLEncoder.encode(base,StandardCharsets.ISO_8859_1);
//		System.out.println(eu);
//		
//		String u = URLDecoder.decode(eu,StandardCharsets.UTF_8);
//		System.out.println(u);
//
//		
//		
//		
//		
//		
//		String s2 = new String(u.getBytes("utf8"), "iso-8859-1");
//		
//		System.out.println(s2);
		
		Main m = new Main();
		String mot =  "échelle";
		String phrase = "Nous être beaux veuve";
		
		m.readWordsList(); // vérification des mots déjà collectés auparavant
		m.extractPage(mot);
		m.extractSentence(phrase); // utilisation de jdm
		m.fillingLists(); // remplissage des données
		
		/*
		 * Pour trouver le node selon le nom : 
		 * graph.Node camiondepompier = graph.Node.getNodeFromString("camion de pompier");
		 * 
		 * Pour trouver le node selon l'id : 
		 * graph.Node camiondepompier = nodesH.get(152109);
		 */
		graph.Node camiondepompier = graph.Node.getNodeFromString("veuve");
		graph.Node voiture = graph.Node.getNodeFromString("beaux");
		graph.Node moto = graph.Node.getNodeFromString("être");
		
		//System.out.println(camiondepompier);

		ArrayList<graph.Edge> motovoiture = graph.Edge.getRelationsFromXandY(moto, voiture);
		ArrayList<graph.Edge> motocamiondepompier = graph.Edge.getRelationsFromXandY(moto, camiondepompier);
		
		System.out.println(motovoiture.size()+" relations entre "+moto.getName()+" et "+voiture.getName()+" : "+motovoiture);
		System.out.println(motocamiondepompier.size()+" relations entre "+moto.getName()+" et "+camiondepompier.getName()+" : "+motocamiondepompier);
		
		
		
		/*
		 * Test : les r_has_part d'un camion de pompier
		 * (contient échelle)
		 *
		ArrayList<graph.Node> resultsdontechelle = graph.Edge.getYFromRelation(camiondepompier, graph.Edge.getIdTypeFromString("r_has_part"));
		System.out.println(resultsdontechelle.size());
		for (graph.Node n : resultsdontechelle) {
			System.out.println(n);
		}
		*/
		
		//m.printNodesRelationsDico();

		
		//String nodemot = "e;22842;'hyène';1;120";
		
		//System.out.println(URLDecoder.decode(nodemot, "UTF-8"));
		
		//graph.Node node = new graph.Node(nodemot);
		
		//System.out.println(node.toString());
		
//		for (graph.Edge e : graph.Edge.getRelationsFromX(relationsH,graph.Node.getNodeFromString("'moto'"))) {
//			System.out.println(e);
//		}
		
/* 
 * vieux test sur la classe phrase		
		
		Phrase p = new Phrase(phrase);
		
		System.out.println(p.getMots());
		
//		for (Pair pa : p.getMots()) {
//			System.out.println(pa);
//		}
		
		for (int i = 0; i < p.getMots().size(); i++) {
			System.out.println(p.getMots().get(i));
			
		}
 */


	}
	
	/*
	 * Extraction de page pour chaque mot de la phrase en paramètres
	 */
	public void extractSentence(String sentence) throws UnsupportedEncodingException, IOException {
		String[] mots = sentence.split(" ");
		
		for (String s : mots) {
			if (!dicoMots.contains(s)) extractPage(s);
		}
	}
	
	/*
	 * Print to test
	 */
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
	
	/*
	 * Vérification des mots déjà utilisés pour ne pas casser JDM
	 */
	public void readWordsList() throws IOException {
		File directory = new File("./");
		String wordsFile = directory.getAbsolutePath().substring(0, directory.getAbsolutePath().length()-1)+"Relations" + File.separator + "wordsList.txt";
		
		// premiere utilisation o� le dir Relations n'existe pas
		String dir = directory.getAbsolutePath().substring(0, directory.getAbsolutePath().length()-1) + "Relations";
		File d = new File(dir);
		d.mkdirs();

		File resW = new File(wordsFile); // Fichier listant les mots qu'on a déjà
		
		if (!resW.exists()) resW.createNewFile();

		
	    BufferedReader br = null;
	    String line = "";
	    	    
	    br = new BufferedReader(new FileReader(resW));
	    while ((line = br.readLine()) != null){
	    	dicoMots.add(line);
	    }
	    
	}
	
	/*
	 * Remplissage des nodes et relations 
	 */
	public void fillingLists() throws IOException {
		File directory = new File("./");
		String relationsFile = directory.getAbsolutePath().substring(0, directory.getAbsolutePath().length()-1)+"Relations/" + "Relations.txt";
	
		/* On splite le fichier en relations */
	    BufferedReader br = null;
	    String line = "";
	    String lineSplitBy = "\n";
	    
	    br = new BufferedReader(new FileReader(relationsFile));
	    while ((line = br.readLine()) != null){
	    	//System.out.println(line);
	    	line = parse(line);
	    	if (line.startsWith("e;")) {
	    		//System.out.println("currentnode : "+line);
	    		graph.Node temp = new graph.Node(line);
	    		if (!temp.isValid()) continue;
		    	nodes.add(temp);
		    	nodesH.putIfAbsent(temp.getId(),temp);
	    	}
	    	if (line.startsWith("r;")) {
	    		graph.Edge temp = new graph.Edge(line);
	    		if (!temp.isValid()) continue;
		    	relations.add(new graph.Edge(line));
		    	relationsH.put(temp.getIdRelation(),temp);
	    	}
	    }
	    
	    //String[] rules = line.split(lineSplitBy);
	}
	
	/*
	 * Utilisée pour ajouter de nouvelles relations après l'init sans avoir à relire le fichier Relations
	 */
	public void addLine(String line) {
    	if (line.startsWith("e;")) {
    		//System.out.println("currentnode : "+line);
    		graph.Node temp = new graph.Node(line);
    		if (!temp.isValid()) return;
	    	nodes.add(temp);
	    	nodesH.putIfAbsent(temp.getId(),temp);
    	}
    	if (line.startsWith("r;")) {
    		graph.Edge temp = new graph.Edge(line);
    		if (!temp.isValid()) return;
	    	relations.add(new graph.Edge(line));
	    	relationsH.put(temp.getIdRelation(),temp);
    	}
	}
	
	public String getUrl(String mot) throws UnsupportedEncodingException{
		return "http://www.jeuxdemots.org/rezo-dump.php?gotermsubmit=Chercher&gotermrel="+URLEncoder.encode(mot, "ISO-8859-1")+"&rel=";
	}
	
	/*
	 * Extraction d'une page selon un mot précis
	 */
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
		    		line = parse(line);
		    		addLine(line);
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
	
	/*
	 * Gère les exceptions de caractères, c'est pas censé être notre boulot mais bon hein
	 * quelle idée de laisser des caractères turcs aussi comment ils sont arrivés sur jdm ??
	 */
	public String parse(String line) throws IOException {
		
		try {
			String l = URLDecoder.decode(line, "UTF-8");
			if (l.contains("&#305;")) {
				l = l.replaceAll("&#305;", "i");
			}
			
			return l;
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		return line;
	}
	
	/*
	 * TODO (eventuellement)
	 */
	public ArrayList<String> pruningSentence(ArrayList<String> s)
	{
		
		
		
		return s;
	}

}

