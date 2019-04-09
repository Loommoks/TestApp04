package su.zencode.testapp04.TestAppApiClient;

import android.graphics.Bitmap;

import java.util.ArrayList;

import su.zencode.testapp04.EaptekaRepositories.Entities.Category;
import su.zencode.testapp04.EaptekaRepositories.Entities.Offer;

public interface IEaptekaApiClient {

    ArrayList<Category> getSubCategories(int id);

    ArrayList<Offer> getOffers(int id);

    Bitmap getImage(String url);
}
