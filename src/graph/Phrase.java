package graph;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;

import com.sun.tools.javac.util.Pair;

public class Phrase {
	
	private String brut; //la contenu textuel de la phrase en brut
	private HashMap<Integer,Boolean> isImportant; //une map qui contiendra les mots non 
	private HashMap<Integer,Node> mots;
	private HashMap<Integer,ArrayList<Node>> groupesMots; 
	//On regroupe plusieurs nodes en une seule, l'int en clé correspondant au node groupant
	
	public Phrase(String mots) {
		this.brut = mots; 
		this.groupesMots = new HashMap<Integer, ArrayList<Node>>();
		this.isImportant = new HashMap<Integer, Boolean>();
		this.mots = makePhrase(mots);
		
		
	}
	
	private HashMap<Integer,Node> makePhrase(String phrase) {
		String[] mots = phrase.split(" ");
		HashMap<Integer,Node> ret = new HashMap<Integer,Node>();
		
		boolean first = false;
		String prec = "";
		for (String s : mots) {
			
//			if (!first) {
//				first = true;
//				prec = s;
//				continue;
//			}
			
			System.out.println(s);
			
			if (Node.getNodeFromString(s)==null) {
				ret.put(ret.size(),new Node());
			} else {
				ret.put(ret.size(),Node.getNodeFromString(s));
			}
			isImportant.put(ret.size()-1, true);
			//prec = s;
		}
		
		return ret;
	}
	
	public static void main(String[] args) {
		String phrase = "le super cheval est là.";
		
		main.Main m = new main.Main();
		try {
			m.init(m,phrase);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		Phrase p = new Phrase(phrase);
		
		//phrase = p.pruningSentence(phrase);
		
		p = p.prunedSentence();
		
//		for (int i = 0; i < p.getMots().size(); i++) {
//			System.out.println(p.getMots().get(i));
//			
//		}
		
		p.readPhrase();
	}
	
	public HashMap<Integer,Node> getMots() {
		return mots;
	}
	
	/*
	 * params : le node moins important, le node qui va le grouper ainsi que la position de ce dernier
	 */
	public void addGroupMots(Node removed, Node grouping, int posgrouping) {
		ArrayList<Node> temp = new ArrayList<Node>();
		temp.add(grouping);
		temp.add(removed);
		groupesMots.put(posgrouping, temp);
		
	}
	
	/*
	 *  param : id du node à check
	 *  check si on doit étudiant le node ou non
	 */
	public boolean checkImportant(int i) {
		return isImportant.get(i);
	}
	
	/*
	 *  renvoie une structure Phrase
	 *  qui enlève les . et change l'importance des mots inutiles (tels que les déterminants) 
	 */
	public Phrase prunedSentence() {
		ArrayList<String> banWords = new ArrayList<String>() {{
			add("le");
			add("la");
			add("Le");
			add("La");
			
		}};
		
		//String ret = brut;
		
		Phrase ret = new Phrase(brut);
		
		ret.brut = ret.brut.split("\\.")[0];
		
		for (String banword : banWords) {
			for (Integer i : ret.mots.keySet()) {
				if (ret.mots.get(i).getName().equals(banword)) {
					ret.addGroupMots(ret.mots.get(i), ret.mots.get(i+1), i+1);
					ret.isImportant.replace(i, false);
				}
			}
		}
		
		return ret;
	}
	
	/*
	 * affichage de la phrase en prenant en compte l'important du mot
	 */
	public void readPhrase() {
		for (Integer i : mots.keySet()) {
			if (checkImportant(i)) System.out.println(mots.get(i));
		}
	}

}
