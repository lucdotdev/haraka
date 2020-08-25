package com.lucdotdev.haraka.ui.home_livreur;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.firebase.ui.firestore.SnapshotParser;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.lucdotdev.haraka.R;
import com.lucdotdev.haraka.adapters.LivreurRecycleAdapterMyDelivery;
import com.lucdotdev.haraka.models.Delivery;

import java.util.Objects;

public class LivreurHomeScreen extends AppCompatActivity implements LivreurRecycleAdapterMyDelivery.OnEmptyList, LivreurRecycleAdapterMyDelivery.OnListItemClick{

    private FirestoreRecyclerAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.livreur_home_screen);

        RecyclerView myDeliveryRecycleView = findViewById(R.id.livreurMyDeliveryRecycle);

        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();




        Query query = firebaseFirestore.collection("delivery")
                .whereEqualTo("livreur_id", Objects.requireNonNull(mAuth.getCurrentUser()).getUid());
        FirestoreRecyclerOptions<Delivery> options = new FirestoreRecyclerOptions.Builder<Delivery>()
                .setQuery(query, snapshot -> {
                    Delivery item = snapshot.toObject(Delivery.class);
                    assert item != null;
                    item.setId(snapshot.getId());
                    return item;
                })
                .build();


       adapter = new LivreurRecycleAdapterMyDelivery(options, (LivreurRecycleAdapterMyDelivery.OnListItemClick) this, (Context) this, (LivreurRecycleAdapterMyDelivery.OnEmptyList) this);


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
        Intent i = new Intent(this, LivreurScanQr.class);
        i.putExtra("id", item.getId());
        startActivity(i);
    }

    @Override
    public void onEmpty(boolean k) {

    }

}