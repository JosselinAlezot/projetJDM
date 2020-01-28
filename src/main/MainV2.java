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
		System.out.println(test.getAllRelations("Plante"));
	}

	public String getAllRelations(String wordToCompute) throws IOException{
		StringBuilder res = new StringBuilder();
		String newRules = "";
		graph.LexicalPattern lexicalPattern = new graph.LexicalPattern();
		lexicalPattern.setLexicalPatterns(lexicalPattern.getLexicalPatternLemmatized());
		WikiExtractor wiki = new WikiExtractor(wordToCompute);
		String allSentences[] = wiki.getTextExtracted().split("\\.");
		//POur toutes les phrases du texte
		for(String actualSentence: allSentences) {
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
							if(graph.Word.relevantGrammTags.contains(x.getGrammaticalTag())) {
								newRules = this.lexicalPatternEvolvedTest(actualSentence, lexicalPattern, x, y, wordsBetweenXAndY);
								if(newRules.length() > 0) res.append(newRules);
							}
						}
					}

				}
			}

		}
		return res.toString();
	}

	public void processAllText(String text) {
		ArrayList<graph.PropertyHolder> listWords = graph.Word.process(text);
		for(graph.PropertyHolder p: listWords) {
			this.dicoMots.put(p.getWord(), new graph.Word(p));
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
