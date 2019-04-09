package su.zencode.testapp04;

public class Config {
    public static class Settings {
        /** Datababe Objects TtL in milliseconds */
        public static final long DATABASE_DATA_TTL = 24*(60*60*1000);
    }

    public static class EaptekaApi {

        public static final String API_KEY = "i&j*3&^2TZ&d";

        public static class JsonDeserializeMap {
            public static final String JSON_ARRAY_SUB_CATEGORIES = "categories";
            public static final String JSON_ARRAY_OFFERS = "offers";
            public static final String JSON_SUB_CATEGORY_ID = "id";
            public static final String JSON_SUB_CATEGORY_NAME = "name";
            public static final String JSON_SUB_CATEGORY_HAS_SUBCATEGORIES = "subcategories";
            public static final String JSON_OFFER_ID = "id";
            public static final String JSON_OFFER_PICTURES_URLS_ARRAY = "pictures";
            public static final String JSON_OFFER_NAME = "name";
            public static final String JSON_OFFER_ICON_URL = "icon";
        }

        public static class Credentials {
            public static final String USERNAME = "eapteka";
            public static final String PASSWORD = "stage";
        }
    }

    public static class EaptekaHttp {
        public static final long CONNECT_TIMEOUT = 15;
        public static final long WRITE_TIMEOUT = 15;
        public static final long READ_TIMEOUT = 30;
        public static final boolean RETRY_ON_CONNECTION_FAILURE = true;

    }

    public static class EaptekaUrlsMap {
        public static final String HOST = "https://stage.eapteka.ru/api/v2";
        public static final String CATEGORIES = "/categories/";
        public static final String OFFERS = "/offers/";
    }

    public static class DbSchema {

        public static final String ROOT_CATEGORY_NAME = "Root";

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

            public static final class JsonSerialisation {

                public static final String JSON_OFFER_ID = "id";
                public static final String JSON_OFFER_NAME = "name";
                public static final String JSON_OFFER_ICON = "icon";
                public static final String JSON_OFFER_PICTURES = "pictures";
                public static final String JSON_CATEGORY_ID = "id";
                public static final String JSON_CATEGORY_NAME = "name";
                public static final String JSON_CATEGORY_HAS_SUBCATEGORIES = "subcategories";
            }
        }
    }
}
