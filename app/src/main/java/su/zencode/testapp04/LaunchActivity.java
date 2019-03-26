package su.zencode.testapp04;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import su.zencode.testapp04.EaptekaApiClient.TestAppClient;

public class LaunchActivity extends AppCompatActivity {
    private static final String TAG = "LaunchActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);

        AsyncTask<Void,Void,String> asyncTask = new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... voids) {
                return new TestAppClient().fetch("","eapteka","stage");
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                Log.d(TAG, s);
            }
        };
        asyncTask.execute();
    }
}
