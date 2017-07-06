package com.fame.plumbum.chataround.emergency.view;

import com.fame.plumbum.chataround.emergency.model.data.EmergencyContactsFeed;

import java.util.List;

/**
 * Created by ramya on 2/7/17.
 */

public interface EmergencyContactsView {
    void showLoader(boolean show);
    void onEmergencyContactsRecieved(List<EmergencyContactsFeed> contactsDataList);
    void showMessage(String message);
    void updateContacts(EmergencyContactsFeed emergencyContactsFeed);
}
