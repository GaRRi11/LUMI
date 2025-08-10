package com.example.myapplication3;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btnEarn).setOnClickListener(v -> {
            startActivity(new Intent(this, EarnActivity.class));
        });

        findViewById(R.id.btnWithdraw).setOnClickListener(v -> {
            startActivity(new Intent(this, WithdrawActivity.class));
        });
    }
}
