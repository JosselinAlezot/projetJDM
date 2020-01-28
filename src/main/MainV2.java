package main;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import graph.LexicalPattern;
import graph.PropertyHolder;

public class MainV2 {

	private HashMap<String,graph.Word> dicoMots = new HashMap<String,graph.Word>();

	public static void main(String args[]) throws IOException {
		MainV2 test = new MainV2();
		test.getRelaFromWiki("Plante");
		//System.out.println(test.getAllRelations("Plante"));
	}



	public String getAllRelations(String wordToCompute) throws IOException{
		int cpt = 0;
		StringBuilder res = new StringBuilder();
		String newRules = "";
		graph.LexicalPattern lexicalPattern = new graph.LexicalPattern();
		lexicalPattern.setLexicalPatterns(lexicalPattern.getLexicalPatternLemmatized());
		System.out.println("cpt:" + cpt);
		WikiExtractor wiki = new WikiExtractor(wordToCompute);
		String allSentences[] = wiki.getTextExtracted().split("\\.");
		//POur toutes les phrases du texte
		for(String actualSentence: allSentences) {
			System.out.println("cpt:" + cpt);
			cpt++;
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
		
		WikiExtractor wikiExtractor = new WikiExtractor(wordToCompute);
		this.processAllText(wikiExtractor.getTextExtracted());
		System.out.println("CC");
		System.out.println(this.dicoMots.size());
		graph.LexicalPattern lexicalPatterns = new graph.LexicalPattern();
		lexicalPatterns.setLexicalPatterns(this.getLemmatizedPattern(lexicalPatterns));
		System.out.println("cc");
		
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

	public void processAllText(String text) {
		ArrayList<graph.PropertyHolder> listWords = graph.Word.process(text);
		for(graph.PropertyHolder p: listWords) {
			//Si on ne connait pas le mot
			if(!this.dicoMots.containsKey(p.getWord()))this.dicoMots.put(p.getWord(), new graph.Word(p));
			//Si on a deja vu le mot
			else { 
				for(String s: p.getLemmatized().split("\\|")){
					//Si on a pas deja le mot lemmatise
					if(!this.dicoMots.get(p.getWord()).getLemmatizedWord().contains(s)){
						this.dicoMots.get(p.getWord()).getLemmatizedWord().add(s);
					}
				}
				if(this.dicoMots.get(p.getWord()).getGrammaticalTag().contains(p.getGrammaticalClass()))
					this.dicoMots.get(p.getWord()).getGrammaticalTag().add(p.getGrammaticalClass());
			}
		}
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
