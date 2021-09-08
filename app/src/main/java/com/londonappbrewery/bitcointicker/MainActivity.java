package com.londonappbrewery.bitcointicker;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.londonappbrewery.bitcointicker.model.CoinModel;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;


public class MainActivity extends AppCompatActivity {

    // Constants:
    // TODO: Create the base URL
    private final String BASE_URL = "https://apiv2.bitcoinaverage.com/";
    private final String API_KEY = "ZTZkNDU3NGUyNDU1NGM2NjljODBiNTFjM2VmYzhmZjA";
    private final String MARKET = "global";
    private final String SYMBOL = "BTC";

    // Member Variables:
    private TextView mPriceTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPriceTextView = (TextView) findViewById(R.id.priceLabel);
        Spinner spinner = (Spinner) findViewById(R.id.currency_spinner);

        // Create an ArrayAdapter using the String array and a spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.currency_array, R.layout.spinner_item);

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);

        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);


        // TODO: Set an OnItemSelected listener on the spinner
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String currency = spinner.getSelectedItem().toString();
                String finalUrl = BASE_URL + "/indices/" + MARKET + "/ticker/" + SYMBOL + currency;
                letsDoSomeNetworking(finalUrl, currency);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    // TODO: complete the letsDoSomeNetworking() method
    private void letsDoSomeNetworking(String url, String currency) {

        AsyncHttpClient client = new AsyncHttpClient();

        client.addHeader("x-ba-key", API_KEY);

        client.get(url, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                // called when response HTTP status is "200 OK"
                log( "JSON: " + response.toString());
                try {
                    int value = response.getInt("ask");
                    CoinModel coin = new CoinModel(value, currency);
                    updateUI(coin);

                } catch (JSONException e) {
                    log("ERROR: " + e.getLocalizedMessage());
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String err, Throwable e) {
                // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                log("Request fail! Status code: " + statusCode);
                log("Fail Message: " + err);
            }
        });

    }

    private void updateUI(CoinModel coin){
        mPriceTextView.setText(String.valueOf(coin.getFullCurency()));
    }

    private void log(String msg) {
        Log.d("BitCoin", msg);
    }
}
