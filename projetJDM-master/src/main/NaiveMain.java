package main;

import java.io.IOException;

import com.sun.xml.internal.fastinfoset.algorithm.BuiltInEncodingAlgorithm.WordListener;

import graph.LexicalPattern;

public class NaiveMain {

	public static void main(String args[]) throws IOException {
		graph.LexicalPattern lexicalPattern = new graph.LexicalPattern();
//				WikiExtractor wikiPlante = new WikiExtractor("Jésus_de_Nazareth");
//				String allSentences[] = wikiPlante.getTextExtracted().split("\\.");

		String test = "Paul est un blond. Bidule a pour pos xd. Bleu est de couleur rouge.";
		String allSentences[] = test.split("\\.");
		StringBuilder wordsBetweenXAndY = new StringBuilder();
		StringBuilder res = new StringBuilder();
		String newRules;
		//Pour toutes les phrases
		for(int nbSentence = 0;nbSentence < allSentences.length;nbSentence++) {
			//On sépare chaque mot de la phrase
			String actualSentenceSeparated[] = allSentences[nbSentence].split(" ");
			//System.out.println("Actual Sentence = " + allSentences[nbSentence]);
			//On definit un x qui s'arrete 2 mots avant la fin parce qu'il n'y aura pas de patrons apres 
			for(int XIndex = 0;XIndex < actualSentenceSeparated.length - 2;XIndex++) {
				String x = actualSentenceSeparated[XIndex];

				//On definit un y 
				for(int YIndex = XIndex+2;YIndex < actualSentenceSeparated.length;YIndex++) {
					if(YIndex - XIndex < 7) {
						wordsBetweenXAndY = wordsBetweenXAndY.delete(0,wordsBetweenXAndY.length());
						String y = actualSentenceSeparated[YIndex];

						//Pour tous les mots entre x et y 
						for(int i = XIndex + 1;i < YIndex;i++) {
							//On ajoute le mot courant a l'ensemble des mots entre
							wordsBetweenXAndY.append(actualSentenceSeparated[i]);
							//Tant qu'on est pas au dernier mot on rajoute un espace entre le mot rajouté et le prochain
							if(i < YIndex -1) wordsBetweenXAndY.append(" ");
						}

						//Test d'apparition de nouveau patrons lexicaux
						newRules = lexicalPatternTest(lexicalPattern, x, y, wordsBetweenXAndY.toString());
//						System.out.println("X =" + x);
//						System.out.println("Y =" + y);
//						System.out.println("Words between =" + wordsBetweenXAndY);
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
