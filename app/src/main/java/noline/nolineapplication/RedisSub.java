package noline.nolineapplication;

import android.os.Handler;
import android.os.Message;
import android.webkit.WebView;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;

/**
 * Created by sangjin on 2016-10-11.
 */
public class RedisSub extends Thread{
    boolean isRunning;
    private String channel;
    private Jedis jedis;
    private static WebView webView;
    private Handler handler;
    public RedisSub(String channel, WebView webView, Handler handler){
        this.channel = channel;
        this.webView = webView;
        this.handler = handler;
        if(jedis == null) {
            JedisConnectionPool jedisConnectionPool = new JedisConnectionPool();
            jedis = jedisConnectionPool.getJedisConnection();
        }
    }

    @Override
    public void run() {
//        while(true) {
            jedis.subscribe(new JedisPubSub() {
                @Override
                public void onMessage(String channel, String message) {
                    //super.onMessage(channel, message);
                    System.out.println("Received message:" + message);
                    //handler.sendEmptyMessage(0);
                    Message msg = new Message(); // 메세지를 생성
                    msg.what = 0;
                    msg.obj = message;
                    handler.sendMessage(msg);

                }
                @Override
                public void onSubscribe(String channel, int subscribedChannels) {
                    System.out.println("onSubscribe :" + channel);
                }
                @Override
                public void onUnsubscribe(String channel, int subscribedChannels) {
                    System.out.println("onUnsubscribe :" + channel);
                }
                @Override
                public void onPMessage(String pattern, String channel, String message) {
                    System.out.println("onPMessage :" + message);
                }
                @Override
                public void onPUnsubscribe(String pattern, int subscribedChannels) {
                    System.out.println("onPUnsubscribe :" + pattern);
                }
                @Override
                public void onPSubscribe(String pattern, int subscribedChannels) {
                    System.out.println("onPSubscribe :" + pattern);
                }

            }, this.channel);
//        }

    }
}
