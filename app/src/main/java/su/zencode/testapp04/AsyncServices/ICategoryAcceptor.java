package su.zencode.testapp04.AsyncServices;

import su.zencode.testapp04.EaptekaRepositories.Category;

public interface ICategoryAcceptor {
    void setupCategory(Category category);
    void updateCategoryData(Category category);
}
