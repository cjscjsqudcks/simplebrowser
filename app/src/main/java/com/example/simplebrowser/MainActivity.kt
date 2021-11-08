package com.example.simplebrowser

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import java.util.*

class MainActivity : AppCompatActivity() {

    private val addressEt: EditText by lazy{
        findViewById(R.id.addressEt)
    }

    private val web : WebView by lazy{
        findViewById(R.id.web)
    }

    private val backBtn: ImageButton by lazy{
        findViewById(R.id.goBackBtn)
    }

    private val forwardBtn:ImageButton by lazy{
        findViewById(R.id.goForwardBtn)
    }
    private val homeBtn:ImageButton by lazy{
        findViewById(R.id.goHomeBtn)
    }

    private val refresher : SwipeRefreshLayout by lazy{
        findViewById(R.id.refresher)
    }
    private var homeAdd ="http://www.google.com"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initWeb()
        bindView()
        backBtn.setOnClickListener {
            if (web.canGoBack()){
                web.goBack()
            }
            else{
                Toast.makeText(this,"처음 페이지 입니다.",Toast.LENGTH_SHORT).show()
            }
        }
        forwardBtn.setOnClickListener {
            if (web.canGoForward()){
                web.goForward()
            }
            else{
                Toast.makeText(this,"가장 최근 페이지 입니다.",Toast.LENGTH_SHORT).show()
            }
        }
        homeBtn.setOnClickListener{
            web.loadUrl(homeAdd)
        }

        refresher.setOnRefreshListener {
            web.reload()
        }

    }

    inner class WebViewClient: android.webkit.WebViewClient(){
        override fun onPageFinished(view: WebView?, url: String?) {
            super.onPageFinished(view, url)
            refresher.isRefreshing=false
        }//페이지 다운로드가 완료 되었을때
    }//inner는 상위 클래스 접근을 위해 씀
    private fun initWeb(){
        web.apply {
            webViewClient= WebViewClient()
            settings.javaScriptEnabled=true
            loadUrl(homeAdd)
            canGoBackOrForward(99)
        }
        addressEt.setText(homeAdd)

    }

    private fun bindView(){
        addressEt.setOnEditorActionListener { v, actionId, event ->
            if (actionId==EditorInfo.IME_ACTION_DONE){
                var texts=v.text.toString()
                if(!texts.contains("https://")){

                    if (!texts.contains("www.")){
                        texts="https://www.$texts"
                    }
                    else
                        texts= "https://$texts"
                    v.setText(texts)

                }
                web.loadUrl(texts)

            }
            return@setOnEditorActionListener false
        }
        addressEt.setOnKeyListener { v, keyCode, event ->
            if (keyCode==66||keyCode==13){
                var texts=addressEt.text.toString()
                if(!texts.contains("https://")){
                    if (!texts.contains("www."))
                        texts="https://www.$texts"
                    else
                        texts= "https://$texts"
                    addressEt.setText(texts)
                }
                web.loadUrl(texts)
            }

            return@setOnKeyListener false
        }

    }

    override fun onBackPressed() {

        if (web.canGoBack()){
            web.goBack()
        }
        else{
            super.onBackPressed()
        }

    }
}