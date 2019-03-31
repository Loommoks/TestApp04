package su.zencode.testapp04.EaptekaRepositories;

public interface IEaptekaCategoryRepository {
    Category getCategory(int id);
    //ArrayList<Category> getCategoryList(int id);
    void addCategory(Category category);
    void update(Category category);
}
