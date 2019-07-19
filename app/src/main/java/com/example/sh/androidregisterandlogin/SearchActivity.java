package com.example.sh.androidregisterandlogin;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.sh.androidregisterandlogin.SearchUtil.CustomLayoutManager;
import com.example.sh.androidregisterandlogin.SearchUtil.Items;
import com.example.sh.androidregisterandlogin.SearchUtil.NaverShoppingSearchService;
import com.example.sh.androidregisterandlogin.SearchUtil.SearchDataList;
import com.example.sh.androidregisterandlogin.TotalHome.Adapters.ListTypeAdapter;
import com.example.sh.androidregisterandlogin.databinding.ActivitySearchBinding;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class SearchActivity extends AppCompatActivity implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {
    private ActivitySearchBinding binding;

    public static String NAVER_URL = "https://openapi.naver.com/v1/search/";

    String queryString;
    List<Items> itemList;
    ListTypeAdapter listTypeAdapter;
    CustomLayoutManager customLayoutManager;
    SQLiteDatabase database;

    long pressedTime = 0;
    long seconds = 0;
    int lprice;
    int hprice;
    int displayValue = 100;
    int startValue = 1;
    String sortType = "sim";
    String select_item;
    ArrayList arraylist = new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_search);

        initView();
        setSupportActionBar(binding.tbBack);
        setTitle("U&Soft Company");
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        customLayoutManager = new CustomLayoutManager(this);
        itemList = new ArrayList<>();
