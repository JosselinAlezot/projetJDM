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

public class WikiExtractor {

	public String title;
	public String textExtracted;
	
	/**
	 * 
	 * @param word
	 * @throws IOException
	 */
	public WikiExtractor(String word) throws IOException {
		textExtracted = getExtraction(word);	
	}
	
	/**
	 * 
	 * @param wordToExtract
	 * @return Un String contenant l'ensemble des balises p de la page Wikipedia analys�e
	 * @throws IOException
	 */
	public String getExtraction(String wordToExtract) throws IOException {
		StringBuilder allTextExtracted = new StringBuilder();
		Document doc = Jsoup.connect("https://fr.wikipedia.org/wiki/"+URLEncoder.encode(wordToExtract, "UTF-8")).get();		title = doc.title().replace("� Wikip�dia","");
		Elements link = doc.getElementsByTag("p");
		for(String l : link.eachText()){
			allTextExtracted.append(l);
		}
		return allTextExtracted.toString();		
	}
		
	

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTextExtracted() {
		return textExtracted;
	}

	public void setTextExtracted(String textExtracted) {
		this.textExtracted = textExtracted;
	}
	
	public static void main(String args[]) throws IOException {
		WikiExtractor test = new WikiExtractor("Bouclier_(arme)");
		System.out.println(test.getTextExtracted());
	}
}
