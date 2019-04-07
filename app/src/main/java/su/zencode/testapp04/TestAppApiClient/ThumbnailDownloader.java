package su.zencode.testapp04.TestAppApiClient;

import android.graphics.Bitmap;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.util.Log;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import su.zencode.testapp04.EaptekaRepositories.Offer;

public class ThumbnailDownloader<T> extends HandlerThread {
    private static final String TAG = "ThumbnailDownloader";
    //private static final int MESSAGE_DOWNLOAD = 0;

    private boolean mHasQuit = false;
    //private ConcurrentMap<T,Offer> mRequestMap = new ConcurrentHashMap<>();
    //private Handler mRequestHandler;
    //private Handler mResponseHandler;
    private ThumbnailDownloadListener<T> mThumbnailDownloadListener;

    public interface ThumbnailDownloadListener<T> {
        void onThumbnailDownloaded(T target, boolean targetChanged,
                                   Bitmap thumbnail, int id);
    }

    public void setThumbnailDownloadListener(ThumbnailDownloadListener<T> listener) {
        mThumbnailDownloadListener = listener;
    }

    public ThumbnailDownloader(Handler responseHandler) {
        super(TAG);
        //mResponseHandler = responseHandler;
    }

    @Override
    public boolean quit() {
        mHasQuit = true;
        return super.quit();
    }

    public void queueThumbnail(T target, String url) {
        Log.i(TAG, "Got a URL: " + url);

    }

    public void clearQueue() {
        //mResponseHandler.removeMessages(MESSAGE_DOWNLOAD);
        //mRequestMap.clear();
    }

    private void handleRequest(final T target) {

        /**
        final Post postContainer = mRequestMap.get(target);

        if (mRequestMap.get(target) == null) {
            return;
        }

        final String url = postContainer.getImageUrl();
        final String urlFull = URLsMap.Endpoints.BASE_URL + url;
        final String postId = postContainer.getId();


        try {
            Bitmap bitmap = new DevExamApiClient().loadThumbnail(urlFull);

            postResponse(target, postId, bitmap);
        } catch (IOException ioe) {
            Log.e(TAG, "Error downloading image", ioe);
        }*/
    }

    private void postResponse(final T target, final String containerId, final Bitmap bitmap) {
        /**
        mResponseHandler.post(new Runnable() {
            @Override
            public void run() {
                if(mHasQuit) {
                    return;
                }

                boolean targetChanged = true;
                if(mRequestMap.get(target).getId().equals(containerId)) {
                    mRequestMap.remove(target);
                    targetChanged = false;
                }
                mThumbnailDownloadListener.onThumbnailDownloaded(
                        target,
                        targetChanged,
                        bitmap,
                        containerId);
            }
        });
         */
    }
}
