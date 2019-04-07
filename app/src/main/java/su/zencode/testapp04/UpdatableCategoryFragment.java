package su.zencode.testapp04;

import su.zencode.testapp04.EaptekaRepositories.Category;

public interface UpdatableCategoryFragment {
    void setupCategory(Category category);
    void updateCategoryData(Category category);
}
