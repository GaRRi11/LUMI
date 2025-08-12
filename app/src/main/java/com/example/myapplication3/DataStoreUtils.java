package com.example.myapplication3;

import android.content.Context;
import android.content.SharedPreferences;

public class DataStoreUtils {

    private static final String PREFS_NAME = "app_prefs";
    private static final String KEY_COINS = "coins";
    private static final String KEY_CASH = "cash";

    public static int getCoins(Context context) {
        return getPrefs(context).getInt(KEY_COINS, 0);
    }

    public static int getCash(Context context) {
        return getPrefs(context).getInt(KEY_CASH, 0);
    }

    public static void saveBalances(Context context, int coins, int cash) {
        getPrefs(context).edit()
                .putInt(KEY_COINS, coins)
                .putInt(KEY_CASH, cash)
                .apply();
    }

    private static SharedPreferences getPrefs(Context context) {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }


    public static void saveCoins(Context context, int coins) {
        getPrefs(context).edit()
                .putInt(KEY_COINS, coins)
                .apply();
    }
}

