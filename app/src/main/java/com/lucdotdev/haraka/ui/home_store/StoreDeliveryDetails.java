package com.lucdotdev.haraka.ui.home_store;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.lucdotdev.haraka.R;

import java.util.Objects;

public class StoreDeliveryDetails extends AppCompatActivity {

    Bundle extra;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.store_delivery_details);

        extra = getIntent().getExtras();

        TextView name = findViewById(R.id.deliveryDetailsName);
        //TextView adress = findViewById(R.id.deliveryDetailsAdress);
        TextView client = findViewById(R.id.deliveryDetailsDestinataire);
        TextView desc = findViewById(R.id.deliveryDetailsDescr);
        TextView deliveryStatus = findViewById(R.id.deliveryDetailsStatus);
        ImageView img = findViewById(R.id.deliveryDetailsImage);
        LinearLayout statusBackground = findViewById(R.id.deliveryDetailsStatusBackground);


        name.setText(extra.getString("name"));
        //adress.setText(extra.getString("adress"));
        client.setText(extra.getString("client"));
        desc.setText(extra.getString("desc"));

        if(extra.getString("photo")!=null){
            Glide.with(this)
                    .load(extra.getString("photo"))
                    .fitCenter()
                    .into(img);
        }
        switch (extra.getInt("status")){
            case 1:
                deliveryStatus.setText("En cours");
                statusBackground.setBackgroundResource(R.drawable.status_pending);
                break;
            case 2:
                deliveryStatus.setText("Réussi");
                statusBackground.setBackgroundResource(R.drawable.status_ok);
                break;
            case 3:
                deliveryStatus.setText("échec");
                statusBackground.setBackgroundResource(R.drawable.status_no);
                break;
            default:
                System.out.println("OUUUUUUUUUT");
        }


    }

    public void gotoQr(View view) {
        Intent intent=new Intent(this, StoreQrCodeScreen.class);
        intent.putExtra("qr", extra.getString("id"));
        startActivity(intent);
    }

    public void goToStoreMain(View view) {
        finish();
    }
}