package com.dining_philosophers.beMyGuest

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.webkit.*
import androidx.appcompat.app.AppCompatActivity
import com.dining_philosophers.coolanu.R
import java.util.logging.Logger


class MainActivity : AppCompatActivity() {

    private lateinit var webView: WebView
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


        webView.webChromeClient = object : WebChromeClient() {
            override fun onShowFileChooser(
                webView: WebView,
                filePathCallback: ValueCallback<Array<Uri>>,
                fileChooserParams: FileChooserParams
            ): Boolean {
                val LOG = Logger.getLogger(this.javaClass.name)

                LOG.warning("onShowFileChooser")

                return super.onShowFileChooser(webView, filePathCallback, fileChooserParams);
            }
        }
    }
}

