package flashcards;

import java.io.Serializable;

public class FlashCard implements Serializable {
    private String term;
    private String definition;
    private int mistakes;

    public FlashCard(String term, String definition) {
        this.term = term;
        this.definition = definition;
        this.mistakes = 0;
    }

    public FlashCard(String term, String definition, int mistakes) {
        this.term = term;
        this.definition = definition;
        this.mistakes = mistakes;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public String getDefinition() {
        return definition;
    }

    public void setDefinition(String definition) {
        this.definition = definition;
    }

    public int getMistakes() {
        return mistakes;
    }

    public void setMistakes(int mistakes) {
        this.mistakes = mistakes;
    }

    @Override
    public String toString() {
        return "FlashCard{" +
                "term='" + term + '\'' +
                ", definition='" + definition + '\'' +
                ", mistakes=" + mistakes +
                '}';
    }
}
