package com.example.sh.androidregisterandlogin.TotalHome.Frags;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sh.androidregisterandlogin.R;
import com.example.sh.androidregisterandlogin.TotalHome.Adapters.PhonebookAdapter;
import com.example.sh.androidregisterandlogin.TotalHome.Datas.AddressData;
import com.example.sh.androidregisterandlogin.databinding.FragmentPhonebookBinding;
import com.google.android.material.appbar.CollapsingToolbarLayout;

import java.util.ArrayList;
import java.util.List;


public class PhoneBookFragment extends Fragment {

    private FragmentPhonebookBinding binding;
    PhonebookAdapter adapter;
    ArrayList<AddressData> contactlist = new ArrayList<>();

    public PhoneBookFragment() {
    }

    public static PhoneBookFragment newInstance() {
        return new PhoneBookFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_phonebook, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((AppCompatActivity) getActivity()).setSupportActionBar(binding.toolbar);
        setHasOptionsMenu(true);
        permissionCheck();
        initCollapsingToolbar(binding.collapsingToolbar);
        initRcv(binding.rcvPhoneBook);
    }

    private void initCollapsingToolbar(CollapsingToolbarLayout ctl) {
        ctl.setTitle("");
        binding.appbar.setExpanded(true);
        String color_change = "연락처개수 : " + getContactList().size();
        ctl.setTitle("연락처개수 : " + getContactList().size());
        ctl.setCollapsedTitleTextAppearance(R.style.coll_main_basic_title);// coll_basic_title 이것도 한번 해보기
        ctl.setExpandedTitleTextAppearance(R.style.coll_expand_title);

    }


    private void permissionCheck() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_CONTACTS}, 1000);
            } else {
                // READ_EXTERNAL_STORAGE 에 대한 권한이 있음.
//                getAudioListFromMediaDatabase();
            }
        }
        // OS가 Marshmallow 이전일 경우 권한체크를 하지 않는다.
        else {
//            getAudioListFromMediaDatabase();
        }
    }

    private void initRcv(RecyclerView rcv) {
        adapter = new PhonebookAdapter(getContactList());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        rcv.setLayoutManager(linearLayoutManager);
        rcv.setHasFixedSize(true);
        rcv.setAdapter(adapter);
    }

    private ArrayList<AddressData> getContactList() {

        Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;

        String[] projection = new String[]{
                ContactsContract.CommonDataKinds.Phone.CONTACT_ID,
                ContactsContract.CommonDataKinds.Phone.NUMBER,
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME};

        String[] selectionArgs = null;
        String sortOrder = ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " COLLATE LOCALIZED ASC";
        Cursor contactCursor = getContext().getContentResolver().query(uri, projection, null, selectionArgs, sortOrder);
        contactCursor.moveToFirst();

        do {
            String phonenumber = contactCursor.getString(1).replaceAll("-",
                    "");

            if (phonenumber.length() == 10) {
                phonenumber = phonenumber.substring(0, 3) + "-"
                        + phonenumber.substring(3, 6) + "-"
                        + phonenumber.substring(6);
            } else if (phonenumber.length() > 8) {
                phonenumber = phonenumber.substring(0, 3) + "-"
                        + phonenumber.substring(3, 7) + "-"
                        + phonenumber.substring(7);
            }

            AddressData acontact = new AddressData();
            acontact.setPhotoid(contactCursor.getLong(0));
            acontact.setPhonenum(phonenumber);
            Log.d("qweqwe", "getContactList: " + contactCursor.getString(2));
            acontact.setName(contactCursor.getString(2));
            contactlist.add(acontact);
        } while (contactCursor.moveToNext());
        return contactlist;
    }


    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        getActivity().getMenuInflater().inflate(R.menu.menu, menu);
        MenuItem item = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) item.getActionView();
        changeSearchViewTextColor(searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
//                키보드의 검색 버튼을 누르면 이 함수가 호출됩니다.
                if (!searchView.isIconified()) {
                    searchView.setIconified(true);
                }
                item.collapseActionView();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
//                이 함수는 searchview에 입력 할 때마다 호출됩니다.
                final List<AddressData> filtermodellist = filter(contactlist, newText);
                adapter.setfileter(filtermodellist);
                return false;
            }
        });
    }


    private List<AddressData> filter(List<AddressData> p1, String query) {
        query = query.toLowerCase();
        final List<AddressData> filteredModelList = new ArrayList<>();
        for (AddressData model : p1) {
            final String text = model.getName().toLowerCase();
            if (text.startsWith(query)) {
                filteredModelList.add(model);
            }
        }
        return filteredModelList;
    }

    private void changeSearchViewTextColor(View view) {
        if (view != null) {
            if (view instanceof TextView) {
                ((TextView) view).setTextColor(Color.BLACK);
                return;
            } else if (view instanceof ViewGroup) {
                ViewGroup viewGroup = (ViewGroup) view;
                for (int i = 0; i < viewGroup.getChildCount(); i++) {
                    changeSearchViewTextColor(viewGroup.getChildAt(i));
                }
            }
        }
    }
}
