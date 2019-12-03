package se.mau.mattiasjonsson.p2.Fragments;


import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

import se.mau.mattiasjonsson.p2.Controller;
import se.mau.mattiasjonsson.p2.JSONConverter;
import se.mau.mattiasjonsson.p2.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment {
    private Spinner groupOptionSpinner;
    private LinearLayout createGroupLayout;
    private ListView groupList;
    private EditText etUsername, etGroupName;
    private Button btnLogin;
    private String[] options = {"Join group", "Create group"};
    private ArrayList<String> groupArrayList = new ArrayList<>();
    private boolean registerGroup;
    private String selectedGroup;

    private Controller controller;
    private JSONConverter jsonConverter;


    public LoginFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        jsonConverter = new JSONConverter();

        controller = new Controller(getActivity(), this);
        controller.connect();
        controller.send(jsonConverter.fetchRegisteredGroups());
        initComp(view);
        listenerAndAdapter();
        return view;
    }

    private void initComp(View view) {
        groupOptionSpinner = view.findViewById(R.id.groupOptionSpinner);
        createGroupLayout = view.findViewById(R.id.createGroupLayout);
        groupList = view.findViewById(R.id.groupList);
        etUsername = view.findViewById(R.id.etUsername);
        etGroupName = view.findViewById(R.id.etGroupName);
        btnLogin = view.findViewById(R.id.btnLogin);
    }
    public void listenerAndAdapter(){
        groupList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                for(int i=0;i<groupArrayList.size();i++)
                    groupList.getChildAt(i).setBackgroundColor(Color.WHITE);
                selectedGroup = ((TextView) view).getText().toString();
                groupList.getChildAt(position).setBackgroundColor(Color.GRAY);
            }
        });
        groupOptionSpinner.setAdapter(new ArrayAdapter(getActivity(), R.layout.spinner_row, options));
        groupOptionSpinner.setOnItemSelectedListener(new AdapterListener());
        btnLogin.setOnClickListener(new ButtonListener());
    }


    public void createNewGroup(){
        createGroupLayout.setVisibility(View.VISIBLE);
        groupList.setVisibility(View.GONE);
        registerGroup = true;
    }

    public void chooseGroup(){
        createGroupLayout.setVisibility(View.GONE);
        groupList.setVisibility(View.VISIBLE);
        registerGroup = false;
    }

    private void login(){
        MapFragment mapFragment = new MapFragment();
        mapFragment.setController(controller);
        mapFragment.setLoginFragment(this);
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.mainFragment, mapFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    private void setSharedPreferences(String name, String group){
        SharedPreferences.Editor editor = getActivity().getSharedPreferences("P2", Activity.MODE_PRIVATE).edit();
        editor.putString("name", name);
        editor.putString("groupName", group);
        editor.apply();
    }

    public void setGroupList(ArrayList<String> arrayList){
        groupArrayList=arrayList;
        groupList.setAdapter(new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, groupArrayList));
    }

    private class AdapterListener implements Spinner.OnItemSelectedListener{
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            switch (parent.getId()) {
                case R.id.groupOptionSpinner:
                    if (position < 1) chooseGroup();
                    else createNewGroup();
                    break;
            }
        }
        @Override
        public void onNothingSelected(AdapterView<?> parent) {}
    }

    private class ButtonListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            String name = etUsername.getText().toString();
            String groupName;
            if (registerGroup) {
                groupName = etGroupName.getText().toString();
            } else {
                groupName = selectedGroup;
            }
            Log.d("test", "username: " + name + "\nGroup name: " + groupName);
            setSharedPreferences(name, groupName);
            login();
        }
    }

}
