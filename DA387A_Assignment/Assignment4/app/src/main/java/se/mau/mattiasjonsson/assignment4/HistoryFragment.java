package se.mau.mattiasjonsson.assignment4;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class HistoryFragment extends Fragment {

    private ListView lvHistory;
    private Button btnClear;
    private ArrayList<String> stepHistory;
    private DatabaseController databaseController;
    private String username;

    public HistoryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_history, container, false);
        setList(view);
        btnClear = view.findViewById(R.id.btnClear);
        btnClear.setOnClickListener(new ButtonListener());
        databaseController = new DatabaseController(getActivity().getApplication());
        return view;
    }

    public void setList(View view) {
        lvHistory = view.findViewById(R.id.lvHistory);
        lvHistory.setAdapter(new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, stepHistory ));
    }

    public void setStepHistory(ArrayList<String> stepHistory) {
        this.stepHistory = stepHistory;
    }

    public void setUsername(String username){
        this.username=username;
    }

    private class ButtonListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            databaseController.clearHistory(username);
            lvHistory.setAdapter(null);
        }
    }
}
