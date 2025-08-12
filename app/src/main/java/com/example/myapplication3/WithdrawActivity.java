package com.example.myapplication3;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;


public class WithdrawActivity extends BaseActivity  {

    private int coins, cash;
    private TextView txtBalances;
    private EditText edtWithdraw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_withdraw);

        txtBalances = findViewById(R.id.txtBalances);
        edtWithdraw = findViewById(R.id.edtWithdraw);
        Button btnWithdraw = findViewById(R.id.btnWithdraw);

        coins = DataStoreUtils.getCoins(this);
        cash = DataStoreUtils.getCash(this);
        updateBalanceDisplay();

        btnWithdraw.setOnClickListener(v -> handleWithdraw());
    }

    private void updateBalanceDisplay() {
        txtBalances.setText("Coins: " + coins + "\nCash: $" + cash);
    }

    private void handleWithdraw() {
        String input = edtWithdraw.getText().toString().trim();
        if (input.isEmpty()) return;

        int amount = Integer.parseInt(input);
        if (amount < 100) {
            showMessage("Minimum withdraw is $100");
        } else if (amount > cash) {
            showMessage("Your balance is $" + cash);
        } else {
            cash -= amount;
            DataStoreUtils.saveBalances(this, coins, cash);
            updateBalanceDisplay();
            showMessage("$" + amount + " withdrawn!");
        }
    }

    private void showMessage(String msg) {
        new AlertDialog.Builder(this)
                .setMessage(msg)
                .setPositiveButton("OK", null)
                .show();
    }
}

