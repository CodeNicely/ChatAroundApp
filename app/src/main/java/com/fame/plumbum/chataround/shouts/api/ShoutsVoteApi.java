package com.fame.plumbum.chataround.shouts.api;

import com.fame.plumbum.chataround.emergency.model.EmergencyContactDeleteData;
import com.fame.plumbum.chataround.helper.Urls;
import com.fame.plumbum.chataround.shouts.model.VoteData;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by meghalagrawal on 09/08/17.
 */

public interface ShoutsVoteApi {

    @FormUrlEncoded
    @POST(Urls.SUB_URL_SHOUTS_VOTE)
    Call<VoteData> requestVote(@Field("userId") String userId,@Field("postId") String postId, @Field("vote_type") int vote_type);

}
