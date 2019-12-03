package se.mau.mattiasjonsson.p2.Activities;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import se.mau.mattiasjonsson.p2.Fragments.LoginFragment;
import se.mau.mattiasjonsson.p2.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.mainFragment, new LoginFragment())
                    .commitNow();
        }
    }
}
