package graph;

import java.util.ArrayList;

public class Sentence {

	private ArrayList<Word> wordsFromSentence;
	
	public Sentence(String sentence) {
		ArrayList<graph.Word> wordsProcessed = graph.Word.process(sentence);
		for(Word p: wordsProcessed) {
			this.wordsFromSentence.add(p);
		}
	}

	public ArrayList<Word> getWordsFromSentence() {
		return wordsFromSentence;
	}

	public void setWordsFromSentence(ArrayList<Word> wordsFromSentence) {
		this.wordsFromSentence = wordsFromSentence;
	}
	
	public ArrayList<Word> getWordsBetweenXAndY(ArrayList<Word> sentence,int x,int y){
		ArrayList<Word> res = new ArrayList<Word>();
		for(int i = x + 1;i < y;i++) {
			res.add(sentence.get(i));
		}
		return res;
	}
	
}
