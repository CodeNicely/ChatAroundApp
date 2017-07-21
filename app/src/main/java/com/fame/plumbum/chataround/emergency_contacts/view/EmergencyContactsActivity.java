package com.fame.plumbum.chataround.emergency_contacts.view;

import android.Manifest;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.fame.plumbum.chataround.R;
import com.fame.plumbum.chataround.emergency.view.Contact;
import com.fame.plumbum.chataround.emergency.view.EmergencyFragment;
import com.fame.plumbum.chataround.emergency_contacts.model.EmergencyContactData;
import com.fame.plumbum.chataround.emergency_contacts.presenter.UpdateContactsPresenter;
import com.fame.plumbum.chataround.emergency_contacts.presenter.UpdateContactsPresenterImpl;
import com.fame.plumbum.chataround.emergency_contacts.provider.RetrofitEmergencyContactsUpdateProvider;
import com.fame.plumbum.chataround.helper.SharedPrefs;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EmergencyContactsActivity extends AppCompatActivity implements EmergencyContactsView {
    @BindView(R.id.contacts_recycler_view)
    RecyclerView recyclerView;

    @BindView(R.id.contacts_progress_bar)
    ProgressBar progressBar;

    @BindView(R.id.update)
    Button update;

    private Toolbar toolbar;
    private EmergencyContactsAdapter emergencyContactsAdapter;
    private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 100;
    private SharedPrefs sharedPrefs;
    private List<EmergencyContactData> contactsFeedList = new ArrayList<>();
    private ProgressDialog progressDialog;
    private List<EmergencyContactData> addedContacts = new ArrayList<>();
    private UpdateContactsPresenter updateContactsPresenter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency_contacts);
        ButterKnife.bind(this);
        sharedPrefs = new SharedPrefs(this);
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        updateContactsPresenter = new UpdateContactsPresenterImpl(this, new RetrofitEmergencyContactsUpdateProvider());

//        addedContacts.add(new EmergencyContactData("Meghal Agrawal", "8109109457", true));


        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);

        toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.white));
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            toolbar.setTitle("Contacts");
        }

        emergencyContactsAdapter = new EmergencyContactsAdapter(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(emergencyContactsAdapter);

        Log.d("Emergency Contact", "Size is:" + String.valueOf(contactsFeedList.size()));

        new Thread(new Runnable() {
            @Override
            public void run() {
                fetchContacts();
            }
        }).start();

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    updateContactsPresenter.updateContactsList(sharedPrefs.getUserId(), makeContactListJsonFromPojo(contactsFeedList).toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST_READ_CONTACTS) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission is granted
                fetchContacts();

            } else {

                Toast.makeText(this, "Until you grant the permission, we canot display the names", Toast.LENGTH_SHORT).show();

            }
        }
    }


    @Override
    public void showMessage(String message) {

        Toast.makeText(this, message, Toast.LENGTH_LONG).show();

    }

    @Override
    public void showProgressDialog(boolean show) {


        if (show) {

            progressDialog.show();

        } else {

            progressDialog.hide();
        }


    }

    @Override
    public void changeDialogMessage(String message) {

        progressDialog.setMessage(message);
    }

    @Override
    public void onEmergencyContactsUpdated() {


        EventBus.getDefault().postSticky(new Contact());
        finish();

        // do anything , Close activity or do something else

    }


    public JSONObject makeContactListJsonFromPojo(List<EmergencyContactData> emergencyContactDataList) throws JSONException {
        JSONObject obj = null;
        JSONArray jsonArray = new JSONArray();
        for (int i = 0; i < emergencyContactDataList.size(); i++) {

            EmergencyContactData emergencyContactData = emergencyContactDataList.get(i);

            if (emergencyContactData.isChecked()) {
                obj = new JSONObject();
                try {
                    obj.put("name", emergencyContactData.getName());
                    obj.put("mobile", emergencyContactData.getMobile());

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                jsonArray.put(obj);
            }
        }
        JSONObject finalObj = new JSONObject();

        finalObj.put("contact_list", jsonArray);

        return finalObj;
    }

    public void fetchContacts() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, PERMISSIONS_REQUEST_READ_CONTACTS);
            //After this point you wait for callback in onRequestPermissionsResult(int, String[], int[]) overriden method
        } else {


            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    showProgressDialog(true);

                }
            });

            // Android version is lesser than 6.0 or the permission is already granted.


