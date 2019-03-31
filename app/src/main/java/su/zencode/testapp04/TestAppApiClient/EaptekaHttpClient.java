package su.zencode.testapp04.TestAppApiClient;

import android.util.Log;

import java.io.IOException;

import okhttp3.Authenticator;
import okhttp3.Credentials;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Route;
import su.zencode.testapp04.TestAppApiClient.EaptekaUrlsMap.Endpoints;

public class EaptekaHttpClient {
    private static final String TAG = "EaptekaHttpClient";

    public String fetchSubCategories(int id, String username, String password) {
        OkHttpClient client = createAuthentificationClient(username, password);
        String url = Endpoints.HOST + Endpoints.CATEGORIES + id;

        return doRequest(client, url);
    }

    public String fetchOffers(int id, String username, String password) {
        OkHttpClient client = createAuthentificationClient(username, password);
        String url = Endpoints.HOST + Endpoints.CATEGORIES + id +Endpoints.OFFERS;

        return doRequest(client, url);
    }

    public String doRequest(OkHttpClient client, String url) {
        Log.d(TAG, "Received request for URL: " + url);

        Request request = new Request.Builder()
                .url(url)
                .header("Content-Type","application/json")
                .header("platform", "android")
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
