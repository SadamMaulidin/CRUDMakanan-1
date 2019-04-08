package com.daffzzaqihaq.crudmakanan.ui.makananbyuser;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.daffzzaqihaq.crudmakanan.R;
import com.daffzzaqihaq.crudmakanan.adapter.MakananAdapter;
import com.daffzzaqihaq.crudmakanan.model.makanan.MakananData;
import com.daffzzaqihaq.crudmakanan.utils.Constant;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class MakananByUser extends Fragment implements MakananByUserContract.View {


    @BindView(R.id.pb_loading)
    ProgressBar pbLoading;
    @BindView(R.id.txt_info)
    TextView txtInfo;
    @BindView(R.id.rl_progress_by_user)
    RelativeLayout rlProgressByUser;
    @BindView(R.id.rv_makanan)
    RecyclerView rvMakanan;
    @BindView(R.id.sr_makanan_by_user)
    SwipeRefreshLayout srMakananByUser;
    Unbinder unbinder;

    private MakananByUserPresenter mMakananByUserPresenter = new MakananByUserPresenter(this);
    private String idUser;

    public MakananByUser() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_makanan_by_user, container, false);
        unbinder = ButterKnife.bind(this, view);

        // Mengambil id User dari sharedpref
        SharedPreferences pref = getContext().getSharedPreferences(Constant.pref_name, 0);

        // Memasukan id user yg sudah diambil ke dalam variable
        idUser = pref.getString(Constant.KEY_USER_ID, "");

        // Merequest data makanan by user
        mMakananByUserPresenter.getListByUser(idUser);

        srMakananByUser.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                srMakananByUser.setRefreshing(false);
                // Merequest data makanan by user
                mMakananByUserPresenter.getListByUser(idUser);

            }
        });

        return view;
    }

    @Override
    public void showProgress() {
        rlProgressByUser.setVisibility(View.VISIBLE);
        srMakananByUser.setVisibility(View.GONE);
    }

    @Override
    public void hideProgress() {
        srMakananByUser.setRefreshing(false);
        rlProgressByUser.setVisibility(View.GONE);
        rvMakanan.setVisibility(View.VISIBLE);
        srMakananByUser.setVisibility(View.VISIBLE);
    }

    @Override
    public void showFoodByUser(List<MakananData> foodByUserList) {
        rvMakanan.setLayoutManager(new LinearLayoutManager(getContext()));
        rvMakanan.setAdapter(new MakananAdapter(MakananAdapter.TYPE_5, getContext(), foodByUserList));
    }

    @Override
    public void showFailureMessage(String msg) {
        srMakananByUser.setVisibility(View.VISIBLE);
        rlProgressByUser.setVisibility(View.VISIBLE);
        rvMakanan.setVisibility(View.GONE);
        pbLoading.setVisibility(View.GONE);
        txtInfo.setText(msg);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
