package main;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import graph.LexicalPattern;
import graph.PropertyHolder;
import graph.Sentence;

public class MainV2 {

	private HashMap<String,graph.Word> dicoMots = new HashMap<String,graph.Word>();

	public static void main(String args[]) throws IOException {
		MainV2 test = new MainV2();
		System.out.println(test.getRelaFromWiki("Plante"));
		//System.out.println(test.getAllRelations("Plante"));
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
		
		WikiExtractor wikiExtractor = new WikiExtractor(wordToCompute);
		String[] allSentences = wikiExtractor.getTextExtracted().split("\\.");
		this.processAllText(wikiExtractor.getTextExtracted());
		System.out.println(wikiExtractor.getTextExtracted());
		System.out.println(this.getDicoMots().keySet().contains("phylogénétiqueLes"));
//
//		graph.LexicalPattern lexicalPatterns = new graph.LexicalPattern();
//		ArrayList<String> stringAllMeanings = lexicalPatterns.getMeaningsBrut();
//		this.processAllText(stringAllMeanings);
		
		String betweenXandYLemm;
		ArrayList<String> tmpRel = new ArrayList<String>();
		//pour toutes les phrases
		for(int cptSent = 0;cptSent < allSentences.length;cptSent++){
			System.out.println("Nb phrases traitees:" + cptSent + " sur " + allSentences.length + " phrases.");
			System.out.println("Phrase:" + allSentences[cptSent]);
			//On sépare chaque mot de la phrase
			if(allSentences[cptSent].startsWith(" ")){
				allSentences[cptSent] = allSentences[cptSent].substring(1,allSentences[cptSent].length());
			}
			String listWords[] = allSentences[cptSent].split(" ");
			//on va fixer le x
			for(int XIndex = 0;XIndex < listWords.length - 2;XIndex++) {
				String x = listWords[XIndex];
				//Si le x a une classe grammaticale interessante
//				System.out.println("Phrase:" + allSentences[cptSent]);
//				System.out.println("test listwords:" + listWords[0]);
//				System.out.println("test listwords:" + listWords[1]);
//				System.out.println("test listwords:" + listWords[2]);
//				System.out.println("test listwords:" + listWords[3]);
//				System.out.println("Word numero: " + XIndex);
//				System.out.println(listWords.length);
//				System.out.println("test listwords:" + listWords[XIndex]);
//				System.out.println("on est la " + x);
				if(this.dicoMots.get(x).grammClassRelevant()){
					//on va fixer le y
					for(int YIndex = XIndex+2;YIndex < listWords.length;YIndex++) {
						//Si le x et le y ne sont pas trop  eloignes
						if(YIndex - XIndex < 7) {
							//Si le y vient d'une classe grammaticale interessante
							if(this.dicoMots.get(x).grammClassRelevant()){
								
								//on recupere les mots entre x et y
								//betweenXandYLemm = allSentences[cptSent].substring(XIndex + 1, YIndex - 1);
								betweenXandYLemm = getBetween(listWords, XIndex, YIndex);
								//System.out.println(allSentences[cptSent]);
//								System.out.println(XIndex + ":" + YIndex);
//								System.out.println(betweenXandYLemm);
								//On lemmatise les mots entre x et y
								System.out.println("BetweenXandY:" + betweenXandYLemm);
								betweenXandYLemm = this.getSentLemmatized(betweenXandYLemm);
								//On compare les mots lemmatises entre x et y avec toutes les traductions qui seront lemmatisees
								//tmpRel = this.compareLexWordsBetw(lexicalPatterns, betweenXandYLemm, XIndex, YIndex);
								//Si on a des nouvelles relations on les rajoute
								if(tmpRel.size() > 0){
									for(String newRel: tmpRel){
										res.append(listWords[XIndex] + " " + newRel + " " + listWords[YIndex]);
									}
								}
							}
						}
					}
				}
			}
		}
		return res.toString();
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
			if(wordTmp.hasMultipleLemm())
				res.append(wordTmp.getUniqueLemm(sentence));
			else {
				res.append(wordTmp.getLemmatizedWord().get(0));
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
			processAllText(s);
			cpt++;
		}
	}
	

	public void processAllText(String text) {
		ArrayList<graph.PropertyHolder> listWords = graph.Word.process(text);
		for(graph.PropertyHolder p: listWords) {
			if(p.getWord().equals("phylogénétiqueLes"))
				System.out.println("ouiii");
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

	public ArrayList<String> compareLexWordsBetw(LexicalPattern lexicalPattern,String sentenceLemm,int XIndex,int Yindex) throws IOException{
		ArrayList<String> res = new ArrayList<String>();
		for(String rel: lexicalPattern.getLexicalPatterns().keySet()){
			for(String meaning: lexicalPattern.getLexicalPatterns().get(rel)){
				String currentMeaningLemmatized = this.getSentLemmatized(meaning);
				if(currentMeaningLemmatized.equals(sentenceLemm))
					res.add(rel);
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