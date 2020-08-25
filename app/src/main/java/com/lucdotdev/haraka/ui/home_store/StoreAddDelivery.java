package com.lucdotdev.haraka.ui.home_store;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.lucdotdev.haraka.R;
import com.lucdotdev.haraka.helpers.CustomLoadingDialog;
import com.lucdotdev.haraka.ui.SplashScreen;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class StoreAddDelivery extends AppCompatActivity {

    private EditText deliveryName;
    private EditText deliveryClientName;
    private EditText deliveryClientNumber;
    private EditText deliveryClientAdress;
    private EditText deliveryDesc;

    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;


    private ImageView imageView;
    private Uri filePath;
    private final int PICK_IMAGE_REQUEST = 71;
    FirebaseStorage storage;
    StorageReference storageReference;
    private String photoPath;


    private final CustomLoadingDialog customLoadingDialog = new CustomLoadingDialog(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.store_add_delivery);

        this.firebaseFirestore = FirebaseFirestore.getInstance();
        this.firebaseAuth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();




        this.deliveryName = findViewById(R.id.myDeliveryName);
        this.deliveryClientName = findViewById(R.id.myDeliveryClient);
        this.deliveryClientNumber = findViewById(R.id.myDeliveryClientNumber);
        this.deliveryClientAdress = findViewById(R.id.myDeliveryClientAdress);
        this.deliveryDesc = findViewById(R.id.myDeliveryDesc);


        Button btnChoose = (Button) findViewById(R.id.btnChoose);
        Button btnUpload = (Button) findViewById(R.id.btnUpload);
        imageView = (ImageView) findViewById(R.id.imgView);

        btnChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadImage();
            }
        });

    }


    public void addDelivery(View view) {
        if(photoPath == null){
            Toast.makeText(StoreAddDelivery.this, "Veuillez téléverser une image avant de continuer", Toast.LENGTH_SHORT).show();

        } else {
            customLoadingDialog.startLoading();

            @SuppressLint("SimpleDateFormat")
            SimpleDateFormat ISO_8601_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:sss'Z'");
            String now = ISO_8601_FORMAT.format(new Date());

            final UUID uuid = UUID.randomUUID();
            final Map<String, Object> item = new HashMap<>();
            item.put("name", deliveryName.getText().toString().trim());
            item.put("delivery_id", uuid.toString());
            item.put("livreur_id", "");
            item.put("store_id", Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid());
            item.put("status", 1);
            item.put("description", deliveryDesc.getText().toString());
            item.put("client_adress", deliveryClientAdress.getText().toString());
            item.put("client_number", deliveryClientNumber.getText().toString());
            item.put("client_name", deliveryClientName.getText().toString());
            item.put("photoPath", photoPath);

            firebaseFirestore.collection("delivery").add(item).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                @Override
                public void onComplete(@NonNull Task<DocumentReference> task) {

                    if (task.isComplete()){
                        customLoadingDialog.ok();

                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {

                                Intent intent=new Intent(StoreAddDelivery.this, StoreQrCodeScreen.class);
                                intent.putExtra("qr", uuid.toString());
                                startActivity(intent);
                                finish();
                            }
                        },2000);

                    }
                    else {
                        customLoadingDialog.dismissLoading();
                        Toast.makeText(StoreAddDelivery.this, "Echec", Toast.LENGTH_LONG).show();
                    }
                }
            });

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null )
        {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                imageView.setImageBitmap(bitmap);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    public void goToStoreMain(View view) {
        finish();
    }

    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Selectionner une image"), PICK_IMAGE_REQUEST);
    }




    private void uploadImage() {

        if(filePath != null)
        {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Téléversement...");
            progressDialog.show();

            final StorageReference ref = storageReference.child("images/"+ UUID.randomUUID().toString());
            ref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    photoPath = uri.toString();
                                }
                            });
                            Toast.makeText(StoreAddDelivery.this, "L'image a été téléversée", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(StoreAddDelivery.this, "Echec "+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot
                                    .getTotalByteCount());
                            progressDialog.setMessage("Téléversement "+(int)progress+"%");
                        }
                    });
        }
        else {
            Toast.makeText(StoreAddDelivery.this, "Veuillez choisir une image", Toast.LENGTH_SHORT).show();
        }
    }

}