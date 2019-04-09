package su.zencode.testapp04.TestAppApiClient;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;

import su.zencode.testapp04.Config.Credentials;
import su.zencode.testapp04.EaptekaRepositories.Category;
import su.zencode.testapp04.Config.EaptekaApiJson.DeserializeMap;
import su.zencode.testapp04.EaptekaRepositories.Offer;
import su.zencode.testapp04.TestAppApiClient.EaptekaUrlsMap.Endpoints;

public class EaptekaApiClient implements IEaptekaApiClient {

    @Override
    public ArrayList<Category> fetchSubCategories(int id) {
        String url = Endpoints.HOST + Endpoints.CATEGORIES + id;
        String response = new EaptekaHttpClient().fetchSubCategories(
                url,
                Credentials.USERNAME,
                Credentials.PASSWORD);
        return parseSubCategoriesJson(response);
    }

    @Override
    public ArrayList<Offer> fetchOffers(int id) {
        String url = Endpoints.HOST + Endpoints.CATEGORIES + id +Endpoints.OFFERS;
        String response = new EaptekaHttpClient().fetchOffers(
                url,
                Credentials.USERNAME,
                Credentials.PASSWORD);
        return parseOffersJson(response);
    }

    @Override
    public Bitmap fetchOfferImage(String url) {
        InputStream inputStream = new EaptekaHttpClient().fetchImage(
                url,
                Credentials.USERNAME,
                Credentials.PASSWORD);
        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
        return bitmap;
    }

    private ArrayList<Category> parseSubCategoriesJson(String jsonBodyString) {

        try {
            JSONObject jsonBody = new JSONObject(jsonBodyString);
            JSONArray categoriesJsonArray =
                    jsonBody.getJSONArray(DeserializeMap.JSON_ARRAY_SUB_CATEGORIES);
            return parseCategoriesArray(categoriesJsonArray);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    private ArrayList<Offer> parseOffersJson(String jsonBodyString) {
        try {
            JSONObject jsonBody = new JSONObject(jsonBodyString);
            JSONArray offersJsonArray =
                    jsonBody.getJSONArray(DeserializeMap.JSON_ARRAY_OFFERS);
            return parseOffersArray(offersJsonArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    private ArrayList<Category> parseCategoriesArray(JSONArray categoriesJsonArray)
            throws JSONException {
        ArrayList<Category> subCategories = new ArrayList<>();
        for(int i = 0; i < categoriesJsonArray.length(); i++) {
            JSONObject subCategory = categoriesJsonArray.getJSONObject(i);
            int id = subCategory.getInt(DeserializeMap.JSON_SUB_CATEGORY_ID);
            Category category = new Category(
                    id,
                    subCategory.getString(DeserializeMap.JSON_SUB_CATEGORY_NAME),
                    subCategory.getBoolean(DeserializeMap.JSON_SUB_CATEGORY_HAS_SUBCATEGORIES)
            );
            subCategories.add(category);
        }
        return subCategories;
    }

    private ArrayList<Offer> parseOffersArray(JSONArray offersJsonArray)
            throws JSONException {
        ArrayList<Offer> offersList = new ArrayList<>();
        for(int i = 0; i < offersJsonArray.length(); i++) {
            JSONObject offerJson = offersJsonArray.getJSONObject(i);
            int id = offerJson.getInt(DeserializeMap.JSON_OFFER_ID);
            JSONArray picturesUrlsJsonArray =
                    offerJson.getJSONArray(DeserializeMap.JSON_OFFER_PICTURES_URLS_ARRAY);
            String[] picturesUrls = parsePicturesUrlsJSONArray(picturesUrlsJsonArray);

            Offer offer = new Offer(
                    id,
                    offerJson.getString(DeserializeMap.JSON_OFFER_NAME),
                    offerJson.getString(DeserializeMap.JSON_OFFER_ICON_URL),
                    picturesUrls
            );
            offersList.add(offer);
        }
        return offersList;
    }

    private String[] parsePicturesUrlsJSONArray(JSONArray picturesUrlsJsonArray)
            throws JSONException {
        String[] picteresUrls = new String[picturesUrlsJsonArray.length()];
        for(int i = 0; i < picturesUrlsJsonArray.length(); i++) {
            picteresUrls[i] = picturesUrlsJsonArray.getString(i);
        }
        return picteresUrls;
    }

}
