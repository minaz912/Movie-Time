package com.movietime.minaz;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity implements DetailActivityFragment.Callback {

    private static final String DETAILFRAGMENT_TAG = "DFTAG";
    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (findViewById(R.id.detail_fragment_container) != null) {
            mTwoPane = true;
            if (savedInstanceState == null) {
                DetailActivityFragment fragment = new DetailActivityFragment();
                Bundle args = new Bundle();
                args.putInt("movie_id", 0);
                fragment.setArguments(args);
                getSupportFragmentManager().beginTransaction().
                        replace(R.id.detail_fragment_container, fragment, DETAILFRAGMENT_TAG)
                        .commit();
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent goToSettings = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(goToSettings);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public void onItemSelected(Bundle bundle) {
        if (mTwoPane) {
            // In two-pane mode, show the detail view in this activity by
            // adding or replacing the detail fragment using a
            // fragment transaction.
            Bundle args = bundle;

            DetailActivityFragment fragment = new DetailActivityFragment();
            fragment.setArguments(args);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.detail_fragment_container, fragment, DETAILFRAGMENT_TAG)
                    .commit();
        } else {
            Intent startDetailActivityIntent = new Intent(this, DetailActivity.class);
            startDetailActivityIntent.putExtras(bundle);
            startActivity(startDetailActivityIntent);
        }
    }
}
