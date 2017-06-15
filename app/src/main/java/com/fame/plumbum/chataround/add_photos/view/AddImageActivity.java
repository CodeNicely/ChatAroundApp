package com.fame.plumbum.chataround.add_photos.view;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.desmond.squarecamera.CameraActivity;
import com.fame.plumbum.chataround.R;
import com.fame.plumbum.chataround.activity.MainActivity;
import com.fame.plumbum.chataround.add_photos.model.ImageDataProviderImp;
import com.fame.plumbum.chataround.add_photos.model.data.ImageData;
import com.fame.plumbum.chataround.add_photos.model.data.ImageUploadData;
import com.fame.plumbum.chataround.add_photos.presenter.ImagePresenter;
import com.fame.plumbum.chataround.add_photos.presenter.ImagePresenterImpl;
import com.fame.plumbum.chataround.add_restroom.view.AddRestroomActivity;
import com.fame.plumbum.chataround.helper.Constants;
import com.fame.plumbum.chataround.helper.Keys;
import com.fame.plumbum.chataround.helper.RxSchedulersHook;
import com.fame.plumbum.chataround.helper.SharedPrefs;
import com.fame.plumbum.chataround.helper.image_loader.GlideImageLoader;
import com.fame.plumbum.chataround.helper.image_loader.ImageLoader;
import com.fame.plumbum.chataround.services.UploadGalleryImageService;
import com.fame.plumbum.chataround.services.UploadRestroomImageService;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.karumi.dexter.listener.single.PermissionListener;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AddImageActivity extends Activity implements
        UploadImageView,
        AddImageView,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {


    //Define a request code to send to Google Play services
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    private static final int MOCK_LOCATION_OFF_REQUEST = 201;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private static final String TAG = "AddImageActivity";
    private static final int CAMERA_REQUEST_ID = 1100;
    private static final String FILE_KEY = "IMAGE_KEY";
    private static final String LIST_KEY = "Image_List";
    private final int RESULT_LOAD_IMAGE = 1;
    private File image;
    private boolean CAMERA_REQUEST = false;
    private boolean GALLERY_REQUEST = false;
    private ImagePresenter imagePresenter;
    private ProgressDialog progressDialog;
    private List<Uri> uriList = new ArrayList<>();

    private double latitude;
    private double longitude;

    private SharedPrefs sharedPrefs;


    @BindView(R.id.uploadButton)
    Button uploadButton;

    @BindView(R.id.restroom_address)
    TextView addressTextView;

    @BindView(R.id.restroom_latitude_longitude)
    TextView latitudeLongitude;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.nestedScrollView)
    NestedScrollView nestedScrollView;

    @BindView(R.id.submitLayout)
    LinearLayout submitLayout;

    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    @BindView(R.id.imageView)
    ImageView imageView;

    @BindView(R.id.mobileEditText)
    EditText mobileEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_photos);
        ButterKnife.bind(this);
        UploadRestroomImageService.ACTIVITY_DESTROYED = false;

        toolbar.setTitle(R.string.add_photo);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                // The next two lines tell the new client that “this” current class will handle connection stuff
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                //fourth line adds the LocationServices API endpoint from GooglePlayServices
                .addApi(LocationServices.API)
                .build();

        // Create the LocationRequest object
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10 * 1000)        // 10 seconds, in milliseconds
                .setFastestInterval(1 * 1000); // 1 second, in milliseconds


        initializeObjects();
        Dexter.initialize(getApplicationContext());


        StaggeredGridLayoutManager staggeredGridLayoutManager = new
                StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.HORIZONTAL);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(
                this,
                LinearLayoutManager.HORIZONTAL,
                false

        );

        linearLayoutManager.setStackFromEnd(true);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imagePresenter.openCamera();
            }
        });

        mobileEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if(s.length()==10){
                    hideKeyboard();
                }
            }
        });


        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (latitude != 0.0 && longitude != 0.0) {

                    String mobile = mobileEditText.getText().toString();

                    Geocoder geocoder;
                    List<Address> addresses;
                    geocoder = new Geocoder(AddImageActivity.this, Locale.getDefault());

                    try {
                        addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                        String country = addresses.get(0).getCountryName();

                        if(!country.contentEquals(Constants.KEY_COUNTRY_INDIA)){
                            mobile=null;
                        }
                    }catch (Exception e){

                        Log.e(TAG,e.getMessage());
                        e.printStackTrace();

                    }


                    if(!mobile.equals(null) || !mobile.equals("") || mobile.length()!=10){
                        mobileEditText.setError("Please Enter Valid Mobile No!");
                        mobileEditText.requestFocus();
                        return;
                    }

                    Intent uploadServiceIntent = new Intent(AddImageActivity.this, UploadGalleryImageService.class);
                    uploadServiceIntent.putExtra(Keys.KEY_USER_MOBILE, "8109109457");
                    uploadServiceIntent.putExtra(Keys.KEY_LATITUDE, latitude);
                    uploadServiceIntent.putExtra(Keys.KEY_LONGITUDE, longitude);
                    uploadServiceIntent.putExtra(Keys.KEY_USER_MOBILE, mobile);
                    getApplicationContext().startService(uploadServiceIntent);


                    if (uriList.size() > 0) {
                        imagePresenter.onImagesUpload(uriList);
                        finish();
//                        showMessage("Photo Added Successfully to this location and this is currently under review");
                    } else {
                        showMessage("Please add a photo to continue");
                    }


                } else {
                    showMessage("Please turn on your GPS to continue, We are unable to fetch your current location");
                }

            }
        });
    }

    public void openCamera() {
        imagePresenter.openCamera();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        UploadRestroomImageService.ACTIVITY_DESTROYED = true;
        if (UploadRestroomImageService.QUEUE_EMPTY) {
            Intent i = new Intent(this, UploadRestroomImageService.class);
            stopService(i);
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.v(this.getClass().getSimpleName(), "onPause()");

        //Disconnect from API onPause()
        if (mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            mGoogleApiClient.disconnect();
        }
    }

    private void initializeObjects() {

        sharedPrefs = new SharedPrefs(this);
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Adding Restroom");
        progressDialog.setMessage("Please Wait . . .");
        imagePresenter = new ImagePresenterImpl(this
                , new ImageDataProviderImp(getApplicationContext())
                , new RxSchedulersHook(), EventBus.getDefault());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        List<Uri> uriList = new ArrayList<>();


        switch (requestCode) {
            case CAMERA_REQUEST_ID:
                if (resultCode == Activity.RESULT_OK) {

                    /*Log.i(TAG, "Result code for Camera : " + resultCode);
                    if (image != null) {
                        uriList.add(Uri.fromFile(image));
                    }*/
                    uriList.add(data.getData());
                }
                break;
            case MOCK_LOCATION_OFF_REQUEST:

                Intent intent = new Intent(AddImageActivity.this, AddImageActivity.class);
                startActivity(intent);
                finish();
                break;

           /* case RESULT_LOAD_IMAGE:

                if (resultCode == Activity.RESULT_OK) {

                    // Clipdata is used for receiving multiple images from Gallery
                    ClipData clip = data.getClipData();

                    if (clip != null) {
                        for (int i = 0; i < clip.getItemCount(); i++) {

                            ClipData.Item item = clip.getItemAt(i);

                            uriList.add(item.getUri());

                        }
                    } else {

                        uriList.add(data.getData());
                    }
                }
                break;*/
            default:
                break;
        }


        if (uriList.size() > 0) {
            this.uriList = uriList;
            imagePresenter.onImagesSelected(uriList);
        }


    }


    @Override
    public void showCamera() {

/*
        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(image));
        Log.i(TAG, image.getPath());

        if (intent.resolveActivity(getPackageManager()) != null) {
            // Start the image capture intent to take photo
            startActivityForResult(intent, CAMERA_REQUEST_ID);
        }

*/

        Intent startCustomCameraIntent = new Intent(this, CameraActivity.class);
        startActivityForResult(startCustomCameraIntent, CAMERA_REQUEST_ID);

    }

    @Override
    public void showGallery() {

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), RESULT_LOAD_IMAGE);


    }

    @Override
    public boolean checkPermissionForCamera() {

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
            return true;
        else
            return false;
    }

    @Override
    public boolean checkPermissionForGallery() {

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
            return true;
        else
            return false;
    }

    @Override
    public boolean requestCameraPermission() {


        Dexter.checkPermissions(new MultiplePermissionsListener() {

            @Override
            public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {


                if (multiplePermissionsReport.areAllPermissionsGranted()) {

                    CAMERA_REQUEST = true;
                } else {
                    CAMERA_REQUEST = false;
                }
            }

            @Override
            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {

            }
        }, Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE);


        return CAMERA_REQUEST;
    }

    @Override
    public boolean requestGalleryPermission() {

        Dexter.checkPermission(new PermissionListener() {
            @Override
            public void onPermissionGranted(PermissionGrantedResponse response) {/* ... */

                GALLERY_REQUEST = true;
            }

            @Override
            public void onPermissionDenied(PermissionDeniedResponse response) {/* ... */

                GALLERY_REQUEST = false;
            }

            @Override
            public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {/* ... */}
        }, Manifest.permission.READ_EXTERNAL_STORAGE);


        return GALLERY_REQUEST;
    }

    @Override
    public void setData(List<ImageData> imageDataList) {


        Glide.with(this).load(imageDataList.get(0).getFile()).into(imageView);
        // Do nothing for now

    }
