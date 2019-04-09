package su.zencode.testapp04.AsyncServices;

import android.graphics.Bitmap;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.util.Log;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import su.zencode.testapp04.TestAppApiClient.EaptekaApiClient;
import su.zencode.testapp04.TestAppApiClient.IEaptekaApiClient;

public class ImageAsyncService<T> extends HandlerThread {
    private static final String TAG = "ImageAsyncService";
    private static final int MESSAGE_DOWNLOAD = 0;

    private boolean mHasQuit = false;
    private Handler mRequestHandler;
    private ConcurrentMap<T, String> mRequestsMap = new ConcurrentHashMap<>();
    private IImageAcceptor<T> mImageAcceptor;
    private Handler mResponseHandler;
    private IEaptekaApiClient mApiClient;

    public interface IImageAcceptor<T> {
        void onImageDownloaded(T target, Bitmap bitmap);
    }

    public void setImageAcceptor(IImageAcceptor<T> listener) {
        mImageAcceptor = listener;
    }

    public ImageAsyncService(Handler responseHandler) {
        super(TAG);
        mResponseHandler = responseHandler;
        mApiClient = new EaptekaApiClient();
    }

    @Override
    protected void onLooperPrepared() {
        mRequestHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if(msg.what == MESSAGE_DOWNLOAD) {
                    T target = (T) msg.obj;
                    Log.i(TAG, "Got a request for URL: " + mRequestsMap.get(target));
                    handleRequest(target);
                }
            }
        };
    }

    @Override
    public boolean quit() {
        mHasQuit = true;
        return super.quit();
    }

    public void queueImage(T target, String url) {
        Log.i(TAG, "Got a URL: " + url);

        if(url == null) {
            mRequestsMap.remove(target);
        } else {
            mRequestsMap.put(target, url);
            mRequestHandler.obtainMessage(MESSAGE_DOWNLOAD, target)
                    .sendToTarget();
        }
    }

    public void clearQueue() {
        mRequestHandler.removeMessages(MESSAGE_DOWNLOAD);
        mRequestsMap.clear();
    }

    private void handleRequest(final T target) {
        final String url = mRequestsMap.get(target);
        if(url == null) return;

        final Bitmap bitmap = mApiClient.fetchOfferImage(url);
        Log.i(TAG, "Bitmap loaded");

        mResponseHandler.post(new Runnable() {
            @Override
            public void run() {
                if(mRequestsMap.get(target) != url ||
                        mHasQuit) {
                    return;
                }

                mRequestsMap.remove(target);
                mImageAcceptor.onImageDownloaded(target, bitmap);
            }
        });
    }

}
