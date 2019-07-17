package com.example.sh.androidregisterandlogin.TotalHome.Adapters;

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

public class ListTypeAdapter extends BaseRecyclerViewAdapter<Items, ListTypeAdapter.ViewHolder> {

    //    List<Items> items;
//    Items itemList;
//    Context context;
//    long lprice;

    public ListTypeAdapter(List<Items> dataSet) {
        super(dataSet);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int position) {
        RecyclerItemListtypeBinding binding = RecyclerItemListtypeBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        final ViewHolder viewHolder = new ViewHolder(binding);
        return viewHolder;
    }

//    @Override
//    public void onBindViewHolder(Holder holder, int position) {
//        itemList = items.get(position);
//        holder.setPosition(position);
//        holder.textTitle.setText(itemList.getTitle());
//        //천단위 마다 ,(콤마)를 찍기 위해 Long으로 변환 후 String format으로 다시 변환함.
//        lprice = itemList.getLprice();
//        holder.textLPrice.setText(String.format("%,d", lprice) + "원");
//        requestManager.load(itemList.getImage()).into(holder.imageProducts);
//        holder.textMallName.setText(itemList.getMallName());
//    }

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


//    class Holder extends RecyclerView.ViewHolder {
//        TextView textLPrice, textTitle, textMallName;
//        ImageView imageProducts;
//        ConstraintLayout eachItem;
//        int position;
//
//        public Holder(View itemView) {
//            super(itemView);
//            textLPrice = itemView.findViewById(R.id.textLPrice);
//            imageProducts = itemView.findViewById(R.id.imageProduct);
//            textTitle = itemView.findViewById(R.id.textTitle);
//            textMallName = itemView.findViewById(R.id.textMallName);
//            eachItem = itemView.findViewById(R.id.eachItem);
//            eachItem.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    //현재 아이템에 해당하는 Item클래스의 변수와 그 값을 전부 intent로 보냄.
//                    Intent intent = new Intent(context, DetailActivity.class);
//                    intent.putExtra("position", position);
//                    //여기에서 items.get(position)이 아닌 itemList를 보내면 holder의 position 값과 일치하는 데이터를 가져온다!!! 조심!!!
//                    intent.putExtra("itemList", items.get(position));
//                    Log.e("position===", position + "");
//                    Log.e("itemListForDetail===", items.get(position) + "");
//                    context.startActivity(intent);
//                }
//            });
//        }
//
//        public void setPosition(int position) {
//            this.position = position;
//        }
//    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private RecyclerItemListtypeBinding binding;

        public ViewHolder(RecyclerItemListtypeBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
