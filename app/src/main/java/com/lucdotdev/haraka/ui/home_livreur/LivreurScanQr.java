package com.lucdotdev.haraka.ui.home_livreur;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.lucdotdev.haraka.R;
import com.lucdotdev.haraka.helpers.CustomLoadingDialog;
import com.lucdotdev.haraka.utils.OrientedQrScan;

import java.util.Objects;

import static android.widget.Toast.LENGTH_LONG;


public class LivreurScanQr extends AppCompatActivity {

    FirebaseFirestore firebaseFirestore;
    Bundle extras;
    final CustomLoadingDialog customLoadingDialog = new CustomLoadingDialog(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.livreur_scan_qr);
        this.firebaseFirestore = FirebaseFirestore.getInstance();
        this.extras = getIntent().getExtras();

        ImageView qrGift = findViewById(R.id.qrGift);

        Glide.with(this)
                .load(R.raw.scan)
                .into(qrGift);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null) {
            if(result.getContents() == null) {
                Toast.makeText(this, "Cancelled", LENGTH_LONG).show();
            } else {
                confirmDelivery(Objects.requireNonNull(result.getContents()));
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    public void scanQr(View view) {
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setCaptureActivity(OrientedQrScan.class);
        integrator.initiateScan();
    }

    public void confirmDelivery(String id){
        customLoadingDialog.startLoading();


        String delivery_id = extras.getString("id");
        assert delivery_id != null;
        firebaseFirestore.collection("delivery").document(delivery_id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful())
                {
                    if(task.getResult().get("delivery_id")== id){
                       upDateDelivery(delivery_id);
                    } else {
                        Toast.makeText(LivreurScanQr.this, "C'est ne pas le bon QR CODE", LENGTH_LONG).show();
                    }
                }
                else {
                    customLoadingDialog.dismissLoading();
                    Toast.makeText(LivreurScanQr.this, "Il y a eu une erreur", LENGTH_LONG).show();
                }
            }
        });
    }


    public void upDateDelivery(String delivery_id){
        firebaseFirestore.collection("delivery").document(delivery_id).update("status", 2).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                customLoadingDialog.ok();
                Handler handler = new Handler();

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        customLoadingDialog.dismissLoading();
                        finish();
                    }
                }, 2000);
            }
        });
    }
}