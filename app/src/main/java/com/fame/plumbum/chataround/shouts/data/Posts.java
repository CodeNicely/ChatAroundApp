package com.fame.plumbum.chataround.shouts.data;

import java.util.List;

public class Posts {
    private int NoOfLikes;
    private List<Like> Like;
    private String TimeStamp;
    private List<Comments> Comments;
    private int NLike;
    private String PosterName;
    private double Longitude;
    private double Latitude;
    private int NComment;
    private String PosterId;
    private String LikeFlag;
    private String Post;
    private String PostId;

    public int getNoOfLikes() {
        return NoOfLikes;
    }

    public void setNoOfLikes(int NoOfLikes) {
        this.NoOfLikes = NoOfLikes;
    }

    public List<Like> getLike() {
        return Like;
    }

    public void setLike(List<Like> Like) {
        this.Like = Like;
    }

    public String getTimeStamp() {
        return TimeStamp;
    }

    public void setTimeStamp(String TimeStamp) {
        this.TimeStamp = TimeStamp;
    }

    public List<Comments> getComments() {
        return Comments;
    }

    public void setComments(List<Comments> Comments) {
        this.Comments = Comments;
    }

    public int getNLike() {
        return NLike;
    }

    public void setNLike(int NLike) {
        this.NLike = NLike;
    }

    public String getPosterName() {
        return PosterName;
    }

    public void setPosterName(String PosterName) {
        this.PosterName = PosterName;
    }

    public double getLongitude() {
        return Longitude;
    }

    public void setLongitude(double Longitude) {
        this.Longitude = Longitude;
    }

    public double getLatitude() {
        return Latitude;
    }

    public void setLatitude(double Latitude) {
        this.Latitude = Latitude;
    }

    public int getNComment() {
        return NComment;
    }

    public void setNComment(int NComment) {
        this.NComment = NComment;
    }

    public String getPosterId() {
        return PosterId;
    }

    public void setPosterId(String PosterId) {
        this.PosterId = PosterId;
    }

    public String getLikeFlag() {
        return LikeFlag;
    }

    public void setLikeFlag(String LikeFlag) {
        this.LikeFlag = LikeFlag;
    }

    public String getPost() {
        return Post;
    }

    public void setPost(String Post) {
        this.Post = Post;
    }

    public String getPostId() {
        return PostId;
    }

    public void setPostId(String PostId) {
        this.PostId = PostId;
    }
}
