package su.zencode.testapp04;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import su.zencode.testapp04.EaptekaApiClient.TestAppClient;

public class LaunchActivity extends AppCompatActivity {
    private static final String TAG = "LaunchActivity22";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);

        AsyncTask<Integer,Void,String> fetchCategoryTask = new AsyncTask<Integer, Void, String>() {
            @Override
            protected String doInBackground(Integer... values) {
                return new TestAppClient().fetchCategory(values[0],"eapteka","stage");
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                Log.d(TAG, s);
            }
        };
        AsyncTask<Integer,Void,String> fetchOffersTask = new AsyncTask<Integer, Void, String>() {
            @Override
            protected String doInBackground(Integer... values) {
                return new TestAppClient().fetchOffers(values[0],"eapteka","stage");
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                Log.d(TAG, s);
            }
        };
        fetchCategoryTask.execute(3817);
        fetchOffersTask.execute(7583);
    }
}
