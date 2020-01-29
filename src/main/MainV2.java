package main;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.w3c.dom.stylesheets.LinkStyle;

import com.sun.org.apache.xml.internal.utils.ListingErrorHandler;
import com.sun.xml.internal.bind.v2.runtime.unmarshaller.XsiNilLoader.Array;

import graph.LexicalPattern;
import graph.Sentence;
import jdk.internal.util.xml.impl.Pair;

public class MainV2 {

	private HashMap<String,graph.Word> dicoMots = new HashMap<String,graph.Word>();

	public static void main(String args[]) throws IOException {
		MainV2 test = new MainV2();
		System.out.println(System.currentTimeMillis());
		long first = System.currentTimeMillis();
		System.out.println(test.getRelaFromWiki("France"));
		//System.out.println(test.getAllRelations("Plante"));

		System.out.println(System.currentTimeMillis()-first);
	}


	public HashMap<String, graph.Word> getDicoMots() {
		return dicoMots;
	}

	public void setDicoMots(HashMap<String, graph.Word> dicoMots) {
		this.dicoMots = dicoMots;
	}

	public String getAllRelations(String wordToCompute) throws IOException{
		//int cpt = 0;
		StringBuilder res = new StringBuilder();
		String newRules = "";
		graph.LexicalPattern lexicalPattern = new graph.LexicalPattern();
		lexicalPattern.setLexicalPatterns(lexicalPattern.getLexicalPatternLemmatized());
		//System.out.println("cpt:" + cpt);
		WikiExtractor wiki = new WikiExtractor(wordToCompute);
		String allSentences[] = wiki.getTextExtracted().split("\\.");
		//POur toutes les phrases du texte
		for(String actualSentence: allSentences) {
			//System.out.println("cpt:" + cpt);
			//cpt++;
			graph.Sentence sentenceStructured = new graph.Sentence(actualSentence);
			//On fixe le x pour tous les mots de la phrase
			for(int XIndex = 0;XIndex < sentenceStructured.getWordsFromSentence().size() - 2;XIndex++) {
				graph.Word x = sentenceStructured.getWordsFromSentence().get(XIndex);
				//Si le x est interessant
				if(graph.Word.relevantGrammTags.contains(x.getGrammaticalTag())) {
					//On fixe un y pour tous les mots de la phrase
					for(int YIndex = XIndex+2;YIndex < sentenceStructured.getWordsFromSentence().size();YIndex++) {
						//Si l'écart entre les 2 mots n'est pas trop grand

						if(YIndex - XIndex < 7) {
							graph.Word y = sentenceStructured.getWordsFromSentence().get(YIndex);
							ArrayList<graph.Word> wordsBetweenXAndY = sentenceStructured.getWordsBetweenXAndY(sentenceStructured.getWordsFromSentence(), XIndex, YIndex);
							if(graph.Word.relevantGrammTags.contains(y.getGrammaticalTag())) {
								newRules = lexicalPatternEvolvedTest(actualSentence, lexicalPattern, x, y, wordsBetweenXAndY);
								if(newRules.length() > 0) res.append(newRules);
							}
						}
					}

				}
			}

		}
		return res.toString();
	}

