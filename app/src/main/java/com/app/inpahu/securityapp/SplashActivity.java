package com.app.inpahu.securityapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.app.inpahu.securityapp.Helpers.PreferencesHelper;
import com.app.inpahu.securityapp.Objects.User;

public class SplashActivity extends AppCompatActivity {

    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        mContext = this;

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                User session  = PreferencesHelper.getUser(getApplicationContext());
                Intent nextActivity = new Intent(mContext, LoginActivity.class);

                if(session != null)
                    nextActivity = new Intent(mContext, MainActivity.class);

                startActivity(nextActivity);
                finish();

            }
        }, 2000);
    }

}
