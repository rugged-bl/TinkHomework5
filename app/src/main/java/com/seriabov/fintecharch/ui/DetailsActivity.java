package com.seriabov.fintecharch.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.seriabov.fintecharch.R;
import com.seriabov.fintecharch.data.entities.CoinInfo;
import com.seriabov.fintecharch.util.TextColoringUtil;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

public class DetailsActivity extends AppCompatActivity {

    private static final String EXTRA_INFO = "extra_info";

    ActionBar actionBar;
    ImageView logo;
    TextView title;
    TextView price;
    TextView change7d;
    TextView change24h;
    TextView change1h;
    TextView marketCap;
    TextView lastUpdate;

    public static void start(Activity activity, CoinInfo coinInfo) {
        Intent intent = new Intent(activity, DetailsActivity.class);
        intent.putExtra(EXTRA_INFO, coinInfo);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        actionBar = getSupportActionBar();
        logo = findViewById(R.id.coin_logo);
        title = findViewById(R.id.coin_title);
        price = findViewById(R.id.price_value);
        change7d = findViewById(R.id.change_value_7d);
        change24h = findViewById(R.id.change_value_24h);
        change1h = findViewById(R.id.change_value_1h);
        marketCap = findViewById(R.id.market_cap_value);
        lastUpdate = findViewById(R.id.last_update_value);

        CoinInfo coinInfo = (CoinInfo) getIntent().getSerializableExtra(EXTRA_INFO);
        if (coinInfo != null)
            showCoinInfo(coinInfo);
    }

    void showCoinInfo(CoinInfo info) {
        actionBar.setTitle(info.getSymbol());

        String logoUrl = getString(R.string.coin_logo_url, info.getSymbol().toLowerCase());
        Glide.with(this)
                .load(logoUrl)
                .into(logo);

        title.setText(info.getName());

        price.setText(getString(R.string.price_format, info.getPriceUsd()));

        TextColoringUtil.applyPercentStyleAndText(change7d, info.getPercentChange7d());

        TextColoringUtil.applyPercentStyleAndText(change24h, info.getPercentChange24h());

        TextColoringUtil.applyPercentStyleAndText(change1h, info.getPercentChange1h());

        marketCap.setText(getString(R.string.price_format, info.getMarketCapUsd()));

        lastUpdate.setText(DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.MEDIUM, Locale.getDefault())
                .format(new Date(info.getLastUpdated() * 1000)));
    }
}
