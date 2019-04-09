package su.zencode.testapp04.EaptekaRepositories;

import java.util.concurrent.ConcurrentHashMap;

import su.zencode.testapp04.EaptekaRepositories.Entities.Category;

public class CacheRepository implements ICategoryRepository {
    private static CacheRepository sCategoriesRepository;

    private ConcurrentHashMap<Integer, Category> mCategories;

    public static CacheRepository getInstance() {
        if(sCategoriesRepository == null) {
            sCategoriesRepository = new CacheRepository();
        }
        return sCategoriesRepository;
    }

    private CacheRepository() {
        mCategories = new ConcurrentHashMap<>();
    }

    @Override
    public void add(Category category) {
        mCategories.put(category.getId(),category);
    }

    @Override
    public void update(Category category) {
        mCategories.put(category.getId(), category);
    }

    @Override
    public Category get(int id) {
        return mCategories.get(id);
    }
}
