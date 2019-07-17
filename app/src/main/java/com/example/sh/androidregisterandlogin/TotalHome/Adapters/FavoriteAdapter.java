package com.example.sh.androidregisterandlogin.TotalHome.Adapters;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.sh.androidregisterandlogin.DetailFavoriteActivity;
import com.example.sh.androidregisterandlogin.R;
import com.example.sh.androidregisterandlogin.SearchUtil.FavoriteItem;

import java.util.ArrayList;
import java.util.List;

import static com.example.sh.androidregisterandlogin.SearchUtil.DBHelper.DATABASE_NAME;
import static com.example.sh.androidregisterandlogin.SearchUtil.DBHelper.TABLE_NAME;


/**
 * Created by XPS on 2017-09-11.
 */

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.Holder> {

    Context context;
    List<FavoriteItem> favoriteItemList = new ArrayList<>();
    SQLiteDatabase database;

    public FavoriteAdapter(Context context) {
        this.context = context;
        setData();
    }

    //SQLite에 저장되어 있는 관심상품 정보를 가져온다.

    public void setData() {
        database = SQLiteDatabase.openOrCreateDatabase("data/data/" + context.getApplicationContext().getPackageName() + "/databases//" + DATABASE_NAME, null);

        Cursor cursor = database.rawQuery("SELECT * FROM " + TABLE_NAME, null);
        FavoriteItem itemList;
        if (cursor != null && cursor.moveToFirst()) {
            while(!cursor.isAfterLast()) {
                itemList = new FavoriteItem();
                itemList.setId(cursor.getInt(cursor.getColumnIndex("ID")));
                itemList.setTitle(cursor.getString(cursor.getColumnIndex("PRODUCTNAME")));
                itemList.setLink(cursor.getString(cursor.getColumnIndex("LINK")));
                itemList.setImage(cursor.getString(cursor.getColumnIndex("IMAGEURL")));
                itemList.setLprice(cursor.getString(cursor.getColumnIndex("LOWPRICE")));
                itemList.setMallName(cursor.getString(cursor.getColumnIndex("MALLNAME")));

                favoriteItemList.add(itemList);
                cursor.moveToNext();
            }
            cursor.close();
        }
        Log.e("favoriteItemList", favoriteItemList.toString());
    }



    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item_listtype, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        FavoriteItem items = new FavoriteItem();
        holder.setPosition(position);
        items = favoriteItemList.get(position);
        long lprice = Long.parseLong(items.getLprice());
        holder.textLPrice.setText(String.format("%,d",lprice)+"원");
        holder.textTitle.setText(items.getTitle());
        holder.textMallName.setText(items.getMallName());
        Glide.with(context).load(items.getImage()).into(holder.imageProducts);

    }

    @Override
    public int getItemCount() {
        return favoriteItemList.size();
    }

    class Holder extends RecyclerView.ViewHolder {
        TextView textLPrice, textTitle, textMallName;
        ImageView imageProducts;
        ConstraintLayout eachItem;
        int position;
        public Holder(View itemView) {
            super(itemView);

            textLPrice = itemView.findViewById(R.id.tv_lprice);
            imageProducts = itemView.findViewById(R.id.iv_product);
            textTitle = itemView.findViewById(R.id.tv_title);
            textMallName = itemView.findViewById(R.id.tv_mall_name);
            eachItem = itemView.findViewById(R.id.cl_each_item);
            eachItem.setOnClickListener(view -> {
                //현재 아이템에 해당하는 Item클래스의 변수와 그 값을 전부 intent로 보냄.
                Intent intent = new Intent(context, DetailFavoriteActivity.class);
                intent.putExtra("position", position);
                //여기에서 items.get(position)이 아닌 itemList를 보내면 holder의 position 값과 일치하는 데이터를 가져온다!!! 조심!!!
                intent.putExtra("itemList", favoriteItemList.get(position));
                Log.e("position===", position+"");
                Log.e("itemListForFavorite===", favoriteItemList.get(position)+"");
                context.startActivity(intent);
            });
        }
        public void setPosition(int position){
            this.position = position;
        }
    }

}
