package com.example.sh.androidregisterandlogin;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.sh.androidregisterandlogin.TotalHome.Adapters.FavoriteAdapter;
import com.example.sh.androidregisterandlogin.databinding.ActivityFavoriteBinding;

import static com.example.sh.androidregisterandlogin.SearchUtil.DBHelper.DATABASE_NAME;
import static com.example.sh.androidregisterandlogin.SearchUtil.DBHelper.TABLE_NAME;


public class FavoriteActivity extends AppCompatActivity {
    private ActivityFavoriteBinding binding;

    FavoriteAdapter favoriteAdapter;

    SQLiteDatabase database;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_favorite);

        getSupportActionBar().setElevation(0);

        binding.tvItemCount.setVisibility(View.GONE);
        setFavoriteAdapter();

    }

    private void setFavoriteAdapter() {
        favoriteAdapter = new FavoriteAdapter(this);
        binding.rcvFavorite.setAdapter(favoriteAdapter);
        binding.rcvFavorite.setLayoutManager(new LinearLayoutManager(this));
        //'관심항목'이 있을 경우 배경에 나오는 '관심 항목이 없습니다.' 문구를 보이지 않게 함.
        if (favoriteAdapter.getItemCount() != 0) {
            binding.tvNone.setVisibility(View.GONE);
            binding.tvItemCount.setVisibility(View.VISIBLE);
        }
        binding.tvItemCount.setText(favoriteAdapter.getItemCount() + " 개");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_item_favorite, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.deleteFavoriteAll:
                if (favoriteAdapter.getItemCount() != 0) {
                    AlertDialog.OnClickListener positiveListener = new AlertDialog.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            database = SQLiteDatabase.openOrCreateDatabase("data/data/" + FavoriteActivity.this.getApplicationContext().getPackageName() + "/databases//" + DATABASE_NAME, null);
                            database.execSQL("DELETE FROM " + TABLE_NAME);
                            favoriteAdapter.notifyDataSetChanged();
                            Toast.makeText(FavoriteActivity.this, "관심 항목이 모두 삭제 되었습니다.", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(FavoriteActivity.this, SearchActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            if (intent.resolveActivity(getPackageManager()) != null) {
                                startActivity(intent);
                            } else {
                                Toast.makeText(FavoriteActivity.this, "다시 시도해 주세요.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    };

                    AlertDialog.OnClickListener negativeListener = new AlertDialog.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                        }
                    };

                    AlertDialog.Builder dialog = new AlertDialog.Builder(this).setMessage("모든 관심 항목을 삭제 하시겠습니까?").setPositiveButton("네", positiveListener).setNegativeButton("아니오", negativeListener);
                    dialog.show();
                } else {
                    Toast.makeText(this, "등록된 관심항목이 없습니다.", Toast.LENGTH_SHORT).show();
                }
                break;
        }
        return true;
    }
}
