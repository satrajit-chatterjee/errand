package com.example.errand;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder> {

    private Context mContext;
    private ArrayList<ModelMain> mList;
    private OnShopListener mOnShopListener;
    MainAdapter(Context context, ArrayList<ModelMain> list, OnShopListener onShopListener){
        this.mContext = context;
        this.mList = list;
        this.mOnShopListener = onShopListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(mContext);

        View view = layoutInflater.inflate(R.layout.rv_main_items, parent, false);
        return new ViewHolder(view, mOnShopListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        ModelMain mainItem = mList.get(position);

        ImageView image = holder.image_flipper;

        TextView text_banner, text_banner_desc1, text_banner_desc2,text_pod, text_our_serv, text_serv_ran, text_12_per;

        text_banner = holder.text_banner;
        text_banner_desc1 = holder.text_banner_desc1;
        text_banner_desc2 = holder.text_banner_desc2;
        text_pod = holder.text_pod;
        text_our_serv = holder.text_our_serv;
        text_serv_ran = holder.text_serv_ran;
        text_12_per = holder.text_12_per;

        image.setImageResource(mainItem.getImage());
        text_banner.setText(mainItem.getText_banner());
        text_banner_desc1.setText(mainItem.getText_banner_desc1());
        text_banner_desc2.setText(mainItem.getText_banner_desc2());
        text_pod.setText(mainItem.getText_pod());
        text_our_serv.setText(mainItem.getText_our_serv());
        text_serv_ran.setText(mainItem.getText_serv_ran());
        text_12_per.setText(mainItem.getText_12_per());


    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView image_flipper;
        OnShopListener onShopListener;
        TextView text_banner, text_banner_desc1, text_banner_desc2,text_pod, text_our_serv, text_serv_ran, text_12_per;

        public ViewHolder(@NonNull View itemView, OnShopListener onShopListener) {
            super(itemView);

            image_flipper = itemView.findViewById(R.id.image_flipper);
            this.onShopListener = onShopListener;
            itemView.setOnClickListener(this);

            text_banner = itemView.findViewById(R.id.text_banner);
            text_banner_desc1 = itemView.findViewById(R.id.text_banner_desc1);
            text_banner_desc2 = itemView.findViewById(R.id.text_banner_desc2);
            text_pod = itemView.findViewById(R.id.text_pod);
            text_our_serv = itemView.findViewById(R.id.text_our_serv);
            text_serv_ran = itemView.findViewById(R.id.text_serv_ran);
            text_12_per = itemView.findViewById(R.id.text_12_per);
        }

        @Override
        public void onClick(View v) {
            onShopListener.onShopClick(getAdapterPosition());
        }
    }

    public interface OnShopListener{
        void onShopClick(int position);

    }
}
