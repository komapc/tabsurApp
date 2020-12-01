package com.dining_philosophers.beMyGuest

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Message
import android.webkit.*
import androidx.appcompat.app.AppCompatActivity
import com.dining_philosophers.coolanu.R
import java.util.logging.Logger


class MainActivity : AppCompatActivity() {

    private lateinit var webView: WebView
    private lateinit var mWebviewPop: WebView

   // private lateinit var mContainer: FrameLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        webView = findViewById(R.id.web_view)
        val webSettings: WebSettings = webView.settings
        webSettings.javaScriptEnabled = true
        webSettings.domStorageEnabled = true
        webSettings.javaScriptCanOpenWindowsAutomatically = true
        webSettings.supportMultipleWindows()
        window.decorView.systemUiVisibility = 0x10

        webView.loadUrl("https://www.BeMyGuest.app")

        webView.webViewClient = object : WebViewClient() {

            override fun onPageFinished(view: WebView, url: String) {
                val LOG = Logger.getLogger(this.javaClass.name)
                LOG.warning("onPageFinished: $url")

                if (url.startsWith("https://m.facebook.com")) {
                    val redirectUrl: String = url
                    view.loadUrl(redirectUrl)
                    LOG.warning("onPageFinished: redirect to $redirectUrl")

                    return
                }
                super.onPageFinished(view, url)
            }
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                val host = Uri.parse(url).host

                val LOG = Logger.getLogger(this.javaClass.name)
                LOG.warning("shouldOverrideUrlLoading: $host")

                if (host == "mobile.facebook.com" || host == "m.facebook.com") {
                    LOG.warning("shouldOverrideUrlLoading: false")
                    return false
                }
                // Otherwise, the link is not for a page on my site, so launch
                // another Activity that handles URLs
                val intent = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://www.bemyguest.app/_oauth/facebook?close")
                )
                LOG.warning("shouldOverrideUrlLoading: start activity!")

                /*val intent = Intent(Intent.ACTION_VIEW, DisplayMessageActivity::class.java).apply {
                    //putExtra(EXTRA_MESSAGE, message)
                }
*/
                startActivity(intent)
                return true
            }

        }
            webView.webChromeClient = object : WebChromeClient() {
            override fun onShowFileChooser(
                webView: WebView,
                filePathCallback: ValueCallback<Array<Uri>>,
                fileChooserParams: FileChooserParams
            ): Boolean {
                val LOG = Logger.getLogger(this.javaClass.name)

                LOG.warning("onShowFileChooser")

                return super.onShowFileChooser(webView, filePathCallback, fileChooserParams)
            }
                override fun onCreateWindow(
                    view: WebView,
                    isDialog: Boolean,
                    isUserGesture: Boolean,
                    resultMsg: Message?
                ): Boolean {
                    val LOG = Logger.getLogger(this.javaClass.name)
                    LOG.warning("webChromeClient onCreateWindowm start")
                    var url: String? = null
                    val href: Message? = view.handler.obtainMessage()
                    if (href != null) {
                        view.requestFocusNodeHref(href)
                        url = href.getData().getString("url")
                    }


                    LOG.warning("webChromeClient onCreateWindow: $url")

                    //LogHelper.d(LOGTAG, "onCreateWindow: $url")

                    // Unfortunately, url is null when "Log In to Post" button is pressed
                 /*   if (url == null || UriHelper.isFacebookHost(Uri.parse(url))) {
                        // Facebook login requires cookies to be enabled, and on more recent versions
                        // of Android, it's also necessary to enable acceptance of 3rd-party cookies
                        // on the WebView that hosts Facebook comments
                        CookieHelper.setAcceptThirdPartyCookies(mWebView, true)
                        EventBusHelper.post(ShowFacebookWebLoginEvent(resultMsg))
                    } else {
                        LogHelper.d(
                            LOGTAG,
                            "Ignoring request from js to open new window for URL: $url"
                        )
                    }*/
                    return true
                }
        }
    }
}

