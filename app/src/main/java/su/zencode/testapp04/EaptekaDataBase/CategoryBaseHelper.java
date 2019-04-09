package su.zencode.testapp04.EaptekaDataBase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import su.zencode.testapp04.Config.DbSchema.CategoryTable;

public class CategoryBaseHelper extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "categoryBase.db";

    public CategoryBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + CategoryTable.NAME +"(" +
                " _id integer primary key autoincrement, " +
                CategoryTable.Cols.ID + ", " +
                CategoryTable.Cols.CATEGORY_NAME + ", " +
                CategoryTable.Cols.HAS_SUB_CATEGORIES + ", " +
                CategoryTable.Cols.SUB_CATEGORIES_LIST + ", " +
                CategoryTable.Cols.OFFERS_LIST + ", " +
                CategoryTable.Cols.UPLOAD_DATE +
                ")"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
