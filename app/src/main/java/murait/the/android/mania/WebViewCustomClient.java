package murait.the.android.mania;

import android.app.ProgressDialog;
import android.content.Context;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

public class WebViewCustomClient extends WebViewClient {

    private Context mContext;
    private PrefManager prefManager;
    private InterstitialAd mInterstitialAd;
    private AdRequest interAdRequest;
    private ProgressDialog progressDialog;

    public WebViewCustomClient(Context mContext) {
        this.mContext = mContext;
        prefManager = new PrefManager(mContext);
    }

    public void setmInterstitialAd(InterstitialAd mInterstitialAd) {
        this.mInterstitialAd = mInterstitialAd;
    }

    public void setInterAdRequest(AdRequest interAdRequest) {
        this.interAdRequest = interAdRequest;
    }

    //If you will not use this method url links are opeen in new brower not in webview
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        view.loadUrl(url);
        prefManager.addVar();
        if (prefManager.getVar() % PrefManager.ADS_SHOW_TIME == 0) {
            // Load ads into Interstitial Ads
            mInterstitialAd.loadAd(interAdRequest);
            if (mInterstitialAd.isLoaded())
                mInterstitialAd.show();
        }
        return true;
    }

    //Show loader on url load
    public void onLoadResource(WebView view, String url) {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(mContext);
            progressDialog.setMessage("Loading...");
        }

        if (!progressDialog.isShowing())
            progressDialog.show();
        prefManager.addVar();
    }


    public void onPageFinished(WebView view, String url) {
        try {
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
                prefManager.addVar();
                progressDialog = null;
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
        //super.onReceivedHttpError(view, request, errorResponse);
        if (progressDialog != null && progressDialog.isShowing())
            progressDialog.dismiss();
    }
}
