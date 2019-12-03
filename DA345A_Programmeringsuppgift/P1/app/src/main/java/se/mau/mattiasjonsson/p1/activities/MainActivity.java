package se.mau.mattiasjonsson.p1.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import se.mau.mattiasjonsson.p1.R;
import se.mau.mattiasjonsson.p1.fragments.MainFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.mainFragment, MainFragment.newInstance())
                    .commitNow();
        }
    }
}
