package com.fame.plumbum.chataround.pollution.view;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.fame.plumbum.chataround.R;
import com.fame.plumbum.chataround.helper.utils.DistanceUtils;
import com.fame.plumbum.chataround.pollution.model.AirPollutionDetails;
import com.fame.plumbum.chataround.pollution.model.AirPollutionIndividualAqi;
import com.fame.plumbum.chataround.pollution.model.AirPollutionIndividualValue;
import com.fame.plumbum.chataround.pollution.presenter.PollutionPresenter;
import com.fame.plumbum.chataround.pollution.presenter.PollutionPresenterImpl;
import com.fame.plumbum.chataround.pollution.provider.RetrofitPollutionProvider;
import com.github.pavlospt.CircleView;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.desmond.squarecamera.SquareCameraPreview.TAG;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PollutionFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PollutionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PollutionFragment extends Fragment implements
        PollutionView,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener,
        SwipeRefreshLayout.OnRefreshListener {


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private PollutuionAqiAdapter pollutuionAqiAdapter;
    private OnFragmentInteractionListener mListener;


    @BindView(R.id.nestedScrollView)
    NestedScrollView scrollView;

    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    @BindView(R.id.circleView)
    CircleView circleView;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @BindView(R.id.aqi_health_notice)
    TextView aqi_health_notice;

    @BindView(R.id.humidity)
    TextView humidity;

    @BindView(R.id.humidity_description)
    TextView humidity_description;

    @BindView(R.id.pollution_data_not_found_layout)
    CardView pollution_data_not_found_layout;

    @BindView(R.id.pollution_data_not_found_message)
    TextView pollution_data_not_found_message;

    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;

    private Context context;

    private GoogleApiClient mGoogleApiClient = null;
    private Location mLastLocation;
    private double latitude;
    private double longitude;

    private PollutionPresenter pollutionPresenter;

    private LocationRequest mLocationRequest;


    public PollutionFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PollutionFragment.
     */
    // TODO: Rename and change types and number of parameters    @Override
    public static PollutionFragment newInstance(String param1, String param2) {
        PollutionFragment fragment = new PollutionFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

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
        View view = inflater.inflate(R.layout.fragment_pollution, container, false);
        ButterKnife.bind(this, view);

        try {
            context = getContext();
        } catch (Throwable e) {
            context = null;
            e.printStackTrace();
        }

        pollutionPresenter = new PollutionPresenterImpl(this, new RetrofitPollutionProvider());

        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(context)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

        // Create the LocationRequest object
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10 * 1000)        // 10 seconds, in milliseconds
                .setFastestInterval(1 * 1000); // 1 second, in milliseconds

/*
        double latitude = 19.130306;
        double longitude = 72.889993;
*/


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(linearLayoutManager);

        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        swipeRefreshLayout.setRefreshing(true);

                                        if (latitude != 0.0 && longitude != 0.0) {

                                            pollutionPresenter.requestAirPollution(
                                                    false, latitude, longitude);

                                        }
                                    }
                                }
        );


        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                int topRowVerticalPosition =
                        (recyclerView == null || recyclerView.getChildCount() == 0) ? 0 : recyclerView.getChildAt(0).getTop();
                swipeRefreshLayout.setEnabled(topRowVerticalPosition >= 0);

            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }
        });


        return view;
    }

    @Override
    public void onStart() {
        mGoogleApiClient.connect();

        super.onStart();
    }

    @Override
    public void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
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
        if (pollutionPresenter != null) {
            pollutionPresenter.onDestroy();
        }
    }

    @Override
    public void showLoader(boolean show) {
        if (show) {

            swipeRefreshLayout.setRefreshing(true);
            progressBar.setVisibility(View.GONE);
            scrollView.setVisibility(View.GONE);

        } else {
            swipeRefreshLayout.setRefreshing(false);
            progressBar.setVisibility(View.GONE);
            scrollView.setVisibility(View.VISIBLE);

        }
    }

    @Override
    public void showMessage(String message) {

        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void setData(AirPollutionDetails airPollutionDetails) {

        List<Double> geo = airPollutionDetails.getData().getCity().getGeo();

        double distance1 = DistanceUtils.calculateDistanceBetweenTwoPoints(
                latitude,
                longitude,
                geo.get(0),
                geo.get(1));

        double distance2 = DistanceUtils.calculateDistanceBetweenTwoPoints(
                latitude,
                longitude,
                geo.get(1),
                geo.get(0));

        double distance = 0.0;
        if (distance1 < distance2) {
            distance = distance1;
        } else {
            distance = distance2;
        }
/*
        Location myLocation=new Location("My Location");
        myLocation.setLatitude(latitude);
        myLocation.setLongitude(longitude);

        Location sensorLocation=new Location("Sensor Location");
        sensorLocation.setLatitude(geo.get(1));
        sensorLocation.setLongitude(geo.get(0));

        distance = myLocation.distanceTo(sensorLocation) ;
*/

        if (distance > 50) {

            pollution_data_not_found_layout.setVisibility(View.VISIBLE);
            /*pollution_data_not_found_message.append("\n\nYour distance from Nearest Sensor is - " +
                    String.valueOf(Math.round(distance)) + " Kms and Sensor Latitude Longitude is "
                    + String.valueOf(geo.get(0)) + "  " + String.valueOf(geo.get(1))
            );
*/
            scrollView.setVisibility(View.GONE);
            return;
        } else {
            pollution_data_not_found_layout.setVisibility(View.GONE);
            scrollView.setVisibility(View.VISIBLE);

        }


        double aqi = airPollutionDetails.getData().getAqi();
        String healthStatement = "";

        if (aqi < 50) {


            circleView.setFillColor(ContextCompat.getColor(context, R.color.good));
            healthStatement = "Air quality is considered satisfactory,and air pollution1 poses " +
                    "little or no risk";

        } else if (aqi > 50 && aqi <= 100) {

            circleView.setFillColor(ContextCompat.getColor(context, R.color.moderate));
            healthStatement = "Air quality is acceptable; however, for some pollutants there may be a " +
                    "moderate health concern for a very small number of people who are unusually " +
                    "sensitive to air pollution1.";

        } else if (aqi > 100 && aqi <= 150) {

            circleView.setFillColor(ContextCompat.getColor(context, R.color.sensitive));
            healthStatement = "Members of sensitive groups may experience health effects. The " +
                    "general public is not likely to be affected.";

        } else if (aqi > 150 && aqi <= 200) {

            circleView.setFillColor(ContextCompat.getColor(context, R.color.unhealthy));
            healthStatement = "Everyone may begin to experience health effects; members of sensitive " +
                    "groups may experience more serious health effects.";

        } else if (aqi > 200 && aqi <= 300) {

            circleView.setFillColor(ContextCompat.getColor(context, R.color.very_unhealthy));
            healthStatement = "Health warnings of emergency conditions. The entire population is " +
                    "more likely to be affected.";

        } else if (aqi > 300) {

            circleView.setFillColor(ContextCompat.getColor(context, R.color.hazardous));
            healthStatement = "Health alert: everyone may experience more serious health effects";

        }


        try {
            humidity.setText("Humidity - " + String.valueOf(airPollutionDetails.getData().getIaqi().getH().getV()));

            if (airPollutionDetails.getData().getIaqi().getH().getV() < 30) {
                humidity_description.setText(" May lead to eye, nose and throat discomfort, contact " +
                        "lens irritation, dry skin, and static electricity buildup");
            } else if (airPollutionDetails.getData().getIaqi().getH().getV() > 30 ||
                    airPollutionDetails.getData().getIaqi().getH().getV() < 60) {
                humidity_description.setText("Optimal");
            } else if (airPollutionDetails.getData().getIaqi().getH().getV() > 60) {

                humidity_description.setText(" May lead to physical discomfort, as sweat is not" +
                        " allowed to readily evaporate from the body and mold growth");
            }

        } catch (Exception e) {
            Log.e(TAG, e.getMessage());

        }


        circleView.setTitleText(String.valueOf(airPollutionDetails.getData().getAqi()) + "\n" + "AQI");
        aqi_health_notice.setText(healthStatement);
        aqi_health_notice.append("\n\n City :" + airPollutionDetails.getData().getCity().getName());


        pollutuionAqiAdapter = new
                PollutuionAqiAdapter(context,
                getIndividualAqiList(airPollutionDetails.getData().getIaqi()));

        List<AirPollutionIndividualValue> list = getIndividualAqiList(airPollutionDetails.
                getData().getIaqi());


        recyclerView.setAdapter(pollutuionAqiAdapter);
        pollutuionAqiAdapter.notifyDataSetChanged();
    }

    List<AirPollutionIndividualValue> getIndividualAqiList(AirPollutionIndividualAqi airPollutionIndividualAqi) {

        List<AirPollutionIndividualValue> airPollutionIndividualValueList = new ArrayList<>();


        if (airPollutionIndividualAqi.getPm25() != null) {


            double aqi = airPollutionIndividualAqi.getPm25().getV();
            int color = ContextCompat.getColor(context, R.color.white);


            if (aqi < 30) {

                color = ContextCompat.getColor(context, R.color.good);

            } else if (aqi > 30 && aqi <= 60) {

                color = ContextCompat.getColor(context, R.color.moderate);


            } else if (aqi > 60 && aqi <= 90) {

                color = ContextCompat.getColor(context, R.color.sensitive);

            } else if (aqi > 90 && aqi <= 120) {

                color = ContextCompat.getColor(context, R.color.unhealthy);

            } else if (aqi > 120 && aqi <= 250) {

                color = ContextCompat.getColor(context, R.color.very_unhealthy);

            } else if (aqi > 250) {

                color = ContextCompat.getColor(context, R.color.hazardous);

            }


            airPollutionIndividualValueList.add(new AirPollutionIndividualValue(
                    airPollutionIndividualAqi.getPm25().getV(), "PM2.5", color, 30, 60, 90, 120, 250, 360));

        } else {

           /* int color = R.color.accentGray;
            airPollutionIndividualValueList.add(new AirPollutionIndividualValue(
                    -9999, "PM 2.5", color,0,0,0,0,0,0));*/

        }

        if (airPollutionIndividualAqi.getPm10() != null) {

            double aqi = airPollutionIndividualAqi.getPm10().getV();
            int color = ContextCompat.getColor(context, R.color.white);
            if (aqi < 50) {

                color = ContextCompat.getColor(context, R.color.good);

            } else if (aqi > 50 && aqi <= 100) {

                color = ContextCompat.getColor(context, R.color.moderate);


            } else if (aqi > 100 && aqi <= 250) {

                color = ContextCompat.getColor(context, R.color.sensitive);

            } else if (aqi > 250 && aqi <= 350) {

                color = ContextCompat.getColor(context, R.color.unhealthy);

            } else if (aqi > 350 && aqi <= 430) {

                color = ContextCompat.getColor(context, R.color.very_unhealthy);

            } else if (aqi > 430) {

                color = ContextCompat.getColor(context, R.color.hazardous);

            }

            airPollutionIndividualValueList.add(new AirPollutionIndividualValue(
                    airPollutionIndividualAqi.getPm10().getV(), "PM10", color, 50, 100, 250, 350, 430, 500));

        } else {

/*            int color = R.color.accentGray;
            airPollutionIndividualValueList.add(new AirPollutionIndividualValue(
                    -9999, "PM 10", color,0,0,0,0,0,0));*/
        }


        if (airPollutionIndividualAqi.getO3() != null) {

            double aqi = airPollutionIndividualAqi.getO3().getV();
            int color = ContextCompat.getColor(context, R.color.white);
            if (aqi < 50) {

                color = ContextCompat.getColor(context, R.color.good);

            } else if (aqi > 50 && aqi <= 100) {

                color = ContextCompat.getColor(context, R.color.moderate);


            } else if (aqi > 100 && aqi <= 168) {

                color = ContextCompat.getColor(context, R.color.sensitive);

            } else if (aqi > 168 && aqi <= 208) {

                color = ContextCompat.getColor(context, R.color.unhealthy);

            } else if (aqi > 208 && aqi <= 748) {

                color = ContextCompat.getColor(context, R.color.very_unhealthy);

            } else if (aqi > 748) {

                color = ContextCompat.getColor(context, R.color.hazardous);

            }


            airPollutionIndividualValueList.add(new AirPollutionIndividualValue(
                    airPollutionIndividualAqi.getO3().getV(), "Ozone", color, 50, 100, 168, 208, 748, 900));

        } else {

  /*          int color = R.color.accentGray;
            airPollutionIndividualValueList.add(new AirPollutionIndividualValue(
                    -9999, "o3", color,0,0,0,0,0,0));*/
        }


        if (airPollutionIndividualAqi.getNo2() != null) {

            double aqi = airPollutionIndividualAqi.getNo2().getV();
            int color = ContextCompat.getColor(context, R.color.white);
            if (aqi < 40) {

                color = ContextCompat.getColor(context, R.color.good);

            } else if (aqi > 40 && aqi <= 80) {

                color = ContextCompat.getColor(context, R.color.moderate);


            } else if (aqi > 80 && aqi <= 180) {

                color = ContextCompat.getColor(context, R.color.sensitive);

            } else if (aqi > 180 && aqi <= 280) {

                color = ContextCompat.getColor(context, R.color.unhealthy);

            } else if (aqi > 280 && aqi <= 400) {

                color = ContextCompat.getColor(context, R.color.very_unhealthy);

            } else if (aqi > 400) {

                color = ContextCompat.getColor(context, R.color.hazardous);

            }


            airPollutionIndividualValueList.add(new AirPollutionIndividualValue(
                    airPollutionIndividualAqi.getNo2().getV(), getResources().
                    getString(R.string.nitrogen_dioxide), color, 40, 80, 180, 280, 400, 500));
        } else {

/*            int color = R.color.accentGray;
            airPollutionIndividualValueList.add(new AirPollutionIndividualValue(
                    -9999, "No2", color,0,0,0,0,0,0));*/
        }


        if (airPollutionIndividualAqi.getCo() != null) {

            double aqi = airPollutionIndividualAqi.getCo().getV();
            int color = ContextCompat.getColor(context, R.color.white);
            if (aqi < 1.0) {

                color = ContextCompat.getColor(context, R.color.good);

            } else if (aqi > 1.0 && aqi <= 2.0) {

                color = ContextCompat.getColor(context, R.color.moderate);


            } else if (aqi > 2.0 && aqi <= 10) {

                color = ContextCompat.getColor(context, R.color.sensitive);

            } else if (aqi > 10 && aqi <= 17) {

                color = ContextCompat.getColor(context, R.color.unhealthy);

            } else if (aqi > 17 && aqi <= 34) {

                color = ContextCompat.getColor(context, R.color.very_unhealthy);

            } else if (aqi > 34) {

                color = ContextCompat.getColor(context, R.color.hazardous);

            }


            airPollutionIndividualValueList.add(new AirPollutionIndividualValue(
                    airPollutionIndividualAqi.getCo().getV(), getResources().
                    getString(R.string.carbon_oxide), color, 1, 2, 10, 17, 34, 50));

            Log.d(TAG, "CO Present " + String.valueOf(airPollutionIndividualAqi.getCo().getV()));


        } else {

  /*          int color = R.color.white;
            airPollutionIndividualValueList.add(new AirPollutionIndividualValue(
                    -9999, "Co", color,0,0,0,0,0,0));

            Log.d(TAG, "CO Not Present");*/

        }

        if (airPollutionIndividualAqi.getS02() != null) {

            double aqi = airPollutionIndividualAqi.getS02().getV();
            int color = ContextCompat.getColor(context, R.color.white);
            if (aqi < 40) {

                color = ContextCompat.getColor(context, R.color.good);

            } else if (aqi > 40 && aqi <= 80) {

                color = ContextCompat.getColor(context, R.color.moderate);


            } else if (aqi > 80 && aqi <= 380) {

                color = ContextCompat.getColor(context, R.color.sensitive);

            } else if (aqi > 380 && aqi <= 800) {

                color = ContextCompat.getColor(context, R.color.unhealthy);

            } else if (aqi > 800 && aqi <= 1600) {

                color = ContextCompat.getColor(context, R.color.very_unhealthy);

            } else if (aqi > 1600) {

                color = ContextCompat.getColor(context, R.color.hazardous);

            }


            airPollutionIndividualValueList.add(new AirPollutionIndividualValue(
                    airPollutionIndividualAqi.getS02().getV(), getResources().
                    getString(R.string.sulphur_dioxide), color, 40, 80, 380, 800, 1600, 1700));

            Log.d(TAG, "S02 Present");

        } else {

/*            int color = R.color.accentGray;
            airPollutionIndividualValueList.add(new AirPollutionIndividualValue(
                    -9999, "So2", color,0,0,0,0,0,0));
            Log.d(TAG, "S02 Not Present");*/

        }

        return airPollutionIndividualValueList;


/*
    This Method is for Temperature that we are not going to use.
    Only Gases Will be Showin in Recycler View .


        if (airPollutionIndividualAqi.getT() != null) {

            airPollutionIndividualValueList.add(new AirPollutionIndividualValue(
                    airPollutionIndividualAqi.getT().getV(), "T"));

        }


         if (airPollutionIndividualAqi.getW() != null) {

            airPollutionIndividualValueList.add(new AirPollutionIndividualValue(
                    airPollutionIndividualAqi.getW().getV(), "W"));

        }
        if (airPollutionIndividualAqi.getWd() != null) {

            airPollutionIndividualValueList.add(new AirPollutionIndividualValue(
                    airPollutionIndividualAqi.getWd().getV(), "WD"));
        }

        if (airPollutionIndividualAqi.getD() != null) {

            airPollutionIndividualValueList.add(new AirPollutionIndividualValue(
                    airPollutionIndividualAqi.getD().getV(), "D"));
        }

        if (airPollutionIndividualAqi.getH() != null) {

            airPollutionIndividualValueList.add(new AirPollutionIndividualValue(
                    airPollutionIndividualAqi.getH().getV(), "H"));
        }*/
/*
This Method is for Pressure that we are not going to use.

        if (airPollutionIndividualAqi.getP() != null) {

            airPollutionIndividualValueList.add(new AirPollutionIndividualValue(
                    airPollutionIndividualAqi.getP().getV(), "P"));

        }
*/


    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(context,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(context,
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

/*

            Delhi
            latitude=28.5223220;
            longitude=77.1742630;
*/

/*

            Korba

            latitude=22.360133;
            longitude=82.719884;
*/

            new PollutionPresenterImpl(this,
                    new RetrofitPollutionProvider())
                    .requestAirPollution(false, latitude, longitude);
        }

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    /**
     * If locationChanges change lat and long
     *
     * @param location
     */
    @Override
    public void onLocationChanged(Location location) {
        latitude = location.getLatitude();
        longitude = location.getLongitude();

/*
        Delhi
        latitude=28.5223220;
        longitude=77.1742630;
*/


/*
        Korba
        latitude=22.360133;
        longitude=82.719884;*/

        new PollutionPresenterImpl(this,
                new RetrofitPollutionProvider())
                .requestAirPollution(true, latitude, longitude);


    }

    @Override
    public void onRefresh() {

        if (latitude != 0.0 && longitude != 0.0) {
            pollutionPresenter.requestAirPollution(false, latitude, longitude);
        }
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


}
