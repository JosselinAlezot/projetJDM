package graph;

import java.util.ArrayList;

import org.annolab.tt4j.TreeTaggerException;
import org.annolab.tt4j.TreeTaggerWrapper;

import com.oracle.webservices.internal.api.message.PropertySet.Property;

import static java.util.Arrays.asList;

import java.io.IOException;

public class Word {

	private String initialWord;
	private ArrayList<String> grammaticalTag = new ArrayList<String>();
	private ArrayList<String> lemmatizedWord = new ArrayList<String>();
	public static final ArrayList<String> relevantGrammTags = new ArrayList<String>() {
		{
			add("ADJ");
			add("NAM");
			add("NOM");
			add("VER:cond");
			add("VER:futu");
			add("VER:impe");
			add("VER:impf");
			add("VER:infi");
			add("VER:pper");
			add("VER:ppre");
			add("VER:pres");
			add("VER:simp");
			add("VER:subi");
			add("VER:subp");
		}};

		public Word(PropertyHolder word) {
			this.grammaticalTag = this.getGrammaticalTag();
			if(!this.getGrammaticalTag().contains(word.getGrammaticalClass()))
				this.grammaticalTag.add(word.getGrammaticalClass());
			this.initialWord = word.getWord();
			for(String s: word.getLemmatized().split("\\|")) {
				this.lemmatizedWord.add(s);
			}
		}

		public Word(String word) {
			ArrayList<PropertyHolder> caracWord = process(word);
			initialWord = word;
			//System.out.println("Word:" + word);
			//System.out.println("carac word size:" + caracWord.size());
			if(!grammaticalTag.contains(caracWord.get(0).getGrammaticalClass()))
				grammaticalTag.add(caracWord.get(0).getGrammaticalClass());
			String lemmatizedWords = caracWord.get(0).getLemmatized();

			for(String s: lemmatizedWords.split("\\|")) {
				lemmatizedWord.add(s);
			}
		}

		public ArrayList<String> getGrammaticalTag() {
			return grammaticalTag;
		}
		public void setGrammaticalTag(ArrayList<String> grammaticalTag) {
			this.grammaticalTag = grammaticalTag;
		}
		public ArrayList<String> getLemmatizedWord() {
			return lemmatizedWord;
		}
		public void setLemmatizedWord(ArrayList<String> lemmatizedWord) {
			this.lemmatizedWord = lemmatizedWord;
		}


		public boolean grammClassRelevant(){
			if(this.getGrammaticalTag().size() > 0){
				for(String tmp: this.getGrammaticalTag()){
					if(graph.Word.relevantGrammTags.contains(tmp)) return true;
				}
				return false;
			}
			return false;
		}


		/**
		 * 
		 * @param text Texte a evaluer
		 * @return Liste de propertyHolder qui contiendra pour chaque le mot initial,sa classe grammatical et ses lemmatisations
		 */
		public static ArrayList<PropertyHolder> process(String text) {
			System.setProperty("treetagger.home", "tree-tagger-windows-3.2.2/TreeTagger/lib");
			TreeTaggerWrapper<String> tt = new TreeTaggerWrapper<>();
			ArrayList<PropertyHolder> relationSet = new ArrayList<PropertyHolder>();
			try {
				tt.setModel("TreeTagger/lib/french.par:utf-8");
				tt.setHandler((token, pos, lemma) -> relationSet.add(new PropertyHolder(token, pos, lemma)));
				tt.process(asList(text.split("[\\s]")));
			} catch (IOException | TreeTaggerException e) {
				e.printStackTrace();
			} finally {
				tt.destroy();
			}
			return relationSet;
		}

		public boolean hasMultipleLemm() {
			return this.getLemmatizedWord().size() > 1;
		}

		public String getUniqueLemm(String sentence) throws IOException {
			if(hasMultipleLemm()) {
				return this.lemmatizedWordChosen(sentence);
			}
			return "";
		}

		/**
		 * 
		 * @param sentence Phrase ou le mot a plusieurs lemmatisations est present
		 * @return Le mot lemmatise choisi
		 * @throws IOException 
		 */
		public String lemmatizedWordChosen(String sentence) throws IOException {
			String res = "";
			ArrayList<PropertyHolder> wordProperty;
			ArrayList<Edge> relTwoWords;
			ArrayList<Integer> numberRelByLemmatizedWord = new ArrayList<Integer>();
			main.Main m = new main.Main();
			m.initWord(m,sentence);
			wordProperty = process(sentence);
			//Pour chacun des mots lemmatise on va compter le nombre de relations avec les autres mots interessants de la phrase
			for(String currentLem: this.getLemmatizedWord()) {
				//Compteur du nombre de relations entre le mot lemmatise et les autres mots interessant de la phrase
				//System.out.println(currentLem);
				int cpt = 0;
				int cpt2 = 0;
				//Pour chaque mot de la phrase
				for(String s: sentence.split(" ")) {
					//si le mot actuel n'est pas celui qui a la contrainte de multiple lemmatisations
					if(!s.equals(this.getInitialWord())) {
						//Si le mot est interessant
						if(relevantGrammTags.contains(wordProperty.get(cpt2).getGrammaticalClass())) {
							relTwoWords = graph.Edge.getRelationsFromXandY(graph.Node.getNodeFromString(currentLem),graph.Node.getNodeFromString(s));
							cpt += relTwoWords.size();
						}
						//System.out.println("cpt:" + cpt);
					}
					cpt2++;
				}
				numberRelByLemmatizedWord.add(cpt);
			}
			//numberRelByLemmatizedWord contient le nb de rel total pour chaque mot lemmatise avec le reste de la phrase
			int nbMaxRel = 0;
			//On va chercher le nbMax de relations
			for(Integer i: numberRelByLemmatizedWord) {
				if(i > nbMaxRel)
					nbMaxRel = i;
			}
			//On retourne le mot contenant le max de relations
			return this.getLemmatizedWord().get(numberRelByLemmatizedWord.indexOf(nbMaxRel));
		}

		public String getInitialWord() {
			return initialWord;
		}

		public void setInitialWord(String initialWord) {
			this.initialWord = initialWord;
		}

		public static void main(String[] args) throws IOException, TreeTaggerException {
			System.setProperty("treetagger.home", "tree-tagger-windows-3.2.2/TreeTagger/lib");
			String texts = "La valorisation de son patrimoine historique, culturel et architectural a permis à la ville d'obtenir le label de Ville d'art et d'histoire. Depuis 2012, date de son inscription sur la liste indicative française, Nîmes travaille son dossier de candidature sur le thème « Nîmes, l'Antiquité au présent » pour l'inscription de la cité bimillénaire au patrimoine mondial de l'UNESCO2.";
			//			text = "n'était";


			for(String text: texts.split("\\.")) {
				process(text).forEach(System.out::println);
			}
			Word test = new Word("sommes");
			String onycroit = test.lemmatizedWordChosen("Nous sommes des veuve");
			System.out.println(onycroit);

			//			ArrayList<PropertyHolder> ontest = process(text);
			//			for(PropertyHolder p: ontest) {
			//				System.out.println("Mot:" + p.getWord() + ". Classe gramm:" + p.getGrammaticalClass() + ". Lemma:" + p.getLemmatized().toString());
			//			}
		}
}
