package su.zencode.testapp04.EaptekaDataBase;

public class CategoryDbSchema {
    public static final class CategoryTable {
        public static final String NAME = "categories";

        public static final class Cols {
            public static final String ID = "id";
            public static final String CATEGORY_NAME = "name";
            public static final String HAS_SUB_CATEGORIES = "has_sub_categories";
            public static final String SUB_CATEGORIES_LIST = "sub_categories_list";
            public static final String OFFERS_LIST = "offers_list";
            public static final String UPLOAD_DATE = "upload_date";
        }
    }
}
