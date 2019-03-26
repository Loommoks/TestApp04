package su.zencode.testapp04.EaptekaApiClient;

import android.util.Log;

import java.io.IOException;

import okhttp3.Authenticator;
import okhttp3.Credentials;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Route;

public class TestAppClient {
    private static final String TAG = "TestAppClient";

    public String fetch(String url, String username, String password) {
        OkHttpClient client = createAuthentificationClient(username, password);

        try {
            String result = doRequest(client, url).body().string();
            return result;
        } catch (IOException e) {
            Log.e(TAG, "request.body.string() error", e);
        }

        return null;
    }

    public Response doRequest(OkHttpClient client, String url) {
        Log.d(TAG, "doRequest()");

        //OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(UrlsMap.Endpoints.HOST + UrlsMap.Endpoints.CATEGORIES + "0")
                .header("Content-Type","application/json")
                .header("platform", "android")
                .header("api-key", "i&j*3&^2TZ&d")
                .build();

        try {
            Response response = client.newCall(request).execute();
            //String result = response.body().string();
            return response;
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

    /**
    private static Response doRequest(OkHttpClient client, String url) {
        Request request = new Request.Builder()
    }*/
}
