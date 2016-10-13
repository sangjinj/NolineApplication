package noline.nolineapplication;

/**
 * Created by sangjin on 2016-10-12.
 */
public class JedisThreadCreate implements Runnable {

    @Override
    public void run() {
        try{
            JedisConnectionPool jedisConnectionPool = new JedisConnectionPool();
            jedisConnectionPool.getJedisConnection();
        }catch (Exception e){

        }

    }
}
