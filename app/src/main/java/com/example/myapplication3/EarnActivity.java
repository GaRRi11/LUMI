package com.example.myapplication3;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class EarnActivity extends AppCompatActivity {

    private TextView txtCoins;
    private int coins;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_earn);

        txtCoins = findViewById(R.id.txtCoins);
        Button btnWatchAd = findViewById(R.id.btnWatchAd);

        coins = DataStoreUtils.getCoins(this);
        updateCoinsDisplay();

        btnWatchAd.setOnClickListener(v -> {
            // Instead of showing ad, instantly give reward
            coins += 10;
            DataStoreUtils.saveBalances(EarnActivity.this, coins, DataStoreUtils.getCash(EarnActivity.this));
            updateCoinsDisplay();
        });
    }

    private void updateCoinsDisplay() {
        txtCoins.setText(coins + " Coins");
    }
}
