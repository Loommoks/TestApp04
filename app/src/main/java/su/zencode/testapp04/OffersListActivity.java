package su.zencode.testapp04;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ProgressBar;

public class OffersListActivity extends AppCompatActivity implements EaptekaProgressBarableActivity{
    private static final String TAG = "OffersListActivity";
    private static final String EXTRA_CATEGORY_ID = "su.zencode.testapp04.category_id";
    private ProgressBar mProgressBar;

    public static Intent newIntent(Context packageContext, int categoryId) {
        Intent intent = new Intent(packageContext, OffersListActivity.class);
        intent.putExtra(EXTRA_CATEGORY_ID, categoryId);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offers_list);

        mProgressBar = findViewById(R.id.launch_progress_bar);

        int categoryId = getIntent().getExtras().getInt(EXTRA_CATEGORY_ID);
        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.offers_activity_fragment_container);

        if(fragment == null) {
            fragment = OffersListFragment.newInstance(categoryId);
            fm.beginTransaction()
                    .add(R.id.offers_activity_fragment_container, fragment)
                    .commit();
        }
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
