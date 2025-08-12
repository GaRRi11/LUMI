package com.example.myapplication3;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.startapp.sdk.adsbase.Ad;
import com.startapp.sdk.adsbase.StartAppAd;
import com.startapp.sdk.adsbase.StartAppSDK;
import com.startapp.sdk.adsbase.adlisteners.AdDisplayListener;
import com.startapp.sdk.adsbase.adlisteners.AdEventListener;
import com.startapp.sdk.adsbase.adlisteners.VideoListener;

public class EarnActivity extends BaseActivity {

    private static final int REWARD_AMOUNT = 10;

    private TextView txtCoins;
    private int coins;

    private StartAppAd rewardedVideoAd;
    private boolean adReady = false;
    private boolean rewardedThisAd = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        StartAppSDK.setTestAdsEnabled(true);

        //     StartAppSDK.init(this, "207903435", true);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_earn);

        txtCoins = findViewById(R.id.txtCoins);
        Button btnWatchAd = findViewById(R.id.btnWatchAd);

        coins = DataStoreUtils.getCoins(this);
        updateCoinsDisplay();


        rewardedVideoAd = new StartAppAd(this);

        // Create a reusable AdEventListener so we can set adReady when loaded
        AdEventListener adEventListener = new AdEventListener() {
            @Override
            public void onReceiveAd(@NonNull Ad ad) {
                adReady = true;
                rewardedThisAd = false; // reset reward guard for the new ad
            }

            @Override
            public void onFailedToReceiveAd(@Nullable Ad ad) {
                adReady = false;
            }
        };

        // initial load (listener will flip adReady when loaded)
        rewardedVideoAd.loadAd(StartAppAd.AdMode.REWARDED_VIDEO, adEventListener);

        btnWatchAd.setOnClickListener(v -> {
            if (adReady) {
                // set the video listener BEFORE showing the ad (required for rewarded flow)
                rewardedVideoAd.setVideoListener(new VideoListener() {
                    @Override
                    public void onVideoCompleted() {
                        if (!rewardedThisAd) {
                            coins += REWARD_AMOUNT;
                            DataStoreUtils.saveCoins(EarnActivity.this, coins);
                            updateCoinsDisplay();
                            rewardedThisAd = true;
                        }
                    }
                });

                // show the ad and handle display lifecycle
                rewardedVideoAd.showAd(new AdDisplayListener() {
                    @Override
                    public void adHidden(Ad ad) {
                        // an ad was closed — load next one
                        adReady = false;
                        rewardedVideoAd.loadAd(StartAppAd.AdMode.REWARDED_VIDEO, adEventListener);
                    }

                    @Override
                    public void adDisplayed(Ad ad) { /* optional */ }

                    @Override
                    public void adClicked(Ad ad) { /* optional */ }

                    @Override
                    public void adNotDisplayed(Ad ad) {
                        // failed to show - try loading again
                        adReady = false;
                        rewardedVideoAd.loadAd(StartAppAd.AdMode.REWARDED_VIDEO, adEventListener);
                    }
                });
            } else {
                // not ready yet — start loading and optionally notify user
                rewardedVideoAd.loadAd(StartAppAd.AdMode.REWARDED_VIDEO, adEventListener);
                // optionally show a Snackbar/Toast: "Ad is loading, try again in a second"
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // if you want to be extra sure: trigger load when coming back
        if (!adReady) {
            rewardedVideoAd.loadAd(StartAppAd.AdMode.REWARDED_VIDEO, new AdEventListener() {
                @Override
                public void onReceiveAd(@NonNull Ad ad) {
                    adReady = true;
                    rewardedThisAd = false;
                }

                @Override
                public void onFailedToReceiveAd(@Nullable Ad ad) {
                    adReady = false;
                }
            });
        }
    }

    private void updateCoinsDisplay() {
        txtCoins.setText(String.valueOf(coins));
    }
}
