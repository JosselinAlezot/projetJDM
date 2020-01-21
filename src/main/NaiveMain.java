package main;

import java.io.IOException;

import graph.LexicalPattern;

public class NaiveMain {

	public static void main(String args[]) throws IOException {
		graph.LexicalPattern lexicalPattern = new graph.LexicalPattern();
//		WikiExtractor wikiPlante = new WikiExtractor("Jésus_de_Nazareth");
//		String allSentences[] = wikiPlante.getTextExtracted().split("\\.");
		
		String test = "Paul est un blond. Bidule a pour pos xd. Bleu est de couleur rouge.";
		String allSentences[] = test.split("\\.");
		StringBuilder wordsBetweenXAndY = new StringBuilder();
		StringBuilder res = new StringBuilder();
		String newRules;
		System.out.println(allSentences[0]);
		//Pour toutes les phrases
		for(int nbSentence = 0;nbSentence < allSentences.length;nbSentence++) {
			//On sépare chaque mot de la phrase
			String actualSentence[] = allSentences[nbSentence].split(" ");
			System.out.println("Actual Sentence = " + actualSentence[0]);
			//On definit un x qui s'arrete 2 mots avant la fin parce qu'il n'y aura pas de patrons apres 
			for(int XIndex = 0;XIndex < actualSentence.length - 2;XIndex++) {
				String x = actualSentence[XIndex];

				//On definit un y 
				for(int YIndex = 2;YIndex < actualSentence.length;YIndex++) {
					if(YIndex - XIndex < 7) {
						String y = actualSentence[YIndex];

						//On recupere les mots entre les x et y 
						for(int i = XIndex + 1;i < YIndex - 1;i++) {
							wordsBetweenXAndY.append(actualSentence[i]);
						}

						//Test d'apparition de nouveau patrons lexicaux
						newRules = lexicalPatternTest(lexicalPattern, x, y, wordsBetweenXAndY.toString());
						if(newRules.length() > 0) res.append(newRules);
					}
				}
			}
		}
		System.out.println("Result:" + res.toString());
	}


	/**
	 * 
	 * @return Les nouvelles relations trouvees 
	 */
	public static String lexicalPatternTest(LexicalPattern lexicalPattern,String x,String y,String wordsBetween) {
		StringBuilder res = new StringBuilder();
		System.out.println("Words between = " + wordsBetween);
		//Pour toutes les cles
		for(String key: lexicalPattern.getLexicalPatterns().keySet()) {
			//pour toutes les traductions de la cle
			for(String meaning: lexicalPattern.getLexicalPatterns().get(key)) {
				//Si les mots entre x et y correspondent a la traduction de la regle
				if(wordsBetween.equals(meaning)) {
					System.out.println("X = " + x + " Y = " + y + " Words between = " + wordsBetween + " Meaning = " + meaning);
					res.append(x + " " + key + " " + y);
					res.append("\n");
				}
			}
		}
		if (res.length() > 0) System.out.println("YOUPI");
		return res.toString();
	}

}
