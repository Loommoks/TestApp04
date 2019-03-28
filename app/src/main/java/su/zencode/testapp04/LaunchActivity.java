package su.zencode.testapp04;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class LaunchActivity extends AppCompatActivity {
    private static final String TAG = "LaunchActivity22";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);

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

}
