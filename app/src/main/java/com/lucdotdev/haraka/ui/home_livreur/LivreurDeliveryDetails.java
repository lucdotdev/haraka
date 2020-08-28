package com.lucdotdev.haraka.ui.home_livreur;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.lucdotdev.haraka.R;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class LivreurDeliveryDetails extends AppCompatActivity {

    Bundle extra;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.livreur_delivery_details);


        extra = getIntent().getExtras();

        TextView name = findViewById(R.id.deliveryDetailsName);
        TextView adress = findViewById(R.id.deliveryDetailsAdress);
        TextView client = findViewById(R.id.deliveryDetailsDestinataire);
        TextView desc = findViewById(R.id.deliveryDetailsDescr);
        TextView deliveryStatus = findViewById(R.id.deliveryDetailsStatus);
        ImageView img = findViewById(R.id.deliveryDetailsImage);
        LinearLayout statusBackground = findViewById(R.id.deliveryDetailsStatusBackground);


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

    public void gotoConfirm(View view) {
        if(extra.getInt("status")==2){
            Toast.makeText(this,"La Livraison a déja été confirmée", Toast.LENGTH_LONG).show();
        }else if(extra.getInt("status")==3) {
            Toast.makeText(this,"La Livraison a été annulée", Toast.LENGTH_LONG).show();
        }else {
            Intent i = new Intent(this, LivreurScanQr.class);
            i.putExtra("id", extra.getString("delivery_id"));
            i.putExtra("qr_verif", extra.getString("qr_id"));
            startActivity(i);
        }
    }

    public void goToStoreMain(View view) {
        finish();
    }

    public void contact(View view) throws UnsupportedEncodingException {
        String message = "Bonjour *" + extra.getString("client") + "* je vous contact pour le produit  "+ " ...";
        String url= "https://api.whatsapp.com/send?phone=" + "+243" + extra.getString("client_number") +"&text=" + URLEncoder.encode(message, "UTF-8");


        try {
            PackageManager pm = this.getPackageManager();
            pm.getPackageInfo("com.whatsapp", PackageManager.GET_ACTIVITIES);
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            startActivity(i);
        } catch (PackageManager.NameNotFoundException e) {
            Toast.makeText(this, "Whatsapp app not installed in your phone", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }
}