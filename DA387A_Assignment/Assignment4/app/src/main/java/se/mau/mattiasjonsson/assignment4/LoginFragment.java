package se.mau.mattiasjonsson.assignment4;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment {

    private Button btnLogin, btnRegister;
    private EditText etUsername, etPassword;
    private TextView tvFail;
    private DatabaseController databaseController;

    public LoginFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        initComp(view);
        databaseController = new DatabaseController(getActivity().getApplication());
        return view;
    }

    private void initComp(View view){
        btnLogin=view.findViewById(R.id.btnLogin);
        btnRegister=view.findViewById(R.id.btnRegister);
        etPassword=view.findViewById(R.id.etPassword);
        etUsername=view.findViewById(R.id.etUsername);
        tvFail=view.findViewById(R.id.tvFail);
        btnLogin.setOnClickListener(new ButtonListener());
        btnRegister.setOnClickListener(new ButtonListener());
    }

    private void loginSuccess(String username){
        StepFragment stepFragment = new StepFragment();
        stepFragment.setUsername(username);
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.mainFragment, stepFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    private void login(String username, String password){
        if(databaseController.login(username, password)){
            loginSuccess(username);
        }
        else tvFail.setText("Login failed");
    }

    private void register(String username, String password){
        if(databaseController.registerUser(username, password)){
            loginSuccess(username);
        }
        else tvFail.setText("Register failed");
    }

    private class ButtonListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            if(v.getId()==R.id.btnLogin) {
                String username = etUsername.getText().toString(), password = etPassword.getText().toString();
                Log.d("Username", username);
                Log.d("Password", password);
                login(username, password);
            }
            if(v.getId()==R.id.btnRegister){
                String username = etUsername.getText().toString(), password = etPassword.getText().toString();
                Log.d("Username", username);
                Log.d("Password", password);
                register(username, password);
            }
        }
    }
}