/*

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        if (image != null) {
            outState.putString(FILE_KEY, image.getPath());
            List<ImageData> imageDataList;
            imageDataList = imagePresenter.getImage_list();
            JsonArray jsonArray = new JsonArray();

            for (int i = 0; i < imageDataList.size(); i++) {
                jsonArray.add(imageDataList.get(i).toJson());
            }

            outState.putString(LIST_KEY, jsonArray.toString());
        }
//        outState.putString(LIST_KEY, imagePresenter.getImage_list().toString());
    }
*/
/*
    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(FILE_KEY))
                image = new File(savedInstanceState.getString(FILE_KEY));

            List<ImageData> imageDataList = new ArrayList<>();
            String json = null;
            if (savedInstanceState.containsKey(LIST_KEY)) {
                json = savedInstanceState.getString(LIST_KEY);
            }
            JsonParser parser = new JsonParser();
            JsonElement tradeElement = null;
            if (json != null) {
                tradeElement = parser.parse(json);
            }
            JsonArray trade = null;
            if (tradeElement != null) {
                trade = tradeElement.getAsJsonArray();
            }
            if (trade != null) {
                for (int i = 0; i < trade.size(); i++) {
                    JsonObject jsonObject;
                    jsonObject = trade.get(i).getAsJsonObject();
                    ImageData imageData = new ImageData(jsonObject.get("file").getAsString());
                    imageDataList.add(imageData);
                }
            }

            imagePresenter.setImageDataList(imageDataList);
            imageAdapter.setData(imageDataList);
            imageAdapter.notifyDataSetChanged();
        }
    }*/
