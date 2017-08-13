package com.fame.plumbum.chataround.shouts.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Posts {
    @SerializedName("NoOfLikes")
    private int NoOfLikes;
    @SerializedName("Like")
    private List<Like> Like;
    @SerializedName("TimeStamp")
    private String TimeStamp;
    @SerializedName("Comments")
    private List<Comments> Comments;
    @SerializedName("PosterImage")
    private String PosterImage;
    @SerializedName("NLike")
    private int NLike;
    @SerializedName("PosterName")
    private String PosterName;
    @SerializedName("Longitude")
    private double Longitude;
    @SerializedName("Latitude")
    private double Latitude;
    @SerializedName("NComment")
    private int NComment;
    @SerializedName("PosterId")
    private String PosterId;
    @SerializedName("LikeFlag")
    private String LikeFlag;
    @SerializedName("Post")
    private String Post;
    @SerializedName("PostId")
    private String PostId;

    @SerializedName("NUpvote")
    private int NUpvote;

    @SerializedName("NDownvote")
    private int NDownvote;

    @SerializedName("UpVoteFlag")
    private boolean UpVoteFlag;

    @SerializedName("DownVoteFlag")
    private boolean DownVoteFlag;



    public int getNoOfLikes() {
        return NoOfLikes;
    }

    public List<Like> getLike() {
        return Like;
    }

    public String getTimeStamp() {
        return TimeStamp;
    }

    public List<Comments> getComments() {
        return Comments;
    }

    public String getPosterImage() {
        return PosterImage;
    }

    public int getNLike() {
        return NLike;
    }

    public String getPosterName() {
        return PosterName;
    }

    public double getLongitude() {
        return Longitude;
    }

    public double getLatitude() {
        return Latitude;
    }

    public int getNComment() {
        return NComment;
    }

    public String getPosterId() {
        return PosterId;
    }

    public String getLikeFlag() {
        return LikeFlag;
    }

    public void setLikeFlag(String likeFlag) {
        LikeFlag = likeFlag;
    }

    public String getPost() {
        return Post;
    }

    public String getPostId() {
        return PostId;
    }

    public int getNDownvote() {
        return NDownvote;
    }

    public int getNUpvote() {
        return NUpvote;
    }

    public boolean isDownVoteFlag() {
        return DownVoteFlag;
    }

    public boolean isUpVoteFlag() {
        return UpVoteFlag;
    }

    public void setUpVoteFlag(boolean upVoteFlag) {
        UpVoteFlag = upVoteFlag;
    }

    public void setDownVoteFlag(boolean downVoteFlag) {
        DownVoteFlag = downVoteFlag;
    }

    public void setNUpvote(int NUpvote) {
        this.NUpvote = NUpvote;
    }

    public void setNDownvote(int NDownvote) {
        this.NDownvote = NDownvote;
    }
}
