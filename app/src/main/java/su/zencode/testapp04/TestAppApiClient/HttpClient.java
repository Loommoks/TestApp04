package su.zencode.testapp04.TestAppApiClient;

import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

import okhttp3.Authenticator;
import okhttp3.Credentials;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Route;
import su.zencode.testapp04.Config.EaptekaHttp;

public class HttpClient {
    private static final String TAG = "HttpClient";
    public static final String CONTENT_TYPE_NAME = "Content-Type";
    public static final String CONTENT_TYPE_APPLICATION_JSON = "application/json";
    public static final String PLATFORM_NAME = "platform";
    public static final String PLATFORM_ANDROID = "android";
    public static final String API_KEY_NAME = "api-key";
    public static final String AUTHORIZATION = "Authorization";

    public String getString(String url, String username, String password, String apiKey) {
        OkHttpClient client = createAuthentificationClient(username, password);
        return requestString(client, url, apiKey);
    }

    public InputStream getByteStream(String url, String username, String password, String apiKey) {
        OkHttpClient client = createAuthentificationClient(username, password);
        return requestByteStream(client, url, apiKey);
    }

    private InputStream requestByteStream(OkHttpClient client, String url, String apiKey) {
        try {
            Response response = getResponse(client, url, apiKey);
            if(response.isSuccessful())
                return response.body().byteStream();
        } catch (IOException e) {
            Log.e(TAG, "Failed to make a OkHttp call", e);
        }
        return null;
    }

    private String requestString(OkHttpClient client, String url, String apiKey) {
        try {
            Response response = getResponse(client, url, apiKey);
            if(response.isSuccessful())
                return response.body().string();
        } catch (IOException e) {
            Log.e(TAG, "Failed to make a OkHttp call", e);
        }
        return null;
    }

    private Response getResponse(OkHttpClient client, String url, String apiKey)
            throws IOException{
        Request request = getRequest(url, apiKey);
        Response response = client.newCall(request).execute();
        return response;

    }

    private static OkHttpClient createAuthentificationClient(
            final String username, final String password) {

        OkHttpClient httpClient = new OkHttpClient.Builder()
                .connectTimeout(EaptekaHttp.CONNECT_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(EaptekaHttp.WRITE_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(EaptekaHttp.READ_TIMEOUT, TimeUnit.SECONDS)
                .retryOnConnectionFailure(EaptekaHttp.RETRY_ON_CONNECTION_FAILURE)
                .authenticator(new Authenticator() {
            @Override
            public Request authenticate(Route route, Response response) {
                String credentials = Credentials.basic(username, password);
                return response.request().newBuilder().header(AUTHORIZATION, credentials).build();
            }
        }).build();
        return httpClient;
    }

    private Request getRequest(String url, String apiKey) {
        return new Request.Builder()
                .url(url)
                .header(CONTENT_TYPE_NAME, CONTENT_TYPE_APPLICATION_JSON)
                .header(PLATFORM_NAME, PLATFORM_ANDROID)
                .header(API_KEY_NAME, apiKey)
                .build();
    }
}
