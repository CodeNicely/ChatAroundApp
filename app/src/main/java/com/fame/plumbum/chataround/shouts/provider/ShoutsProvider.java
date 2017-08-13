package com.fame.plumbum.chataround.shouts.provider;

import com.fame.plumbum.chataround.shouts.OnShoutsReceived;
import com.fame.plumbum.chataround.shouts.OnVoteResponse;

/**
 * Created by meghalagrawal on 10/06/17.
 */

public interface ShoutsProvider {

    void requestShouts(String userId,
                       int counter,
                       double latitude,
                       double longitude,
                       OnShoutsReceived onShoutsReceived);

    void requestVote(String userId,
                     String postId,
                     int vote_type,
                     OnVoteResponse onVoteResponse
                     );

}
