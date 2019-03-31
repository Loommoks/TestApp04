package su.zencode.testapp04.EaptekaRepositories;

import java.util.concurrent.ConcurrentHashMap;

public class CacheCategoriesRepositoryI implements IEaptekaCategoryRepository {
    private static CacheCategoriesRepositoryI sCategoriesRepository;
    private static final String TAG = ".CacheCategoriesRepositoryI";

    private ConcurrentHashMap<Integer, Category> mCategories;

    public static IEaptekaCategoryRepository getInstance() {
        if(sCategoriesRepository == null) {
            sCategoriesRepository = new CacheCategoriesRepositoryI();
        }

        return sCategoriesRepository;
    }

    private CacheCategoriesRepositoryI() {
        mCategories = new ConcurrentHashMap<>();
    }

    @Override
    public void addCategory(Category category) {
        mCategories.put(category.getId(),category);
    }

    @Override
    public void update(Category category) {
        //todo update method
    }

    @Override
    public Category getCategory(int id) {
        return mCategories.get(id);
    }
}
