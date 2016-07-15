package com.uwimonacs.fstmobile.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;
import com.google.gson.annotations.SerializedName;

/**
 * Model for FAQs
 */
@Table(name = "FAQ")
public class FAQ extends Model {

    @SerializedName("id")
    @Column(name = "FAQid")
    private int id;

    @SerializedName("question")
    @Column(name = "question")
    private String question;

    @SerializedName("answer")
    @Column(name = "answer")
    private String answer;

    //Constructors

    public FAQ()
    {
        super();
    }

    public FAQ(int id, String question, String answer)
    {
        super();

        this.id = id;
        this.question = question;
        this.answer = answer;
    }

    //Getters

    public int getFAQId() { return this.id; }

    public String getQuestion() { return this.question; }

    public String getAnswer() { return this.answer; }

    //Setters

    public void setId(int id) { this.id = id; }

    public void setQuestion(String question) { this.question = question; }

    public void setAnswer(String answer) { this.answer = answer; }

    public static FAQ findOrCreateFromJson(FAQ new_faq) {
        int FAQId = new_faq.getFAQId();

        FAQ existingFAQ = new Select().from(FAQ.class).where("FAQId = ?", FAQId).executeSingle();

        if (existingFAQ != null)
        {
            // found and return existing
            return existingFAQ;
        }

        else
        {
            // create and return new user
            FAQ faq = new_faq;
            faq.save();
            return faq;
        }
    }
}
