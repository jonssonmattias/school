package se.mau.mattiasjonsson.p1.fragments;


import android.app.Activity;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import se.mau.mattiasjonsson.p1.MainViewModel;
import se.mau.mattiasjonsson.p1.R;
import se.mau.mattiasjonsson.p1.activities.MainActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment {
    private Button btnLogin;
    private EditText etFirstname, etLastname;
    private MainViewModel mainViewModel;
    private SharedPreferences sharedPreferences;

    public LoginFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        mainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        sharedPreferences = getActivity().getSharedPreferences("P1", Activity.MODE_PRIVATE);
        initializeComponents(view);
        btnLogin.setOnClickListener(new ButtonListener());
        return view;
    }

    private void initializeComponents(View view){
        etFirstname = view.findViewById(R.id.etLoginFirstname);
        etLastname = view.findViewById(R.id.etLoginLastname);
        btnLogin = view.findViewById(R.id.btnLogin);
        mainViewModel.getInitialName(etFirstname, etLastname, sharedPreferences);
    }

    private class ButtonListener implements View.OnClickListener {
        public void onClick(View v) {
            mainViewModel.setName( etFirstname.getText().toString(), etLastname.getText().toString(),sharedPreferences.edit());
            Intent intent = new Intent(getActivity(), MainActivity.class);
            startActivity(intent);
        }
    }
}
