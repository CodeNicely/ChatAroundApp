package com.fame.plumbum.chataround.fragments;

import android.Manifest;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.fame.plumbum.chataround.MySingleton;
import com.fame.plumbum.chataround.R;
import com.fame.plumbum.chataround.activity.MainActivity;
import com.fame.plumbum.chataround.adapters.Notifs;
import com.fame.plumbum.chataround.database.DBHandler;
import com.fame.plumbum.chataround.database.NotifTable;
import com.fame.plumbum.chataround.helper.Urls;
import com.fame.plumbum.chataround.models.ImageSendData;
import com.fame.plumbum.chataround.queries.ServerAPI;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;

/**
 * Created by pankaj on 4/8/16.
 */
public class MyProfile extends Fragment implements
        SwipeRefreshLayout.OnRefreshListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {
    View rootView = null;
    public String uid;
    Notifs adapter;
    CircleImageView user_image;
    ListView listView;
    TextView phone_view, name_view;
    public double lat, lng;
    private Context context;

    JSONArray[] completeListOfPosts;

    SharedPreferences sharedPreferences;
    MainActivity activity;

    private LocationRequest mLocationRequest;

    public String name = "", email = "";
    SharedPreferences.Editor editor;
    Bitmap bitmap;
    File file;
    Uri mCropImageUri;
    CropImageView mCropImageView;
    public SwipeRefreshLayout swipeRefreshLayout;

    private GoogleApiClient mGoogleApiClient = null;
    private Location mLastLocation;
    private double latitude;
    private double longitude;

    @Override
    public void onRefresh() {
        activity.needSomethingTweet = true;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        context = getContext();
        rootView = inflater.inflate(R.layout.my_profile, container, false);
        listView = (ListView) rootView.findViewById(R.id.my_tweets_list);
        swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        swipeRefreshLayout.setRefreshing(true);
                                        activity.needSomethingTweet = true;
                                    }
                                }
        );
        user_image = (CircleImageView) rootView.findViewById(R.id.image_user);
        name_view = (TextView) rootView.findViewById(R.id.name_user);
        phone_view = (TextView) rootView.findViewById(R.id.phone_user);

        mCropImageView = (CropImageView) rootView.findViewById(R.id.cropImageView);
        mCropImageView.setAspectRatio(1, 1);
        mCropImageView.setFixedAspectRatio(true);
        user_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(getPickImageChooserIntent(), 200);
            }
        });

        mGoogleApiClient = new GoogleApiClient.Builder(getContext())
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

        mGoogleApiClient.connect();

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        activity = (MainActivity) getActivity();
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        editor = sharedPreferences.edit();
        uid = sharedPreferences.getString("uid", null);
        name = sharedPreferences.getString("user_name", "");
        email = sharedPreferences.getString("user_email", "");
        completeListOfPosts = new JSONArray[]{new JSONArray()};
        activity.needSomethingTweet = true;
        name_view.setText(name.replace("%20", " "));


        /*Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(getContext(), Locale.getDefault());

        try {
            addresses = geocoder.getFromLocation(lat, lng, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            String address="";

           try {
               address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
           }catch (ArrayIndexOutOfBoundsException e){
           }

            String city = addresses.get(0).getLocality();
            String state = addresses.get(0).getAdminArea();
            String country = addresses.get(0).getCountryName();
            String postalCode = addresses.get(0).getPostalCode();
            String knownName = addresses.get(0).getFeatureName();

            if (knownName != null) {

                phone_view.setText(knownName);
                phone_view.append(", " + address);
                phone_view.append(", " + city);
                phone_view.append(", " + state);
                phone_view.append(", " + country);


            } else {
                phone_view.setText(address);

            }

        } catch (IOException e) {
            e.printStackTrace();
        }
*/
        getImage();
    }

    private String getRealPathFromURI(Uri contentURI) {
        String result;
        Cursor cursor = getActivity().getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) {
            result = contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(idx);
            cursor.close();
        }
        return result;
    }

    private void getImage() {
        final String[] image_name = new String[1];
        MySingleton.getInstance(getContext().getApplicationContext()).
                getRequestQueue();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Urls.BASE_URL + "ImageName?UserId=" + sharedPreferences.getString("uid", "578b119a7c4ec26dcab64a21"),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject json = new JSONObject(response);
                            if (!response.contains("Profile Image")) {
                                image_name[0] = json.getString("ImageName");
                                Picasso.with(getContext()).load(Urls.BASE_URL + "ImageReturn?ImageName=" + image_name[0])
                                        .resize(512, 512).error(R.drawable.user).into(user_image);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), "Error receiving data!", Toast.LENGTH_SHORT).show();
            }
        });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(5000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getInstance(getContext()).addToRequestQueue(stringRequest);
    }

    //3. How To Upload
    void sendImage_one() {
        ServerAPI api = ServerAPI.retrofit.create(ServerAPI.class);

        RequestBody to_server = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("file", file.getName(),
                to_server);

        api.upload(sharedPreferences.getString("uid", "578b119a7c4ec26dcab64a21"), body).enqueue(new Callback<ImageSendData>() {
            @Override
            public void onResponse(Call<ImageSendData> call, retrofit2.Response<ImageSendData> response) {
                ImageSendData formSever = response.body();
                if (formSever.getStatus() == 200) {
                    Toast.makeText(getContext(), "Image Uploaded!", Toast.LENGTH_SHORT).show();
                    getImage();
                } else {
                    Toast.makeText(getContext(), "Error uploading image!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ImageSendData> call, Throwable t) {
                Toast.makeText(getContext(), "Error uploading image!", Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            Uri imageUri = getPickImageResultUri(data);
            file = new File(getRealPathFromURI(imageUri));
            mCropImageView.setImageUriAsync(imageUri);
            final LinearLayout crop_ll = (LinearLayout) rootView.findViewById(R.id.ll_crop);
            Button cropImage = (Button) rootView.findViewById(R.id.cropImageButton);
            crop_ll.setVisibility(View.VISIBLE);
            final RelativeLayout rl_profile_main = (RelativeLayout) rootView.findViewById(R.id.rl_profile_main);
            rl_profile_main.setVisibility(View.GONE);
            mCropImageView.setImageUriAsync(mCropImageUri);
            cropImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    bitmap = mCropImageView.getCroppedImage();
                    if (bitmap != null) {
                        rl_profile_main.setVisibility(View.VISIBLE);
                        crop_ll.setVisibility(View.GONE);
                        user_image.setImageBitmap(bitmap);
                        savebitmap(bitmap);
                        sendImage_one();
                    }
                }
            });
        }
    }

    private void savebitmap(Bitmap bitmap) {
        FileOutputStream fOut = null;
        try {
            file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "pickImageResult.jpg");
            fOut = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, fOut);
            fOut.flush();
            fOut.close();
        } catch (IOException ignored) {
        }

    }

    private Uri getCaptureImageOutputUri() {
        File f = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        file = new File(f, "pickImageResult.jpg");
        Uri outputFileUri = Uri.fromFile(file);
        return outputFileUri;
    }

    /**
     * Get the URI of the selected image from  {@link #getPickImageChooserIntent()}.<br/>
     * Will return the correct URI for camera  and gallery image.
     *
     * @param data the returned data of the  activity result
     */
    public Uri getPickImageResultUri(Intent data) {
        boolean isCamera = true;
        if (data != null && data.getData() != null) {
            String action = data.getAction();
            isCamera = action != null && action.equals(MediaStore.ACTION_IMAGE_CAPTURE);
        }
        return isCamera ? getCaptureImageOutputUri() : data.getData();
    }

    public Intent getPickImageChooserIntent() {

// Determine Uri of camera image to  save.
        Uri outputFileUri = getCaptureImageOutputUri();

        List<Intent> allIntents = new ArrayList<>();
        PackageManager packageManager = activity.getPackageManager();

// collect all camera intents
        Intent captureIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        List<ResolveInfo> listCam = packageManager.queryIntentActivities(captureIntent, 0);
        for (ResolveInfo res : listCam) {
            Intent intent = new Intent(captureIntent);
            intent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
            intent.setPackage(res.activityInfo.packageName);
            if (outputFileUri != null) {
                intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
            }
            allIntents.add(intent);
        }

// collect all gallery intents
        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        List<ResolveInfo> listGallery = packageManager.queryIntentActivities(galleryIntent, 0);
        for (ResolveInfo res : listGallery) {
            Intent intent = new Intent(galleryIntent);
            intent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
            intent.setPackage(res.activityInfo.packageName);
            allIntents.add(intent);
        }

// the main intent is the last in the  list (fucking android) so pickup the useless one
        Intent mainIntent = allIntents.get(allIntents.size() - 1);
        for (Intent intent : allIntents) {
            if (intent.getComponent().getClassName().equals("com.android.documentsui.DocumentsActivity")) {
                mainIntent = intent;
                break;
            }
        }
        allIntents.remove(mainIntent);

// Create a chooser from the main  intent
        Intent chooserIntent = Intent.createChooser(mainIntent, "Select source");

// Add all other intents
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, allIntents.toArray(new Parcelable[allIntents.size()]));

        return chooserIntent;
    }


    @Override
    public void onResume() {
        super.onResume();
        if (sharedPreferences != null) {
            name = sharedPreferences.getString("user_name", null);
        }
        activity.needSomethingTweet = true;
    }
