package com.fame.plumbum.chataround.restroom.view;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.fame.plumbum.chataround.R;
import com.fame.plumbum.chataround.restroom.model.RestRoomData;
import com.fame.plumbum.chataround.restroom.presenter.RestRoomPresenter;
import com.fame.plumbum.chataround.restroom.presenter.RestRoomPresenterImpl;
import com.fame.plumbum.chataround.restroom.provider.RestRoomProviderImpl;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link RestroomFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link RestroomFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RestroomFragment extends Fragment implements RestRoomView, OnMapReadyCallback , LocationListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private List<RestRoomData> restRoomDataList;
    private RestRoomPresenter restRoomPresenter;
    private GoogleMap googleMap;

    @BindView(R.id.progressBar)
    ProgressBar progressBar;


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public RestroomFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RestroomFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RestroomFragment newInstance(String param1, String param2) {
        RestroomFragment fragment = new RestroomFragment();
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
        View view = inflater.inflate(R.layout.fragment_restroom, container, false);


        restRoomPresenter = new RestRoomPresenterImpl(this, new RestRoomProviderImpl());
        ButterKnife.bind(this, view);
        LocationManager lm = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return view;
        }
        Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        double longitude = location.getLongitude();
        double latitude = location.getLatitude();
        latitude = 28.567522;
        longitude = 77.218951;
        restRoomPresenter.requestRestRooms(latitude, longitude);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


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
    public void showLoader(boolean show) {
        if (show) {

            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
        }
    }

    @Override
    public void showMessage(String message) {

    }

    @Override
    public void onReceived(List<RestRoomData> restRoomDataList) {

        this.restRoomDataList = restRoomDataList;
        Log.d("Data Received", String.valueOf(restRoomDataList.size()));

        SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    /* protected Marker createMarker(double latitude, double longitude, String title, String snippet, int iconResID) {

         return googleMap.addMarker(new MarkerOptions()
                 .position(new LatLng(latitude, longitude))
                 .anchor(0.5f, 0.5f)
                 .title(title)
                 .snippet(snippet)
         .icon(BitmapDescriptorFactory.fromResource(iconResID)));
     }
 */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap=googleMap;
        double latitude = 28.567522;
        double longitude = 77.218951;

        LatLng delhi = new LatLng(latitude, longitude);

        for (int i = 0; i < restRoomDataList.size(); i++) {
            googleMap.addMarker(new MarkerOptions()
                            .position(new LatLng(restRoomDataList.get(i).getLatitude(),
                                    restRoomDataList.get(i).getLongitude()))
                            .anchor(0.5f, 0.5f)
                            .title(restRoomDataList.get(i).getName())
                            .snippet(restRoomDataList.get(i).getComment())
//                .icon(BitmapDescriptorFactory.fromResource(R.drawable.back))
            );
        }
//        googleMap.moveCamera(CameraUpdateFactory.newLatLng(delhi));
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(delhi, 12.0f));
    }

    @Override
    public void onLocationChanged(Location location) {

        googleMap.
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
