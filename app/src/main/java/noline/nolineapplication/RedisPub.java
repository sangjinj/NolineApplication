package noline.nolineapplication;

import redis.clients.jedis.Jedis;

/**
 * Created by sangjin on 2016-10-11.
 */
public class RedisPub implements Runnable {
    Jedis jedis = new Jedis("175.126.74.86");
    boolean isRunning;
    private String channel, msg;
    public RedisPub(String channel, String msg){
        this.channel = channel;
        this.msg = msg;
    }

    @Override
    public void run() {
        jedis.publish(channel,msg);

    }


//    public String pubSendMsg(String channel, String msg) {
//
//        HashMap<String, String> pMap = new HashMap<String, String>();
//        pMap.put("CHANNEL",channel);
//        pMap.put("MSG",msg);
//
//        Thread thread = new Thread(new Runnable() {
//            @Override
//            public void run() {
//                System.out.println("pubSendMsg :::::::::::::::::: ");
//                jedis.publish("A","abcdefghijklmn");
//
//            }
//        });
//        isRunning = true;
//        thread.start();
//        return "";
//    }

}
