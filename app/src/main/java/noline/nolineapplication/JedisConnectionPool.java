package noline.nolineapplication;

import java.util.logging.Logger;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.exceptions.JedisConnectionException;

/**
 * Created by sangjin on 2016-10-12.
 */
public class JedisConnectionPool {
    private static Logger logger = Logger.getLogger(JedisConnectionPool.class.getName());

    static Jedis jedisPool;

    public JedisConnectionPool() {
        jedisPool = null;
    }
    public synchronized Jedis getJedisConnection() {

        System.out.println("-------------------------- getJedisConnection ------------------------------------------");
        try {
            if (jedisPool == null) {
                System.out.println("----- jedis 신규연결 -----");
                // ManagementFactory 안드로이드엔 없음
                // 안드로이드 시스템에서는 굳이 pool 을 만들 필요가 없어서 그냥 컨넥션만 맺는다.
                jedisPool = new Jedis("175.126.74.86");
            }else{
                System.out.println("----- jedis 이미연결되었음 -----");
            }
            return jedisPool;
        } catch (JedisConnectionException e) {
            System.out.println("JedisConnectionException ::::"+e);
            throw e;
        }
    }

    public synchronized void close(Jedis resource) {  // koushik: removed static, added code to increase robustness
        if (jedisPool != null) {
            try {
                if (resource != null) {
                    //jedisPool.returnResource(resource);
                    resource = null;
                }
            } catch (JedisConnectionException e) {
                //jedisPool.returnBrokenResource(resource);
                resource = null;
            } finally {
                if (resource != null)
                    //jedisPool.returnResource(resource);
                resource = null;
            }
        }
    }
}