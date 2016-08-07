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




    public News()
    {
        super();
    }
    //Constructor Method
    public News(int id, String title, String description, String story){

        super();

        this.newsId = id;
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

    public int getNewsId() {
        return newsId;
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
            UpdateNews(existingNews,new_news);
            return existingNews;
        } else {
            // create and return new user
            News news = new_news;
            news.save();
            return news;
        }
    }

    private static void UpdateNews(News old_news,News new_news)
    {
        old_news.setTitle(new_news.getTitle());
        old_news.setDescription(new_news.getDescription());
        old_news.setStory(new_news.getStory());
        old_news.setCreated(new_news.getCreated());
        old_news.setImage_url(new_news.getImage_url());
        old_news.setUrl(new_news.getUrl());
        old_news.save();

    }


}
