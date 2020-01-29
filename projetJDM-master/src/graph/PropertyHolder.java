package graph;

public class PropertyHolder {
    private String word;
    private String grammaticalClass;
    private String lemmatized;

    public PropertyHolder(String word, String grammaticalClass, String lemmatized) {
        this.word = word;
        this.grammaticalClass = grammaticalClass;
        this.lemmatized = lemmatized;
    }

    public String getWord() {
        return word;
    }

    public String getGrammaticalClass() {
        return grammaticalClass;
    }

    public String getLemmatized() {
        return lemmatized;
    }

    public String toString() {
        return this.word + " -> " +  this.grammaticalClass + " -> " +  this.lemmatized;
    }
}