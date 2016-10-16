package com.fame.plumbum.chataround.database;

/**
 * Created by pankaj on 25/8/16.
 */
public class NotifTable {
    String id, post_id;
    int NComment, NLike;

    public NotifTable(String post_id, int NComment, int NLike){
        this.post_id = post_id;
        this.NComment = NComment;
        this.NLike = NLike;
    }

    public String getPost_Id() {
        return post_id;
    }

    public int getNComment() {
        return NComment;
    }

    public int getNLike() {
        return NLike;
    }

    public void setPostId(String post_id) {
        this.post_id = post_id;
    }

    public void setNComment(int NComment) {
        this.NComment = NComment;
    }

    public void setNLike(int NLike) {
        this.NLike = NLike;
    }
}