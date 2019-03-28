package su.zencode.testapp04.EaptekaRepositories;

import java.util.concurrent.ConcurrentHashMap;

import su.zencode.testapp04.EaptekaCategoryRepository;

public class CategoriesRepository implements EaptekaCategoryRepository {
    private static CategoriesRepository sCategoriesRepository;
    private static final String TAG = ".CategoriesRepository";

    private ConcurrentHashMap<Integer, Category> mCategories;

    public static CategoriesRepository getInstance() {
        if(sCategoriesRepository == null) {
            sCategoriesRepository = new CategoriesRepository();
        }

        return sCategoriesRepository;
    }

    private CategoriesRepository() {
        mCategories = new ConcurrentHashMap<>();
    }

    private void putCategory(Category category) {
        mCategories.put(category.getId(),category);
    }

    private Category getCategory(int id) {
        return mCategories.get(id);
    }


    @Override
    public void put(Category category) {
        putCategory(category);
    }

    @Override
    public Category get(int id) {
        return getCategory(id);
    }
}
