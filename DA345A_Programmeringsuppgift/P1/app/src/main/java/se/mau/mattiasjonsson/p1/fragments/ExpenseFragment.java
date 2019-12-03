package se.mau.mattiasjonsson.p1.fragments;


import android.app.Activity;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
public class ExpenseFragment extends Fragment {
    private MainViewModel mainViewModel;
    private String[] category = {"Food", "Leisure", "Travel", "Accommodation", "Other"};
    private EditText etExpenseName, etExpenseAmount, etExpenseDate;
    private Button btnAdtExpense;
    private Spinner stExpenseCategory;

    public ExpenseFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_expense, container, false);
        mainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        initializeComponents(view);
        btnAdtExpense.setOnClickListener(new ButtonListener());
        return view;
    }
    private void initializeComponents(View view){
        etExpenseName = view.findViewById(R.id.etExpenseName);
        etExpenseAmount = view.findViewById(R.id.etExpenseAmount);
        etExpenseDate = view.findViewById(R.id.etExpenseDate);
        btnAdtExpense = view.findViewById(R.id.btnAddExpense);
        stExpenseCategory = view.findViewById(R.id.spExpenseCategory);
        stExpenseCategory.setAdapter(new ArrayAdapter(getActivity(), R.layout.spinner_row, category));
    }

    private class ButtonListener implements View.OnClickListener {
        public void onClick(View v) {
            String name = etExpenseName.getText().toString(), amount = "-"+etExpenseAmount.getText().toString(),category = stExpenseCategory.getSelectedItem().toString(), date = etExpenseDate.getText().toString(), username = getActivity().getSharedPreferences("P1", Activity.MODE_PRIVATE).getString("firstname",null)+" "+getActivity().getSharedPreferences("P1", Activity.MODE_PRIVATE).getString("lastname",null);
            mainViewModel.add(name, amount, date, category, 1, username);
        }

    }
}
