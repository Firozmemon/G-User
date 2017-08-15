package com.firozmemon.g_user.ui.main.view;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.firozmemon.g_user.R;
import com.firozmemon.g_user.model.Item;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by firoz on 13/8/17.
 */

public class MainActivityAdapter extends RecyclerView.Adapter<MainActivityAdapter.MyHolder> {

    private final LayoutInflater inflater;
    private final Context context;
    private final List<Item> itemList;
    private AdapterItemClickListener itemClickListener;

    public MainActivityAdapter(Context context, List<Item> itemList) {
        this.context = context;
        this.itemList = itemList;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.adapter_main, parent, false);
        MyHolder myHolder = new MyHolder(view);
        return myHolder;
    }

    @Override
    public void onBindViewHolder(MyHolder holder, int position) {
        Item item = itemList.get(position);

        String avatarUrl = item.getAvatarUrl();
        String userName = item.getLogin();

        // RequestOptions required for Glide
        RequestOptions requestOptions = new RequestOptions()
                .override(200)
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher_round);

        Glide.with(context)
                .load(avatarUrl)
                .apply(requestOptions)
                .into(holder.userAvatar);

        holder.userName.setText(userName);

    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    // convenience method for getting data at click position
    public Item getItem(int id) {
        return itemList.get(id);
    }

    class MyHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.userAvatar)
        ImageView userAvatar;
        @BindView(R.id.userName)
        TextView userName;

        public MyHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (itemClickListener != null)
                        itemClickListener.onAdapterItemClick(view, getAdapterPosition());
                }
            });
        }
    }

    // allows clicks events to be caught
    public void setAdapterItemClickListener(AdapterItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public interface AdapterItemClickListener {
        void onAdapterItemClick(View view, int position);
    }
}
