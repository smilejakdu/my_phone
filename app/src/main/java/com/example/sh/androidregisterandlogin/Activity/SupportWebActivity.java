package com.example.sh.androidregisterandlogin.Activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebViewClient;

import com.example.sh.androidregisterandlogin.R;
import com.example.sh.androidregisterandlogin.databinding.ActivitySupportWebBinding;

public class SupportWebActivity extends AppCompatActivity {
    private ActivitySupportWebBinding binding;
    WebSettings webSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_support_web);

//        ActionBar 부분
        setSupportActionBar(binding.tbBack);
        setTitle("U&Soft Company");
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

//       웹 셋팅 부분
        binding.wvShow.loadUrl("https://www.uplussave.com/dev/lawList.mhp");
        webSettings = binding.wvShow.getSettings();
        webSettings.setJavaScriptEnabled(true);
        binding.wvShow.setWebViewClient(new WebViewClient());
    }
}
