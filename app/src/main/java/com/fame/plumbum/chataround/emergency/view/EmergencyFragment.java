package com.fame.plumbum.chataround.emergency.view;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.fame.plumbum.chataround.LocationService;
import com.fame.plumbum.chataround.R;
import com.fame.plumbum.chataround.emergency.model.data.EmergencyContactsFeed;
import com.fame.plumbum.chataround.emergency.presenter.EmergencyContactsPresenter;
import com.fame.plumbum.chataround.emergency.presenter.EmergencyContactsPresenterImpl;
import com.fame.plumbum.chataround.emergency.presenter.UpdateContactsPresenter;
import com.fame.plumbum.chataround.helper.SharedPrefs;

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
public class EmergencyFragment extends Fragment implements EmergencyContactsView {
    private Button sosButton;
    private TextView sosText;
    private static int count=0;
    private static boolean isStarted=false;
    private LinearLayout addContactsLayout;
    @BindView(R.id.emergency_contacts_recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.selected_contacts_text)
    TextView selectedContactsTextView;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    private Context context;
    private SharedPrefs sharedPrefs;
    @BindView(R.id.add_contacts_button)
    Button addContactsButton;
    private EmergencyContactsAdapter emergencyContactsAdapter;
    private EmergencyContactsPresenter emergencyContactsPresenter;
    private UpdateContactsPresenter updateContactsPresenter;


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
        View view=inflater.inflate(R.layout.fragment_emergency, container, false);
        ButterKnife.bind(this, view);
        sosButton=(Button)view.findViewById(R.id.sosButton);
        sosText=(TextView)view.findViewById(R.id.sos_service_text);
        addContactsLayout=(LinearLayout)view.findViewById(R.id.add_contacts_layout);
        context = getContext();
        LinearLayoutManager linearLayoutManager= new LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(linearLayoutManager);
        emergencyContactsAdapter=new EmergencyContactsAdapter(context,null);
        recyclerView.setAdapter(emergencyContactsAdapter);
        addContactsLayout.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
        sharedPrefs = new SharedPrefs(context);
        emergencyContactsPresenter= new EmergencyContactsPresenterImpl(this);
       // emergencyContactsPresenter.getContacts(sharedPrefs.getUserId());
        sosButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isStarted){
                    count++;
                    if(count==3) {
                        sosText.setText(getActivity().getString(R.string.enable_sos_service_text));
                        sosButton.setText("STOP SOS");
                        getActivity().startService(new Intent(getActivity(), LocationService.class));
                        isStarted = true;
                    }

                }
                else
                {
                    count--;
                    if(count==0){
                        getActivity().stopService(new Intent(getActivity(),LocationService.class));
                        sosButton.setText("START SOS");
                        sosText.setText(getActivity().getString(R.string.disable_sos_service_text));
                        isStarted=false;
                    }


                }

            }
        });
        addContactsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(getActivity(),EmergencyContactsActivity.class);
                startActivity(intent);
            }
        });
        return view;
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
    public void showLoader(boolean show) {
        if (show) {
            recyclerView.setVisibility(View.GONE);
            progressBar.setVisibility(View.GONE);

        } else {
            progressBar.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);

        }

    }

    @Override
    public void onEmergencyContactsRecieved(List<EmergencyContactsFeed> contactsDataList) {
        if(contactsDataList.size()==0||contactsDataList.isEmpty()){
            addContactsLayout.setVisibility(View.VISIBLE);
            selectedContactsTextView.setVisibility(View.GONE);
            recyclerView.setVisibility(View.GONE);
        }
        else
        {
            addContactsLayout.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
        emergencyContactsAdapter.setEmergencyContactsFeedList(contactsDataList);
        emergencyContactsAdapter.notifyDataSetChanged();

    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();


    }

    @Override
    public void updateContacts(EmergencyContactsFeed emergencyContactsFeed) {

    }

    protected void requestContactsUpdate(String userId,EmergencyContactsFeed emergencyContactsFeed,boolean isChecked,int position)
    {

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
