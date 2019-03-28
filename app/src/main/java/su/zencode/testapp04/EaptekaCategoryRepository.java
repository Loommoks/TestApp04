package su.zencode.testapp04;

import su.zencode.testapp04.EaptekaRepositories.Category;

public interface EaptekaCategoryRepository {

    void put(Category category);

    Category get(int id);
}
