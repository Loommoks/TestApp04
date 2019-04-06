package su.zencode.testapp04.EaptekaRepositories;

import android.content.Context;

//todo x naming
//todo x extract dbrepository
//todo x extract jsonHelper
public class CacheableDatabaseRepository implements IEaptekaCategoryRepository {
    //todo x naming
    private static CacheableDatabaseRepository sDatabseRepositoryI;
    //todo x naming (mCache)
    private IEaptekaCategoryRepository mCache;
    private IEaptekaCategoryRepository mDatabase;


    public static CacheableDatabaseRepository getInstance(Context context) {
        if(sDatabseRepositoryI == null) {
            sDatabseRepositoryI = new CacheableDatabaseRepository(context);
        }
        return sDatabseRepositoryI;
    }
    //todo x передавать базу и репозиторий через конструктор
    private CacheableDatabaseRepository(Context context) {
        mCache = CacheRepository.getInstance();
        mDatabase = DatabaseRepository.getInstance(context);
    }

    @Override
    public void add(Category category) {
        mCache.add(category);
        mDatabase.add(category);

    }

    @Override
    public void update(Category category) {
        mCache.update(category);
        mDatabase.update(category);
    }

    @Override
    public Category get(int id) {
        Category category = mCache.get(id);
        if(category == null) {
            category = mDatabase.get(id);
            if(category != null) mCache.add(category);
        }
        return category;
    }

}
