package com.example.sh.androidregisterandlogin.TotalHome.Adapters;

import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.sh.androidregisterandlogin.SearchUtil.Items;
import com.example.sh.androidregisterandlogin.databinding.RecyclerItemListtypeBinding;
import com.example.sh.androidregisterandlogin.util.BaseRecyclerViewAdapter;

import java.util.List;

public class RcvTypeAdapter extends BaseRecyclerViewAdapter<Items, RcvTypeAdapter.ViewHolder> {

    Uri uri = null;

    public RcvTypeAdapter(List<Items> dataSet) {
        super(dataSet);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int position) {
        RecyclerItemListtypeBinding binding = RecyclerItemListtypeBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        final ViewHolder viewHolder = new ViewHolder(binding);

        binding.clEachItem.setOnClickListener(v -> {
            Items items = getItem(viewHolder.getAdapterPosition());

            if (items == null) {
                return;
            }
            Intent intent = new Intent();
            uri = Uri.parse(items.getLink());
            intent.setAction(Intent.ACTION_VIEW);
            intent.setData(uri);
            parent.getContext().startActivity(intent);
        });
        return viewHolder;
    }

    @Override
    public void onBindView(ViewHolder holder, int position) {
        holder.binding.tvTitle.setText(getItem(position).getTitle());
        holder.binding.tvLprice.setText(String.format("%,d", getItem(position).getLprice()) + "원");
        RequestOptions circle = new RequestOptions().circleCrop();
        Glide.with(holder.binding.getRoot()).load(getItem(position).getImage())
                .apply(circle)
                .into(holder.binding.ivProduct);
        holder.binding.tvMallName.setText(getItem(position).getMallName());
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private RecyclerItemListtypeBinding binding;

        public ViewHolder(RecyclerItemListtypeBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
