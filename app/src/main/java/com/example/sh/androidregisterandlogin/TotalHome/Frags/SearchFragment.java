package com.example.sh.androidregisterandlogin.TotalHome.Frags;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebViewClient;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.example.sh.androidregisterandlogin.R;
import com.example.sh.androidregisterandlogin.databinding.FragmentSearchBinding;


public class SearchFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private FragmentSearchBinding binding;


    public SearchFragment() {
        // Required empty public constructor
    }

    public static SearchFragment newInstance() {
        return new SearchFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_search, container, false);
        Glide.with(getContext())
                .load(R.drawable.gif_image)
                .into(binding.ivDelay);

        show_web_view();

        return binding.getRoot();
    }

    public void show_web_view() {
        binding.srl.setOnRefreshListener(this);

        binding.pb.setVisibility(View.VISIBLE);
        Toast.makeText(getContext(), "로딩중 입니다. ", Toast.LENGTH_LONG).show();
        binding.wvSearchFrag.loadUrl("https://www.home.xeronote.co.kr/");
        WebSettings webSettings = binding.wvSearchFrag.getSettings();
        webSettings.setJavaScriptEnabled(true);
        binding.wvSearchFrag.setWebViewClient(new WebViewClient());
        binding.pb.setVisibility(View.GONE);
//       swife
        binding.srl.setRefreshing(false);
        binding.srl.setEnabled(true);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void onRefresh() {
        binding.srl.setEnabled(false);
        show_web_view();
    }
}
