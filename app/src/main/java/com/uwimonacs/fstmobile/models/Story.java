/**
 * @param imageId
 * @param title
 * @param description
 * @param storyDetail
 */
package com.uwimonacs.fstmobile.models;

import com.uwimonacs.fstmobile.R;

import java.util.ArrayList;
import java.util.List;

public class Story{
    //Variable Declarations
    int imageId; //differentiates each news item - do we need an id for each image?
    String title; //Headline
    String description; //Short excerpt of story
    String storyDetail; //Actual news story

    //Constructor Method
    public Story(int id, String title, String description, String story){
        this.imageId = id;
        this.title = title;
        this.description = description;
        this.storyDetail = story;
    }

    /*Getters*/


    public String getTitle(){
        return title;
    }

    public String getDescription(){
        return description;
    }

    public String getStory(){
        return storyDetail;
    }

    public int getImageId(){
        return imageId;
    }

    /*Setters*/
    public void setTitle(String newTitle){
        title = newTitle;
    }

    public void setDescription(String newDescription){
        description = newDescription;
    }

    public void setStory(String newStoryDetail){
        storyDetail = newStoryDetail;
    }

    public void setImageId(int newFeatureImage){
        imageId = newFeatureImage;
    }

    private List<Story> news;

    /*Array list with three news items*/
    private void placeHolder(){
        news = new ArrayList<>();
        //news.add(new Story(R.drawable.s1,"Title1","Description1","Detail1"));
        //news.add(new Story(R.drawable.s2,"Title2","Description2","Detail2"));
        //news.add(new Story(R.drawable.s3,"Title3","Description3","Detail3"));
    //	news.add(new Story(R.drawable.s4,"Title4","Description4","Detail4"));
    }
}
