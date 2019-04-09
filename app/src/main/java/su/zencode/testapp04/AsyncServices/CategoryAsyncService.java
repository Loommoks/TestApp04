package su.zencode.testapp04.AsyncServices;

import android.content.Context;
import android.os.AsyncTask;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import su.zencode.testapp04.EaptekaRepositories.CacheRepository;
import su.zencode.testapp04.EaptekaRepositories.Category;
import su.zencode.testapp04.Config;
import su.zencode.testapp04.EaptekaRepositories.DatabaseRepository;
import su.zencode.testapp04.EaptekaRepositories.IEaptekaCategoryRepository;
import su.zencode.testapp04.EaptekaRepositories.Offer;
import su.zencode.testapp04.TestAppApiClient.EaptekaApiClient;
import su.zencode.testapp04.TestAppApiClient.IEaptekaApiClient;

public class CategoryAsyncService {
    private static CategoryAsyncService sCategoryAsyncService;
    private IEaptekaCategoryRepository mCache;
    private IEaptekaCategoryRepository mDatabase;
    private IEaptekaApiClient mApiClient;
    private HashMap<Integer, ICategoryAcceptor> mSetupCategoryMap;
    private HashMap<Integer, ICategoryAcceptor> mUpdateCategoryDataMap;

    public static CategoryAsyncService getInstance(Context context) {
        if(sCategoryAsyncService == null){
            sCategoryAsyncService = new CategoryAsyncService(context);
        }
        return sCategoryAsyncService;
    }

    private CategoryAsyncService(Context context) {
        mCache = CacheRepository.getInstance();
        mDatabase = DatabaseRepository.getInstance(context.getApplicationContext());
        mApiClient = new EaptekaApiClient();
        mSetupCategoryMap = new HashMap<>();
        mUpdateCategoryDataMap = new HashMap<>();
    }

    public void getCategory(int id, ICategoryAcceptor updatableFragment) {
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
            if(hasActualDate(category)) {
                setupCategoryData(category);
                return;
            }
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
        ICategoryAcceptor fragment = mSetupCategoryMap.get(category.getId());
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

    private boolean hasActualDate(Category category) {
        Date currentDate = new Date();
        Date uploadDate = category.getUploadDate();
        long difference = currentDate.getTime() - uploadDate.getTime();
        long diffDays = difference/(Config.Settings.DATABASE_DATA_TTL);
        return (diffDays < 1);
    }

    private void setupCategoryData(Category category) {
        ICategoryAcceptor fragment = mUpdateCategoryDataMap.get(category.getId());
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
                for (Category subCategory :
                        subCategories) {
                    mDatabase.add(subCategory);
                    mCache.add(subCategory);
                }
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
