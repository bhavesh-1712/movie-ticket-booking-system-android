package com.softwarehub.movieticketbookingsystem;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.softwarehub.movieticketbookingsystem.SharedPreference.SharedPreferenceKeys;
import com.softwarehub.movieticketbookingsystem.SharedPreference.Utilities;

public class SplashActivity extends AppCompatActivity {

    private Handler handler;
    private ProgressBar progressBar;
    private static int checkNet = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);


        Snackbar mySnackbar = Snackbar.make(findViewById(R.id.my_constraint_layout_splash_activity), "Your Internet Connection Is Off", Snackbar.LENGTH_SHORT);
        if (isInternetOn() == false) {
            mySnackbar.show();
            checkNet = 1;
        }

        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (checkNet == 0) {
                    String userId = Utilities.getInstance().getPreference(SplashActivity.this, SharedPreferenceKeys.user_id);
                    if (TextUtils.isEmpty(userId)) {
                        Intent intent = new Intent(SplashActivity.this,LoginActivity.class);
                        startActivity(intent);
                    }else {
                        Intent intent = new Intent(SplashActivity.this,MainActivity.class);
                        startActivity(intent);
                    }
                } else {
                    Toast.makeText(SplashActivity.this, "Please start your internet", Toast.LENGTH_SHORT).show();
                }
            }
        }, 3000);
    }

    private boolean isInternetOn() {
        ConnectivityManager connec = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connec.getNetworkInfo(0).getState() == NetworkInfo.State.CONNECTED ||
                connec.getNetworkInfo(0).getState() == NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == NetworkInfo.State.CONNECTED ||
                connec.getNetworkInfo(0).getState() == NetworkInfo.State.CONNECTING) {
            return true;
        } else if (connec.getNetworkInfo(0).getState() == NetworkInfo.State.DISCONNECTED ||
                connec.getNetworkInfo(1).getState() == NetworkInfo.State.DISCONNECTING) {
            return false;
        }
        return false;
    }
}