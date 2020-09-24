import java.util.*;

class ActionsSet {

    private ArrayList<String> triggers,subjects,consumed,produced;
    private String narration;

    public ActionsSet(){
        this.triggers = new ArrayList<String>();
        this.subjects = new ArrayList<String>();
        this.consumed = new ArrayList<String>();
        this.produced = new ArrayList<String>();
    }

    public void addTriggers(String triggers){
        this.triggers.add(triggers);
    }

    public boolean containTriggers(String triggers){
        return this.triggers.contains(triggers);
    }

    public void addSubjects(String subjects){
        this.subjects.add(subjects);
    }

    public ArrayList<String> getSubjects(){
        return subjects;
    }

    public void addConsumed(String consumed){
        this.consumed.add(consumed);
    }

    public ArrayList<String> getConsumed(){
        return consumed;
    }

    public void addProduced(String produced){
        this.produced.add(produced);
    }

    public ArrayList<String> getProduced(){
        return produced;
    }

    public void setNarrtion(String narration){
        this.narration = narration;
    }

    public String getNarration(){
        return narration;
    }
}