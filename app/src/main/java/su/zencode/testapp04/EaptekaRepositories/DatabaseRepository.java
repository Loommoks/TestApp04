package su.zencode.testapp04.EaptekaRepositories;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import org.json.JSONArray;

import java.util.ArrayList;

import su.zencode.testapp04.EaptekaDataBase.CategoryBaseHelper;
import su.zencode.testapp04.EaptekaDataBase.CategoryCursorWrapper;
import su.zencode.testapp04.EaptekaDataBase.CategoryDbSchema;
import su.zencode.testapp04.EaptekaDataBase.DbJsonHelper.Serializer;


public class DatabaseRepository implements IEaptekaCategoryRepository {
    public static final String ROOT_CATEGORY_NAME = "Root";
    private static DatabaseRepository sRepository;
    private Context mContext;
    private SQLiteDatabase mDatabase;

    public static DatabaseRepository getInstance(Context context) {
        if(sRepository == null) {
            sRepository = new DatabaseRepository(context);
        }
        return sRepository;
    }

    private DatabaseRepository(Context context) {
        mContext = context;
        mDatabase = new CategoryBaseHelper(mContext).getWritableDatabase();
    }

    @Override
    public Category get(int id) {
        String idString = Integer.toString(id);
        CategoryCursorWrapper cursor = queryCategories(
                CategoryDbSchema.CategoryTable.Cols.ID + " = ?",
                new String[] { idString }
        );

        try {
            if (cursor.getCount() == 0) {
                if(id == 0) {
                    Category baseCategory = new Category(0, ROOT_CATEGORY_NAME, true);
                    add(baseCategory);
                    return baseCategory;
                }
                return null;
            }

            cursor.moveToFirst();
            return cursor.getCategory();
        } finally {
            cursor.close();
        }
    }

    @Override
    public void add(Category category) {
        ContentValues values = getContentValues(category);
        mDatabase.insert(CategoryDbSchema.CategoryTable.NAME, null, values);
    }

    @Override
    public void update(Category category) {
        String id = Integer.toString(category.getId());
        ContentValues values = getContentValues(category);

        mDatabase.update(CategoryDbSchema.CategoryTable.NAME, values,
                CategoryDbSchema.CategoryTable.Cols.ID + " = ?",
                new String[] {id});
    }

    private CategoryCursorWrapper queryCategories(String whereClause, String[] whereArgs) {
        Cursor cursor = mDatabase.query(
                CategoryDbSchema.CategoryTable.NAME,
                null,
                whereClause,
                whereArgs,
                null,
                null,
                null
        );

        return new CategoryCursorWrapper(cursor);
    }

    private ContentValues getContentValues(Category category) {
        ContentValues values = new ContentValues();
        String id = Integer.toString(category.getId());
        values.put(CategoryDbSchema.CategoryTable.Cols.ID, id);
        values.put(CategoryDbSchema.CategoryTable.Cols.CATEGORY_NAME, category.getName());
        values.put(CategoryDbSchema.CategoryTable.Cols.HAS_SUB_CATEGORIES, category.hasSubCategories() ? 1 : 0);
        putSubCategories(values, category);
        putOffersList(values, category);
        //todo put upload date
        return values;
    }

    private void putSubCategories (ContentValues values, Category category) {
        if(category.hasSubCategories()) {
            ArrayList<Category> categories = category.getSubCategoriesList();
            if(categories == null) {
                values.putNull(CategoryDbSchema.CategoryTable.Cols.SUB_CATEGORIES_LIST);
                return;
            }
            JSONArray subCategoryList = Serializer.getJsonSubCategoriesList(categories);
            values.put(CategoryDbSchema.CategoryTable.Cols.SUB_CATEGORIES_LIST, subCategoryList.toString());
        }
    }

    private void putOffersList (ContentValues values, Category category) {
        if(!category.hasSubCategories()) {
            ArrayList<Offer> offers = category.getOfferList();
            if(offers == null) {
                values.putNull(CategoryDbSchema.CategoryTable.Cols.OFFERS_LIST);
                return;
            }
            JSONArray jsonOffers = Serializer.getJsonOffersList(offers);
            values.put(CategoryDbSchema.CategoryTable.Cols.OFFERS_LIST, jsonOffers.toString());
        }
    }


}
