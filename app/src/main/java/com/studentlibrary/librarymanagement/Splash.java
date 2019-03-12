package com.studentlibrary.librarymanagement;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class Splash extends AppCompatActivity {
    private static int SPLASH_TIME_OUT = 3000;
    private SharedPreferenceUtility sharedPreferenceUtility;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        sharedPreferenceUtility=new SharedPreferenceUtility(this);

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                String mUsername=sharedPreferenceUtility.getName();
                if (mUsername==null) {
                    Intent login=new Intent(Splash.this,LoginActivity.class);
                    startActivity(login);
                }
                else
                {
                    Intent i = new Intent(Splash.this, MainActivity.class);
                    startActivity(i);
                }
                finish();
            }
        }, SPLASH_TIME_OUT);

    }
}
