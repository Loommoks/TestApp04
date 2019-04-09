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

    public void getCategory(int id, ICategoryAcceptor categoryAcceptor) {
        mSetupCategoryMap.put(id,categoryAcceptor);
        mUpdateCategoryDataMap.put(id,categoryAcceptor);
        startFetch(id);
    }

    private void startFetch(int id) {
        Category category = mCache.get(id);
        if(category != null) {
            dispatchCategory(category);
            if(!category.isEmpty()) {
                dispatchCategoryData(category);
                return;
            }
        }
        new CategoryDatabaseFetchTask().execute(id);
    }

    private void onCategoryFetchedFromDatabase(Category category) {
        mCache.update(category);
        dispatchCategory(category);
        if(!category.isEmpty()) {
            if(!isExpired(category)) {
                dispatchCategoryData(category);
                return;
            }
        }
        new CategoryDataWebFetchTask(category).execute();
    }

    private void onCategoryFetchedFromWeb(Category category) {
        mCache.update(category);
        mDatabase.update(category);
        dispatchCategoryData(category);
        return;
    }

    private void dispatchCategory(Category category) {
        ICategoryAcceptor acceptor = mSetupCategoryMap.get(category.getId());
        if(acceptor != null) {
            acceptor.initializeCategory(category);
            mSetupCategoryMap.remove(category.getId());
        }
    }

    private void dispatchCategoryData(Category category) {
        ICategoryAcceptor acceptor = mUpdateCategoryDataMap.get(category.getId());
        if(acceptor != null) {
            acceptor.updateCategoryData(category);
            mUpdateCategoryDataMap.remove(category.getId());
        }
    }

    private boolean isExpired(Category category) {
        Date currentDate = new Date();
        Date uploadDate = category.getUploadDate();
        long difference = currentDate.getTime() - uploadDate.getTime();
        return (difference > Config.Settings.DATABASE_DATA_TTL);
    }

    private class CategoryDatabaseFetchTask extends AsyncTask<Integer, Void, Integer> {
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

    private class CategoryDataWebFetchTask extends AsyncTask<Void, Void, Void> {
        Category mCategory;

        public CategoryDataWebFetchTask(Category category) {
            mCategory = category;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            if(mCategory.hasSubCategories()) {
                ArrayList<Category> subCategories =
                        mApiClient.fetchSubCategories(mCategory.getId());
                mCategory.setSubCategoriesList(subCategories);
                for (Category subCategory :
                        subCategories) {
                    mCache.add(subCategory);
                    mDatabase.add(subCategory);
                }
            } else {
                ArrayList<Offer> offers =
                        mApiClient.fetchOffers(mCategory.getId());
                mCategory.setOfferList(offers);
                mDatabase.update(mCategory);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void v) {
            onCategoryFetchedFromWeb(mCategory);
        }
    }

}
