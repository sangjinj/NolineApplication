package noline.nolineapplication;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import redis.clients.jedis.Jedis;

public class MainActivity extends Activity {
    WebView webView;
    ComponentName dService;    // 시작 서비스의 이름

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Jedis jedis = new Jedis("175.126.74.86");
        webView = (WebView) findViewById(R.id.webView);
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        // 자바스크립트 쪽에 오브젝트를 추가한다
        Context mContext = this.getApplicationContext();
        webView.addJavascriptInterface(new NolineJavascriptIf(this,webView), "nJsCall");
        webView.setWebViewClient(new NolineWebviewClient());
        webView.setWebChromeClient(new NolineWebChromeClient(){});
        webView.loadUrl("http://175.126.74.86:8888/nolineMain.jsp");

        // 알람설정 서비스 위한 데몬서비스 시작
        dService = startService(new Intent(mContext, DaemonService.class));
    }

    // 웹 뷰 내부의 URL 인식
    private class NolineWebviewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            System.out.println("WebViewClient start ::::::::::::::::::::::::::::::::::::: ");
            view.loadUrl(url);
            return true;
        }
    }
    // 웹 뷰 내부의 URL 인식
    private class NolineWebChromeClient extends WebChromeClient {

        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            System.out.println("WebChromeClient start ::::::::::::::::::::::::::::::::::::: ");
            view.loadUrl(url);
            return true;
        }
    }

    Handler handler = new Handler() { // 메인에서 생성한 핸들러
        @Override
        public void handleMessage(Message msg) {
            if(msg.what == 0){
                // 메세지를 통해 받은 값을 BackValue 로 출력
                System.out.println("Handler Msg :::::::::::::::: " + msg);
                Toast.makeText(getApplicationContext(),"handler Msg"+msg.toString(),Toast.LENGTH_SHORT).show();
            }
        } // end handleMessage
    };

    public  class abc{

        public String abcd(){
            webView.loadUrl("javascript:console.log('abcdefg')");
            System.out.println("--------------------------");
            return "";
        }


    }
}
