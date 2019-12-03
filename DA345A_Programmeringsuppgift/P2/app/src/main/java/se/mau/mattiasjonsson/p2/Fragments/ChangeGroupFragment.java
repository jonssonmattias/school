package se.mau.mattiasjonsson.p2.Fragments;


import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import se.mau.mattiasjonsson.p2.Controller;
import se.mau.mattiasjonsson.p2.JSONConverter;
import se.mau.mattiasjonsson.p2.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChangeGroupFragment extends DialogFragment {
    private Button btnChooseGroup;
    private ListView groupList;
    private Controller controller;
    private ArrayList<String> groups;
    private HashMap<String, ArrayList<String>> members = new HashMap<>();
    private JSONConverter jsonConverter;
    private String selectedGroup, currentGroup, username;
    private MapFragment mapFragment;
    private SharedPreferences.Editor sharedPreferencesEditor;

    public ChangeGroupFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_change_group, container, false);
        btnChooseGroup = view.findViewById(R.id.btnChooseGroup);
        groupList = view.findViewById(R.id.chooseGroupList);
        btnChooseGroup.setOnClickListener(new ButtonListener());
        groupList.setOnItemClickListener(new ListListener());
        jsonConverter = new JSONConverter();
        sharedPreferencesEditor = getActivity().getSharedPreferences("P2", Activity.MODE_PRIVATE).edit();
        controller.send(jsonConverter.fetchRegisteredGroups());
        return view;
    }

    public void setController(Controller controller) {
        this.controller = controller;
        controller.setFragment(this);
    }

    public void setGroupList(ArrayList<String> arrayList){
        groups=arrayList;
        groupList.setAdapter(new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, groups));
        for(String group : groups)
            controller.send(jsonConverter.getAllMembers(group));
    }

    public void setCurrentGroup(String currentGroup) {
        this.currentGroup = currentGroup;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setMapFragment(MapFragment mapFragment) {
        this.mapFragment = mapFragment;
    }

    public void setMemberList(String group, ArrayList<String> member) {
        members.put(group, member);
    }

    private class ListListener implements ListView.OnItemClickListener{
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            for(int i=0;i<groups.size();i++)
                groupList.getChildAt(i).setBackgroundColor(Color.WHITE);
            selectedGroup = ((TextView) view).getText().toString();
            groupList.getChildAt(position).setBackgroundColor(Color.GRAY);
        }
    }

    private class ButtonListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            if(selectedGroup!=null)
                if(!selectedGroup.equals(currentGroup))
                    if (!members.get(selectedGroup).contains(username)) {
                        controller.send(jsonConverter.registerUser(username, selectedGroup));
                        mapFragment.setGroupName(selectedGroup);
                    }
                    else {
                        sharedPreferencesEditor.putString("groupName", selectedGroup);
                        mapFragment.setGroupName(selectedGroup);
                    }
            Log.d("ChangedGroup", selectedGroup+", "+currentGroup);
            controller.setFragment(mapFragment);
            dismiss();
        }
    }

}
