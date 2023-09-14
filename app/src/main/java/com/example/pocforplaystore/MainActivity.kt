package com.example.pocforplaystore

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {
    lateinit var webView: WebView
    object AndroidJSInterface {
        @JavascriptInterface
        fun onClicked() {
            Log.d("HelpButton", "Help button clicked")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        webView = findViewById(R.id.webView1)
        webView.settings.javaScriptEnabled = true
        webView.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView, url: String) {
                loadJs(view)
            }
        }
        webView.addJavascriptInterface(AndroidJSInterface, "Android")


        //   webView.loadUrl("https://www.google.com/search?q=www.goa.gov.in+vacancy+2023&rlz=1C1CHBF_enIN1057IN1057&oq=www.go&aqs=chrome.1.69i60j0i131i433i512j69i57j0i131i433i512j69i65j69i60l3.8082j0j4&sourceid=chrome&ie=UTF-8")
        webView.loadUrl("https://www.amazon.in/")
    }

    private fun loadJs(webView: WebView) {
        webView.loadUrl(
            """
             javascript: (function(){
var myEle = document.getElementById("widget-purchaseConfirmationDetails");
if(myEle != null) { 
    Android.onClicked();
}
             })()
         """
        )
    }
}
