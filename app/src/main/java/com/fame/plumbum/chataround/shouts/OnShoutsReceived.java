package com.fame.plumbum.chataround.shouts;

import com.fame.plumbum.chataround.shouts.model.ShoutsData;

/**
 * Created by meghalagrawal on 10/06/17.
 */

public interface OnShoutsReceived {

    void onSuccess(ShoutsData shoutsData);
    void onFailed(String message);
}
