package com.lucdotdev.haraka.ui.home_store;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.lucdotdev.haraka.R;
import com.lucdotdev.haraka.adapters.StoreRecycleAdapterMyDelivery;
import com.lucdotdev.haraka.models.Delivery;

import java.util.Objects;

public class StoreMyDelivery extends AppCompatActivity implements StoreRecycleAdapterMyDelivery.OnEmptyList, StoreRecycleAdapterMyDelivery.OnListItemClick {

    private FirestoreRecyclerAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.store_my_delivery);


        RecyclerView myDeliveryRecycleView = findViewById(R.id.myDeliveryRecycle);

        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();




        Query query = firebaseFirestore.collection("delivery")
                .whereEqualTo("store_id", Objects.requireNonNull(mAuth.getCurrentUser()).getUid());
        FirestoreRecyclerOptions<Delivery> options = new FirestoreRecyclerOptions.Builder<Delivery>()
                .setQuery(query, Delivery.class)
                .build();


        this.adapter = new StoreRecycleAdapterMyDelivery(options, this, (Context) this, (StoreRecycleAdapterMyDelivery.OnEmptyList) this);


        myDeliveryRecycleView .setLayoutManager(new LinearLayoutManager(this));
        myDeliveryRecycleView.setNestedScrollingEnabled(false);
        myDeliveryRecycleView .setAdapter(adapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    @Override
    public void onItemClick(Delivery item, int position) {
        Intent i = new Intent(this, StoreDeliveryDetails.class);
        i.putExtra("name", item.getClient_name());
        i.putExtra("adress", item.getClient_adress());
        i.putExtra("status", item.getStatus());
        i.putExtra("photo", item.getPhotoPath());
        i.putExtra("desc", item.getDescription());
        i.putExtra("client", item.getClient_name());
        i.putExtra("id", item.getDelivery_id());
        startActivity(i);
    }

    @Override
    public void onEmpty(boolean k) {

        TextView notItem = findViewById(R.id.emptyList);
        if(!k){
            notItem.setVisibility(View.GONE);
        }else {
            notItem.setVisibility(View.VISIBLE);
        }
    }

    public void goToStoreMain(View view) {
        finish();
    }
}