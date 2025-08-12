package com.example.myapplication3;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.startapp.sdk.adsbase.StartAppSDK;

public class MainActivity extends BaseActivity {

    private static final int PERMISSION_REQUEST_CODE = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkPermissionAndInit();

        findViewById(R.id.btnEarn).setOnClickListener(v -> {
            startActivity(new Intent(this, EarnActivity.class));
        });

        findViewById(R.id.btnWithdraw).setOnClickListener(v -> {
            startActivity(new Intent(this, WithdrawActivity.class));
        });
    }

    private void checkPermissionAndInit() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED) {
            // Request permission
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_PHONE_STATE},
                    PERMISSION_REQUEST_CODE);
        } else {
            // Permission already granted
            initStartAppSDK();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted
                initStartAppSDK();
            } else {
                // Permission denied
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_PHONE_STATE)) {
                    // Show rationale and ask again
                    showPermissionExplanationDialog();
                } else {
                    // Permission denied permanently ("Don't ask again")
                    showPermissionDeniedForeverDialog();
                }
            }
        }
    }

    private void showPermissionExplanationDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Permission Required")
                .setMessage("This app requires the Phone State permission to function properly. Please grant it.")
                .setCancelable(false)
                .setPositiveButton("Grant", (dialog, which) -> {
                    ActivityCompat.requestPermissions(MainActivity.this,
                            new String[]{Manifest.permission.READ_PHONE_STATE},
                            PERMISSION_REQUEST_CODE);
                })
                .setNegativeButton("Exit App", (dialog, which) -> {
                    finish();
                })
                .show();
    }

    private void showPermissionDeniedForeverDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Permission Denied")
                .setMessage("You have denied the permission permanently. Please enable it from app settings.")
                .setCancelable(false)
                .setPositiveButton("Open Settings", (dialog, which) -> {
                    Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    intent.setData(Uri.parse("package:" + getPackageName()));
                    startActivity(intent);
                })
                .setNegativeButton("Exit App", (dialog, which) -> {
                    finish();
                })
                .show();
    }

    private void initStartAppSDK() {
        StartAppSDK.init(this, "207903435", true);
    }
}
