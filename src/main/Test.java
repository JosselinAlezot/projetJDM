package main;
import java.util.ArrayList; 
import java.util.HashMap;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A simple example, used on the jsoup website.
 */
public class Test {
	public static void main(String[] args) throws IOException {
		
		String motDep = "Bouclier_(arme)";
		
		Document doc = Jsoup.connect("https://fr.wikipedia.org/wiki/"+URLEncoder.encode(motDep, "UTF-8")).get();
		log(doc.title());
		
		//System.out.println(doc);
		
		Elements link = doc.getElementsByTag("p");
		for(String l : link.eachText()){
			System.out.println(l);
		}
		//System.out.println("Text: " + link.text());
		ArrayList<String> mots = new ArrayList<String>();
		//ArrayList<ArrayList<String>> motPOS = new ArrayList<ArrayList<String>>();
		
		
		HashMap<String,String> motPOS = new HashMap <String,String>();
		
		

	}

	private static void log(String msg, String... vals) {
		System.out.println(String.format(msg, vals));
	}
}