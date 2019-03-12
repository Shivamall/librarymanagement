package com.studentlibrary.librarymanagement;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Shiva XiD on 01-11-2017.
 */

public class SharedPreferenceUtility {

    private final SharedPreferences mSharedPreferences;

    public SharedPreferenceUtility(Context context) {
        mSharedPreferences = context.getSharedPreferences("my_preferences", Context.MODE_PRIVATE);
    }

    public String getName() {
        return mSharedPreferences.getString("name", null);
    }

    public void setName(String name) {
        mSharedPreferences.edit().putString("name", name).apply();
    }
}
