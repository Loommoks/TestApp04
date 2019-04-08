package su.zencode.testapp04.CategoryLab;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;

import java.util.HashMap;

import su.zencode.testapp04.EaptekaRepositories.Offer;
import su.zencode.testapp04.TestAppApiClient.EaptekaApiClient;
import su.zencode.testapp04.UpdatableCategoryFragment;
import su.zencode.testapp04.UpdatableOffersFragment;

public class OfferLab implements IOfferLab {
    private static OfferLab sOfferLab;
    private CategoryLab mCategoryLab;
    private HashMap<Integer, UpdatableOffersFragment> mImageRequests;

    public static OfferLab getInstance(Context context) {
        if(sOfferLab == null) {
            sOfferLab = new OfferLab(context);
        }
        return sOfferLab;
    }

    private OfferLab(Context context) {
        mCategoryLab = CategoryLab.getInstance(context);
        mImageRequests = new HashMap<>();
    }

    @Override
    public void getCategory(int id, UpdatableCategoryFragment updatableFragment) {
        mCategoryLab.getCategory(id, updatableFragment);
    }

    @Override
    public void getOfferImage(Offer offer, UpdatableOffersFragment offersFragment) {
        mImageRequests.put(offer.getId(), offersFragment);
        if(offer.getIconBitmap() != null) {
            onOfferImageReceived(offer);
            return;
        }
        new FetchOfferImageTask(offer).execute();
    }

    private void onOfferImageReceived(Offer offer) {
        int id = offer.getId();
        if(offer.getIconBitmap() != null) {
            UpdatableOffersFragment fragment = mImageRequests.get(id);
            if(fragment == null) return;
            fragment.updateOfferImage(offer);
            mImageRequests.remove(id);
        }
    }

    private class FetchOfferImageTask extends AsyncTask<Void, Void, Void> {
        private Offer mOffer;

        public FetchOfferImageTask(Offer offer) {
            mOffer = offer;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            EaptekaApiClient apiClient = new EaptekaApiClient();
            Bitmap bitmap = apiClient.fetchOfferImage(mOffer.getIconUrl());
            mOffer.setIconBitmap(bitmap);
            return null;
        }

        @Override
        protected void onPostExecute(Void v) {
            onOfferImageReceived(mOffer);
        }
    }

}
