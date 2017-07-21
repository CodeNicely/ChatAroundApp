package com.fame.plumbum.chataround.emergency_contacts.view;

import java.util.List;

/**
 * Created by ramya on 2/7/17.
 */

public interface EmergencyContactsView {

    void showMessage(String message);

    void showProgressDialog(boolean show);

    void changeDialogMessage(String message);

    void onEmergencyContactsUpdated();
}
