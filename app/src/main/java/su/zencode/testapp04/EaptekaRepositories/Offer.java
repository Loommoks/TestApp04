package su.zencode.testapp04.EaptekaRepositories;

import android.graphics.Bitmap;

public class Offer {
    private int mId;
    private String mName;
    private String mIcon;
    private String[] mPicturesUrls;
    private Bitmap mIconBitmap;


    public Offer(int id, String name, String icon, String[] pictures) {
        mId = id;
        mName = name;
        mIcon = icon;
        mPicturesUrls = pictures;
    }

    public int getId() {
        return mId;
    }

    public String getName() {
        return mName;
    }

    public String getIcon() {
        return mIcon;
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
