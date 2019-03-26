package su.zencode.testapp04.EaptekaRepositories;

import java.util.List;

public class Category {
    private int mId;
    private String mName;
    private boolean mHasSubCategories;
    private List<Category> mSubCategoriesList;
    private List<Integer> mSubCategoriesIdList;
    private List<Offer> mOfferList;
    private List<Integer> mOffersIdList;

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

    public List<Category> getSubCategoriesList() {
        return mSubCategoriesList;
    }

    public void setSubCategoriesList(List<Category> subCategoriesList) {
        mSubCategoriesList = subCategoriesList;
    }

    public List<Integer> getSubCategoriesIdList() {
        return mSubCategoriesIdList;
    }

    public void setSubCategoriesIdList(List<Integer> subCategoriesIdList) {
        mSubCategoriesIdList = subCategoriesIdList;
    }

    public List<Offer> getOfferList() {
        return mOfferList;
    }

    public void setOfferList(List<Offer> offerList) {
        mOfferList = offerList;
    }

    public List<Integer> getOffersIdList() {
        return mOffersIdList;
    }

    public void setOffersIdList(List<Integer> offersIdList) {
        mOffersIdList = offersIdList;
    }
}
