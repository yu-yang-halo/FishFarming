package cn.fuck.fishfarming.web;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.kaopiz.kprogresshud.KProgressHUD;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.fuck.fishfarming.R;
import cn.fuck.fishfarming.activity.StatusBarActivity;

/**
 * Created by Administrator on 2016/12/6.
 */

public class WebActivity extends StatusBarActivity {
    public static String KEY_WEB_URL="web_url_key";
    public static String KEY_WEB_TITLE="web_title_key";
    @BindView(R.id.webView)
    WebView webView;
    private String webUrl;
    private String title;
    private KProgressHUD kProgressHUD;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_web);

        ButterKnife.bind(this);


        initCustomActionBar();
        webUrl=getIntent().getStringExtra(KEY_WEB_URL);
        title=getIntent().getStringExtra(KEY_WEB_TITLE);
        if(title!=null){
            tvTitle.setText(title);
        }





        kProgressHUD=KProgressHUD.create(this).setLabel("加载中...");



        webView.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                Log.v("onProgressChanged","onProgressChanged "+newProgress);
            }
        });

        webView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                Log.v("onPageStarted","onPageStarted "+url);
                kProgressHUD.show();
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                Log.v("onPageFinished","onPageFinished "+url);
                kProgressHUD.dismiss();
            }
        });

        if(webUrl!=null){

            webView.loadUrl(webUrl);

        }




    }
}