	public String getRelaFromWiki(String wordToCompute) throws IOException{
		StringBuilder res = new StringBuilder();
		int cptNbRelTotal = 0;
		WikiExtractor wikiExtractor = new WikiExtractor(wordToCompute);
		String[] allSentences = wikiExtractor.getTextExtracted().split("\\.");
		//this.processAllText(wikiExtractor.getTextExtracted());
		this.processAllText1(wikiExtractor.getTextExtracted());
		System.out.println(wikiExtractor.getTextExtracted());
		System.out.println(this.getDicoMots().keySet().contains("phylogénétiqueLes"));
		//lexicalPatterns.getLexicalPatterns()
		graph.LexicalPattern lexicalPatterns = new graph.LexicalPattern();
		
		for (ArrayList<String> a : lexicalPatterns.getLexicalPatterns().values()) {
			System.out.println(a);
		}
		
		
		
		String stringAllMeanings = lexicalPatterns.getMeaningBrutString();
		this.processAllText1(stringAllMeanings);
		String betweenXandYLemm;
		ArrayList<String> tmpRel = new ArrayList<String>();
		//pour toutes les phrases
		for(int cptSent = 0;cptSent < allSentences.length;cptSent++){
			//System.out.println("Nb phrases traitees:" + cptSent + " sur " + allSentences.length + " phrases.");
			//System.out.println("Phrase:" + allSentences[cptSent]);
			//On sépare chaque mot de la phrase
			if(allSentences[cptSent].startsWith(" ")){
				allSentences[cptSent] = allSentences[cptSent].substring(1,allSentences[cptSent].length());
			}
			String listWords[] = allSentences[cptSent].split(" ");
			
			for (String motls : listWords) {
				System.out.print(motls+" ");
			}
				System.out.println();
			
			
			//on va fixer le x
			for(int XIndex = 0;XIndex < listWords.length - 2;XIndex++) {
				String x = listWords[XIndex];
				if(!checkWord(x)) {
					ArrayList<graph.Word> tmp = graph.Word.process(x);
					this.getDicoMots().put(x, tmp.get(0));
				}
				//System.out.println(x+" "+this.dicoMots.get(x));
				if(this.dicoMots.get(x)!=null && this.dicoMots.get(x).grammClassRelevant()){
					//on va fixer le y
					for(int YIndex = XIndex+2;YIndex < listWords.length;YIndex++) {
						//System.out.println(listWords[XIndex]+" -> "+dejavus.get(listWords[XIndex]));
						
						//Si le x et le y ne sont pas trop  eloignes
						if(YIndex - XIndex < 7) {
							String y = listWords[YIndex];
							if(!checkWord(y)) {
								ArrayList<graph.Word> tmp = graph.Word.process(y);
								this.getDicoMots().put(y, tmp.get(0));
							}
							//Si le y vient d'une classe grammaticale interessante
							//System.out.println("Taille listMots:" + listWords.length);
							//System.out.println("Index y" + YIndex + " mot =" + listWords[YIndex]);
							//System.out.println(this.dicoMots.get(listWords[YIndex]));
							
							
							if(this.dicoMots.get(listWords[YIndex])!=null && this.dicoMots.get(listWords[YIndex]).grammClassRelevant()){
								//on recupere les mots entre x et y
								//betweenXandYLemm = allSentences[cptSent].substring(XIndex + 1, YIndex - 1);
								
								betweenXandYLemm = getBetween(listWords, XIndex, YIndex);
								//System.out.println(allSentences[cptSent]);
								//								System.out.println(XIndex + ":" + YIndex);
								//								System.out.println(betweenXandYLemm);
								//On lemmatise les mots entre x et y
								betweenXandYLemm = this.getSentLemmatized(betweenXandYLemm);
								
//								if (lexicalPatterns.getLexicalPatterns().values().contains(betweenXandYLemm+" ")) {
//									System.out.println("---- Affiche : "+betweenXandYLemm);
//									continue;
//								}
								
								//if (!checkInPattern(lexicalPatterns,betweenXandYLemm)) continue;
								
								//On compare les mots lemmatises entre x et y avec toutes les traductions qui seront lemmatisees
								tmpRel = this.compareLexWordsBetw(lexicalPatterns, betweenXandYLemm, XIndex, YIndex, listWords);
								//Si on a des nouvelles relations on les rajoute
								if(tmpRel.size() > 0){
									//System.out.println("xi:"+listWords[XIndex]+this.dicoMots.get(listWords[XIndex]).getGrammaticalTag());
									//System.out.println("yi:"+listWords[YIndex]+this.dicoMots.get(listWords[YIndex]).getGrammaticalTag());
									for(String newRel: tmpRel){
										res.append(listWords[XIndex] + " " + newRel + " " + listWords[YIndex]+",");
										cptNbRelTotal++;
									}
								}
							}
						}
					}
				}
			}
			System.out.println(cptSent + " done sur " + allSentences.length + " Taille res:" + cptNbRelTotal);
			System.out.println(res);
		}
		return res.toString();
	}
	
	public boolean checkInPattern(LexicalPattern lexicalPatterns, String betweenXandYLemm) {
		for (ArrayList a : lexicalPatterns.getLexicalPatterns().values()) {
			if (a.contains(betweenXandYLemm)) {
				//System.out.println("---- Affiche : "+betweenXandYLemm);
				return true;
			}
		}
		return false;
	}

	public boolean checkWord(String s) {
		return this.getDicoMots().containsKey(s);
	}

	public static String getBetween(String[] wordsSentence,int Xindex,int Yindex){
		StringBuilder res = new StringBuilder();
		for(int i = Xindex + 1;i < Yindex;i++){
			res.append(wordsSentence[i]);
			res.append(" ");
		}
		res.delete(res.length()-1,res.length());
		return res.toString();
	}

