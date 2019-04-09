package su.zencode.testapp04.TestAppApiClient;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;

import su.zencode.testapp04.Config.EaptekaApi;
import su.zencode.testapp04.Config.EaptekaApi.Credentials;
import su.zencode.testapp04.Config.EaptekaUrlsMap;
import su.zencode.testapp04.EaptekaRepositories.Category;
import su.zencode.testapp04.Config.EaptekaApi.JsonDeserializeMap;
import su.zencode.testapp04.EaptekaRepositories.Offer;

public class EaptekaApiClient implements IEaptekaApiClient {

    @Override
    public ArrayList<Category> fetchSubCategories(int id) {
        String url = EaptekaUrlsMap.HOST + EaptekaUrlsMap.CATEGORIES + id;
        String response = new HttpClient().requestString(
                url,
                Credentials.USERNAME,
                Credentials.PASSWORD,
                EaptekaApi.API_KEY );
        return parseSubCategoriesJson(response);
    }

    @Override
    public ArrayList<Offer> fetchOffers(int id) {
        String url = EaptekaUrlsMap.HOST + EaptekaUrlsMap.CATEGORIES + id +EaptekaUrlsMap.OFFERS;
        String response = new HttpClient().requestString(
                url,
                Credentials.USERNAME,
                Credentials.PASSWORD,
                EaptekaApi.API_KEY );
        return parseOffersJson(response);
    }

    @Override
    public Bitmap fetchOfferImage(String url) {
        InputStream inputStream = new HttpClient().requestByteStream(
                url,
                Credentials.USERNAME,
                Credentials.PASSWORD,
                EaptekaApi.API_KEY );
        return BitmapFactory.decodeStream(inputStream);
    }

    private ArrayList<Category> parseSubCategoriesJson(String jsonBodyString) {

        try {
            JSONObject jsonBody = new JSONObject(jsonBodyString);
            JSONArray categoriesJsonArray =
                    jsonBody.getJSONArray(JsonDeserializeMap.JSON_ARRAY_SUB_CATEGORIES);
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
                    jsonBody.getJSONArray(JsonDeserializeMap.JSON_ARRAY_OFFERS);
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
            int id = subCategory.getInt(JsonDeserializeMap.JSON_SUB_CATEGORY_ID);
            Category category = new Category(
                    id,
                    subCategory.getString(JsonDeserializeMap.JSON_SUB_CATEGORY_NAME),
                    subCategory.getBoolean(
                            JsonDeserializeMap.JSON_SUB_CATEGORY_HAS_SUBCATEGORIES)
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
            int id = offerJson.getInt(JsonDeserializeMap.JSON_OFFER_ID);
            JSONArray picturesUrlsJsonArray =
                    offerJson.getJSONArray(JsonDeserializeMap.JSON_OFFER_PICTURES_URLS_ARRAY);
            String[] picturesUrls = parsePicturesUrlsJSONArray(picturesUrlsJsonArray);

            Offer offer = new Offer(
                    id,
                    offerJson.getString(JsonDeserializeMap.JSON_OFFER_NAME),
                    offerJson.getString(JsonDeserializeMap.JSON_OFFER_ICON_URL),
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
