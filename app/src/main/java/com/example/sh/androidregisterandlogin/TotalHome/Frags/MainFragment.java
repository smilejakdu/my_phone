package com.example.sh.androidregisterandlogin.TotalHome.Frags;

import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sh.androidregisterandlogin.MapActivity;
import com.example.sh.androidregisterandlogin.R;
import com.example.sh.androidregisterandlogin.SearchActivity;
import com.example.sh.androidregisterandlogin.TotalApp.UserAppsActivity;
import com.example.sh.androidregisterandlogin.TotalBattery.BatteryActivity;
import com.example.sh.androidregisterandlogin.TotalHome.Adapters.MainAdapter;
import com.example.sh.androidregisterandlogin.TotalHome.Adapters.PhonePriceAdapter;
import com.example.sh.androidregisterandlogin.TotalHome.Datas.MainDataItem;
import com.example.sh.androidregisterandlogin.TotalHome.Datas.PhonePriceDataItem;
import com.example.sh.androidregisterandlogin.TotalMusic.TotalMusicActivity;
import com.example.sh.androidregisterandlogin.databinding.FragmentMainBinding;
import com.github.clans.fab.FloatingActionMenu;
import com.google.android.material.appbar.CollapsingToolbarLayout;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainFragment extends Fragment {

    private FragmentMainBinding binding;
    private MainAdapter adapter;
    private SearchView searchView;
    private ArrayList<MainDataItem> mainDataItemList = new ArrayList<>();
    private final int REQ_CODE_SPEECH_INPUT = 100;
    public TextToSpeech tts;
    FloatingActionMenu quickFloatingMenu;

    //오늘날짜가져오기
    SimpleDateFormat mmonth = new SimpleDateFormat("YYYY-MM-dd");
    long mNow;
    Date mDate;

    private ArrayList<PhonePriceDataItem> phonePriceList = new ArrayList();
    private ProgressDialog progressDialog;

    public MainFragment() {

    }

    public static MainFragment newInstance() {
        return new MainFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_main, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ((AppCompatActivity) getActivity()).setSupportActionBar(binding.toolbar);
        setHasOptionsMenu(true);
        initCollapsingToolbar(binding.collapsingToolbar);
        initRcv(binding.rcvMain);
        new AsyncTaskTest().execute();

        binding.llSearchMove.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), SearchActivity.class);
            startActivity(intent);
        });
        binding.ivMap.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), MapActivity.class);
            startActivity(intent);
        });

        binding.quickFloatingActionMenuAdd.setOnClickListener(v -> {
            Toast.makeText(getContext(), "첫번째 선택", Toast.LENGTH_SHORT).show();
            binding.quickFloatingActionMenu.close(true);

        });

        binding.quickFloatingActionVoice.setOnClickListener(v -> {
            promptSpeechInput();
            Toast.makeText(getContext(), "음성 선택", Toast.LENGTH_SHORT).show();
            binding.quickFloatingActionMenu.close(true);
        });

        binding.tvSupportDay.setText(getToday());
    }

    private void initRcv(RecyclerView rcv) {
        adapter = new MainAdapter(getModels());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        rcv.setLayoutManager(linearLayoutManager);
        rcv.setHasFixedSize(true);
        rcv.setAdapter(adapter);
    }


    class AsyncTaskTest extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(getContext());
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.show();

        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                Document doc = Jsoup.connect("https://www.uplussave.com/dev/lawList.mhp").get();
                Elements mElementDataSize = doc.select("tbody").select("tr");

                for (Element elem : mElementDataSize) {
                    String totalData = elem.select("td").text(); //
                    String myTitle = elem.select("tr p[class=phoneName]").text();
                    String myImgUrl = elem.select("tr p[class=phoneImg] img").attr("src");
                    String myGosi = "공시지원금" + elem.select("td span[class=point_color01]").text();
                    String myBirthday = "공시일 :" + elem.select("td span[class=color_gy8]").text();
                    String myModel_name = "모델명 : " + elem.select("td").next().first().text();
                    String myShipment = "출고가 : " + elem.select("td").next().next().first().text();
                    String mySellMoney = "판매가 : " + elem.select("td").next().next().next().next().first().text();
//                    판매가 순위로 정해서 나타내기 .

                    phonePriceList.add(new PhonePriceDataItem(myTitle, myImgUrl, myGosi, myBirthday, myModel_name, myShipment, mySellMoney));
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            PhonePriceAdapter phonePriceAdapter = new PhonePriceAdapter(phonePriceList);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
            binding.rcvSearch.setLayoutManager(linearLayoutManager);
            binding.rcvSearch.setHasFixedSize(true);
            binding.rcvSearch.setAdapter(phonePriceAdapter);
            progressDialog.dismiss();

        }
    }

    private void initCollapsingToolbar(CollapsingToolbarLayout ctl) {
        ctl.setTitle("");
        binding.appbar.setExpanded(true);
        ctl.setTitle("U&Soft Company");
        ctl.setCollapsedTitleTextAppearance(R.style.coll_main_basic_title);
        ctl.setExpandedTitleTextAppearance(R.style.coll_expand_title);
    }

    private ArrayList<MainDataItem> getModels() {

        MainDataItem mainDataItem;

        mainDataItem = new MainDataItem();
        mainDataItem.setName("Music");
        mainDataItem.setImg(R.drawable.ic_main_music);
        mainDataItemList.add(mainDataItem);

        mainDataItem = new MainDataItem();
        mainDataItem.setName("Battery");
        mainDataItem.setImg(R.drawable.ic_main_batter);
        mainDataItemList.add(mainDataItem);

        mainDataItem = new MainDataItem();
        mainDataItem.setName("App");
        mainDataItem.setImg(R.drawable.appimage2);
        mainDataItemList.add(mainDataItem);

        mainDataItem = new MainDataItem();
        mainDataItem.setName("info");
        mainDataItem.setImg(R.drawable.appimage3);
        mainDataItemList.add(mainDataItem);

        return mainDataItemList;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        getActivity().getMenuInflater().inflate(R.menu.menu, menu);
        MenuItem item = menu.findItem(R.id.action_search);
        searchView = (SearchView) item.getActionView();
        ((EditText) searchView.findViewById(R.id.search_src_text)).setHintTextColor(getResources().getColor(R.color.colorPrimary));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                if (!searchView.isIconified()) {
                    searchView.setIconified(true);
                }
                item.collapseActionView();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                final List<MainDataItem> filtermodellist = filter(mainDataItemList, newText);
                adapter.setfileter(filtermodellist);
                return false;
            }
        });
    }

    private List<MainDataItem> filter(List<MainDataItem> p1, String query) {
        query = query.toLowerCase();
        final List<MainDataItem> filteredModelList = new ArrayList<>();
        for (MainDataItem model : p1) {
            final String text = model.getName().toLowerCase();
            if (text.startsWith(query)) {
                filteredModelList.add(model);

            }
        }
        return filteredModelList;
    }


    private void promptSpeechInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                getString(R.string.speech_prompt));

        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getContext(),
                    getString(R.string.speech_not_supported),
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == getActivity().RESULT_OK && null != data) {

                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

                    binding.speechInputTxt.setText(result.get(0));  //분홍색 글씨

                    String voice_result = binding.speechInputTxt.getText().toString();   // 이렇게적으면 안녕으로 바뀌게 된다 .
                    String text, text1 = "안녕", test2 = "누구야";
                    String app = "어플", battry = "배터리", music = "음악", music2 = "오디오", music3 = "노래";
                    String music_start1 = "음악재생", music_start2 = "노래재생", music_title_start = "틀어줘";
                    String search_word1 = "검색";

                    String result1 = voice_result.trim().replaceAll(" ", "");
                    if (result1.replaceAll(" ", "").contains(text1) || result1.contains(test2)) {
                        text = "네 안녕하세요. 저는 혀니 입니다.";
                        tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);
                    } else if (result1.contains(music_title_start)) {
                        Intent intent = new Intent(getContext(), TotalMusicActivity.class);
                        String title = result1.replaceAll("틀어줘", "");
                        String music_title_start_in_music_start = "노래", music_title_start_in_music_start2 = "음악";

                        if (music_title_start_in_music_start.equals(title) || music_title_start_in_music_start2.equals(title)) {
                            Intent music_title_start_in_music_start_intent = new Intent(getContext(), TotalMusicActivity.class);
                            music_title_start_in_music_start_intent.putExtra("music_start", 1);
                            startActivity(intent);
                        }
                        intent.putExtra("music_title", title);
                        startActivity(intent);
                    } else if (result1.equals(music_start1) || result1.equals(music_start2)) {
                        Intent intent = new Intent(getContext(), TotalMusicActivity.class);
                        intent.putExtra("music_start", 1);
                        startActivity(intent);
                    } else if (result1.contains(app)) {
                        Intent intent = new Intent(getContext(), UserAppsActivity.class);
                        startActivity(intent);
                    } else if (result1.contains(battry)) {
                        Intent intent = new Intent(getContext(), BatteryActivity.class);
                        startActivity(intent);
                    } else if (result1.contains(music) || result1.contains(music2) || result1.contains(music3)) {
                        Intent intent = new Intent(getContext(), TotalMusicActivity.class);
                        startActivity(intent);
                    } else if (result1.contains(search_word1)) {
                        Intent intent = new Intent(getContext(), SearchActivity.class);
                        String search_word_result = result1.replaceAll("검색해줘", "").replaceAll("검색", "");
                        intent.putExtra("search_word_result", search_word_result);
                        startActivity(intent);
                    }
                }
                break;
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            getAudioListFromMediaDatabase();
        }
    }


    private void getAudioListFromMediaDatabase() {
        (getActivity()).getSupportLoaderManager().initLoader(0, null, new LoaderManager.LoaderCallbacks<Cursor>() {

            @Override
            public Loader<Cursor> onCreateLoader(int id, Bundle args) {
                Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                String[] projection = new String[]{
                        MediaStore.Audio.Media._ID,
                        MediaStore.Audio.Media.TITLE,
                        MediaStore.Audio.Media.ARTIST,
                        MediaStore.Audio.Media.ALBUM,
                        MediaStore.Audio.Media.ALBUM_ID,
                        MediaStore.Audio.Media.DURATION,
                        MediaStore.Audio.Media.DATA
                };
                String selection = MediaStore.Audio.Media.IS_MUSIC + " = 1";
                String sortOrder = MediaStore.Audio.Media.TITLE + " COLLATE LOCALIZED ASC";
                return new CursorLoader(getContext(), uri, projection, selection, null, sortOrder);
            }

            @Override
            public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
                data.moveToFirst(); // 만약에 cursor 라는 것이 아무것도 밑에 내려갈것이 없을때는 다시 맨위로 올려버린다.
                if (data != null && data.getCount() > 0) {
                    while (data.moveToNext()) {
                        Log.d("qwe", "Title:" + data.getString(data.getColumnIndex(MediaStore.Audio.Media.TITLE)));
                    }
                }
            }

            @Override
            public void onLoaderReset(Loader<Cursor> loader) {

            }
        });
    }

    //현재 월 가져오기
    private String getToday() {
        mNow = System.currentTimeMillis();
        mDate = new Date(mNow);
        return mmonth.format(mDate);
    }
}
