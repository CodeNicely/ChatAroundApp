package com.fame.plumbum.chataround.shouts.presenter;

/**
 * Created by meghalagrawal on 10/06/17.
 */

public interface ShoutsPresenter {

    void requestShouts(String userId,int counter, double latitude, double longitude);

    void requestVote(String userId,String postId,int vote_type,int position);// -1 id for downvote, +1 is for UpVote
}
