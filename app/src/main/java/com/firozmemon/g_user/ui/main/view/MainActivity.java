package com.firozmemon.g_user.ui.main.view;

import android.app.PendingIntent;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.customtabs.CustomTabsCallback;
import android.support.customtabs.CustomTabsClient;
import android.support.customtabs.CustomTabsIntent;
import android.support.customtabs.CustomTabsServiceConnection;
import android.support.customtabs.CustomTabsSession;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.firozmemon.g_user.R;
import com.firozmemon.g_user.api.ApiCallingAgent;
import com.firozmemon.g_user.api.ApiRepository;
import com.firozmemon.g_user.helper.MyClipboardManager;
import com.firozmemon.g_user.model.Item;
import com.firozmemon.g_user.model.UserData;
import com.firozmemon.g_user.ui.main.presenter.MainActivityPresenter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;

public class MainActivity extends AppCompatActivity implements MainActivityView, MainActivityAdapter.AdapterItemClickListener {

    public static final String DEFAULT_USER_NAME = "firoz memon";

    MainActivityPresenter presenter;
    MainActivityAdapter adapter;
    UserData userData;

    private CustomTabsClient client;

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
        // Binding service only when some data is present
        String packageName = "com.android.chrome";
        CustomTabsClient.bindCustomTabsService(this, packageName, serviceConnection);

        userData = (UserData) obj;

        if (userData.getItems().isEmpty() || userData.getItems().size() < 1) {
            noDataFound.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            noDataFound.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);

            recyclerView.setLayoutManager(new GridLayoutManager(MainActivity.this, 2));

            adapter = new MainActivityAdapter(MainActivity.this, userData.getItems());
            adapter.setAdapterItemClickListener(this);

            recyclerView.setAdapter(adapter);
        }
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
        Item item = adapter.getItem(position);

        final String url = item.getHtmlUrl();

        setupAndOpenChromeCustomTab(url);
    }

    private void setupAndOpenChromeCustomTab(final String url) {
        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder(getSession());
        builder.setToolbarColor(ContextCompat.getColor(this, android.R.color.darker_gray));
        builder.setCloseButtonIcon(BitmapFactory.decodeResource(getResources(),
                android.R.drawable.ic_menu_share));
        builder.addMenuItem(getString(R.string.connect), setMenuItem());
        builder.setActionButton(BitmapFactory.decodeResource(getResources(),
                android.R.drawable.ic_menu_share), getString(R.string.share), addActionButton());
        builder.setStartAnimations(this, android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        builder.setExitAnimations(this, android.R.anim.slide_in_left, android.R.anim.slide_out_right);

        CustomTabsIntent mIntent = builder.build();
        try {
            mIntent.launchUrl(this, Uri.parse(url));
        } catch (ActivityNotFoundException e) {
            Snackbar.make(coordinatorLayout, R.string.noActivityFound, Snackbar.LENGTH_LONG)
                    .setAction(R.string.copyLink, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (MyClipboardManager.getInstance(MainActivity.this).copyToClipboard(url)) {
                                Snackbar.make(coordinatorLayout, R.string.linkCopiedSuccessfully, Snackbar.LENGTH_LONG)
                                        .show();
                            } else {
                                Snackbar.make(coordinatorLayout, R.string.linkCannotBeCopied, Snackbar.LENGTH_LONG)
                                        .show();
                            }
                        }
                    }).show();
        }
    }


    private CustomTabsSession getSession() {
        return client.newSession(new CustomTabsCallback() {
            @Override
            public void onNavigationEvent(int navigationEvent, Bundle extras) {
                super.onNavigationEvent(navigationEvent, extras);
            }
        });
    }

    private PendingIntent addActionButton() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Connect at LinkedIn");
        shareIntent.putExtra(Intent.EXTRA_TEXT, "Connect with Dev at LinkedIn : https://www.linkedin.com/in/firozmemon0/");
        return PendingIntent.getActivity(this, 0, shareIntent, 0);
    }

    private PendingIntent setMenuItem() {
        Intent playStoreIntent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("https://www.linkedin.com/in/firozmemon0/"));
        return PendingIntent.getActivity(this, 0, playStoreIntent, 0);
    }

    CustomTabsServiceConnection serviceConnection = new CustomTabsServiceConnection() {
        @Override
        public void onCustomTabsServiceConnected(ComponentName componentName, CustomTabsClient customTabsClient) {
            client = customTabsClient;
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            client = null;
        }
    };
}
