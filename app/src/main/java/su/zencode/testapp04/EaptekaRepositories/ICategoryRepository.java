package su.zencode.testapp04.EaptekaRepositories;

import su.zencode.testapp04.EaptekaRepositories.Entities.Category;

public interface ICategoryRepository {
    Category get(int id);
    void add(Category category);
    void update(Category category);
}
