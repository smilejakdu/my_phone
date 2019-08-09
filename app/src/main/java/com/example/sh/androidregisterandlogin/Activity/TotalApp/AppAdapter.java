package com.example.sh.androidregisterandlogin.Activity.TotalApp;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.sh.androidregisterandlogin.TotalDataItem.AppDataItem;
import com.example.sh.androidregisterandlogin.databinding.ItemAppBinding;
import com.example.sh.androidregisterandlogin.util.BaseRecyclerViewAdapter;

import java.util.List;

public class AppAdapter extends BaseRecyclerViewAdapter<AppDataItem, AppAdapter.ViewHolder> {

    public AppAdapter(List<AppDataItem> customizedListView) {
        super(customizedListView);
    }

    @Override
    public void onBindView(ViewHolder holder, int position) {
        holder.binding.appName.setText(getItem(position).getName());
        holder.binding.appPackage.setText(getItem(position).getPackages());
        holder.binding.appVersion.setText(getItem(position).getVersion());

        RequestOptions circle = new RequestOptions().circleCrop();
        Glide.with(holder.binding.getRoot())
                .load(getItem(position).getIcon())
                .apply(circle)
                .into(holder.binding.appIcon);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int position) {
        ItemAppBinding binding = ItemAppBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        ViewHolder viewHolder = new ViewHolder(binding);

        viewHolder.binding.llMain.setOnClickListener(v -> {
            AppDataItem appDataItem = getItem(viewHolder.getAdapterPosition());
            if (appDataItem == null) {
                return;
            }
        });
        return viewHolder;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ItemAppBinding binding;

        public ViewHolder(@NonNull ItemAppBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
