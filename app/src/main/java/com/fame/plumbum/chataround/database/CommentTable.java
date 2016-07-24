package com.fame.plumbum.chataround.database;

/**
 * Created by pankaj on 23/7/16.
 */
public class CommentTable {
    String post_id;
    int comment_id;
    String commentor_id;
    String comment;
    String timestamp;

    public CommentTable(){
        post_id = "";
        comment = "";
        commentor_id = "";
        timestamp = "";
    }

    public CommentTable(String post_id, int comment_id, String commentor_id, String comment, String timestamp, String image){
        this.timestamp = timestamp;
        this.commentor_id = commentor_id;
        this.comment = comment;
        this.comment_id = comment_id;
        this.post_id = post_id;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setComment_id(int comment_id) {
        this.comment_id = comment_id;
    }

    public void setCommentor_id(String commentor_id) {
        this.commentor_id = commentor_id;
    }

    public void setPost_id(String post_id) {
        this.post_id = post_id;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public String getComment() {
        return comment;
    }

    public String getPost_id() {
        return post_id;
    }

    public int getComment_id() {
        return comment_id;
    }

    public String getCommentor_id() {
        return commentor_id;
    }
}
