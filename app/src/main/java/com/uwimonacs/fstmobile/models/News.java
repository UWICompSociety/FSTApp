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
import com.google.gson.annotations.SerializedName;


@Table(name="News")
public class News extends Model {
    //Variable Declarations

    @SerializedName("id")
    @Column(name="newsId")
    int newsId; //differentiates each news item - do we need an id for each image?

    @SerializedName("title")
    @Column(name="title")
    String title; //Headline

    @SerializedName("description")
    @Column(name="description")
    String description; //Short excerpt of story

    @SerializedName("story")
    @Column(name="story")
    String storyDetail; //Actual news story

    @SerializedName("image_url")
    @Column(name="image_url")
    String image_url;


    @SerializedName("created")
    @Column(name="created")
    String created;

    @SerializedName("news_url")
    @Column(name="url")
    String url;


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


    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getStoryDetail() {
        return storyDetail;
    }

    public void setStoryDetail(String storyDetail) {
        this.storyDetail = storyDetail;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public static News findOrCreateFromJson(News new_news) {
        int newsId = new_news.getNewsId();
        News existingNews =
                new Select().from(News.class).where("newsId = ?", newsId).executeSingle();
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
