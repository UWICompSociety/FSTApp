package com.uwimonacs.fstmobile.models;

/**
 * Created by Brand_000 on 21/06/2016.
 * Model for FAQs
 */
public class FAQ {

    protected int id;
    protected String question;
    protected String answer;

    //Constructor
    public FAQ(int id, String question, String answer)
    {
        this.id = id;
        this.question = question;
        this.answer = answer;
    }

    //Getters

    public int getId() { return this.id; }

    public String getQuestion() { return this.question; }

    public String getAnswer() { return this.answer; }

    //Setters

    public void setId(int id) { this.id = id; }

    public void setQuestion(String question) { this.question = question; }

    public void setAnswer(String answer) { this.answer = answer; }
}