	public String getSentLemmatized(String sentence) throws IOException{
		StringBuilder res = new StringBuilder();
		for(String word: sentence.split(" ")){
			//			System.out.println(word);
			//			System.out.println(sentence);
			graph.Word wordTmp = this.dicoMots.get(word);
			//			System.out.println(this.getDicoMots().keySet().contains(word));
			//			System.out.println(wordTmp.toString());
			//			System.out.println(wordTmp.getInitialWord());
			if (wordTmp!=null) {
				if(wordTmp.hasMultipleLemm())
					res.append(wordTmp.getUniqueLemm(sentence));
				else {
					res.append(wordTmp.getLemmatizedWord().get(0));
				}
			}

			res.append(" ");
		}
		res.delete(res.length()-1,res.length());
		return res.toString();
	}


	public String getCurrentSentLemm(String sentence) throws IOException{
		StringBuilder res = new StringBuilder();
		String listWords[] = sentence.split(" ");
		//pour tous les mots de la phrase
		for(String word: listWords){
			//pour toutes les mots du dico
			for(String key: this.dicoMots.keySet()){
				//Si le mot evalue correspond a la cle du dico alors on va rajouter le lemm a la phrase
				if(word.equals(key)){
					if(this.dicoMots.get(key).hasMultipleLemm()){
						res.append(this.dicoMots.get(key).getUniqueLemm(sentence));
					}
					else{
						res.append(this.dicoMots.get(key).getLemmatizedWord().get(0));
					}
					res.append(" ");
				}
			}
		}
		//On enleve l'espace de fin
		res.delete(res.length()-1, res.length());
		return res.toString();
	}



	public HashMap<String,ArrayList<String>> getLemmatizedPattern(LexicalPattern lexicalPattern) throws IOException{
		HashMap<String,ArrayList<String>> res = new HashMap<String,ArrayList<String>>();
		StringBuilder tmpSentenceLem = new StringBuilder();
		ArrayList<String> allMeaningsLemm = new ArrayList<String>();
		graph.Word tmpWord;
		//Pour toutes les cles
		for(String currentRel: lexicalPattern.getLexicalPatterns().keySet()){
			//Pour toutes les traductions
			for(String currentMeaning: lexicalPattern.getLexicalPatterns().get(currentRel)){
				//Pour tous les mots
				for(String currentWord: currentMeaning.split(" ")){
					//Si le mot est inconnu
					if(!this.dicoMots.containsKey(currentWord)){
						tmpWord = new graph.Word(currentWord);
						//ON rajoute le mot dans le dico
						this.dicoMots.put(currentWord, tmpWord);
					}
					else {
						tmpWord = this.dicoMots.get(currentWord);
					}
					//Si le mot a plusieurs lemm possible on choisit le bon
					if(tmpWord.hasMultipleLemm()) tmpSentenceLem.append(tmpWord.getUniqueLemm(currentMeaning));
					else { tmpSentenceLem.append(tmpWord.getLemmatizedWord().get(0)); }
					tmpSentenceLem.append(" ");
				}
				//On enleve le dernier espace
				tmpSentenceLem.delete(tmpSentenceLem.length() - 1, tmpSentenceLem.length());
				//on ajoute la phrase lemmatise a la liste des traduction lemma de la relation courante
				allMeaningsLemm.add(tmpSentenceLem.toString());
				tmpSentenceLem.delete(0, tmpSentenceLem.length());
			}
			res.put(currentRel, allMeaningsLemm);
			allMeaningsLemm.clear();
		}
		return res;
	}

	public void processAllText(ArrayList<String> text){
		int cpt = 0;
		for(String s: text){
			//System.out.println("Nb Rela traitées:" + cpt + " sur " + text.size());
			//processAllText(s);
			processAllText1(s);
			cpt++;
		}
	}

	public void processAllText1(String text) {
		ArrayList<graph.Word> listWords = graph.Word.process(text);
		int cpt = 0;
		for(graph.Word p: listWords) {
			if(!this.dicoMots.containsKey(p.getInitialWord()))this.dicoMots.put(p.getInitialWord(), p);
			//Si on a deja vu le mot
			else { 
				//S'il a une nouvelle classe
				if(hasNewGramm(p)) {
					this.dicoMots.get(p.getInitialWord()).getGrammaticalTag().add(p.getGrammaticalTag().get(0));
				}
				//s'il a un nouveau lemm
				if(hasNewLemm(p))
					this.dicoMots.get(p.getInitialWord()).getLemmatizedWord().add(p.getGrammaticalTag().get(0));
			}
			System.out.println(cpt + " sur " + text.length());
			cpt += p.getInitialWord().length();
		}

	}

