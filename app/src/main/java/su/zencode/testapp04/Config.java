package su.zencode.testapp04;

public class Config {
    public static class Settings {
        public static final int DATABASE_DATA_TTL = 24*(1000*60*60);
    }

    public static class EaptekaApiJson {
        public static class DeserializeMap {
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
    }

    public static class Credentials {
        public static final String USERNAME = "eapteka";
        public static final String PASSWORD = "stage";
    }
}
