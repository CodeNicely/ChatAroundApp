package com.fame.plumbum.chataround.shouts.provider;

import com.fame.plumbum.chataround.shouts.OnShoutsReceived;

/**
 * Created by meghalagrawal on 10/06/17.
 */

public interface ShoutsProvider {

    void requestShouts(String userId,
                       int counter,
                       double latitude,
                       double longitude,
                       OnShoutsReceived onShoutsReceived);

}
