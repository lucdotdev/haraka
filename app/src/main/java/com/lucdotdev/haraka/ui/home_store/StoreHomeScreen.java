package com.lucdotdev.haraka.ui.home_store;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.lucdotdev.haraka.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class StoreHomeScreen extends AppCompatActivity {

    private FirebaseAuth mAuth;
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.store_home_screen);

        this.mAuth = FirebaseAuth.getInstance();
        TextView welcomeText = findViewById(R.id.welcomeText);

        Bundle extras = getIntent().getExtras();
        String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
        int valableTime = Integer.parseInt(currentTime.substring(0,2));

        if(valableTime >= 7 && valableTime<=16){
            assert extras != null;
            welcomeText.setText("Bonjour "+extras.getString("name") +"!");
        } else {
            assert extras != null;
            welcomeText.setText("Bonsoir "+ extras.getString("name")+ "!");
        }
    }

    public void gotoaddDelivery(View view) {
        Intent i = new Intent(StoreHomeScreen.this, StoreAddDelivery.class);
        startActivity(i);
    }

    public void storeLogout(View view) {
        mAuth.signOut();
        finish();
    }

    public void gotomyDelivery(View view) {
        Intent i = new Intent(StoreHomeScreen.this, StoreMyDelivery.class);
        startActivity(i);
    }
}