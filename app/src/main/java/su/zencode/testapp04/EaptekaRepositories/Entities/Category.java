package su.zencode.testapp04.EaptekaRepositories.Entities;

import java.util.ArrayList;
import java.util.Date;

public class Category {
    private int mId;
    private String mName;
    private boolean mHasSubCategories;
    private ArrayList<Category> mSubCategoriesList;
    private ArrayList<Offer> mOfferList;
    private Date mUploadDate;

    public Category(int id, String name, boolean hasSubcategories) {
        mId = id;
        mName = name;
        mHasSubCategories = hasSubcategories;
        mSubCategoriesList = null;
        mOfferList = null;
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


    public Date getUploadDate() {
        return mUploadDate;
    }

    public void setUploadDate(Date uploadDate) {
        mUploadDate = uploadDate;
    }

    public boolean isEmpty() {
        if(mHasSubCategories) return (mSubCategoriesList == null);
        else return (mOfferList == null);
    }
}
