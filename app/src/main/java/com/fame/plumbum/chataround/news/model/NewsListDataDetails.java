package com.fame.plumbum.chataround.news.model;

/**
 * Created by ramya on 10/3/17.
 */

public class NewsListDataDetails {
    private String published_at;
    private String image;
    private String title;
    private String body;
    private String body_small;
    private String source;
    private String author;

    public NewsListDataDetails(String published_at, String image, String title, String body, String body_small, String source, String author) {
        this.published_at = published_at;
        this.image = image;
        this.title = title;
        this.body = body;
        this.body_small = body_small;
        this.source = source;
        this.author = author;
    }

    public String getPublished_at() {
        return published_at;
    }

    public void setPublished_at(String published_at) {
        this.published_at = published_at;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getBody_small() {
        return body_small;
    }

    public void setBody_small(String body_small) {
        this.body_small = body_small;
    }
}
