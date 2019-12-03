package se.mau.mattiasjonsson.p1.activities;

import android.support.v4.app.*;
import android.support.v7.app.*;
import android.os.Bundle;
import android.util.Log;

import se.mau.mattiasjonsson.p1.R;
import se.mau.mattiasjonsson.p1.fragments.LoginFragment;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setFragment(new LoginFragment(), false);
        Log.d("db",getDatabasePath("budget.db").getPath());
    }
    public void setFragment (Fragment fragment, boolean backstack) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.loginFragment, fragment);
        if(backstack)
            fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}
