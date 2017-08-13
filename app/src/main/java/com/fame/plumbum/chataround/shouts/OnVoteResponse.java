package com.fame.plumbum.chataround.shouts;

import com.fame.plumbum.chataround.shouts.model.VoteData;

/**
 * Created by meghalagrawal on 09/08/17.
 */

public interface OnVoteResponse {

    void onSuccess(VoteData voteData);

    void onFailed(String message);

}
