package graph;
import java.util.ArrayList; 
import java.util.HashMap;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import com.sun.javafx.applet.Splash;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//Creer un objet contenant un attribut hashmapp qui correspond a toutes les relations avec leur traduction
public class LexicalPattern {

	private HashMap<String,ArrayList<String>> lexicalPatterns;

	public LexicalPattern() throws IOException {
		ArrayList<String> textBrut = extractText();
		lexicalPatterns = formatRuleAndText(textBrut);
	}

	public HashMap<String, ArrayList<String>> getLexicalPatterns() {
		return lexicalPatterns;
	}

	public void setLexicalPatterns(HashMap<String, ArrayList<String>> lexicalPatterns) {
		this.lexicalPatterns = lexicalPatterns;
	}
	
	public String toStringLexicalPattern() {
		String res = "";
		for(String key: this.getLexicalPatterns().keySet()) {
			for(String meaning: this.getLexicalPatterns().get(key)) {
				System.out.println("Relation:" + key + ". LangageNaturel:" + meaning);
				res += "Relation:" + key + "/// LangageNaturel:" + meaning;
				res += "\n";
			}
		}
		return res;
	}

	//Si probleme d'encodage mettre l'encodage des fichiers texte en UTF-8
	/**
	 * 
	 * @return Texte du fichier ./LexicalPattern/LexicalPatterns.txt formatte pour que chaque ligne corresponde a une relation et ses traductions
	 * @throws IOException
	 */
	public static ArrayList<String> extractText() throws IOException {
		File directory = new File("./");
		String relationsFile = directory.getAbsolutePath().substring(0, directory.getAbsolutePath().length()-1)+"LexicalPattern/" + "LexicalPatterns.txt";
		ArrayList<String> res = new ArrayList<String>();
		BufferedReader br = null;
		String line = "";
		String lineSplitBy = ";";
		br = new BufferedReader(new FileReader(relationsFile));
		while ((line = br.readLine()) != null){
			res.add(line);
		}
		return res;
	}

	/**
	 * 
	 * @param listText Le texte formate pour que chaque ligne corresponde a une relation et ses traductions
	 * @return HashMap contenant en cle la relation et en objet les traductions en langage naturel
	 */
	public static HashMap<String,ArrayList<String>> formatRuleAndText(ArrayList<String> listText){
		HashMap<String,ArrayList<String>> res = new HashMap<String,ArrayList<String>>();
		for(String currentRelat: listText) {
			//On extrait la relation comme currentRelat correspond a toute la ligne (relation et traductions langage naturel)
			String relation = currentRelat.substring(0,currentRelat.indexOf("="));
			//			System.out.println("Relation:" + relation);
			ArrayList<String> meanings = getMeanings(currentRelat,relation);
			res.put(relation, meanings);
		}

		return res;
	}

	/**
	 * 
	 * @param lineToFormat Represente la relation et ses traductions de ce format r_relation = traductions,traduction2
	 * @param relation Represente r_relation sur l'exmple precedent
	 * @return Liste de toutes les traductions possibles en langage naturel de la regle
	 */
	public static ArrayList<String> getMeanings(String lineToFormat, String relation){
		ArrayList<String> meanings = new ArrayList<String>();
		String allMeanings = lineToFormat.substring(lineToFormat.indexOf("=") + 1, lineToFormat.length());
		String[] allMeaningsSplit = allMeanings.split(",");
		for(String currentMeaning: allMeaningsSplit) {
			meanings.add(currentMeaning);
		}
		
		return meanings;
	}



	/**
	 * 
	 * @param brutMeaning Les traductions
	 * @return Tableau de string avec en premiere case la traduction enlevee et en deuxieme case le texte restant sans la traduction retournee
	 * @throws IOException 
	 */


	public static void main(String args[]) throws IOException {
		LexicalPattern test = new LexicalPattern();
		System.out.println(test.toStringLexicalPattern());
	}
}