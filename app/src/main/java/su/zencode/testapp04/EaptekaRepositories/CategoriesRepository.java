package su.zencode.testapp04.EaptekaRepositories;

import java.util.concurrent.ConcurrentHashMap;

public class CategoriesRepository {
    private static CategoriesRepository sCategoriesRepository;
    private static final String TAG = ".CategoriesRepository";

    private ConcurrentHashMap<Integer, Category> mCategories;

    public static CategoriesRepository getIstance() {
        if(sCategoriesRepository == null) {
            sCategoriesRepository = new CategoriesRepository();
        }

        return sCategoriesRepository;
    }

    private CategoriesRepository() {
        mCategories = new ConcurrentHashMap<>();
    }

    public void putCategory(Category category) {
        mCategories.put(category.getId(),category);
    }

    public Category getCategory(int id) {
        return mCategories.get(id);
    }


}
