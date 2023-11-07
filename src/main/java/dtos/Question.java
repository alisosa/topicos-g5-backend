package dtos;



public class Question {

    private String question;
    private String type;
    private Boolean scorable;

    public Question (String question, String type){
        this.question = question;
        this.type = type;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Boolean getScorable() {
        return scorable;
    }

    public void setScorable(Boolean scorable) {
        this.scorable = scorable;
    }
}
