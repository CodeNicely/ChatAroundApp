package com.fame.plumbum.chataround.emergency.presenter;

/**
 * Created by ramya on 2/7/17.
 */

public interface EmergencyPresenter {
    void getContacts(String userId);

    void startSos(String userId, double latitude, double longitude);

    void deleteContact(String userId,String mobile);
}
