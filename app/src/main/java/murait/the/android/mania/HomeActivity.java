package murait.the.android.mania;

import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.DownloadListener;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private final static int FILECHOOSER_RESULTCODE = 190;
    private ProgressDialog progressDialog;
    private PrefManager prefManager;
    private Context mContext;
    private WebView webView;
    private String url = "https://www.theandroid-mania.com/";
    private View llError;
    private String currentUrl = "";
    private AdView mAdView;
    private InterstitialAd mInterstitialAd;
    private AdRequest interAdRequest;
    private ValueCallback<Uri[]> mUploadMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mContext = this;

        llError = findViewById(R.id.llError);

        prefManager = new PrefManager(mContext);

        loadAds();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phno = "tel:" + getResources().getString(R.string.phone);
                Intent i = new Intent(Intent.ACTION_DIAL, Uri.parse(phno));
                startActivity(i);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        webView = (WebView) findViewById(R.id.webView);

        if (isOnline()) {
            llError.setVisibility(View.GONE);
            webView.setVisibility(View.VISIBLE);
            startWebView(url);
        } else {
            llError.setVisibility(View.VISIBLE);
            webView.setVisibility(View.GONE);
            if (progressDialog != null && progressDialog.isShowing())
                progressDialog.dismiss();
        }


    }

    private void loadAds() {
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(getString(R.string.interstitial_full_screen));

        interAdRequest = new AdRequest.Builder().build();
        // Load ads into Interstitial Ads
        mInterstitialAd.loadAd(interAdRequest);

        mInterstitialAd.setAdListener(new AdListener() {
            public void onAdLoaded() {
                if (mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                }
            }
        });

        mAdView = (AdView) findViewById(R.id.adView);

        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();

        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                mAdView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAdClosed() {
                mAdView.setVisibility(View.GONE);
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                mAdView.setVisibility(View.GONE);
            }

            @Override
            public void onAdLeftApplication() {
                mAdView.setVisibility(View.GONE);
            }

            @Override
            public void onAdOpened() {
                super.onAdOpened();
            }
        });

        mAdView.loadAd(adRequest);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (webView.canGoBack()) {
            if (isOnline()) {
                llError.setVisibility(View.GONE);
                webView.setVisibility(View.VISIBLE);
                webView.goBack();
            } else {
                llError.setVisibility(View.VISIBLE);
                webView.setVisibility(View.GONE);
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
            }
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_share) {

            String shareBody = webView.getTitle() + " - " + getResources().getString(R.string.app_name) + webView.getUrl();

            Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, getResources().getString(R.string.app_name));
            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
            startActivity(Intent.createChooser(sharingIntent, "Share using"));

            return true;
        } else if (id == R.id.action_rate) {
            try {
                Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + mContext.getPackageName()));
                startActivity(myIntent);
            } catch (ActivityNotFoundException e) {
                Toast.makeText(this, "No application can handle this request."
                        + " Please install a webbrowser", Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.nav_home) {
            if (isOnline()) {
                llError.setVisibility(View.GONE);
                webView.setVisibility(View.VISIBLE);
                startWebView(url);
            } else {
                llError.setVisibility(View.VISIBLE);
                webView.setVisibility(View.GONE);
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
            }
        } else if (id == R.id.nav_share) {

            String shareBody = "https://play.google.com/store/apps/details?id=" + mContext.getPackageName();
            Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, getResources().getString(R.string.app_name));
            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
            startActivity(Intent.createChooser(sharingIntent, "Share using"));

        } else if (id == R.id.nav_mail) {

            String[] TO = {getResources().getString(R.string.email)};
            String[] CC = {""};

            Intent emailIntent = new Intent(Intent.ACTION_SEND);

            emailIntent.setData(Uri.parse("mailto:" + getResources().getString(R.string.email)));
            emailIntent.setType("text/plain");
            emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
            emailIntent.putExtra(Intent.EXTRA_CC, CC);
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, getResources().getString(R.string.app_name) + " - Inquiry from mobile");
            emailIntent.putExtra(Intent.EXTRA_TEXT, "Hi, " + getResources().getString(R.string.app_name) + "\nURL: " + webView.getUrl() + "\n\n");

            try {
                startActivity(Intent.createChooser(emailIntent, "Send mail..."));
                finish();
            } catch (android.content.ActivityNotFoundException ex) {
                Toast.makeText(mContext, "There is no email client installed.", Toast.LENGTH_SHORT).show();
            }
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void startWebView(String url) {

        //Create new webview Client to show progress dialog
        //When opening a url or click on link

        WebViewCustomClient webViewCustomClient = new WebViewCustomClient(mContext);
        webViewCustomClient.setInterAdRequest(interAdRequest);
        webViewCustomClient.setmInterstitialAd(mInterstitialAd);
        webView.setWebViewClient(webViewCustomClient);

        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {

                if (mUploadMessage != null) {
                    mUploadMessage.onReceiveValue(null);
                }

                mUploadMessage = filePathCallback;

                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.addCategory(Intent.CATEGORY_OPENABLE);
                i.setType("*/*");

                startActivityForResult(Intent.createChooser(i, "File Chooser"), FILECHOOSER_RESULTCODE);

                return true;
            }
        });

        // Javascript inabled on webview
        webView.getSettings().setJavaScriptEnabled(true);

        // Other webview options
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        webView.setScrollbarFadingEnabled(false);
        webView.getSettings().setBuiltInZoomControls(false);
        webView.getSettings().setAllowContentAccess(true);

        webView.setDownloadListener(new DownloadListener() {
            public void onDownloadStart(String url, String userAgent,
                                        String contentDisposition, String mimetype,
                                        long contentLength) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });

        currentUrl = url;
        //Load url in webview
        webView.loadUrl(url);


    }

    protected boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        } else {
            return false;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {

        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == FILECHOOSER_RESULTCODE) {
            if (null == mUploadMessage || intent == null || resultCode != RESULT_OK) {
                return;
            }
            Uri[] result = null;
            String dataString = intent.getDataString();
            if (dataString != null) {
                result = new Uri[]{Uri.parse(dataString)};
            }
            mUploadMessage.onReceiveValue(result);
            mUploadMessage = null;
        }
    }


}
