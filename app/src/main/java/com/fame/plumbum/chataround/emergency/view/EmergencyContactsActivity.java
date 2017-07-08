package com.fame.plumbum.chataround.emergency.view;

import android.Manifest;
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
import android.support.v4.content.PermissionChecker;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.fame.plumbum.chataround.R;
import com.fame.plumbum.chataround.emergency.model.EmergencyContactsProviderImpl;
import com.fame.plumbum.chataround.emergency.model.data.EmergencyContactsFeed;
import com.fame.plumbum.chataround.emergency.presenter.EmergencyContactsPresenter;
import com.fame.plumbum.chataround.emergency.presenter.EmergencyContactsPresenterImpl;
import com.fame.plumbum.chataround.helper.SharedPrefs;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EmergencyContactsActivity extends AppCompatActivity {
    @BindView(R.id.contacts_recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.contacts_progress_bar)
    ProgressBar progressBar;
    private Toolbar toolbar;
    private EmergencyContactsAdapter emergencyContactsAdapter;
    private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 100;
    private SharedPrefs sharedPrefs;
    private List<EmergencyContactsFeed> contactsFeedList=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency_contacts);
        ButterKnife.bind(this);
        sharedPrefs=new SharedPrefs(this);
        toolbar=(Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.white));
        if(toolbar!=null){
            setSupportActionBar(toolbar);
            toolbar.setTitle("Contacts");
        }
        showContacts();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST_READ_CONTACTS) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission is granted
                showContacts();
            } else {
                Toast.makeText(this, "Until you grant the permission, we canot display the names", Toast.LENGTH_SHORT).show();
            }
        }
    }
    private void showContacts(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, PERMISSIONS_REQUEST_READ_CONTACTS);
            //After this point you wait for callback in onRequestPermissionsResult(int, String[], int[]) overriden method
        } else {
            if(!fetchContacts()){
                Toast.makeText(this,"Unable to fetch contacts",Toast.LENGTH_LONG).show();
            }
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            emergencyContactsAdapter=new EmergencyContactsAdapter(this,contactsFeedList);
            recyclerView.setAdapter(emergencyContactsAdapter);
            emergencyContactsAdapter.setEmergencyContactsFeedList(contactsFeedList);
            emergencyContactsAdapter.notifyDataSetChanged();

            // Android version is lesser than 6.0 or the permission is already granted.

        }
    }
    public boolean fetchContacts() {

        String phoneNumber = null;

        Uri CONTENT_URI = ContactsContract.Contacts.CONTENT_URI;

        String _ID = ContactsContract.Contacts._ID;

        String DISPLAY_NAME = ContactsContract.Contacts.DISPLAY_NAME;
        String HAS_PHONE_NUMBER = ContactsContract.Contacts.HAS_PHONE_NUMBER;

        Uri PhoneCONTENT_URI = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;

        String Phone_CONTACT_ID = ContactsContract.CommonDataKinds.Phone.CONTACT_ID;

        String NUMBER = ContactsContract.CommonDataKinds.Phone.NUMBER;

        ContentResolver contentResolver = getContentResolver();

        Cursor cursor = contentResolver.query(CONTENT_URI, null,ContactsContract.Contacts.HAS_PHONE_NUMBER + " = 1", null,  "UPPER(" + ContactsContract.Contacts.DISPLAY_NAME + ") ASC");

        assert cursor != null;
        if (cursor.getCount() > 0) {

            while (cursor.moveToNext()) {

                String contact_id = cursor.getString(cursor.getColumnIndex( _ID ));

                String name = cursor.getString(cursor.getColumnIndex( DISPLAY_NAME ));
                String image_uri = cursor
                        .getString(cursor
                                .getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_URI));

                int hasPhoneNumber = Integer.parseInt(cursor.getString(cursor.getColumnIndex( HAS_PHONE_NUMBER )));

                if (hasPhoneNumber > 0) {

                    // Query and loop for every phone number of the contact

                    Cursor phoneCursor = contentResolver.query(PhoneCONTENT_URI, null, Phone_CONTACT_ID + " = ?", new String[] { contact_id }, null);

                    assert phoneCursor != null;
                    while (phoneCursor.moveToNext()) {

                        phoneNumber = phoneCursor.getString(phoneCursor.getColumnIndex(NUMBER));

                    }

                    phoneCursor.close();

                    EmergencyContactsFeed emergencyContactsFeed = new EmergencyContactsFeed();// adding contacts into the list
                    contactsFeedList.add(emergencyContactsFeed.setData(name, phoneNumber, image_uri,false));

                }

            }
            cursor.close();

        }
        if(contactsFeedList.size()==0)
        {
            return false;
        }
        return true;

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search,menu);
        SearchManager searchManager = (SearchManager)
                getSystemService(Context.SEARCH_SERVICE);
        final SearchView searchView = (SearchView)menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(searchManager.
                getSearchableInfo(getComponentName()));
        searchView.setSubmitButtonEnabled(true);
        SearchView.OnQueryTextListener queryTextListener =new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //  emergencyContactsAdapter.filter(newText,contactsFeedList);
                //emergencyContactsAdapter.getFilter().filter(newText);
                filter(newText);

                return false;
            }
        };
        searchView.setOnQueryTextListener(queryTextListener);
        return true;
    }
    private void filter(String text) {
        //new array list that will hold the filtered data
        ArrayList<EmergencyContactsFeed> filterdNames = new ArrayList<>();
        EmergencyContactsFeed emergencyContactsFeed = new EmergencyContactsFeed();// adding contacts into the list


        //looping through existing elements
        for (EmergencyContactsFeed s : contactsFeedList) {
            //if the existing elements contains the search input

            if (s.getContactName().toLowerCase().contains(text.toLowerCase())) {
                //adding the element to filtered list
                filterdNames.add(emergencyContactsFeed.setData(s.getContactName(),s.getContactNumber(),s.getContactImage(),false));
            }
        }

        //calling a method of the adapter class and passing the filtered list
        emergencyContactsAdapter.filterList(filterdNames);
    }

}
