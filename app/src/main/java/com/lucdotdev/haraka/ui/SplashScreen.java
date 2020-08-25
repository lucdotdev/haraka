package com.lucdotdev.haraka.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.lucdotdev.haraka.R;
import com.lucdotdev.haraka.ui.home_livreur.LivreurHomeScreen;
import com.lucdotdev.haraka.ui.home_livreur.LivreurScanQr;
import com.lucdotdev.haraka.ui.home_store.StoreHomeScreen;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        SharedPreferences prefs = getSharedPreferences("AUTH", MODE_PRIVATE);
        int type = prefs.getInt("account_type", 2);
       /// boolean isLogin = prefs.getBoolean("is_login", false);
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if(currentUser!= null) {
            Handler handler = new Handler();
            if(type == 1){
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent=new Intent(SplashScreen.this, StoreHomeScreen.class);
                        startActivity(intent);
                        finish();
                    }
                },2000);
            } else if(type== 2){
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent=new Intent(SplashScreen.this, LivreurHomeScreen.class);
                        startActivity(intent);
                        finish();
                    }
                },2000);
            }
        } else {
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent=new Intent(SplashScreen.this, LoginScreen.class);
                    startActivity(intent);
                    finish();
                }
            },2000);
        }
    }
    

}