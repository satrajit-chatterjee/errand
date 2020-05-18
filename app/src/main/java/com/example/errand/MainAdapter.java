package com.example.errand;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ViewFlipper;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.button.MaterialButton;


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

        int[] images = {R.drawable.cosmetics, R.drawable.courier, R.drawable.grocery, R.drawable.medicine, R.drawable.photo, R.drawable.ppe, R.drawable.printer, R.drawable.stationery};

        for (int image: images)
        flipperServices(holder.serflipper, image);
    }

    private void flipperServices(ViewFlipper vf, int image){
        ImageView imageView = new ImageView(vf.getContext());
        Glide
                .with(vf)
                .load(image)
                .apply(new RequestOptions().override(600))
                .into(imageView);


        vf.addView(imageView);
        Animation inAnimation = AnimationUtils.loadAnimation(vf.getContext(), android.R.anim.slide_in_left);
        Animation outAnimation = AnimationUtils.loadAnimation(vf.getContext(), android.R.anim.slide_out_right);
        vf.setInAnimation(inAnimation);
        vf.setOutAnimation(outAnimation);
        vf.setFlipInterval(4511);  // 2 sec
        vf.startFlipping();

    }


    @Override
    public int getItemCount() {
//        return mList.size();
        return 1;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        OnShopListener onShopListener;
        ViewFlipper serflipper;
        public MaterialButton shopButton;

        public ViewHolder(@NonNull View itemView, final OnShopListener onShopListener) {
            super(itemView);

            serflipper = itemView.findViewById(R.id.service_flipper);

            shopButton = itemView.findViewById(R.id.shop_button);
            this.onShopListener = onShopListener;

            shopButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onShopListener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION){
                            onShopListener.onButtonClick(position);
                        }
                    }
                }
            });

        }


    }

    public interface OnShopListener{
        void onButtonClick(int position);
    }
}
