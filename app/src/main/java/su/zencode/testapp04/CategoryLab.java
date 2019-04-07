package su.zencode.testapp04;

import android.content.Context;
import android.os.AsyncTask;

import java.util.ArrayList;
import java.util.HashMap;

import su.zencode.testapp04.EaptekaRepositories.CacheRepository;
import su.zencode.testapp04.EaptekaRepositories.Category;
import su.zencode.testapp04.EaptekaRepositories.DatabaseRepository;
import su.zencode.testapp04.EaptekaRepositories.IEaptekaCategoryRepository;
import su.zencode.testapp04.EaptekaRepositories.Offer;
import su.zencode.testapp04.TestAppApiClient.EaptekaApiClient;
import su.zencode.testapp04.TestAppApiClient.IEaptekaApiClient;

public class CategoryLab {
    private static CategoryLab sCategoryLab;
    private IEaptekaCategoryRepository mCache;
    private IEaptekaCategoryRepository mDatabase;
    private IEaptekaApiClient mApiClient;
    private HashMap<Integer, UpdatableCategoryFragment> mSetupCategoryMap;
    private HashMap<Integer, UpdatableCategoryFragment> mUpdateCategoryDataMap;

    public static CategoryLab getInstance(Context context) {
        if(sCategoryLab == null){
            sCategoryLab = new CategoryLab(context);
        }
        return sCategoryLab;
    }

    private CategoryLab(Context context) {
        mCache = CacheRepository.getInstance();
        mDatabase = DatabaseRepository.getInstance(context.getApplicationContext());
        mApiClient = new EaptekaApiClient();
        mSetupCategoryMap = new HashMap<>();
        mUpdateCategoryDataMap = new HashMap<>();
    }

    public void getCategory(int id, UpdatableCategoryFragment updatableFragment) {
        mSetupCategoryMap.put(id,updatableFragment);
        mUpdateCategoryDataMap.put(id,updatableFragment);
        startFetch(id);
    }

    private void startFetch(int id) {
        Category category = mCache.get(id);
        if(category != null) {
            setupCategory(category);
            if(hasActualData(category)) {
                setupCategoryData(category);
                return;
            }
        }
        new FetchDatabaseCategoryTask().execute(id);
    }

    private void onCategoryFetchedFromDatabase(Category category) {
        mCache.update(category);
        setupCategory(category);
        if(hasActualData(category)) {
            setupCategoryData(category);
            return;
        }
        new FetchCategoryDataTask(category).execute();
    }

    private void onCategoryFetchedFromWeb(Category category) {
        mCache.update(category);
        mDatabase.update(category);
        setupCategoryData(category);
        return;
    }

    private void setupCategory(Category category) {
        UpdatableCategoryFragment fragment = mSetupCategoryMap.get(category.getId());
        if(fragment != null) {
            fragment.setupCategory(category);
            mSetupCategoryMap.remove(category.getId());
        }
    }

    private boolean hasActualData(Category category) {
        if(category.hasSubCategories())
            return (category.getSubCategoriesList() != null);
        return (category.getOfferList() != null);
    }

    private void setupCategoryData(Category category) {
        UpdatableCategoryFragment fragment = mUpdateCategoryDataMap.get(category.getId());
        if(fragment != null) {
            fragment.updateCategoryData(category);
            mUpdateCategoryDataMap.remove(category.getId());
        }
    }

    private class FetchDatabaseCategoryTask extends AsyncTask<Integer, Void, Integer> {
        Category mCategory;

        @Override
        protected Integer doInBackground(Integer... values) {
            mCategory = mDatabase.get(values[0]);
            return values[0];
        }

        @Override
        protected void onPostExecute(Integer id) {
            onCategoryFetchedFromDatabase(mCategory);

        }
    }

    private class FetchCategoryDataTask extends AsyncTask<Void, Void, Void> {
        Category mCategory;

        public FetchCategoryDataTask(Category category) {
            mCategory = category;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            IEaptekaApiClient apiClient = new EaptekaApiClient();
            if(mCategory.hasSubCategories()) {
                ArrayList<Category> subCategories =
                        apiClient.fetchSubCategories(mCategory.getId());
                mCategory.setSubCategoriesList(subCategories);
            } else {
                ArrayList<Offer> offers =
                        apiClient.fetchOffers(mCategory.getId());
                mCategory.setOfferList(offers);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void v) {
            onCategoryFetchedFromWeb(mCategory);
        }
    }

}
