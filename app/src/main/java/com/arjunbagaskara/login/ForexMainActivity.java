package com.arjunbagaskara.login;

import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import java.text.DecimalFormat;

import cz.msebera.android.httpclient.Header;

public class ForexMainActivity extends AppCompatActivity {
    private ProgressBar loadingProgressBar;
    private SwipeRefreshLayout swipeRefreshLayout1;
    private TextView idrTextView, ilsTextView, impTextView, usdTextView, iqdTextView, irrTextView, iskTextView, jepTextView, jmdTextView, jodTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forex_main);

        swipeRefreshLayout1 = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout1);
        idrTextView = (TextView) findViewById(R.id.idrTextView);
        ilsTextView = (TextView) findViewById(R.id.ilsTextView);
        impTextView = (TextView) findViewById(R.id.impTextView);
        usdTextView = (TextView) findViewById(R.id.usdTextView);
        iqdTextView = (TextView) findViewById(R.id.iqdTextView);
        irrTextView = (TextView) findViewById(R.id.irrTextView);
        iskTextView = (TextView) findViewById(R.id.iskTextView);
        jepTextView = (TextView) findViewById(R.id.jepTextView);
        jmdTextView = (TextView) findViewById(R.id.jmdTextView);
        jodTextView = (TextView) findViewById(R.id.jodTextView);
        loadingProgressBar = (ProgressBar) findViewById(R.id.loadingProgressBar);

        initSwipeRefreshLayout();
        initForex();
    }

    private void initSwipeRefreshLayout() {
        swipeRefreshLayout1.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initForex();

                swipeRefreshLayout1.setRefreshing(false);
            }
        });
    }

    public String formatNumber(double number, String format) {
        DecimalFormat decimalFormat = new DecimalFormat(format);
        return decimalFormat.format(number);
    }

    private void initForex() {
        loadingProgressBar.setVisibility(TextView.VISIBLE);

        String url = "https://openexchangerates.org/api/latest.json?app_id=d7947806abc94b4fae6d40e007a89ac6";

        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        asyncHttpClient.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Gson gson = new Gson();
                ForexRootModel rootModel = gson.fromJson(new String(responseBody), ForexRootModel.class);
                ForexRatesModel ratesModel = rootModel.getRatesModel();

                double ils = ratesModel.getIDR() / ratesModel.getILS();
                double imp = ratesModel.getIDR() / ratesModel.getIMP();
                double usd = ratesModel.getIDR() / ratesModel.getUSD();
                double iqd = ratesModel.getIDR() / ratesModel.getIQD();
                double irr = ratesModel.getIDR() / ratesModel.getIRR();
                double isk = ratesModel.getIDR() / ratesModel.getISK();
                double jep = ratesModel.getIDR() / ratesModel.getJEP();
                double jmd = ratesModel.getIDR() / ratesModel.getJMD();
                double jod = ratesModel.getIDR() / ratesModel.getJOD();
                double idr = ratesModel.getIDR();

                idrTextView.setText(formatNumber(idr, "###.##0.##" ));
                ilsTextView.setText(formatNumber(ils, "###.##0.##" ));
                impTextView.setText(formatNumber(imp, "###.##0.##" ));
                usdTextView.setText(formatNumber(usd, "###.##0.##" ));
                iqdTextView.setText(formatNumber(iqd, "###.##0.##" ));
                irrTextView.setText(formatNumber(irr, "###.##0.##" ));
                iskTextView.setText(formatNumber(isk, "###.##0.##" ));
                jepTextView.setText(formatNumber(jep, "###.##0.##" ));
                jmdTextView.setText(formatNumber(jmd, "###.##0.##" ));
                jodTextView.setText(formatNumber(jod, "###.##0.##" ));

                loadingProgressBar.setVisibility(TextView.INVISIBLE);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();

                loadingProgressBar.setVisibility(TextView.INVISIBLE);

            }
        });
    }
}