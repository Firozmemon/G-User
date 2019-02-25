package com.firozmemon.g_user.ui.main;

import android.app.PendingIntent;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
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
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.firozmemon.g_user.R;
import com.firozmemon.g_user.helper.MyClipboardManager;
import com.firozmemon.g_user.helper.Utility;
import com.firozmemon.g_user.model.Item;
import com.firozmemon.g_user.model.UserData;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.AndroidInjection;

public class MainActivity extends AppCompatActivity implements MainActivityContract.View,
        MainActivityAdapter.AdapterItemClickListener, SearchView.OnQueryTextListener {

    public static final String DEFAULT_USER_NAME = "firoz memon";

    @Inject
    MainActivityContract.Presenter presenter;
    MainActivityAdapter adapter;
    UserData userData;

    SearchView searchView;

    private CustomTabsClient client;

    @BindView(R.id.coordinatorLayout)
    CoordinatorLayout coordinatorLayout;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.noDataFound)
    TextView noDataFound;

    @Inject
    Context applicationContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        // Displaying app icon in toolbar
        getSupportActionBar().setIcon(R.mipmap.ic_launcher);
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (searchView != null && !searchView.isIconified()) {
            searchView.setIconified(true);
        }

        if (Utility.isInternetAvailable(applicationContext)) {
            if (adapter == null) // this will be true, only for first time
                presenter.getUserData(DEFAULT_USER_NAME);
        } else
            displayInternetNotAvailable(true);
    }

    @Override
    protected void onStop() {
        super.onStop();

        presenter.unsubscribe();
    }

    @Override
    public void onBackPressed() {
        if (!searchView.isIconified()) {
            searchView.setIconified(true);
        } else
            super.onBackPressed();
    }

    @Override
    public void displaySuccess(Object obj) {
        // Binding service only when some data is present
        String packageName = "com.android.chrome";
        CustomTabsClient.bindCustomTabsService(this, packageName, serviceConnection);

        userData = (UserData) obj;

        displayRecyclerView(true);

        recyclerView.setLayoutManager(new GridLayoutManager(MainActivity.this, 2));

        adapter = new MainActivityAdapter(MainActivity.this, userData.getItems());
        adapter.setAdapterItemClickListener(this);

        recyclerView.setAdapter(adapter);
    }

    @Override
    public void displayError(String message) {
        displayRecyclerView(false);
        Snackbar.make(coordinatorLayout, message, Snackbar.LENGTH_LONG)
                .show();
    }

    /**
     * This method handles display of RecyclerView/NoDataFound Textview
     * w.r.t
     * boolean passed as parameter
     *
     * @param showRecyclerView
     */
    private void displayRecyclerView(boolean showRecyclerView) {
        if (showRecyclerView) {
            noDataFound.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        } else {
            noDataFound.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onAdapterItemClick(View view, int position) {
        Item item = adapter.getItem(position);

        final String url = item.getHtmlUrl();

        if (Utility.isInternetAvailable(applicationContext))
            setupAndOpenChromeCustomTab(url);
        else
            displayInternetNotAvailable(false);
    }

    private void setupAndOpenChromeCustomTab(final String url) {
        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder(getSession());
        builder.setToolbarColor(ContextCompat.getColor(this, android.R.color.darker_gray));
//        builder.setCloseButtonIcon(BitmapFactory.decodeResource(getResources(),
//                android.R.drawable.ic_menu_share));
        builder.addMenuItem(getString(R.string.connect), setMenuItem());
        builder.setActionButton(BitmapFactory.decodeResource(getResources(),
                android.R.drawable.ic_menu_share), getString(R.string.share), addActionButton());
        builder.setStartAnimations(applicationContext, android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        builder.setExitAnimations(applicationContext, android.R.anim.slide_in_left, android.R.anim.slide_out_right);

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
        shareIntent.putExtra(Intent.EXTRA_TEXT, "Connect with Project Creator at LinkedIn : https://www.linkedin.com/in/firozmemon0/");
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
//            client.warmup(1); // If possible, issue the warmup call in advance to reduce waiting when the custom tab activity is started
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            client = null;
        }
    };


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);

        MenuItem item = menu.findItem(R.id.action_search);
        searchView = (SearchView) MenuItemCompat.getActionView(item);
        searchView.setOnQueryTextListener(this);

        MenuItemCompat.setOnActionExpandListener(item,
                new MenuItemCompat.OnActionExpandListener() {
                    @Override
                    public boolean onMenuItemActionCollapse(MenuItem item) {
                        // SearchView collapsed

                        // Hide keyboard
                        InputMethodManager imm = (InputMethodManager) getSystemService(
                                MainActivity.this.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(searchView.getWindowToken(), 0);

                        return true; // Return true to collapse action view
                    }

                    @Override
                    public boolean onMenuItemActionExpand(MenuItem item) {
                        // SearchView expanded
                        return true; // Return true to expand action view
                    }
                });

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        if (Utility.isInternetAvailable(MainActivity.this)) {
            if (presenter != null)
                presenter.getUserData(query);
        } else
            displayInternetNotAvailable(false);
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return true;
    }

    /**
     * Displays <code>Internet Not Available</code> in Snackbar
     * and also provides retry button to check if connected to internet.
     * If connected, will load default user data
     *
     * @param shouldModifyUI if true, will update UI accordingly
     */
    public void displayInternetNotAvailable(final boolean shouldModifyUI) {
        if (shouldModifyUI)
            displayRecyclerView(false);

        Snackbar snackbar = Snackbar.make(coordinatorLayout, R.string.internetNotAvailable, Snackbar.LENGTH_INDEFINITE)
                .setAction(R.string.retry, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (Utility.isInternetAvailable(MainActivity.this)) {
                            if (presenter != null)
                                presenter.getUserData(DEFAULT_USER_NAME);
                        } else
                            displayInternetNotAvailable(shouldModifyUI);
                    }
                });
        snackbar.getView()
                .setBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.colorPrimary));

        snackbar.show();
    }
}
