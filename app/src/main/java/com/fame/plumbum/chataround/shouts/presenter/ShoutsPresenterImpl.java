package com.fame.plumbum.chataround.shouts.presenter;

import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.CustomEvent;
import com.fame.plumbum.chataround.shouts.OnShoutsReceived;
import com.fame.plumbum.chataround.shouts.OnVoteResponse;
import com.fame.plumbum.chataround.shouts.model.ShoutsData;
import com.fame.plumbum.chataround.shouts.model.VoteData;
import com.fame.plumbum.chataround.shouts.provider.ShoutsProvider;
import com.fame.plumbum.chataround.shouts.view.ShoutsView;

/**
 * Created by meghalagrawal on 10/06/17.
 */

public class ShoutsPresenterImpl implements ShoutsPresenter {

    private ShoutsView shoutsView;
    private ShoutsProvider shoutsProvider;

    public ShoutsPresenterImpl(ShoutsView shoutsView, ShoutsProvider shoutsProvider) {
        this.shoutsView = shoutsView;
        this.shoutsProvider = shoutsProvider;
    }

    @Override
    public void requestShouts(String userId, int counter, double latitude, double longitude) {
        shoutsView.showLoader(true);
        shoutsProvider.requestShouts(userId, counter, latitude, longitude, new OnShoutsReceived() {
            @Override
            public void onSuccess(ShoutsData shoutsData) {
                if (shoutsData.getStatus() == 200) {
                    shoutsView.setData(shoutsData.getPosts());

                } else {
                    shoutsView.showMessage(shoutsData.getMessage());
                    Answers.getInstance().logCustom(new CustomEvent("Shout module loading failed - Server end"));

                }
                shoutsView.showLoader(false);
            }

            @Override
            public void onFailed(String message) {

                Answers.getInstance().logCustom(new CustomEvent("Shout module loading failed - local"));

                shoutsView.showLoader(false);
                shoutsView.showMessage(message);


            }
        });
    }

    @Override
    public void requestVote(String userId, String postId, final int vote_type, final int position) {
        shoutsProvider.requestVote(userId, postId, vote_type, new OnVoteResponse() {
            @Override
            public void onSuccess(VoteData voteData) {

                if(voteData.isSuccess()) {
                    shoutsView.onVoteSuccessful(position,vote_type);
                }else{
                    shoutsView.showMessage(voteData.getMessage());
                }
            }

            @Override
            public void onFailed(String message) {

                shoutsView.showMessage(message);
            }
        });



    }


}
