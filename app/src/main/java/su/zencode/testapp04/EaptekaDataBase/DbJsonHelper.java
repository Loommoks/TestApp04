package su.zencode.testapp04.EaptekaDataBase;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import su.zencode.testapp04.Config.DbSchema.CategoryTable.JsonSerialisation;
import su.zencode.testapp04.EaptekaRepositories.Entities.Category;
import su.zencode.testapp04.EaptekaRepositories.Entities.Offer;

public class DbJsonHelper {


    public static class Serializer {

        public static JSONArray getJsonSubCategoriesList
                (ArrayList<Category> subCategoriesList) {
            JSONArray jsonArray = new JSONArray();
            for (Category category:
                    subCategoriesList) {
                JSONObject jsonCuttedCategory = getCuttedJSONCategory(category);
                jsonArray.put(jsonCuttedCategory);
            }
            return jsonArray;
        }

        public static JSONArray getJsonOffersList(ArrayList<Offer> offers) {
            JSONArray jsonArray = new JSONArray();
            for (Offer offer:
                    offers) {
                JSONObject jsonOffer = getJsonOffer(offer);
                jsonArray.put(jsonOffer);
            }
            return jsonArray;
        }

        private static JSONObject getJsonOffer(Offer offer) {
            try {
                JSONObject jsonOffer = new JSONObject();
                jsonOffer.put(JsonSerialisation.JSON_OFFER_ID,offer.getId());
                jsonOffer.put(JsonSerialisation.JSON_OFFER_NAME,offer.getName());
                jsonOffer.put(JsonSerialisation.JSON_OFFER_ICON, offer.getIconUrl());
                putJsonPictures(JsonSerialisation.JSON_OFFER_PICTURES, offer, jsonOffer);
                return jsonOffer;
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        private static void putJsonPictures(String name,
                                            Offer offer,
                                            JSONObject jsonOffer) throws JSONException {
            JSONArray jsonPictures = new JSONArray();
            for (String picture :
                    offer.getPicturesUrls()) {
                jsonPictures.put(picture);
            }
            jsonOffer.put(name, jsonPictures);
        }

        private static JSONObject getCuttedJSONCategory(Category category) {
            try {
                JSONObject jsonCategory = new JSONObject();
                jsonCategory.put(JsonSerialisation.JSON_CATEGORY_ID, category.getId());
                jsonCategory.put(JsonSerialisation.JSON_CATEGORY_NAME, category.getName());
                jsonCategory.put(JsonSerialisation.JSON_CATEGORY_HAS_SUBCATEGORIES, category.hasSubCategories());
                return jsonCategory;
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    public static class Deserializer {

        public static ArrayList<Offer> parseJsonOffersList(String jsonOffersList) {

            try {
                ArrayList<Offer> offers = new ArrayList<>();
                JSONArray jsonArray = new JSONArray(jsonOffersList);

                for(int i = 0; i < jsonArray.length(); i++) {
                    Offer offer = parseJsonOffer(jsonArray.getJSONObject(i));
                    offers.add(offer);
                }

                return offers;
            } catch (JSONException e) {
                Log.e("CategoryCursorWrapper", "Failed to parse offers JsonArray", e);
            }
            return null;
        }

        public static ArrayList<Category> parseJsonSubCategoriesList(String jsonSubCategoriesList) {
            try {
                ArrayList<Category> subCategoriesList = new ArrayList<>();
                JSONArray jsonArray = new JSONArray(jsonSubCategoriesList);
                for(int i = 0; i < jsonArray.length(); i++){
                    Category subCategory = parseJsonSubCategory(jsonArray.getJSONObject(i));
                    subCategoriesList.add(subCategory);
                }
                return subCategoriesList;
            } catch (JSONException e) {
                Log.e("CategoryCursorWrapper", "Failed to parse subcategories JsonArray", e);
            }
            return null;
        }

        private static Category parseJsonSubCategory(JSONObject jsonCategory) {
            try {
                int id = jsonCategory.getInt(JsonSerialisation.JSON_CATEGORY_ID);
                String name = jsonCategory.getString(JsonSerialisation.JSON_CATEGORY_NAME);
                boolean hasSubCategories = jsonCategory.getBoolean(JsonSerialisation.JSON_CATEGORY_HAS_SUBCATEGORIES);
                return new Category(id, name, hasSubCategories);
            } catch (JSONException e) {
                Log.e("CategoryCursorWrapper", "Failed to parse subcategory", e);
            }

            return null;
        }

        private static Offer parseJsonOffer(JSONObject jsonOffer) {
            try {
                int id = jsonOffer.getInt(JsonSerialisation.JSON_OFFER_ID);
                String name = jsonOffer.getString(JsonSerialisation.JSON_OFFER_NAME);
                String icon = jsonOffer.getString(JsonSerialisation.JSON_OFFER_ICON);
                String[] pictures = getPictures(JsonSerialisation.JSON_OFFER_PICTURES,jsonOffer);
                return new Offer(id, name, icon, pictures);
            } catch (JSONException e) {
                Log.e("CategoryCursorWrapper", "Failed to parse Json Offer", e);
            }
            return null;
        }

        private static String[] getPictures(String name, JSONObject jsonOffer) throws JSONException {
            JSONArray jsonPictures = jsonOffer.getJSONArray(name);
            String[] pictures = new String[jsonPictures.length()];
            for(int i = 0; i < jsonPictures.length(); i++) {
                pictures[i] = jsonPictures.getString(i);
            }
            return pictures;
        }
    }
}
