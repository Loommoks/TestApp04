package su.zencode.testapp04.TestAppApiClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import su.zencode.testapp04.EaptekaRepositories.IEaptekaCategoryRepository;
import su.zencode.testapp04.EaptekaRepositories.CacheCategoriesRepositoryI;
import su.zencode.testapp04.EaptekaRepositories.Category;
import su.zencode.testapp04.EaptekaRepositories.Offer;

public class EaptekaApiClient implements IEaptekaApiClient {
    IEaptekaCategoryRepository mRepository;
    Category mCategory;

    public EaptekaApiClient() {
        mRepository = CacheCategoriesRepositoryI.getInstance();
    }

    @Override
    public void fetchSubCategories(int id) {
        mCategory = mRepository.getCategory(id);
        if(mCategory.getSubCategoriesList() == null) {
            //todo add database check&load
            String response = new EaptekaHttpClient().fetchSubCategories(id, "eapteka", "stage");
            mCategory.setSubCategoriesList(parseSubCategoriesJson(response));
        }
    }

    @Override
    public void fetchOffers(int id) {
        mCategory = mRepository.getCategory(id);
        if(mCategory.getOfferList() == null) {
            String response = new EaptekaHttpClient().fetchOffers(id,"eapteka","stage");
            mCategory.setOfferList(parseOffersJson(response));
        }
    }

    private ArrayList<Category> parseSubCategoriesJson(String jsonBodyString) {

        try {
            JSONObject jsonBody = new JSONObject(jsonBodyString);
            JSONArray categoriesJsonArray = jsonBody.getJSONArray("categories");
            return parseCategoriesArray(categoriesJsonArray);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    private ArrayList<Offer> parseOffersJson(String jsonBodyString) {
        try {
            JSONObject jsonBody = new JSONObject(jsonBodyString);
            JSONArray offersJsonArray = jsonBody.getJSONArray("offers");
            return parseOffersArray(offersJsonArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    private ArrayList<Category> parseCategoriesArray(JSONArray categoriesJsonArray) throws JSONException {
        ArrayList<Category> subCategories = new ArrayList<>();
        for(int i = 0; i < categoriesJsonArray.length(); i++) {
            JSONObject subCategory = categoriesJsonArray.getJSONObject(i);
            int id = subCategory.getInt("id");
            Category category = new Category(
                    id,
                    subCategory.getString("name"),
                    subCategory.getBoolean("subcategories")
            );
            subCategories.add(category);
            if(mRepository.getCategory(id) == null)
                saveCategoryToRepo(category);
        }
        return subCategories;
    }

    private ArrayList<Offer> parseOffersArray(JSONArray offersJsonArray) throws JSONException {
        ArrayList<Offer> offersList = new ArrayList<>();
        for(int i = 0; i < offersJsonArray.length(); i++) {
            JSONObject offerJson = offersJsonArray.getJSONObject(i);
            int id = offerJson.getInt("id");
            JSONArray picturesUrlsJsonArray = offerJson.getJSONArray("pictures");
            String[] picturesUrls = parsePicturesUrlsJSONArray(picturesUrlsJsonArray);

            Offer offer = new Offer(
                    id,
                    offerJson.getString("name"),
                    offerJson.getString("icon"),
                    picturesUrls
            );
            offersList.add(offer);
        }
        return offersList;
    }

    private String[] parsePicturesUrlsJSONArray(JSONArray picturesUrlsJsonArray) throws JSONException {
        String[] picteresUrls = new String[picturesUrlsJsonArray.length()];
        for(int i = 0; i < picturesUrlsJsonArray.length(); i++) {
            picteresUrls[i] = picturesUrlsJsonArray.getString(i);
        }
        return picteresUrls;
    }

    private void saveCategoryToDb(Category category) {

    }

    private void saveCategoryToRepo(Category category) {
        mRepository.addCategory(category);
    }
}
