package su.zencode.testapp04;

import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;

import su.zencode.testapp04.EaptekaRepositories.CategoriesRepository;
import su.zencode.testapp04.EaptekaRepositories.Category;

public class LaunchActivity extends AppCompatActivity implements CategoryHostActivity{
    private static final String TAG = "LaunchActivity22";
    //private CategoriesRepository mCategoriesRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);

        if(fragment == null) {
            fragment = createFragment(0);
            fm.beginTransaction()
                    .add(R.id.fragment_container, fragment)
                    .commit();
        }

        /**
        AsyncTask<Integer,Void,Integer> fetchSubCategoriesTask = new AsyncTask<Integer, Void, Integer>() {
            @Override
            protected Integer doInBackground(Integer... values) {
                Category category = mCategoriesRepository.getCategory(values[0]);
                if(category.getSubCategoriesList() == null)
                    new EaptekaFetcher().fetchSubCategories(values[0]);

                return values[0];
            }

            @Override
            protected void onPostExecute(Integer id) {
                showSubCategories(id);
                ArrayList<Category> subCategories = mCategoriesRepository.getCategory(id).getSubCategoriesList();
                Log.d(TAG, subCategories.toString());
            }
        };
        AsyncTask<Integer,Void,Integer> fetchOffersTask = new AsyncTask<Integer, Void, Integer>() {
            @Override
            protected Integer doInBackground(Integer... values) {
                Category category = mCategoriesRepository.getCategory(values[0]);
                if(category.getOfferList() == null)
                    new EaptekaFetcher().fetchOffers(values[0]);
                return values[0];
            }

            @Override
            protected void onPostExecute(Integer id) {
                Log.d(TAG, mCategoriesRepository.getCategory(id).getOfferList().toString());
            }
        };
        */
        //Category baseCategory = new Category(0, "base", true);
        //CategoriesRepository.getIstance().putCategory(baseCategory);
        //fetchSubCategoriesTask.execute(0);
        //fetchOffersTask.execute(7583);
    }

    private Fragment createFragment(int id) {
        return CategoriesListFragment.newInstance(id);
    }

    @Override
    public void replaceCategoryList(int id) {



        /**
        //FragmentManager fm = getSupportFragmentManager();
        Fragment newFragment = createFragment(id);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        //Fragment newFragment = fm.findFragmentById(R.id.fragment_container);
        transaction.replace(R.id.fragment_container, newFragment);
        transaction.addToBackStack(null);
        transaction.commit();*/
    }
}