//            android.os.Handler handler = new Handler();
//            handler.postDelayed(new Runnable() {
//                @Override
//                public void run() {


            String phoneNumber = null;

            Uri CONTENT_URI = ContactsContract.Contacts.CONTENT_URI;

            String _ID = ContactsContract.Contacts._ID;

            String DISPLAY_NAME = ContactsContract.Contacts.DISPLAY_NAME;
            String HAS_PHONE_NUMBER = ContactsContract.Contacts.HAS_PHONE_NUMBER;

            Uri PhoneCONTENT_URI = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;

            String Phone_CONTACT_ID = ContactsContract.CommonDataKinds.Phone.CONTACT_ID;

            String NUMBER = ContactsContract.CommonDataKinds.Phone.NUMBER;

            ContentResolver contentResolver = getContentResolver();

            Cursor cursor = contentResolver.query(CONTENT_URI, null, ContactsContract.Contacts.HAS_PHONE_NUMBER + " = 1", null, "UPPER(" + ContactsContract.Contacts.DISPLAY_NAME + ") ASC");

            int count = 0;

            assert cursor != null;
            if (cursor.getCount() > 0) {


                while (cursor.moveToNext()) {

                    count++;

                    String contact_id = cursor.getString(cursor.getColumnIndex(_ID));

                    final String name = cursor.getString(cursor.getColumnIndex(DISPLAY_NAME));

                    int hasPhoneNumber = Integer.parseInt(cursor.getString(cursor.getColumnIndex(HAS_PHONE_NUMBER)));

                    if (hasPhoneNumber > 0) {

                        // Query and loop for every phone number of the contact

                        Cursor phoneCursor = contentResolver.query(PhoneCONTENT_URI, null, Phone_CONTACT_ID + " = ?", new String[]{contact_id}, null);

                        assert phoneCursor != null;
                        while (phoneCursor.moveToNext()) {

                            phoneNumber = phoneCursor.getString(phoneCursor.getColumnIndex(NUMBER));

                        }


                        phoneCursor.close();

                        assert phoneNumber != null;
                        phoneNumber = phoneNumber.replace(" ", "");

                        if (phoneNumber.startsWith("+91")) {
                            phoneNumber = phoneNumber.substring(3, phoneNumber.length());
                        }

                        if (phoneNumber.length() == 10) {
                            boolean checked = false;
                            for (int i = 0; i < addedContacts.size(); i++) {

                                if (phoneNumber.equals(addedContacts.get(i).getMobile())) {
                                    checked = true;
                                    Log.d("EmergencyContacts", "Checked is True");
                                }
                            }


                            contactsFeedList.add(new EmergencyContactData(name, phoneNumber, checked));
                            EventBus.getDefault().post(new ContactFetchEvent(count, cursor.getCount()));

//                            final String finalPhoneNumber = phoneNumber;
                       /*     runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    emergencyContactsAdapter.addEmergencyContact(new EmergencyContactData(name, finalPhoneNumber, false));
                                    emergencyContactsAdapter.notifyItemChanged(contactsFeedList.size());

                                    showProgressDialog(false);
                                }
                            });*/

                        }

                    }

                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        emergencyContactsAdapter.setEmergencyContactDataList(contactsFeedList);
                        emergencyContactsAdapter.notifyDataSetChanged();

                        showProgressDialog(false);
                    }
                });

                cursor.close();


            }

//                this.contactsFeedList = contactsFeedList;

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    emergencyContactsAdapter.setEmergencyContactDataList(contactsFeedList);
                    emergencyContactsAdapter.notifyDataSetChanged();

                    showProgressDialog(false);
                }
            });

        }
//            }, 100);

    }

    public static class ContactFetchEvent {

        private int count;
        private int total;

        public ContactFetchEvent(int count, int total) {
            this.count = count;
            this.total = total;
        }

        public int getTotal() {
            return total;
        }

        public int getCount() {
            return count;
        }

        /* Additional fields if needed */
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onContactFetchEvent(ContactFetchEvent event) {


        /* Do something */

        progressDialog.setMessage("Loading Contacts " + String.valueOf(event.getCount()) + " of " + String.valueOf(event.getTotal()));
    }


    public static class GetContactListEvent {


        List<EmergencyContactData> emergencyContactDataList;

        public GetContactListEvent(List<EmergencyContactData> emergencyContactDataList) {
            this.emergencyContactDataList = emergencyContactDataList;
        }

        public List<EmergencyContactData> getEmergencyContactDataList() {
            return emergencyContactDataList;
        }

        /* Additional fields if needed */
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onOldContactEvent(GetContactListEvent getContactListEvent) {


        /* Do something */

        Log.d("Emergency Contact", "onOldContactEvent Size" + String.valueOf(getContactListEvent.emergencyContactDataList.size()));

        addedContacts = getContactListEvent.emergencyContactDataList;

    }


    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);

//        EventBus.getDefault().registerSticky(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    public void addContactToSelectedContactList(EmergencyContactData emergencyContactData) {

        for (int i = 0; i < contactsFeedList.size(); i++) {
            if (contactsFeedList.get(i).getMobile().equals(emergencyContactData.getMobile())) {
                contactsFeedList.get(i).setChecked(emergencyContactData.isChecked());
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search, menu);
        SearchManager searchManager = (SearchManager)
                getSystemService(Context.SEARCH_SERVICE);
        final SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(searchManager.
                getSearchableInfo(getComponentName()));
        int searchSrcTextId = getResources().getIdentifier("android:id/search_src_text", null, null);
        EditText searchEditText = (EditText) searchView.findViewById(searchSrcTextId);
        searchView.setSubmitButtonEnabled(true);
        SearchView.OnQueryTextListener queryTextListener = new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filter(newText);

                return false;
            }
        };
        searchView.setOnQueryTextListener(queryTextListener);
        return true;
    }

    private void filter(String text) {
        //new array list that will hold the filtered data
        ArrayList<EmergencyContactData> filterdNames = new ArrayList<>();


        //looping through existing elements
        for (EmergencyContactData s : contactsFeedList) {
            //if the existing elements contains the search input

            if (s.getName().toLowerCase().contains(text.toLowerCase())) {
                //adding the element to filtered list
                filterdNames.add(new EmergencyContactData(s.getName(), s.getMobile(), s.isChecked()));
            }
        }

        //calling a method of the adapter class and passing the filtered list
        emergencyContactsAdapter.setEmergencyContactDataList(filterdNames);
        emergencyContactsAdapter.notifyDataSetChanged();
    }


}