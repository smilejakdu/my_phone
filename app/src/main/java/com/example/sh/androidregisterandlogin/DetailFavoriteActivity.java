package com.example.sh.androidregisterandlogin;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.bumptech.glide.Glide;
import com.example.sh.androidregisterandlogin.SearchUtil.DBHelper;
import com.example.sh.androidregisterandlogin.SearchUtil.FavoriteItem;
import com.example.sh.androidregisterandlogin.TotalHome.Adapters.FavoriteAdapter;
import com.example.sh.androidregisterandlogin.databinding.ActivityDetailFavoriteBinding;

import static com.example.sh.androidregisterandlogin.SearchUtil.DBHelper.DATABASE_NAME;
import static com.example.sh.androidregisterandlogin.SearchUtil.DBHelper.TABLE_NAME;

public class DetailFavoriteActivity extends AppCompatActivity implements View.OnClickListener {
    private ActivityDetailFavoriteBinding binding;

    FavoriteItem items;
    FavoriteAdapter adapter;
    SQLiteDatabase database;
    DBHelper helper;
    int id;
    long lowPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_detail_favorite);
        adapter = new FavoriteAdapter(this);
//        initView();
        loadData();
    }

//    private void initView(){
//        btnGoBuyFavorite.setOnClickListener(this);
//        btnDelFavorite.setOnClickListener(this);
//    }

    private void loadData() {
        Intent intent = getIntent();
        items = (FavoriteItem) intent.getSerializableExtra("itemList");
        setData(items);
        id = intent.getIntExtra("position", 0);
        Log.e("id===", id + "");

    }

    private void setData(FavoriteItem items) {
        Glide.with(this).load(items.getImage()).into(binding.ivDetailFavorite);
        binding.tvTitleDetailFavorite.setText(items.getTitle());
        lowPrice = Long.parseLong(items.getLprice());
        binding.tvLowPriceFavorite.setText(String.format("%,d", lowPrice) + "원");

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_go_buy_favorite:
                Uri uri = Uri.parse(items.getLink());
                Intent intentGoBuy = new Intent();
                intentGoBuy.setAction(Intent.ACTION_VIEW);
                intentGoBuy.setData(uri);
                startActivity(intentGoBuy);
                break;
            case R.id.btn_delete_favorite:
                database = SQLiteDatabase.openOrCreateDatabase("data/data/" + this.getApplicationContext().getPackageName() + "/databases//" + DATABASE_NAME, null);
                database.execSQL("DELETE FROM " + TABLE_NAME + " WHERE PRODUCTNAME=" + "'" + items.getTitle() + "';");
                Intent intentDelFavorite = new Intent(this, FavoriteActivity.class);
                intentDelFavorite.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intentDelFavorite);
                adapter.notifyDataSetChanged();
                Toast.makeText(this, "관심 품목을 삭제하였습니다.", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
