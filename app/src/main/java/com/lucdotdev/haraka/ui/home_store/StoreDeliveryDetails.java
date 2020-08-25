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
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lucdotdev.haraka.R;

import java.util.Objects;

public class StoreDeliveryDetails extends AppCompatActivity {

    Bundle extra;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.store_delivery_details);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow();
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }

        extra = getIntent().getExtras();

        TextView name = findViewById(R.id.deliveryDetailsName);
        TextView adress = findViewById(R.id.deliveryDetailsAdress);
        TextView client = findViewById(R.id.deliveryDetailsDestination);
        TextView desc = findViewById(R.id.deliveryDetailsDescription);
        TextView status = findViewById(R.id.deliveryDetailsStatus);
        ImageView img = findViewById(R.id.deliveryDetailsImage);


        name.setText(extra.getString("name"));
        adress.setText(extra.getString("adress"));
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
                status.setText("En cours");
                //holder.statusIcon.setBackgroundTintList(ColorStateList.valueOf(0xff88B04B));
                break;
            case 2:
                status.setText("Réussi");
                //holder.statusIcon.setBackgroundTintList(ColorStateList.valueOf(0xff88B04B));
                break;
            case 3:
                status.setText("échec");
                //holder.statusIcon.setBackgroundTintList(ColorStateList.valueOf(0xffFF6F61));
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