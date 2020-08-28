package com.lucdotdev.haraka.adapters;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.lucdotdev.haraka.R;
import com.lucdotdev.haraka.models.Delivery;

public class StoreRecycleAdapterMyDelivery extends FirestoreRecyclerAdapter<Delivery, StoreRecycleAdapterMyDelivery.StoreRecycleAdapterMyDeliveryViewHolder> {



    private OnListItemClick onListItemClick;
    private OnEmptyList onEmptyList;
    private Context i;



    public StoreRecycleAdapterMyDelivery(@NonNull FirestoreRecyclerOptions<Delivery> options, OnListItemClick onListItemClick, Context k, OnEmptyList onEmptyList) {
        super(options);
        this.onListItemClick = onListItemClick;
        this.onEmptyList = onEmptyList;
        this.i = k;

    }

    @NonNull
    @Override
    public StoreRecycleAdapterMyDeliveryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.store_recycle_item_my_delivery, parent, false);
        return new StoreRecycleAdapterMyDeliveryViewHolder(view);
    }

    @Override
    public void onError(@NonNull FirebaseFirestoreException e) {
        super.onError(e);
        if(getItemCount()==0){
            onEmptyList.onEmpty(true);

        } else {
            onEmptyList.onEmpty(false);
        }
    }

    @Override
    public void onDataChanged() {
        super.onDataChanged();
        if(getItemCount()==0){
            onEmptyList.onEmpty(true);

        } else {
            onEmptyList.onEmpty(false);
        }

    }




    @Override
    protected void onBindViewHolder(@NonNull StoreRecycleAdapterMyDeliveryViewHolder holder, final int position, @NonNull final Delivery model) {
        holder.deliveryName.setText(model.getName());
      //  if(model.getPhotoPath()!=null){
        //    Glide.with(i)
          //          .load(model.getPhotoPath())
            //        .fitCenter()
              //      .into(holder.deliveryImage);
        //}
        switch (model.getStatus()){
            case 1:
                holder.deliveryStatus.setText("En cours");
                holder.statusBackground.setBackgroundResource(R.drawable.status_pending);
                break;
            case 2:
                holder.deliveryStatus.setText("Réussi");
                holder.statusBackground.setBackgroundResource(R.drawable.status_ok);
                break;
            case 3:
                holder.deliveryStatus.setText("échec");
                holder.statusBackground.setBackgroundResource(R.drawable.status_no);
                break;
            default:
                System.out.println("OUUUUUUUUUT");
        }
    }


    public  class StoreRecycleAdapterMyDeliveryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView deliveryName;
        private ImageView deliveryImage;
        private TextView deliveryStatus;
        private FloatingActionButton statusIcon;
        private LinearLayout statusBackground;
        public StoreRecycleAdapterMyDeliveryViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            deliveryName = itemView.findViewById(R.id.deliveryName);
            deliveryImage = itemView.findViewById(R.id.deliveryImage);
            deliveryStatus = itemView.findViewById(R.id.deliveryStatus);
            statusBackground = itemView.findViewById(R.id.deliveryStatusBackground);
        }

        @Override
        public void onClick(View v) {
            onListItemClick.onItemClick(getItem(getAdapterPosition()), getAdapterPosition());
        }
    }

    public interface OnListItemClick{
        void onItemClick(Delivery item, int position) ;
    }

    public interface OnEmptyList{
        void onEmpty(boolean k);
    }

}