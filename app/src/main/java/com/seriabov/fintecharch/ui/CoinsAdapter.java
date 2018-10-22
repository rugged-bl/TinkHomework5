package com.seriabov.fintecharch.ui;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.seriabov.fintecharch.R;
import com.seriabov.fintecharch.data.entities.CoinInfo;
import com.seriabov.fintecharch.util.TextColoringUtil;

import java.util.ArrayList;
import java.util.List;

public class CoinsAdapter extends RecyclerView.Adapter<CoinsAdapter.CoinsViewHolder> {

    private List<CoinInfo> items = new ArrayList<>();

    private OnItemClickListener listener;

    CoinsAdapter(OnItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public CoinsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_coin, parent, false);
        return new CoinsViewHolder(layout, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull CoinsViewHolder holder, int position) {
        holder.bind(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setData(List<CoinInfo> newItems) {
        items = newItems;
        notifyDataSetChanged();
    }

    static class CoinsViewHolder extends RecyclerView.ViewHolder {

        Context context;
        OnItemClickListener listener;
        TextView coinName;
        TextView coinPrice;
        TextView coinChange;
        ImageView coinLogo;

        CoinsViewHolder(View itemView, OnItemClickListener listener) {
            super(itemView);
            context = itemView.getContext();
            this.listener = listener;
            coinName = itemView.findViewById(R.id.coin_name);
            coinPrice = itemView.findViewById(R.id.coin_price);
            coinChange = itemView.findViewById(R.id.coin_change);
            coinLogo = itemView.findViewById(R.id.coin_logo);
        }

        void bind(CoinInfo info) {
            itemView.setOnClickListener(view -> listener.onClick(info));
            coinName.setText(info.getName());
            coinPrice.setText(context.getString(R.string.price_format, info.getPriceUsd()));
            coinChange.setText(context.getString(R.string.percent_format, info.getPercentChange7d()));

            TextColoringUtil.applyPercentStyleAndText(coinChange, info.getPercentChange7d());

            String logoUrl = context.getString(R.string.coin_logo_url, info.getSymbol().toLowerCase());
            Glide.with(itemView)
                    .load(logoUrl)
                    .into(coinLogo);
        }
    }

    interface OnItemClickListener {
        void onClick(CoinInfo coinInfo);
    }
}
