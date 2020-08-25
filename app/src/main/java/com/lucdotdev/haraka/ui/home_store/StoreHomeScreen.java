package com.lucdotdev.haraka.ui.home_store;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.lucdotdev.haraka.R;

public class StoreHomeScreen extends AppCompatActivity {

    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.store_home_screen);

        this.mAuth = FirebaseAuth.getInstance();
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