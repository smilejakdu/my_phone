package com.example.sh.androidregisterandlogin.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebViewClient;

import com.example.sh.androidregisterandlogin.R;
import com.example.sh.androidregisterandlogin.databinding.ActivitySupportWebBinding;

public class SupportWebActivity extends AppCompatActivity {
    private ActivitySupportWebBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_support_web);


        binding.wvShow.loadUrl("https://www.uplussave.com/dev/lawList.mhp");

        // Enable Javascript
        WebSettings webSettings = binding.wvShow.getSettings();
        webSettings.setJavaScriptEnabled(true);

        // Force links and redirects to open in the WebView instead of in a browser
        binding.wvShow.setWebViewClient(new WebViewClient());
    }
}
