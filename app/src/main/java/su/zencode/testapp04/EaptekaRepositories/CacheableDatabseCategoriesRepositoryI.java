package su.zencode.testapp04.EaptekaRepositories;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class CacheableDatabseCategoriesRepositoryI implements IEaptekaCategoryRepository {
    private IEaptekaCategoryRepository mRepository;
    private SQLiteDatabase mDatabase;

    public CacheableDatabseCategoriesRepositoryI(Context context) {
        mRepository = CacheCategoriesRepositoryI.getInstance();

    }

    @Override
    public void addCategory(Category category) {
        mRepository.addCategory(category);
        //todo save to database
    }

    @Override
    public void update(Category category) {

    }

    @Override
    public Category getCategory(int id) {
        Category category = mRepository.getCategory(id);
        if(category == null) {
            //todo load from database
        }
        return category;
    }
}