	public boolean hasNewLemm(graph.Word w) {
		if(this.getDicoMots().get(w.getInitialWord()).getLemmatizedWord().contains(w.getGrammaticalTag().get(0)))
			return true;
		return false;	
	}

	public boolean hasNewGramm(graph.Word w) {
		if(this.getDicoMots().get(w.getInitialWord()).getGrammaticalTag().contains(w.getGrammaticalTag().get(0)))
			return true;
		return false;	
	}

	//
	//	public void processAllText(String text) {
	//		ArrayList<graph.Word> listWords = graph.Word.process(text);
	//		for(graph.Word p: listWords) {
	//			//Si on ne connait pas le mot
	//			if(!this.dicoMots.containsKey(p.getInitialWord()))this.dicoMots.put(p.getInitialWord(), p);
	//			//Si on a deja vu le mot
	//			else { 
	//				for(String s: p.getLemmatized().split("\\|")){
	//					//Si on a pas deja le mot lemmatise
	//					if(!this.dicoMots.get(p.getWord()).getLemmatizedWord().contains(s)){
	//						this.dicoMots.get(p.getWord()).getLemmatizedWord().add(s);
	//					}
	//				}
	//				if(this.dicoMots.get(p.getWord()).getGrammaticalTag().contains(p.getGrammaticalClass()))
	//					this.dicoMots.get(p.getWord()).getGrammaticalTag().add(p.getGrammaticalClass());
	//			}
	//		}
	//	}

	public ArrayList<String> compareLexWordsBetw(LexicalPattern lexicalPattern,String sentenceLemm,int XIndex,int YIndex, String[] listWords) throws IOException{
		ArrayList<String> res = new ArrayList<String>();
		boolean isThere = false;
		for (ArrayList a : lexicalPattern.getLexicalPatterns().values()) {
			if (a.contains(sentenceLemm)) {
				//System.out.println("---- Affiche : "+betweenXandYLemm);
				isThere = true;
			}
		}
		if (isThere = false) return res;
		for(String rel: lexicalPattern.getLexicalPatterns().keySet()){
			for(String meaning: lexicalPattern.getLexicalPatterns().get(rel)){
				//String currentMeaningLemmatized = this.getSentLemmatized(meaning);
				if(meaning.equals(sentenceLemm)){
					res.add(rel);
					break;
				}
			}
		}
		return res;
	}


	public static String lexicalPatternEvolvedTest(String sentence,LexicalPattern lexicalPattern, graph.Word x, graph.Word y,ArrayList<graph.Word> betweenXAndY) throws IOException {
		StringBuilder res = new StringBuilder();
		StringBuilder wordsBetween = new StringBuilder();
		for(int i = 0;i < betweenXAndY.size();i++) {
			if(betweenXAndY.get(i).hasMultipleLemm()) wordsBetween.append(betweenXAndY.get(i).getUniqueLemm(sentence));
			else { 
				wordsBetween.append((betweenXAndY.get(i).getLemmatizedWord().get(0)));
			}
			wordsBetween.append(" ");
		}
		for(String key: lexicalPattern.getLexicalPatterns().keySet()) {
			for(String meaning: lexicalPattern.getLexicalPatterns().get(key)) {
				//Si les mots entre x et y correspondent a la traduction de la regle
				if(wordsBetween.toString().equals(meaning)) {
					//System.out.println("Match wordsbetween et meaning= " + wordsBetween + " " +  meaning);
					//System.out.println("X = " + x + " Y = " + y + " Words between = " + wordsBetween + " Meaning = " + meaning);
					res.append(x + " " + key + " " + y);
					res.append("\n");
				}
			}
		}
		return res.toString();
	}


	/**
	 * 
	 * @return Les nouvelles relations trouvees 
	 */
	//Probleme de match entre la traduction de la regle et les mots entre
	public static String lexicalPatternTest(LexicalPattern lexicalPattern,String x,String y,String wordsBetween) {
		StringBuilder res = new StringBuilder();

		//Pour toutes les cles
		for(String key: lexicalPattern.getLexicalPatterns().keySet()) {
			//pour toutes les traductions de la cle
			for(String meaning: lexicalPattern.getLexicalPatterns().get(key)) {

				//Si les mots entre x et y correspondent a la traduction de la regle
				if(wordsBetween.equals(meaning)) {
					//System.out.println("Match wordsbetween et meaning= " + wordsBetween + " " +  meaning);
					//System.out.println("X = " + x + " Y = " + y + " Words between = " + wordsBetween + " Meaning = " + meaning);
					res.append(x + " " + key + " " + y);
					res.append("\n");
				}
			}
		}
		return res.toString();
	}

}
