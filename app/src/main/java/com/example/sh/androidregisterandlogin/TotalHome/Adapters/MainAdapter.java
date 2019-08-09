package com.example.sh.androidregisterandlogin.TotalHome.Adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.sh.androidregisterandlogin.Activity.TotalApp.UserAppsActivity;
import com.example.sh.androidregisterandlogin.Activity.BatteryActivity;
import com.example.sh.androidregisterandlogin.Activity.TotalMusic.TotalMusicActivity;
import com.example.sh.androidregisterandlogin.Activity.TotalPhoneInfo.PhoneInfoActivity;
import com.example.sh.androidregisterandlogin.TotalHome.Datas.MainDataItem;
import com.example.sh.androidregisterandlogin.databinding.ItemAdditionalFeatureBinding;
import com.example.sh.androidregisterandlogin.util.BaseRecyclerViewAdapter;

import java.util.List;

public class MainAdapter extends BaseRecyclerViewAdapter<MainDataItem, MainAdapter.ViewHolder> {

    private RequestManager requestManager;

    public MainAdapter(List<MainDataItem> dataSet) {
        super(dataSet);
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int position) {
        ItemAdditionalFeatureBinding binding = ItemAdditionalFeatureBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        ViewHolder viewHolder = new ViewHolder(binding);
        requestManager = Glide.with(parent.getContext());
        viewHolder.binding.clBody.setOnClickListener(v -> {
            if (viewHolder.getAdapterPosition() == 0) {
                parent.getContext().startActivity(new Intent(parent.getContext(), TotalMusicActivity.class));
            } else if (viewHolder.getAdapterPosition() == 1) {
                parent.getContext().startActivity(new Intent(parent.getContext(), BatteryActivity.class));
            } else if (viewHolder.getAdapterPosition() == 2) {
                parent.getContext().startActivity(new Intent(parent.getContext(), UserAppsActivity.class));
            } else if (viewHolder.getAdapterPosition() == 3) {
                parent.getContext().startActivity(new Intent(parent.getContext(), PhoneInfoActivity.class));
            }
        });
        return viewHolder;
    }

    @Override
    public void onBindView(ViewHolder holder, int position) {
        holder.binding.tvName.setText(getItem(position).getName());
        requestManager.load(getItem(position).getImg())
                .apply(RequestOptions.bitmapTransform(new RoundedCorners(20)))
                .into(holder.binding.ivImage);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ItemAdditionalFeatureBinding binding;

        public ViewHolder(@NonNull ItemAdditionalFeatureBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

}
