package su.zencode.testapp04.EaptekaRepositories;

import java.util.ArrayList;
import java.util.List;

public class Category {
    private int mId;
    private String mName;
    private boolean mHasSubCategories;
    private ArrayList<Category> mSubCategoriesList;
    private List<Integer> mSubCategoriesIdList;
    private ArrayList<Offer> mOfferList;
    private List<Integer> mOffersIdList;

    //todo x naming subcategories-> "hasSubcategories"
    public Category(int id, String name, boolean subcategories) {
        mId = id;
        mName = name;
        mHasSubCategories = subcategories;
        mSubCategoriesList = null;
        mSubCategoriesIdList = null;
        mOfferList = null;
        mOffersIdList = null;
    }

    public int getId() {
        return mId;
    }

    public String getName() {
        return mName;
    }

    public boolean hasSubCategories() {
        return mHasSubCategories;
    }

    public ArrayList<Category> getSubCategoriesList() {
        return mSubCategoriesList;
    }

    public void setSubCategoriesList(ArrayList<Category> subCategoriesList) {
        mSubCategoriesList = subCategoriesList;
    }

    public List<Integer> getSubCategoriesIdList() {
        return mSubCategoriesIdList;
    }

    public void setSubCategoriesIdList(List<Integer> subCategoriesIdList) {
        mSubCategoriesIdList = subCategoriesIdList;
    }

    public Offer getOffer(int id) {
        if (mOfferList == null) return null;
        return mOfferList.get(id);
    }

    public ArrayList<Offer> getOfferList() {
        return mOfferList;
    }

    public void setOfferList(ArrayList<Offer> offerList) {
        mOfferList = offerList;
    }

    public List<Integer> getOffersIdList() {
        return mOffersIdList;
    }

    public void setOffersIdList(List<Integer> offersIdList) {
        mOffersIdList = offersIdList;
    }
}