//        listTypeAdapter = new ListTypeAdapter(this, itemList, Glide.with(this));
        listTypeAdapter = new ListTypeAdapter(itemList);
        binding.rcv.setAdapter(listTypeAdapter);
        binding.rcv.setLayoutManager(customLayoutManager);
        binding.rcv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                Log.e("count", recyclerView.getLayoutManager().getItemCount() + "");
                Log.e("lastVisible", customLayoutManager.findLastVisibleItemPosition() + "");
                int lastVisible = customLayoutManager.findLastVisibleItemPosition();
                /*if(lastVisible +1 == itemList.size()){
                    startValue = startValue + 10;
                    setRetrofit(queryString);
                }*/
                if (lastVisible == itemList.size() - 1) {
                    startValue = startValue + 100;
                    setRetrofit(queryString);
                }
            }
        });
        search_word_play();
        spinnerSort();


    }

    private void search_word_play() {
        Intent intent = getIntent();
        String search_word = intent.getStringExtra("search_word_result");
        Log.d("search_word", "search_word_play: " + search_word);
        goSearch();
        binding.etQuery.setText(search_word);

    }

    public void spinnerSort() {
        arraylist.add("유사도 순");
        arraylist.add("최저가 순");
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, arraylist);
        binding.spinnerSort.setAdapter(adapter);
        binding.spinnerSort.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                select_item = String.valueOf(arraylist.get(position));
                switch (select_item) {
                    case "유사도 순":
                        try {
                            if (!binding.etQuery.getText().toString().isEmpty()) {

                                binding.rcv.scrollToPosition(0);
                                Toast.makeText(SearchActivity.this, "검색어와 유사한 물품을 검색합니다.", Toast.LENGTH_SHORT).show();
                                sortType = "sim";
                                clearData();
                                setRetrofit(queryString);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;

                    case "최저가 순":
                        try {
                            if (!binding.etQuery.getText().toString().isEmpty()) {

                                binding.rcv.scrollToPosition(0);
                                Toast.makeText(SearchActivity.this, "최저가 순으로 검색합니다.", Toast.LENGTH_SHORT).show();
                                sortType = "asc";
                                clearData();
                                setRetrofit(queryString);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void initView() {

        binding.ibSearch.setOnClickListener(this);
        binding.pb.setVisibility(View.GONE);
        binding.srl.setOnRefreshListener(this);

        binding.srl.setOnRefreshListener(() -> {
            if (!binding.etQuery.getText().toString().isEmpty()) {
                binding.srl.setRefreshing(false);
                clearData();
                Toast.makeText(SearchActivity.this, "다시 검색합니다.", Toast.LENGTH_SHORT).show();
                setRetrofit(queryString);
            } else {
                binding.srl.setRefreshing(false);
                binding.pb.setVisibility(View.GONE);
                Toast.makeText(SearchActivity.this, "검색어를 입력하세요.", Toast.LENGTH_SHORT).show();
            }
        });

        binding.etQuery.setOnKeyListener((view, keyCode, keyEvent) -> {
            if (keyCode == keyEvent.KEYCODE_ENTER && keyEvent.getAction() == KeyEvent.ACTION_UP) {
                goSearch();
                hideKeyboard();
                return true;
            }
            return false;
        });
    }

    private void clearData() {
        itemList.clear();
        startValue = 1;
        lprice = 2147483647;
    }

    private HttpLoggingInterceptor loggingInterceptor() {
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor(message -> Log.e("okhttp : ", message + ""));
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return httpLoggingInterceptor;
    }

    public void setRetrofit(String queryString) {
        binding.pb.setVisibility(View.VISIBLE);
        binding.tvLowPrice.setVisibility(View.INVISIBLE);

        if (startValue == 1) {
            itemList.clear();
            lprice = 2147483647;
        }

        OkHttpClient client = new OkHttpClient.Builder().addNetworkInterceptor(loggingInterceptor()).build();
        Retrofit retrofit = new Retrofit.Builder().baseUrl(NAVER_URL).client(client).addCallAdapterFactory(RxJava2CallAdapterFactory.create()).addConverterFactory(GsonConverterFactory.create()).build();
        NaverShoppingSearchService naverShoppingSearchService = retrofit.create(NaverShoppingSearchService.class);
        Observable<SearchDataList> getSearchData = naverShoppingSearchService.getSearchDataList(queryString, displayValue, startValue, sortType);
        getSearchData.observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new Observer<SearchDataList>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(SearchDataList searchDataList) {
                for (Items itemResult : searchDataList.getItems()) {
                    itemResult.setTitle(itemResult.getTitle().replace("<b>", ""));
                    itemResult.setTitle(itemResult.getTitle().replace("</b>", ""));
                    if (lprice >= itemResult.getLprice()) {
                        lprice = itemResult.getLprice();
                    }
                    itemList.add(itemResult);
                }
            }

            @Override
            public void onError(Throwable e) {
                Log.e("error data", e.getLocalizedMessage() + "");
                binding.pb.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onComplete() {
                listTypeAdapter.notifyDataSetChanged();
                binding.srl.setRefreshing(false);
                binding.srl.setEnabled(true);
                binding.pb.setVisibility(View.INVISIBLE);
                if (itemList.size() == 0) {
                    lprice = 2147483647;
                    binding.pb.setVisibility(View.GONE);
                    binding.tvLowPrice.setText("검색 결과 없음");
                    Toast.makeText(SearchActivity.this, "검색 결과가 없습니다.", Toast.LENGTH_SHORT).show();
                }
                if (lprice != 2147483647) {
                    binding.tvLowPrice.setText(String.format("%,d", lprice) + "원");
                } else {
                    binding.tvLowPrice.setText("검색 결과 없음");
                    Toast.makeText(SearchActivity.this, "검색 결과가 없습니다.", Toast.LENGTH_SHORT).show();
                }
                binding.tvLowPrice.setVisibility(View.VISIBLE);
                Log.e("listItemPositon",
                        customLayoutManager.findLastCompletelyVisibleItemPosition() + "");
                binding.pb.setVisibility(View.INVISIBLE);
            }
        });
    }

    private boolean networkCheck() {
        boolean connect = true;
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo == null) {
            connect = false;
        }
        return connect;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ib_search:
                clearData();
                goSearch();
                hideKeyboard();
                break;
        }
    }

    public void goSearch() {
        //검색데이터 가져오기!!
        Intent intent = getIntent();
        String search_word = intent.getStringExtra("search_word_result");

        if (search_word != null) {
            Log.d("queryString", "여기로 들어오나요 ??");
            queryString = search_word;
            Log.d("queryString", "goSearch: " + queryString);
        } else {
            queryString = binding.etQuery.getText().toString();
        }
        binding.pb.setVisibility(View.VISIBLE);
        if (queryString.equals("")) {
            Toast.makeText(SearchActivity.this, "검색어를 입력하세요", Toast.LENGTH_SHORT).show();
            binding.pb.setVisibility(View.GONE);
        } else {
            if (networkCheck()) {
                clearData();
                listTypeAdapter.notifyDataSetChanged();
                setRetrofit(queryString);
                binding.pb.setVisibility(View.GONE);
            } else {
                Toast.makeText(this, "인터넷에 연결되어 있지 않아 검색할 수 없습니다.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    //엔터키를 누르고 나면(혹은 상품 검색 버튼을 누르면) InputMethodManage를 통해 키보드를 숨김.
    public void hideKeyboard() {
        InputMethodManager immanager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        immanager.hideSoftInputFromWindow(binding.etQuery.getWindowToken(), 0);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_item_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRefresh() {
        binding.srl.setEnabled(false);
        goSearch();
    }
}


