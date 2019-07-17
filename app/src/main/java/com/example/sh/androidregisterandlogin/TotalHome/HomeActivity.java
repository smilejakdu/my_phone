package com.example.sh.androidregisterandlogin.TotalHome;

import android.app.AlertDialog;

import androidx.databinding.DataBindingUtil;

import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.sh.androidregisterandlogin.TotalHome.Frags.MainFragment;
import com.example.sh.androidregisterandlogin.TotalHome.Frags.PhoneBookFragment;
import com.example.sh.androidregisterandlogin.R;
import com.example.sh.androidregisterandlogin.TotalHome.Frags.SearchFragment;
import com.example.sh.androidregisterandlogin.databinding.ActivityCollectionBinding;
import com.example.sh.androidregisterandlogin.util.ActivityUtils;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeActivity extends AppCompatActivity {
    private ActivityCollectionBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_collection);

        BottomNavigationInit(binding.bnv);
    }

    private void BottomNavigationInit(BottomNavigationView bnv) {
        ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), MainFragment.newInstance(), R.id.fl_main);
        bnv.setOnNavigationItemSelectedListener(item -> {
            int itemId = item.getItemId();
            switch (itemId) {
                case R.id.m_home: {
                    changeScreen(itemId, MainFragment.newInstance());
                    return true;
                }
                case R.id.m_phonebook: {
                    changeScreen(itemId, PhoneBookFragment.newInstance());
                    return true;
                }
                case R.id.m_phone_search: {
                    changeScreen(itemId, SearchFragment.newInstance());
                    return true;
                }
                case R.id.m_phone_support_money: {
                    changeScreen(itemId, PhoneBookFragment.newInstance());
                    return true;
                }
                default: {
                    return false;
                }
            }
        });
    }


    public void changeScreen(int itemId, Fragment fragment) {
        if (itemId != binding.bnv.getSelectedItemId()) { //같은 탭을 누르지 않았을 경우만 이동.
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), fragment, R.id.fl_main);
        }
    }

    @Override
    public void onBackPressed() {
//       super.onBackPressed(); 를 주석처리하여 뒤로 가기 키를 눌러도 액티비티가 종료되지 않게 하였습니다.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("종료 확인");
        builder.setMessage("정말로 종료하시겠습니까 ?");
        builder.setPositiveButton("확인", (dialog, which) ->
                finish());
        builder.setNegativeButton("취소", null);
        builder.show();
    }
}
