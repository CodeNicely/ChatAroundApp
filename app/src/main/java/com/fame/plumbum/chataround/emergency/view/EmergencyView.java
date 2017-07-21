package com.fame.plumbum.chataround.emergency.view;

import com.fame.plumbum.chataround.emergency.model.StartSosData;
import com.fame.plumbum.chataround.emergency_contacts.model.EmergencyContactData;

import java.util.List;

/**
 * Created by meghalagrawal on 13/07/17.
 */

public interface EmergencyView {
    void showMessage(String message);

    void showProgressDialog(boolean show);

    void changeDialogMessage(String title,String message);

    void onEmergencyContactsUpdated();

    void onEmergencyContactsRecieved(List<EmergencyContactData> contactsDataList);

    void onSosStarted(StartSosData startSosData);

    void onContactDeleted();

    void showLoader(boolean show);
}