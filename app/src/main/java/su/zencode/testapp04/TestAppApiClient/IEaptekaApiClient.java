package su.zencode.testapp04.TestAppApiClient;

import android.graphics.Bitmap;

import java.util.ArrayList;

import su.zencode.testapp04.EaptekaRepositories.Category;
import su.zencode.testapp04.EaptekaRepositories.Offer;

public interface IEaptekaApiClient {

    ArrayList<Category> fetchSubCategories(int id);

    ArrayList<Offer> fetchOffers(int id);

    Bitmap fetchOfferImage(String url);
}
