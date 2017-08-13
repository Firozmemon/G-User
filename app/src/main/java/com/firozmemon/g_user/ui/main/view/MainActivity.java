package com.firozmemon.g_user.ui.main.view;

import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.firozmemon.g_user.R;
import com.firozmemon.g_user.api.ApiCallingAgent;
import com.firozmemon.g_user.api.ApiRepository;
import com.firozmemon.g_user.model.UserData;
import com.firozmemon.g_user.ui.main.presenter.MainActivityPresenter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;

public class MainActivity extends AppCompatActivity implements MainActivityView,MainActivityAdapter.AdapterItemClickListener {

    public static final String DEFAULT_USER_NAME = "firoz memon";

    MainActivityPresenter presenter;
    MainActivityAdapter adapter;
    UserData userData;

    @BindView(R.id.coordinatorLayout)
    CoordinatorLayout coordinatorLayout;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.noDataFound)
    TextView noDataFound;

    @OnClick(R.id.fab)
    void onFabClicked() {
        Snackbar.make(coordinatorLayout, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
    }

    @Override
    protected void onStart() {
        super.onStart();

        ApiRepository apiRepository = ApiCallingAgent.getInstance();
        presenter = new MainActivityPresenter(this, apiRepository, AndroidSchedulers.mainThread());
        presenter.getUserData(DEFAULT_USER_NAME);
    }

    @Override
    protected void onStop() {
        super.onStop();

        presenter.unsubscribe();
    }

    @Override
    public void displaySuccess(Object obj) {
        userData = (UserData) obj;

        noDataFound.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);

        recyclerView.setLayoutManager(new GridLayoutManager(MainActivity.this, 2));

        adapter = new MainActivityAdapter(MainActivity.this, userData.getItems());
        adapter.setAdapterItemClickListener(this);

        recyclerView.setAdapter(adapter);
    }

    @Override
    public void displayError(String message) {
        Snackbar.make(coordinatorLayout, message, Snackbar.LENGTH_LONG)
                .show();
        noDataFound.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
    }

    @Override
    public void onAdapterItemClick(View view, int position) {

    }
}
