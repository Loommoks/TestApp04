package su.zencode.testapp04.EaptekaDataBase;

import android.database.Cursor;
import android.database.CursorWrapper;

import java.util.ArrayList;
import java.util.Date;

import su.zencode.testapp04.Config.DbSchema.CategoryTable;
import su.zencode.testapp04.EaptekaDataBase.DbJsonHelper.Deserializer;
import su.zencode.testapp04.EaptekaRepositories.Category;
import su.zencode.testapp04.EaptekaRepositories.Offer;

public class CategoryCursorWrapper extends CursorWrapper {

    public CategoryCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Category getCategory() {
        int id = getInt(getColumnIndex(CategoryTable.Cols.ID));
        String name = getString(getColumnIndex(CategoryTable.Cols.CATEGORY_NAME));
        int intHasSubCategories = getInt(getColumnIndex(CategoryTable.Cols.HAS_SUB_CATEGORIES));

        Category category = new Category(id, name, (intHasSubCategories != 0));
        setSubCategoriesList(category);
        setOffersList(category);
        setUploadDate(category);

        return category;
    }

    private void setUploadDate(Category category) {
        long dateLong = getLong(getColumnIndex(CategoryTable.Cols.UPLOAD_DATE));
        Date uploadDate = new Date(dateLong);
        category.setUploadDate(uploadDate);
    }

    private void setOffersList(Category category) {
        if(!category.hasSubCategories()) {
            String jsonOffersList =
                    getString(getColumnIndex(CategoryTable.Cols.OFFERS_LIST));
            if(jsonOffersList == null) return;
            ArrayList<Offer> offers =
                    Deserializer.parseJsonOffersList(jsonOffersList);
            category.setOfferList(offers);
        }
    }

    private void setSubCategoriesList(Category category) {
        if(category.hasSubCategories()) {
            String jsonSubCategoriesList =
                    getString(getColumnIndex(CategoryTable.Cols.SUB_CATEGORIES_LIST));
            if (jsonSubCategoriesList == null) return;
            ArrayList<Category> subCategoriesList =
                    Deserializer.parseJsonSubCategoriesList(jsonSubCategoriesList);
            category.setSubCategoriesList(subCategoriesList);
        }
    }

}
