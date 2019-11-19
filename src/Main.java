

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

/* mots actuellement dans le fichier relations : 
 * - hyène
 */

public class Main {

	public ArrayList<String> relations;
	public ArrayList<String> nodes;
	
	public Main() {
		relations = new ArrayList<String>();
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) throws IOException {
		Main m = new Main();
		String mot =  "hyène";
		m.extractPage(mot);
		
//		m.fillingLists();
//		
//		for (String r : m.relations){
//			System.out.println(r);
//		}

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
	    	relations.add(line);
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
		
		File resG = new File(directory + File.separator + filenameGlobal); // Fichier listant les noeuds+relations gloables
		File res = new File(directory + File.separator + filename); // Fichier listant les noeuds+relations du nouveau mot
		if (res.exists()) return;
		
		FileWriter writerG = new FileWriter(resG, true); 
		res.createNewFile();

		FileWriter writer = new FileWriter(res); 

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

