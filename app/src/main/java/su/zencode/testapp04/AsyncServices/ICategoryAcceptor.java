package su.zencode.testapp04.AsyncServices;

import su.zencode.testapp04.EaptekaRepositories.Entities.Category;

public interface ICategoryAcceptor {
    void initializeCategory(Category category);
    void updateCategoryData(Category category);
}
