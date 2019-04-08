package su.zencode.testapp04;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

public class LaunchActivity extends AppCompatActivity implements IProgressBarableActivity {
    private static final String TAG = "LaunchActivity22";
    private ProgressBar mProgressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);

        mProgressBar = findViewById(R.id.launch_progress_bar);

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.launch_activity_fragment_container);

        if(fragment == null) {
            fragment = createFragment(0);
            fm.beginTransaction()
                    .add(R.id.launch_activity_fragment_container, fragment)
                    .commit();
        }
    }

    private Fragment createFragment(int id) {
        return CategoriesListFragment.newInstance(id);
    }

    @Override
    public void showProgressBar() {
        mProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgressBar() {
        mProgressBar.setVisibility(View.INVISIBLE);
    }
}
