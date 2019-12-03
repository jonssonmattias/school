package se.mau.mattiasjonsson.p1.fragments;

import android.app.Activity;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import se.mau.mattiasjonsson.p1.HeaderTextView;
import se.mau.mattiasjonsson.p1.MainViewModel;
import se.mau.mattiasjonsson.p1.R;
import se.mau.mattiasjonsson.p1.activities.CameraActivity;

public class MainFragment extends Fragment {

    private MainViewModel mainViewModel;
    private String name;
    private HeaderTextView tvName;
    private Button btnIncome, btnExpense, btnBudget, btnCamera;

    public static MainFragment newInstance() {
        return new MainFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mainViewModel = ViewModelProviders.of(getActivity()).get(MainViewModel.class);
        name = mainViewModel.getName(getActivity().getSharedPreferences("P1", Activity.MODE_PRIVATE));
        initializeComponents(getView());
        setListeners();
    }

    private void setListeners(){
        btnIncome.setOnClickListener(new ButtonListener(0));
        btnExpense.setOnClickListener(new ButtonListener(1));
        btnBudget.setOnClickListener(new ButtonListener(2));
        btnCamera.setOnClickListener(new ButtonListener(3));
    }

    private void initializeComponents(View view){
        tvName = view.findViewById(R.id.tvWelcome);
        btnIncome = view.findViewById(R.id.btnIncome);
        btnExpense = view.findViewById(R.id.btnExpense);
        btnBudget = view.findViewById(R.id.btnBudget);
        btnCamera = view.findViewById(R.id.btnCamera);
        tvName.setText("Welcome "+name);
    }

    private class ButtonListener implements View.OnClickListener {
        private int i;
        public ButtonListener(int i){
            this.i=i;
        }
        public void onClick(View v) {
            if (i==0)
                setFragment(new IncomeFragment(),true);
            else if(i==1)
                setFragment(new ExpenseFragment(), true);
            else if(i==2)
                setFragment(new ViewFragment(), true);
            else if(i==3){
                startActivity(new Intent(getContext(), CameraActivity.class));
            }

        }

        public void setFragment (Fragment fragment, boolean backstack) {
            FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.mainFragment, fragment);
            if(backstack)
                fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }
    }

}
