package su.zencode.testapp04.TestAppApiClient;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;

import okhttp3.Authenticator;
import okhttp3.Credentials;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Route;

public class EaptekaHttpClient {
    private static final String TAG = "EaptekaHttpClient";

    public String fetchSubCategories(String url, String username, String password) {
        OkHttpClient client = createAuthentificationClient(username, password);
        return doRequest(client, url);
    }

    public String fetchOffers(String url, String username, String password) {
        OkHttpClient client = createAuthentificationClient(username, password);
        return doRequest(client, url);
    }

    public InputStream fetchImage(String url, String username, String password) {
        Log.d(TAG, "Received request for Image URL: " + url);

        OkHttpClient client = createAuthentificationClient(username, password);

        Request request = new Request.Builder()
                .url(url)
                .header("Content-Type","application/json")
                .header("platform", "android")
                //todo x inject api-key from ctor
                .header("api-key", "i&j*3&^2TZ&d")
                .build();

        try {
            Response response = client.newCall(request).execute();
            /**final Bitmap bitmap = BitmapFactory.decodeStream(
                    response.body().byteStream());*/

            return response.body().byteStream();
        } catch (IOException e) {
            Log.e(TAG, "Failed to make a OkHttp call", e);
        }
        return null;
    }

    public String doRequest(OkHttpClient client, String url) {
        Log.d(TAG, "Received request for URL: " + url);

        Request request = new Request.Builder()
                .url(url)
                .header("Content-Type","application/json")
                .header("platform", "android")
                //todo x inject api-key from ctor
                .header("api-key", "i&j*3&^2TZ&d")
                .build();

        try {
            Response response = client.newCall(request).execute();
            return response.body().string();
        } catch (IOException e) {
            Log.e(TAG, "Failed to make a OkHttp call", e);
        }

        return null;
    }

    private static OkHttpClient createAuthentificationClient(final String username,
                                                             final String password) {
        OkHttpClient httpClient = new OkHttpClient.Builder().authenticator(new Authenticator() {
            @Override
            public Request authenticate(Route route, Response response) throws IOException {
                String credentials = Credentials.basic(username, password);
                return response.request().newBuilder().header("Authorization", credentials).build();
            }
        }).build();
        return httpClient;
    }

}
