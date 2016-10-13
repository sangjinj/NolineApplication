package noline.nolineapplication;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.Toast;

import redis.clients.jedis.Jedis;

/**
 * Created by sangjin on 2016-10-11.
 */
public class NolineJavascriptIf {
    private static Context mContext;
    private static WebView webView;
    private Jedis jedis;

    public  NolineJavascriptIf(Context mContext, WebView webView){

        this.mContext = mContext;
        this.webView = webView;
        if(jedis == null) {
            JedisConnectionPool jedisConnectionPool = new JedisConnectionPool();
            jedis = jedisConnectionPool.getJedisConnection();;
        }
    }
    public NolineJavascriptIf(){
        if(jedis == null) {
            JedisConnectionPool jedisConnectionPool = new JedisConnectionPool();
            jedis = jedisConnectionPool.getJedisConnection();;
        }
    }

    @JavascriptInterface
    public String sendMsg(String inMsg){
        Toast.makeText(mContext,inMsg,Toast.LENGTH_SHORT).show();
        System.out.println("-------------------------------------- sendMsg 5 --------------------------------------");
        String fromId,toId,toMsg;
        String[] arrMsg;

        arrMsg = inMsg.split(":");
        fromId = arrMsg[0];
        toId = arrMsg[1];
        toMsg = arrMsg[2];
        jedis.publish(toId,inMsg);
        return inMsg;
    }
    @JavascriptInterface
    public String subChannelCreate(String channel){
        //Toast.makeText(mContext,"received :: "+receivedMsg,Toast.LENGTH_SHORT).show();
        System.out.println("-------------------------------------- subChannelCreate 5 --------------------------------------");
        try {
            Runnable subChannelCreate = new RedisSub(channel,webView, handler);
            Thread subThread = new Thread(subChannelCreate);
            subThread.start();
        }catch (Exception e){
            return e.toString();
        }
        return "SUCCESS";
    }

    @JavascriptInterface
    public String receiveMsg(String receivedMsg){
        System.out.println("-------------------------------------- receivedMsg 5 --------------------------------------"+webView);
        System.out.println("-------------------------------------- "+ receivedMsg+" -------------------------------------");
        Toast.makeText(mContext,receivedMsg,Toast.LENGTH_SHORT).show();
        webView.loadUrl("javascript:receiveMsg('"+receivedMsg+"')");

        Intent intent = new Intent(this.mContext , MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this.mContext , 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        Notification notif = new Notification.Builder(this.mContext)
                .setAutoCancel(true)
                .setWhen(System.currentTimeMillis())
                .setContentTitle("주문 알림")
                .setContentText(" ["+receivedMsg+"] ")
                .setSmallIcon(R.drawable.tour_logo)
                .setContentIntent(contentIntent)
                .setDefaults(Notification.DEFAULT_LIGHTS| Notification.DEFAULT_SOUND)
                //.setLargeIcon(R.drawable.jellybean)
                .setStyle(new Notification.BigTextStyle()
                        .bigText(receivedMsg))
                .build();
        NotificationManager notificationManager = (NotificationManager) this.mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1, notif);
        return receivedMsg;
    }

    Handler handler = new Handler() { // 메인에서 생성한 핸들러
        @Override
        public void handleMessage(Message msg) {
            if(msg.what == 0){
                // 메세지를 통해 받은 값을 BackValue 로 출력
                receiveMsg(msg.obj.toString());
            }
        } // end handleMessage
    };
}
