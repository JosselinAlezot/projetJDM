package graph;

import java.util.ArrayList;

public class Sentence {

	private ArrayList<Word> wordsFromSentence;
	
	public Sentence(String sentence) {
		ArrayList<PropertyHolder> wordsProcessed = graph.Word.process(sentence);
		for(PropertyHolder p: wordsProcessed) {
			this.wordsFromSentence.add(new Word(p));
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
		for(int i = 1;i < y;i++) {
			res.add(sentence.get(x + i));
		}
		return res;
	}
}