//        imagePresenter.setImageDataList(savedInstanceState.getString(LIST_KEY));


    public void fileFromPath(String filePath) {

        image = new File(filePath);

    }

    @Override
    public void showLoader(boolean show) {


        if (show) {
            progressDialog.show();
        } else {
            progressDialog.dismiss();
        }

    }


    @Override
    public void showMessage(String message) {

        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onImageAdded(ImageUploadData imageUploadData) {

        Intent uploadServiceIntent = new Intent(AddImageActivity.this, UploadGalleryImageService.class);
        uploadServiceIntent.putExtra(Keys.KEY_RESTROOM_ID, imageUploadData.getMessage());
        getApplicationContext().startService(uploadServiceIntent);


        if (uriList.size() > 0) {
            imagePresenter.onImagesUpload(uriList);
        }

        finish();
        showMessage("Restroom Added Successfully");

    }

    /**
     * If connected get lat and long
     */
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    public void onConnected(Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        if (location == null) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);

        } else {
            if (isMockLocation(location)) {
                return;
            }
            //If everything went fine lets get latitude and longitude
            latitude = location.getLatitude();
            longitude = location.getLongitude();


            latitudeLongitude.setText("Current Location - " + String.valueOf(latitude)
                    + " , " + longitude);
            Geocoder geocoder;
            List<Address> addresses;
            geocoder = new Geocoder(AddImageActivity.this, Locale.getDefault());

            try {
                addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                String city = addresses.get(0).getLocality();
                String state = addresses.get(0).getAdminArea();
                String country = addresses.get(0).getCountryName();
                String postalCode = addresses.get(0).getPostalCode();
                String knownName = addresses.get(0).getFeatureName();


                if(country.contentEquals(Constants.KEY_COUNTRY_INDIA)){
                    mobileEditText.setVisibility(View.VISIBLE);
                    if(sharedPrefs.getUserMobile()!=null){
                        mobileEditText.setText(sharedPrefs.getUserMobile());
                    }
                }else{
                    mobileEditText.setVisibility(View.GONE);
                    mobileEditText.setText("");
                    mobileEditText.setEnabled(false);
                }


                if (knownName != null) {

                    addressTextView.setText(address);
                    addressTextView.append(", " + city);
                    addressTextView.append(", " + state);
                    addressTextView.append(", " + country);


                } else {
                    addressTextView.setText(address);

                }
                showRestroomAddLayout(true);

            } catch (IOException e) {
                e.printStackTrace();
            }


        }
    }

    void showRestroomAddLayout(boolean show) {
        if (show) {

            nestedScrollView.setVisibility(View.VISIBLE);
            submitLayout.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);

        } else {
            nestedScrollView.setVisibility(View.GONE);
            submitLayout.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);

        }

    }

    @Override
    public void onConnectionSuspended(int i) {
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
            /*
             * Google Play services can resolve some errors it detects.
             * If the error has a resolution, try sending an Intent to
             * start a Google Play services activity that can resolve
             * error.
             */
        if (connectionResult.hasResolution()) {
            try {
                // Start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult(this, CONNECTION_FAILURE_RESOLUTION_REQUEST);
                    /*
                     * Thrown if Google Play services canceled the original
                     * PendingIntent
                     */
            } catch (IntentSender.SendIntentException e) {
                // Log the error
                e.printStackTrace();
            }
        } else {
                /*
                 * If no resolution is available, display a dialog to the
                 * user with the error.
                 */
            Log.e("Error", "Location services connection failed with code " + connectionResult.getErrorCode());
        }
    }

    /**
     * If locationChanges change lat and long
     *
     * @param location
     */
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    public void onLocationChanged(Location location) {
        if (isMockLocation(location)) {
            return;
        }


        latitude = location.getLatitude();
        longitude = location.getLongitude();


        latitudeLongitude.setText("Current Location - " + String.valueOf(latitude)
                + " , " + longitude);
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(AddImageActivity.this, Locale.getDefault());

        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            String city = addresses.get(0).getLocality();
            String state = addresses.get(0).getAdminArea();
            String country = addresses.get(0).getCountryName();
            String postalCode = addresses.get(0).getPostalCode();
            String knownName = addresses.get(0).getFeatureName();

            if(country.contentEquals(Constants.KEY_COUNTRY_INDIA)){
                mobileEditText.setVisibility(View.VISIBLE);
                if(sharedPrefs.getUserMobile()!=null){
                    mobileEditText.setText(sharedPrefs.getUserMobile());
                }
            }else{
                mobileEditText.setVisibility(View.GONE);
                mobileEditText.setText("");
                mobileEditText.setEnabled(false);
            }


            if (knownName != null) {

                addressTextView.setText(address);
                addressTextView.append(", " + city);
                addressTextView.append(", " + state);
                addressTextView.append(", " + country);


            } else {
                addressTextView.setText(address);

            }
            showRestroomAddLayout(true);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    public boolean isMockLocation(Location location) {
        if (location.isFromMockProvider()) {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(this,R.style.AlertDialogTheme);
            alertDialog
                    .setTitle(R.string.mock_location_title)
                    .setMessage(R.string.mock_location_message)
                    .setCancelable(false)
                    .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            try {
                                Intent intent = new Intent(Settings.ACTION_APPLICATION_DEVELOPMENT_SETTINGS);
                                ComponentName componentName = intent.resolveActivity(getPackageManager());
                                if (componentName == null) {
                                    Toast.makeText(getApplicationContext(), "No Activity to handle Intent action", Toast.LENGTH_SHORT).show();
                                } else {
                                    startActivityForResult(intent,MOCK_LOCATION_OFF_REQUEST);
                                }
                            } catch (Exception e) {

                                Toast.makeText(AddImageActivity.this, "Error " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    })
                    .setNegativeButton("Exit", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    }).show();

            return true;
        } else {
            return false;
        }
    }

    private void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

}