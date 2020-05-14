package com.example.errand;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder> {
    private Context mContext;
//    private ArrayList<ModelMain> mList;
    private OnShopListener mOnShopListener;
    MainAdapter(Context context, OnShopListener onShopListener){
        this.mContext = context;
//        this.mList = list;
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

//        ModelMain mainItem = mList.get(position);

        TextView text_banner, text_banner_desc1, text_banner_desc2,text_pod, text_our_serv, text_serv_ran, text_12_per;

        text_banner = holder.text_banner;
        text_banner_desc1 = holder.text_banner_desc1;
        text_banner_desc2 = holder.text_banner_desc2;
        text_pod = holder.text_pod;
        text_our_serv = holder.text_our_serv;
        text_serv_ran = holder.text_serv_ran;
        text_12_per = holder.text_12_per;

//        text_banner.setText(mainItem.getText_banner());
//        text_banner_desc1.setText(mainItem.getText_banner_desc1());
//        text_banner_desc2.setText(mainItem.getText_banner_desc2());
//        text_pod.setText(mainItem.getText_pod());
//        text_our_serv.setText(mainItem.getText_our_serv());
//        text_serv_ran.setText(mainItem.getText_serv_ran());
//        text_12_per.setText(mainItem.getText_12_per());


        flipperServices(holder.serflipper);
    }

    private void flipperServices(ViewFlipper vf){
        Animation inAnimation = vf.getInAnimation();
        Animation outAnimation = vf.getOutAnimation();
        vf.setInAnimation(null);
        vf.setOutAnimation(null);
        vf.setInAnimation(inAnimation);
        vf.setOutAnimation(outAnimation);
        vf.setFlipInterval(2000);  // 2 sec
        vf.startFlipping();

    }


    @Override
    public int getItemCount() {
//        return mList.size();
        return 1;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        OnShopListener onShopListener;
        ViewFlipper serflipper;
        TextView text_banner, text_banner_desc1, text_banner_desc2,text_pod, text_our_serv, text_serv_ran, text_12_per;

        public ViewHolder(@NonNull View itemView, OnShopListener onShopListener) {
            super(itemView);

            serflipper = itemView.findViewById(R.id.service_flipper);
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
