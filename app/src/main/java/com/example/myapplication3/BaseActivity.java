package com.example.myapplication3;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;

import androidx.appcompat.app.AppCompatActivity;

public class BaseActivity extends AppCompatActivity {

    private FrameLayout noInternetLayout;

    private final BroadcastReceiver networkReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (isConnected(context)) {
                noInternetLayout.setVisibility(View.GONE);
            } else {
                noInternetLayout.setVisibility(View.VISIBLE);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Note: don't call setContentView here,
        // subclasses must call it and include the noInternetLayout with id noInternetLayout
    }

    @Override
    protected void onStart() {
        super.onStart();
        registerReceiver(networkReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(networkReceiver);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (noInternetLayout != null) {
            noInternetLayout.setVisibility(isConnected(this) ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        // Expect every layout to have this view included
        noInternetLayout = findViewById(R.id.noInternetLayout);
        if (noInternetLayout == null) {
            throw new RuntimeException("Layout must include a View with id 'noInternetLayout'");
        }
    }

    protected boolean isConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm != null) {
            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            return activeNetwork != null && activeNetwork.isConnected();
        }
        return false;
    }
}
