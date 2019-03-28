package su.zencode.testapp04;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

public class OffersListActivity extends AppCompatActivity {
    private static final String TAG = "OffersListActivity";
    private static final String EXTRA_CATEGORY_ID = "su.zencode.testapp04.category_id";


    public static Intent newIntent(Context packageContext, int categoryId) {
        Intent intent = new Intent(packageContext, OffersListActivity.class);
        intent.putExtra(EXTRA_CATEGORY_ID, categoryId);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offers_list);

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
}
