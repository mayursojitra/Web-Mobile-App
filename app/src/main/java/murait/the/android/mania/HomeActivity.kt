package murait.the.android.mania

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Intent
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.webkit.*
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.navigation.ui.AppBarConfiguration
import com.google.android.gms.ads.*
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.material.navigation.NavigationView
import murait.the.android.mania.databinding.ActivityHomeBinding

class HomeActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityHomeBinding
    private val url = "https://www.theandroid-mania.com/"
    private var currentUrl = ""
    private val FILECHOOSER_RESULTCODE = 190
    private var mInterstitialAd: InterstitialAd? = null
    private var interAdRequest: AdRequest? = null
    private var progressDialog: ProgressDialog? = null
    private var mUploadMessage: ValueCallback<Array<Uri>>? = null
    private var prefManager: PrefManager? = null
    private var mAdIsLoading: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarHome.toolbar)

        MobileAds.initialize(this) {}

        prefManager = PrefManager(baseContext)
        progressDialog = ProgressDialog(this)
        progressDialog?.setMessage("Loading...")

        binding.appBarHome.fab.setOnClickListener {
            val phone = "tel:" + resources.getString(R.string.phone)
            val i = Intent(Intent.ACTION_DIAL, Uri.parse(phone))
            startActivity(i)
        };

        val actionBarDrawerToggle = ActionBarDrawerToggle(
            this,
            binding.drawerLayout,
            binding.appBarHome.toolbar,
            R.string.app_name,
            R.string.app_name
        );
        binding.drawerLayout.addDrawerListener(actionBarDrawerToggle);
        binding.navView.setNavigationItemSelectedListener(this)
        actionBarDrawerToggle.syncState()

        loadBannerAds()
        loadInterAds()

        if (isOnline()) {
            binding.appBarHome.contentMain.llError.rlContent.visibility = View.GONE
            binding.appBarHome.contentMain.webView.visibility = View.VISIBLE
            startWebView(url)
        } else {
            binding.appBarHome.contentMain.llError.rlContent.visibility = View.VISIBLE
            binding.appBarHome.contentMain.webView.visibility = View.GONE
            if (progressDialog != null && progressDialog?.isShowing == true)
                progressDialog?.dismiss()
        }
    }

    private fun loadInterAds() {
        var interAdRequest = AdRequest.Builder().build()

        InterstitialAd.load(
            this, getString(R.string.interstitial_full_screen), interAdRequest,
            object : InterstitialAdLoadCallback() {
                override fun onAdFailedToLoad(adError: LoadAdError) {
                    mInterstitialAd = null
                    mAdIsLoading = false
                }

                override fun onAdLoaded(interstitialAd: InterstitialAd) {
                    mInterstitialAd = interstitialAd
                    mAdIsLoading = false
                }
            }
        )
    }

    private fun showInterAds(url: String) {
        if (mInterstitialAd != null) {
            mInterstitialAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
                override fun onAdDismissedFullScreenContent() {
                    binding.appBarHome.contentMain.webView.loadUrl(url)
                    loadInterAds()
                }

                override fun onAdFailedToShowFullScreenContent(adError: AdError?) {

                }

                override fun onAdShowedFullScreenContent() {
                    mInterstitialAd = null;
                }
            }
            mInterstitialAd?.show(this)
        } else {
            binding.appBarHome.contentMain.webView.loadUrl(url)
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun startWebView(url: String) {

        binding.appBarHome.contentMain.webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                prefManager?.addVar()
                if (mInterstitialAd != null && prefManager?.tVar?.rem(PrefManager.ADS_SHOW_TIME) == 0) {
                    showInterAds(url.toString())
                } else {
                    view?.loadUrl(url.toString())
                }
                return true
            }

            //Show loader on url load
            /*override fun onLoadResource(view: WebView?, url: String?) {
                if (progressDialog == null) {
                    progressDialog = ProgressDialog(this@HomeActivity)
                    progressDialog?.setMessage("Loading...")
                }
                if (progressDialog != null && progressDialog?.isShowing == false)
                    progressDialog?.show()
                prefManager!!.addVar()
            }*/


            override fun onPageFinished(view: WebView?, url: String?) {
                try {
                    if (progressDialog!!.isShowing) {
                        progressDialog!!.dismiss()
                        prefManager!!.addVar()
                        progressDialog = null
                    }
                } catch (exception: Exception) {
                    exception.printStackTrace()
                }
            }

            override fun onReceivedHttpError(
                view: WebView?,
                request: WebResourceRequest?,
                errorResponse: WebResourceResponse?
            ) {
                //super.onReceivedHttpError(view, request, errorResponse);
                if (progressDialog != null && progressDialog?.isShowing == false) progressDialog?.dismiss()
            }
        }
        binding.appBarHome.contentMain.webView.webChromeClient = object : WebChromeClient() {
            override fun onShowFileChooser(
                webView: WebView,
                filePathCallback: ValueCallback<Array<Uri>>,
                fileChooserParams: FileChooserParams
            ): Boolean {
                mUploadMessage?.onReceiveValue(null)
                mUploadMessage = filePathCallback
                val i = Intent(Intent.ACTION_GET_CONTENT)
                i.addCategory(Intent.CATEGORY_OPENABLE)
                i.type = "*/*"
                startActivityForResult(
                    Intent.createChooser(i, "File Chooser"),
                    FILECHOOSER_RESULTCODE
                )
                return true
            }


        }


        // Other webview options
        binding.appBarHome.contentMain.webView.settings.javaScriptEnabled = true

        // Other webview options
        binding.appBarHome.contentMain.webView.settings.loadWithOverviewMode = true
        binding.appBarHome.contentMain.webView.settings.useWideViewPort = true
        binding.appBarHome.contentMain.webView.scrollBarStyle = WebView.SCROLLBARS_OUTSIDE_OVERLAY
        binding.appBarHome.contentMain.webView.isScrollbarFadingEnabled = false
        binding.appBarHome.contentMain.webView.settings.builtInZoomControls = false
        binding.appBarHome.contentMain.webView.settings.allowContentAccess = true

        binding.appBarHome.contentMain.webView.setDownloadListener(DownloadListener { url, userAgent, contentDisposition, mimetype, contentLength ->
            val i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse(url)
            startActivity(i)
        })

        currentUrl = url
        //Load url in webview
        //Load url in webview
        binding.appBarHome.contentMain.webView.loadUrl(url)
    }

    private fun loadBannerAds() {
        val adRequest = AdRequest.Builder().build()
        binding.appBarHome.contentMain.adView.loadAd(adRequest)

        binding.appBarHome.contentMain.adView.adListener = object : AdListener() {
            override fun onAdLoaded() {
                binding.appBarHome.contentMain.adView.visibility = View.VISIBLE
            }

            override fun onAdFailedToLoad(adError: LoadAdError) {
                binding.appBarHome.contentMain.adView.visibility = View.GONE
            }

            override fun onAdOpened() {
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
            }

            override fun onAdClicked() {
                // Code to be executed when the user clicks on an ad.
            }

            override fun onAdClosed() {
                // Code to be executed when the user is about to return
                // to the app after tapping on an ad.
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        //menuInflater.inflate(R.menu.home, menu)
        return true
    }

    override fun onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    private fun closeDrawer() {
        binding.drawerLayout.closeDrawer(GravityCompat.START)
    }

    override fun onNavigationItemSelected(menuItem: MenuItem): Boolean {
        closeDrawer()
        when (menuItem.getItemId()) {

        }
        return false
    }

    private fun isOnline(): Boolean {
        val cm = getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        val netInfo = cm.activeNetworkInfo
        return netInfo != null && netInfo.isConnectedOrConnecting
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        super.onActivityResult(requestCode, resultCode, intent)
        if (requestCode == FILECHOOSER_RESULTCODE) {
            if (null == mUploadMessage || intent == null || resultCode != RESULT_OK) {
                return
            }
            val dataString = intent.dataString
            mUploadMessage!!.onReceiveValue(arrayOf(Uri.parse(dataString)))
            mUploadMessage = null
        }
    }
}