package com.fame.plumbum.chataround.emergency.view;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fame.plumbum.chataround.R;
import com.fame.plumbum.chataround.emergency.model.StartSosData;
import com.fame.plumbum.chataround.emergency.presenter.EmergencyPresenter;
import com.fame.plumbum.chataround.emergency.presenter.EmergencyPresenterImpl;
import com.fame.plumbum.chataround.emergency.provider.RetrofitEmergencyProvider;
import com.fame.plumbum.chataround.emergency.service.EmergencyLocationServiceNew;
import com.fame.plumbum.chataround.emergency_contacts.model.EmergencyContactData;
import com.fame.plumbum.chataround.emergency_contacts.view.EmergencyContactsActivity;
import com.fame.plumbum.chataround.helper.FButton;
import com.fame.plumbum.chataround.helper.Keys;
import com.fame.plumbum.chataround.helper.SharedPrefs;
import com.fame.plumbum.chataround.emergency.service.EmergencyLocationService;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link EmergencyFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link EmergencyFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EmergencyFragment extends Fragment implements EmergencyView{

    private static final String TAG = "EmergencyFragment";
    private FButton sosButton;
    private TextView sosText;
    private static int count = 0;
    private static boolean isStarted = false;
    private LinearLayout addContactsLayout;

    @BindView(R.id.emergency_contacts_recycler_view)
    RecyclerView recyclerView;

    @BindView(R.id.selected_contacts_text)
    TextView selectedContactsTextView;

    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    @BindView(R.id.allLayouts)
    RelativeLayout allLayouts;

    private Context context;

    private SharedPrefs sharedPrefs;

    @BindView(R.id.add_contacts_button)
    Button addContactsButton;

    private EmergencyAdapter emergencyContactsAdapter;
    private EmergencyPresenter emergencyPresenter;
    private ProgressDialog progressDialog;


    private List<EmergencyContactData> contactDataList = new ArrayList<>();


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public EmergencyFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EmergencyFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EmergencyFragment newInstance(String param1, String param2) {
        EmergencyFragment fragment = new EmergencyFragment();
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
        View view = inflater.inflate(R.layout.fragment_emergency, container, false);
        ButterKnife.bind(this, view);
        context = getContext();

        progressDialog=new ProgressDialog(context);

        sosButton = (FButton) view.findViewById(R.id.sosButton);
        sosText = (TextView) view.findViewById(R.id.sos_service_text);
        addContactsLayout = (LinearLayout) view.findViewById(R.id.add_contacts_layout);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        emergencyContactsAdapter = new EmergencyAdapter(context,this);
        recyclerView.setAdapter(emergencyContactsAdapter);
        addContactsLayout.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
        sharedPrefs = new SharedPrefs(context);
        emergencyPresenter = new EmergencyPresenterImpl(this, new RetrofitEmergencyProvider());
        // emergencyPresenter.getContacts(sharedPrefs.getUserId());

        final boolean[] red = {true};



        final Handler handler = new Handler();
        final Runnable runnable = new Runnable() {
            public void run() {
                //
                // Do the stuff
                //



                
                if (red[0]) {

                    red[0] =false;

                    sosButton.setButtonColor(ContextCompat.getColor(context,R.color.gray));



                } else {

                    red[0] =true;

                    sosButton.setButtonColor(ContextCompat.getColor(context,R.color.colorAccentRed));


                }
                handler.postDelayed(this, 500);
            }
        };


        if(sharedPrefs.isSosOngoing()){
                runnable.run();
            count=3;
            isStarted=true;
        }

        sosButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (!isStarted) {
                    count++;

                    if (count == 3) {

                        emergencyPresenter.startSos(sharedPrefs.getUserId(), 12.12, 13.13);
                        sosText.setText(getActivity().getString(R.string.disable_sos_service_text));
                    }

                } else {
                    count--;
                    if (count == 0) {
                        getActivity().stopService(new Intent(getActivity(), EmergencyLocationServiceNew.class));
//                        sosButton.setText("START SOS");
                        sosText.setText(getActivity().getString(R.string.enable_sos_service_text));
                        isStarted = false;
                        sharedPrefs.setSosOngoing(false);

                    }

                }
                if (count == 0) {

                    sosButton.setButtonColor(ContextCompat.getColor(context,R.color.gray));
                    handler.removeCallbacks(runnable);
                }

                if (count == 1) {
                    sosButton.setButtonColor(ContextCompat.getColor(context,R.color.colorAccent));
                    handler.removeCallbacks(runnable);
                }

                if (count == 2) {
                    sosButton.setButtonColor(ContextCompat.getColor(context,R.color.colorAccentRed));
                    handler.removeCallbacks(runnable);
                }

                if (count == 3) {
                    runnable.run();
                }
            }
        });



 /*       sosButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (!isStarted) {
                    count++;

                    if (count == 3) {

                        emergencyPresenter.startSos(sharedPrefs.getUserId(), 12.12, 13.13);
                        sosText.setText(getActivity().getString(R.string.enable_sos_service_text));

                    }

                } else {
                    count--;
                    if (count == 0) {
                        getActivity().stopService(new Intent(getActivity(), EmergencyLocationService.class));
//                        sosButton.setText("START SOS");
                        sosText.setText(getActivity().getString(R.string.disable_sos_service_text));
                        isStarted = false;
                    }


                }

                if (count == 0) {

                    final int sdk = android.os.Build.VERSION.SDK_INT;
                    if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                        sosButton.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.translucent_button));
                    } else {
                        sosButton.setBackground(ContextCompat.getDrawable(context, R.drawable.translucent_button));
                    }
                }


                if (count == 1) {

                    final int sdk = android.os.Build.VERSION.SDK_INT;
                    if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                        sosButton.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.yellow_button));
                    } else {
                        sosButton.setBackground(ContextCompat.getDrawable(context, R.drawable.yellow_button));
                    }
                }

                if (count == 2) {

                    final int sdk = android.os.Build.VERSION.SDK_INT;
                    if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                        sosButton.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.red_button));
                    } else {
                        sosButton.setBackground(ContextCompat.getDrawable(context, R.drawable.red_button));
                    }

                    handler.removeCallbacks(runnable);

                }

                if (count == 3) {

                    runnable.run();

                }


            }
        });*/
        addContactsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EmergencyContactsActivity emergencyContactsActivity = new EmergencyContactsActivity();

                Intent intent = new Intent(getActivity(), emergencyContactsActivity.getClass());

               /* if (!EventBus.getDefault().isRegistered(emergencyContactsActivity)) {

                    EventBus.getDefault().register(emergencyContactsActivity);

                }*/

                EventBus.getDefault().postSticky(new EmergencyContactsActivity.GetContactListEvent(contactDataList));

                startActivity(intent);


            }
        });

        /*swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        swipeRefreshLayout.setRefreshing(true);

                                        emergencyPresenter.getContacts(sharedPrefs.getUserId());
                                    }
                                }
        );*/

        emergencyPresenter.getContacts(sharedPrefs.getUserId());

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        if(!EventBus.getDefault().isRegistered(this)){

            EventBus.getDefault().register(this);
        }
    }

    @Override
    public void onStop() {
        super.onStop();

        if(EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().unregister(this);
        }

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
        /*if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }*/
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    @Override
    public void onEmergencyContactsRecieved(List<EmergencyContactData> contactsDataList) {

        this.contactDataList = contactsDataList;

        if (contactsDataList.size() == 0 || contactsDataList.isEmpty()) {
//            addContactsLayout.setVisibility(View.VISIBLE);
            selectedContactsTextView.setVisibility(View.GONE);
            recyclerView.setVisibility(View.GONE);
        } else {
//            addContactsLayout.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
        emergencyContactsAdapter.setEmergencyContactDataList(contactsDataList);
        emergencyContactsAdapter.notifyDataSetChanged();

    }

    @Override
    public void onSosStarted(StartSosData startSosData) {

        Intent intent = new Intent(getActivity(), EmergencyLocationServiceNew.class);

        intent.putExtra(Keys.KEY_SOS_ID, startSosData.getSosId());

        getActivity().startService(intent);
        sosText.setText(getActivity().getString(R.string.enable_sos_service_text));
//        sosButton.setText("STOP SOS");

        isStarted = true;

        sharedPrefs.setSosOngoing(true);


    }

    @Override
    public void onContactDeleted() {
        emergencyPresenter.getContacts(sharedPrefs.getUserId());
    }

    @Override
    public void showLoader(boolean show) {
        if(show){
            allLayouts.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
        }else{
            allLayouts.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);

        }
    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();


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
    public void changeDialogMessage(String title,String message) {

        progressDialog.setTitle(title);
        progressDialog.setMessage(message);


    }

    @Override
    public void onEmergencyContactsUpdated() {

    }



    public void deleteContact(String mobile) {

        Log.d(TAG,"Mobile"+mobile);
        emergencyPresenter.deleteContact(sharedPrefs.getUserId(),mobile);

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


    @Subscribe(sticky = true,threadMode = ThreadMode.MAIN)
    public void onContactsUpdatedEvent(Contact contact) {


        /* Do something */

        emergencyPresenter.getContacts(sharedPrefs.getUserId());

    }


}