/*
    public void getAllPosts(List<JSONObject> response, int count) {
        try {
//            JSONObject jO = new JSONObject(response);
//            JSONArray currentListOfPost = jO.getJSONArray("Posts");
//            JSONArray currentListOfPost = response;


            JSONArray mine = new JSONArray();
            DBHandler db = new DBHandler(getContext());
            List<NotifTable> notifTables = db.getNotifs();
            for (int i = 0; i < response.size(); i++) {
                for (int j = 0; j < notifTables.size(); j++) {
                    if (notifTables.get(j).getPost_Id().contentEquals(response.get(i).getString("PostId"))) {
                        response.get(i).put("NLike", Integer.parseInt(response.get(i).getString("NLike")) + notifTables.get(j).getNLike());
                        response.get(i).put("NComment", Integer.parseInt(response.get(i).getString("NComment")) + notifTables.get(j).getNComment());
                        break;
                    }
                }
            }

            db.deleteNotif();

            for (int i = 0; i < response.size(); i++) {
                if (response.get(i).getString("PosterId").contentEquals(uid)) {
                    if (!response.get(i).getString("NLike").contentEquals("0") || !response.get(i).getString("NComment").contentEquals("0")) {
                        mine.put(response.get(i));
                        db.addNotif(new NotifTable(response.get(i).getString("PostId"),
                                Integer.parseInt(response.get(i).getString("NComment")),
                                Integer.parseInt(response.get(i).getString("NLike"))));
                    }
                }
            }
            TextView midText = (TextView) rootView.findViewById(R.id.midText);
            if (mine.length() > 0) {
                if (count == 0) {
                    midText.setVisibility(View.GONE);
                    adapter = new Notifs(getContext(), mine);
                    listView.setAdapter(adapter);
                    listView.setVisibility(View.VISIBLE);
                } else {
                    for (int i = 0; i < mine.length(); i++)
                        adapter.posts.put(mine.getJSONObject(i));
                    adapter.total = adapter.total + mine.length();
                    int index = listView.getFirstVisiblePosition();
                    View v = listView.getChildAt(0);
                    int top = (v == null) ? 0 : v.getTop();

                    adapter.notifyDataSetChanged();
                    listView.setSelectionFromTop(index, top);
                }
            } else {
                listView.setVisibility(View.INVISIBLE);
                midText.setVisibility(View.VISIBLE);
            }
        } catch (JSONException ignored) {
        }
    }*/


    public void getAllPosts(String response, int count) {
        try {
            JSONObject jO = new JSONObject(response);
            JSONArray currentListOfPost = jO.getJSONArray("Posts");


            JSONArray mine = new JSONArray();
            DBHandler db = new DBHandler(getContext());
            List<NotifTable> notifTables = db.getNotifs();
            for (int i = 0; i < currentListOfPost.length(); i++) {
                for (int j = 0; j < notifTables.size(); j++) {
                    if (notifTables.get(j).getPost_Id().contentEquals(currentListOfPost.getJSONObject(i).getString("PostId"))) {
                        currentListOfPost.getJSONObject(i).put("NLike", Integer.parseInt(currentListOfPost.getJSONObject(i).getString("NLike")) + notifTables.get(j).getNLike());
                        currentListOfPost.getJSONObject(i).put("NComment", Integer.parseInt(currentListOfPost.getJSONObject(i).getString("NComment")) + notifTables.get(j).getNComment());
                        break;
                    }
                }
            }

            db.deleteNotif();

            for (int i = 0; i < currentListOfPost.length(); i++) {
                if (currentListOfPost.getJSONObject(i).getString("PosterId").contentEquals(uid)) {
                    if (!currentListOfPost.getJSONObject(i).getString("NLike").contentEquals("0") || !currentListOfPost.getJSONObject(i).getString("NComment").contentEquals("0")) {
                        mine.put(currentListOfPost.getJSONObject(i));
                        db.addNotif(new NotifTable(currentListOfPost.getJSONObject(i).getString("PostId"), Integer.parseInt(currentListOfPost.getJSONObject(i).getString("NComment")), Integer.parseInt(currentListOfPost.getJSONObject(i).getString("NLike"))));
                    }
                }
            }
            TextView midText = (TextView) rootView.findViewById(R.id.midText);
            if (mine.length() > 0) {
                if (count == 0) {
                    midText.setVisibility(View.GONE);
                    adapter = new Notifs(getContext(), mine);
                    listView.setAdapter(adapter);
                    listView.setVisibility(View.VISIBLE);
                } else {
                    for (int i = 0; i < mine.length(); i++)
                        adapter.posts.put(mine.getJSONObject(i));
                    adapter.total = adapter.total + mine.length();
                    int index = listView.getFirstVisiblePosition();
                    View v = listView.getChildAt(0);
                    int top = (v == null) ? 0 : v.getTop();

                    adapter.notifyDataSetChanged();
                    listView.setSelectionFromTop(index, top);
                }
            } else {
                listView.setVisibility(View.INVISIBLE);
                midText.setVisibility(View.VISIBLE);
            }
        } catch (JSONException ignored) {
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

        if (ActivityCompat.checkSelfPermission(getContext(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getContext(),
                        Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        if (mLastLocation != null) {
            latitude = mLastLocation.getLatitude();
            longitude = mLastLocation.getLongitude();

            Geocoder geocoder;
            List<Address> addresses;
            geocoder = new Geocoder(getContext(), Locale.getDefault());

            try {
                addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                if(addresses.size()>0) {
                    String city = addresses.get(0).getLocality();
                    String state = addresses.get(0).getAdminArea();
//                String country = addresses.get(0).getCountryName();
//                String postalCode = addresses.get(0).getPostalCode();
//                String knownName = addresses.get(0).getFeatureName();


                    phone_view.setText(city);
                    phone_view.append(", " + state);
                }else{
                    Toast.makeText(context, "Unable to fetch location!", Toast.LENGTH_SHORT).show();
                }

            } catch (IOException e) {
                Toast.makeText(context, "Got Error", Toast.LENGTH_SHORT).show();

                Log.d("MyProfile ", "Exception");
                e.printStackTrace();
            }

        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {


        latitude = location.getLatitude();
        longitude = location.getLongitude();

        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(getContext(), Locale.getDefault());

        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
//            String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            if(addresses.size()>0) {
                String city = addresses.get(0).getLocality();
                String state = addresses.get(0).getAdminArea();
//                String country = addresses.get(0).getCountryName();
//                String postalCode = addresses.get(0).getPostalCode();
//                String knownName = addresses.get(0).getFeatureName();


                phone_view.setText(city);
                phone_view.append(", " + state);
            }else{
                Toast.makeText(context, "Unable to fetch location!", Toast.LENGTH_SHORT).show();
            }


        } catch (IOException e) {
            Toast.makeText(context, "Got Error", Toast.LENGTH_SHORT).show();

            Log.d("MyProfile ", "Exception");
            e.printStackTrace();
        }


    }
}