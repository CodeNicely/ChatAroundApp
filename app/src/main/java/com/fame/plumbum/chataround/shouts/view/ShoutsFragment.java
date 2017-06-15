package com.fame.plumbum.chataround.shouts.view;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.CustomEvent;
import com.fame.plumbum.chataround.MySingleton;
import com.fame.plumbum.chataround.R;
import com.fame.plumbum.chataround.helper.Keys;
import com.fame.plumbum.chataround.helper.SharedPrefs;
import com.fame.plumbum.chataround.helper.Urls;
import com.fame.plumbum.chataround.helper.image_loader.GlideImageLoader;
import com.fame.plumbum.chataround.helper.image_loader.ImageLoader;
import com.fame.plumbum.chataround.shouts.model.Posts;
import com.fame.plumbum.chataround.shouts.presenter.ShoutsPresenter;
import com.fame.plumbum.chataround.shouts.presenter.ShoutsPresenterImpl;
import com.fame.plumbum.chataround.shouts.provider.RetrofitShoutsProvider;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ShoutsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ShoutsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ShoutsFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener,
        ShoutsView {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String TAG ="ShoutsFragment" ;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    View rootView = null;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    ShoutsRecyclerAdapter shoutsRecyclerAdapter;


    private int count = 0;
    private SharedPreferences sharedPreferences;

    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private Context context;

    @BindView(R.id.shout)
    Button shout;

    @BindView(R.id.loadMore)
    Button loadMore;

    private ImageLoader glideImageLoader;

    private SwipeRefreshLayout swipeRefreshLayout;
    private double latitude, longitude;

    private ShoutsPresenter shoutsPresenter;


    private OnFragmentInteractionListener mListener;
    private SharedPrefs sharedPrefs;

    public ShoutsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ShoutsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ShoutsFragment newInstance(String param1, String param2) {
        ShoutsFragment fragment = new ShoutsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_world, container, false);
        context = getContext();

        shoutsRecyclerAdapter = new ShoutsRecyclerAdapter(this, context);
        glideImageLoader = new GlideImageLoader(context);
        shoutsPresenter = new ShoutsPresenterImpl(this, new RetrofitShoutsProvider());

        ButterKnife.bind(this, rootView);
        sharedPrefs = new SharedPrefs(context);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(shoutsRecyclerAdapter);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);

        mGoogleApiClient = new GoogleApiClient.Builder(context)
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
                .setFastestInterval(1000); // 1 second, in milliseconds

        mGoogleApiClient.connect();


        swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe);
        swipeRefreshLayout.setOnRefreshListener(this);


        loadMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count++;
                shoutsPresenter.requestShouts(sharedPreferences.getString("uid", null), count, latitude, longitude);

            }
        });


        shout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actionShout();

            }
        });


        return rootView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    @Override
    public void onRefresh() {
        count=0;
        shoutsPresenter.requestShouts(sharedPreferences.getString("uid", null), count, latitude, longitude);


    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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


            //If everything went fine lets get latitude and longitude
            latitude = location.getLatitude();
            longitude = location.getLongitude();
            count=0;
            shoutsPresenter.requestShouts(sharedPreferences.getString("uid", null), count, latitude, longitude);

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

        shoutsPresenter.requestShouts(sharedPreferences.getString("uid", null), count, latitude, longitude);

    }


    public void actionShout() {

        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_create_post);
        final EditText content = (EditText) dialog.findViewById(R.id.post_content);
        final TextView mTextView = (TextView) dialog.findViewById(R.id.num_chars);
        final TextWatcher mTextEditorWatcher = new TextWatcher() {
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //This sets a textview to the current length
                mTextView.setText(String.valueOf(140 - s.length()) + "/140");
            }

            public void afterTextChanged(Editable s) {
            }
        };
        content.addTextChangedListener(mTextEditorWatcher);


        Button create_post = (Button) dialog.findViewById(R.id.post_button);
        create_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                Toast.makeText(MainActivity.this, Urls.BASE_URL + "Post?UserId=" + profile.uid + "&UserName=" + profile.name.replace(" ", "%20") + "&Post=Hello&Latitude=" + latitude + "&Longitude=" + longitude, Toast.LENGTH_SHORT).show();
                Log.d(TAG, Urls.BASE_URL + "Post?UserId=" + sharedPreferences.getString("uid", null) + "&UserName=" + sharedPreferences.getString("user_name", "") + "&Post=Hello&Latitude=" + latitude + "&Longitude=" + longitude);
                String content_txt = content.getText().toString();
                if (latitude != 0 && longitude != 0) {
                    RequestQueue queue = MySingleton.getInstance(context).getRequestQueue();
                    content_txt = content_txt.replace("\n", "%0A");
                    StringRequest stringRequest = new StringRequest(Request.Method.GET, Urls.BASE_URL + "Post?UserId=" + sharedPreferences.getString("uid", null) + "&UserName=" + sharedPreferences.getString("user_name", "").replace(" ", "%20") + "&Post=" + content_txt.replace(" ", "%20") + "&Latitude=" + latitude + "&Longitude=" + longitude,
                            new Response.Listener<String>() {
                                public static final String TAG = "MainActivity";

                                @Override
                                public void onResponse(String response) {
                                    Log.d(TAG, "Response " + response);

                                    Answers.getInstance().logCustom(new CustomEvent("Adding Shout Successful")
                                            .putCustomAttribute(Keys.USER_EMAIL, sharedPrefs.getEmail())
                                            .putCustomAttribute(Keys.KEY_LATITUDE, latitude)
                                            .putCustomAttribute(Keys.KEY_LONGITUDE, longitude)
                                    );
                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            error.printStackTrace();
                            Answers.getInstance().logCustom(new CustomEvent("Adding Shout Failed")
                                    .putCustomAttribute(Keys.USER_EMAIL, sharedPrefs.getEmail())
                                    .putCustomAttribute(Keys.KEY_LATITUDE, latitude)
                                    .putCustomAttribute(Keys.KEY_LONGITUDE, longitude)
                            );
                            Toast.makeText(context, "Error sending data!", Toast.LENGTH_SHORT).show();
                        }
                    });
                    stringRequest.setRetryPolicy(new DefaultRetryPolicy(5000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                    MySingleton.getInstance(context).addToRequestQueue(stringRequest);
                } else {
                    Toast.makeText(context, "Location Error", Toast.LENGTH_SHORT).show();
                }
                dialog.dismiss();
            }
        });
        dialog.show();
    }


    void showProfileDialog(String userName, String userImage) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_proifle);
        com.rey.material.widget.Button okay = (com.rey.material.widget.Button) dialog.findViewById(R.id.okay);
        CircleImageView circleImageView = (CircleImageView) dialog.findViewById(R.id.circleImageView);
        TextView name = (TextView) dialog.findViewById(R.id.name);

        name.setText(userName);

        Glide.with(context).load(userImage).placeholder(R.drawable.user_big).into(circleImageView);


        okay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();


    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showLoader(boolean show) {
        if (show) {
            swipeRefreshLayout.setRefreshing(true);
        } else {
            swipeRefreshLayout.setRefreshing(false);
        }

    }

    @Override
    public void setData(List<Posts> posts) {
        if (count > 0) {
            shoutsRecyclerAdapter.appendData(posts);

        } else {
            shoutsRecyclerAdapter.setData(posts);
        }
        shoutsRecyclerAdapter.notifyDataSetChanged();
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    void changeReportStatus(String postId, final int position){
        RequestQueue queue = MySingleton.getInstance(context.getApplicationContext()).getRequestQueue();
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Urls.BASE_URL + "Like?UserId=" + sp.getString("uid", "") + "&PostId=" + postId + "&UserName=" + sp.getString("user_name", "").replace(" ", "%20") + "&Latitude=" + latitude + "&Longitude=" + longitude,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jo = new JSONObject(response);
                            if(jo.getInt("Status")==200){

                                Answers.getInstance().logCustom(new CustomEvent("Shout Reported Successful"));

                                if(shoutsRecyclerAdapter.getPostsList().get(position).getLikeFlag().contentEquals("0")){
                                    shoutsRecyclerAdapter.getPostsList().get(position).setLikeFlag("1");
                                }else{
                                    shoutsRecyclerAdapter.getPostsList().get(position).setLikeFlag("0");

                                }
                                shoutsRecyclerAdapter.notifyItemChanged(position);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Answers.getInstance().logCustom(new CustomEvent("Shout Reporting Failed"));

                Toast.makeText(context, "Error sending data!", Toast.LENGTH_SHORT).show();
            }
        });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(5000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getInstance(context).addToRequestQueue(stringRequest);

    }

}
