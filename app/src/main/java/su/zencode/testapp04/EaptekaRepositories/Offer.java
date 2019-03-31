package su.zencode.testapp04.EaptekaRepositories;

import android.graphics.Bitmap;

public class Offer {
    private int mId;
    private String mName;
    private String mIconUrl;
    private String[] mPicturesUrls;

    private Bitmap mIconBitmap;


    public Offer(int id, String name, String iconUrl, String[] pictures) {
        mId = id;
        mName = name;
        mIconUrl = iconUrl;
        mPicturesUrls = pictures;
    }

    public int getId() {
        return mId;
    }

    public String getName() {
        return mName;
    }

    public String getIconUrl() {
        return mIconUrl;
    }

    public String[] getPicturesUrls() {
        return mPicturesUrls;
    }

    public Bitmap getIconBitmap() {
        return mIconBitmap;
    }

    public void setIconBitmap(Bitmap iconBitmap) {
        mIconBitmap = iconBitmap;
    }
}
