package com.fame.plumbum.chataround.models;

/**
 * Created by pankaj on 23/7/16.
 */
public class PostData {
    int Status;
    String Message;
    String PostId;
    String error;

    public int getStatus() {
        return Status;
    }

    public String getError() {
        return error;
    }

    public String getMessage() {
        return Message;
    }

    public String getPostId() {
        return PostId;
    }

    public void setError(String error) {
        this.error = error;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public void setPostId(String postId) {
        PostId = postId;
    }

    public void setStatus(int status) {
        Status = status;
    }
}
