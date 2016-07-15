/**
 * @param imageId
 * @param title
 * @param description
 * @param storyDetail
 */
package com.uwimonacs.fstmobile.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;


@Table(name="News")
public class News extends Model {
    //Variable Declarations

    @Column(name="newsId")
    int newsId; //differentiates each news item - do we need an id for each image?

    @Column(name="title")
    String title; //Headline

    @Column(name="description")
    String description; //Short excerpt of story

    @Column(name="story")
    String storyDetail; //Actual news story


    int image;


    public News()
    {
        super();
    }
    //Constructor Method
    public News(int id, int image, String title, String description, String story){

        super();

        this.newsId = id;
        this.title = title;
        this.image =image;
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

    public int getNewsId() {
        return newsId;
    }

    public int getImage() {
        return image;
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

    public void setNewsId(int newsId) {
        this.newsId = newsId;
    }



    public void setImage(int image) {
        this.image = image;
    }

    public static News findOrCreateFromJson(News new_news) {
        int newsId = new_news.getNewsId();
        News existingNews =
                new Select().from(Contact.class).where("newsId = ?", newsId).executeSingle();
        if (existingNews != null) {
            // found and return existing
            return existingNews;
        } else {
            // create and return new user
            News news = new_news;
            news.save();
            return news;
        }
    }


}
