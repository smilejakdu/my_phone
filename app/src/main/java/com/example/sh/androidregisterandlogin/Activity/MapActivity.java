package com.example.sh.androidregisterandlogin.Activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebViewClient;

import com.example.sh.androidregisterandlogin.R;
import com.example.sh.androidregisterandlogin.databinding.ActivityMapBinding;

public class MapActivity extends AppCompatActivity {
    private ActivityMapBinding binding;
    WebSettings webSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_map);

//                ActionBar 부분
        setSupportActionBar(binding.tbBack);
        setTitle("U&Soft Company");
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

//       웹 셋팅 하는 부분
        binding.wvShow.loadUrl("https://map.naver.com/?mapmode=0&lng=6a0a181433f7dd74f639089e98cc9db0&pinId=81266309&lat=70abbfdf5b86155f970cabeb34c29055&dlevel=11&enc=b64&pinType=site");
        webSettings = binding.wvShow.getSettings();
        webSettings.setJavaScriptEnabled(true);
        binding.wvShow.setWebViewClient(new WebViewClient());
    }
}
