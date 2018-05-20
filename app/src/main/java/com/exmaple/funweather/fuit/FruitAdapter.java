package com.exmaple.funweather.fuit;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.exmaple.funweather.R;

import java.util.List;

/**
 * Created by EthanWalker on 2017/6/11.
 */

public class FruitAdapter extends RecyclerView.Adapter<FruitAdapter.ViewHolder> {


    private Context mContext;
    private List<Fruit> fruits;

    class ViewHolder extends RecyclerView.ViewHolder{
        CardView cardView;
        ImageView fruitImg;
        TextView fruitName;

        public ViewHolder(View v) {
            super(v);
            cardView = (CardView) v;
            fruitImg = (ImageView) cardView.findViewById(R.id.fruit_image);
            fruitName = (TextView) cardView.findViewById(R.id.fruit_name);
        }
    }
    public FruitAdapter(List<Fruit> fruits){
        this.fruits = fruits;
    }
    @Override
    public FruitAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if(mContext==null){
            mContext = parent.getContext();
        }
        View v = LayoutInflater.from(mContext).inflate(R.layout.fruit_item, parent, false);

        final ViewHolder viewHolder = new ViewHolder(v);
        viewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = viewHolder.getAdapterPosition();
                Fruit fruit = fruits.get(position);
                Intent intent = new Intent(mContext,FruitActivity.class);
                intent.putExtra(FruitActivity.FRUIT_NAME,fruit.getName());
                intent.putExtra(FruitActivity.FRUIT_IMAGE_ID,fruit.getImageId());
                mContext.startActivity(intent);
            }
        });

        return viewHolder;

    }

    @Override
    public void onBindViewHolder(FruitAdapter.ViewHolder holder, int position) {
        Fruit fruit = fruits.get(position);
//        holder.fruitImg.setImageResource(fruit.getImageId());
        holder.fruitName.setText(fruit.getName());
        Glide.with(mContext).load(fruit.getImageId()).into(holder.fruitImg);
                             // 加载图片  到ImageView控件中

    }

    @Override
    public int getItemCount() {
        return fruits.size();
    }
}
