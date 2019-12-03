package se.mau.mattiasjonsson.p1.fragments;


import android.app.Activity;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import se.mau.mattiasjonsson.p1.MainViewModel;
import se.mau.mattiasjonsson.p1.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class IncomeFragment extends Fragment {
    private MainViewModel mainViewModel;
    private String[] category = {"Salary", "Other"};
    private EditText etIncomeName, etIncomeAmount, etIncomeDate;
    private Button btnAddIncome;
    private Spinner spIncomeCategory;

    public IncomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_income, container, false);
        mainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        initializeComponents(view);
        btnAddIncome.setOnClickListener(new ButtonListener());
        return view;
    }
    private void initializeComponents(View view){
        etIncomeName = view.findViewById(R.id.etIncomeName);
        etIncomeAmount = view.findViewById(R.id.etIncomeAmount);
        etIncomeDate = view.findViewById(R.id.etIncomeDate);
        btnAddIncome = view.findViewById(R.id.btnAddIncome);
        spIncomeCategory = view.findViewById(R.id.spIncomeCategory);
        spIncomeCategory.setAdapter(new ArrayAdapter(getActivity(), R.layout.spinner_row, category));
    }

    private class ButtonListener implements View.OnClickListener {

        public void onClick(View v) {
            String name = etIncomeName.getText().toString(), amount = etIncomeAmount.getText().toString(),category = spIncomeCategory.getSelectedItem().toString(), date = etIncomeDate.getText().toString(), username = mainViewModel.getName(getActivity().getSharedPreferences("P1", Activity.MODE_PRIVATE));
            mainViewModel.add(name, amount, date, category, 0, username);
        }

    }
}
