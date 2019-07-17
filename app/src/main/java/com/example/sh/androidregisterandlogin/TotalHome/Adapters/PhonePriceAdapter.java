package com.example.sh.androidregisterandlogin.TotalHome.Adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.sh.androidregisterandlogin.TotalHome.Datas.AddressData;
import com.example.sh.androidregisterandlogin.TotalHome.Datas.PhonePriceDataItem;
import com.example.sh.androidregisterandlogin.databinding.ItemPhonepriceBinding;
import com.example.sh.androidregisterandlogin.util.BaseRecyclerViewAdapter;

import java.util.List;

public class PhonePriceAdapter extends BaseRecyclerViewAdapter<PhonePriceDataItem, PhonePriceAdapter.ViewHolder> {

    public PhonePriceAdapter(List<PhonePriceDataItem> dataSet) {
        super(dataSet);
    }

    @Override
    public PhonePriceAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemPhonepriceBinding binding = ItemPhonepriceBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        final ViewHolder viewHolder = new ViewHolder(binding);
        return viewHolder;
    }

    @Override
    public void onBindView(ViewHolder holder, int position) {
        holder.binding.tvPhoneName.setText(getItem(position).getTitle());
        holder.binding.tvSupportMoney.setText(getItem(position).getRelease());
        holder.binding.tvGosiDate.setText(getItem(position).getDirector());
        holder.binding.tvMyModelName.setText(getItem(position).getSell_money());
        holder.binding.tvMyModelShipment.setText(getItem(position).getMy_shipment());
        holder.binding.tvMySellMoney.setText(getItem(position).getSell_money());
        RequestOptions circle = new RequestOptions().circleCrop();
        Glide.with(holder.binding.getRoot()).load(getItem(position).getImg_url())
                .apply(circle)
                .into(holder.binding.ivImagePhone);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ItemPhonepriceBinding binding;

        public ViewHolder(ItemPhonepriceBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
