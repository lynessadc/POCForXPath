package com.example.pocforplaystore

import android.os.Bundle
import android.util.JsonReader
import android.util.JsonToken
import android.util.Log
import android.webkit.JavascriptInterface
import android.webkit.ValueCallback
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import java.io.IOException
import java.io.StringReader

class MainActivity : AppCompatActivity() {
    lateinit var webView: WebView
    lateinit var button: Button


    object AndroidJSInterface {
        @JavascriptInterface
        fun onClicked(msg: String?) {
            Log.d("Ordered Value ", "Ordered values is  : $msg")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        webView = findViewById(R.id.webView1)

        webView.settings.loadWithOverviewMode = true
        webView.settings.useWideViewPort = true
        webView.settings.javaScriptEnabled = true
        webView.settings.domStorageEnabled = true
        webView.settings.javaScriptCanOpenWindowsAutomatically = true
        webView.settings.databaseEnabled = true
        webView.settings.allowFileAccess = true
        webView.settings.allowContentAccess = true
        webView.settings.allowFileAccessFromFileURLs = true
        webView.settings.allowUniversalAccessFromFileURLs = true
        WebView.setWebContentsDebuggingEnabled(true);
        val ajioScript =
            "(function() { var a = document.getElementById('cart-order-total').textContent ; return a  ; })();"
        val amazonScript = """
                           (function() {
                        var elements = document.getElementsByClassName("a-offscreen");
                          var regex = /(&nbsp;|<([^>]+)>)/ig;
                         var body = elements[0].textContent;
                         var result = body.replace(regex, '');
                         console.log(result);
                         return result;
                         })();
                """


        webView.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                webView.evaluateJavascript(amazonScript, ValueCallback<String?> { value ->
                    val jsonReader = JsonReader(StringReader(value))
                    jsonReader.isLenient = true
                    try {
                        if (jsonReader.peek() != JsonToken.NULL && jsonReader.peek() == JsonToken.STRING) {
                            val msg = jsonReader.nextString()
                            if (msg != null) {
                                Log.d("output", msg)
                                AndroidJSInterface.onClicked(msg)
                            }
                        }
                        jsonReader.close()
                    } catch (e: IOException) {
                        Log.e("Exception", "evaluateJavascript IOException : ", e)
                    }
                })
            }
        }
        webView.loadUrl("https://www.amazon.in");

    }


}