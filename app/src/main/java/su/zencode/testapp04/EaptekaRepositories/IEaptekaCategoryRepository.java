package su.zencode.testapp04.EaptekaRepositories;

public interface IEaptekaCategoryRepository {
    Category get(int id);
    void add(Category category);
    void update(Category category);
}
