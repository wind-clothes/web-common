package com.web.common.web.common.util.ocs;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * <pre>
 * </pre>
 *
 * @author: xiongchengwei
 * @date: 2016年3月16日 下午6:10:21
 */
public class JedisConfig {

    public static final String QUEUE_KEY_PREFIX = "queue:";

    public static final int TIMEOUT = 30;

    /**
     * 获取jedispoolconfig对象
     *
     * @return
     */
    public static JedisPoolConfig getJedisPoolConfig() {
        JedisPoolConfig config = new JedisPoolConfig();

        // config.setMaxActive(Integer.parseInt(maxActive.trim())); //链接池中最大连接数,默认为8.
        config.setMaxIdle(8);// 链接池中最大空闲的连接数,默认为8.
        config.setMinIdle(0); // 连接池中最少空闲的连接数,默认为0.
        // config.setMaxWait(15000); //当连接池资源耗尽时，调用者最大阻塞的时间，超时将跑出异常
        config.setMinEvictableIdleTimeMillis(300000); // 空闲连接多长时间后会被收回
        config.setSoftMinEvictableIdleTimeMillis(
            -1);// 连接空闲的最小时间，达到此值后空闲链接将会被移除，且保留“minIdle”个空闲连接数。默认为-1.
        config.setNumTestsPerEvictionRun(3); // 对于“空闲链接”检测线程而言，每次检测的链接资源的个数。默认为3.
        config.setTestOnBorrow(
            false); // 向调用者输出“链接”资源时，是否检测是有有效，如果无效则从连接池中移除，并尝试获取继续获取。默认为false。建议保持默认值.
        config.setTestOnReturn(false); // 向连接池“归还”链接时，是否检测“链接”对象的有效性。默认为false。建议保持默认值
        config
            .setTestWhileIdle(false);// 向调用者输出“链接”对象时，是否检测它的空闲超时；默认为false。如果“链接”空闲超时，将会被移除。建议保持默认值.
        config.setTimeBetweenEvictionRunsMillis(60000);// 一分钟,多长时间检查一次连接池中空闲的连接
        // 当“连接池”中active数量达到阀值时，即“链接”资源耗尽时，连接池需要采取的手段, 默认为1：
        // 0 : 抛出异常，1 : 阻塞，直到有可用链接资源2 : 强制创建新的链接资源
        // config.setWhenExhaustedAction((byte)2);
        return config;
    }

    public static JedisPool getJedisPool() {
        JedisPoolConfig config = getJedisPoolConfig();
        String host = null;
        String port = null;
        String expire = null;
        JedisPool pool = new JedisPool(config, host.trim(), Integer.parseInt(port.trim()),
            Integer.parseInt(expire.trim()));
        return pool;
    }

    public static void startReids() {
        JedisPool jedisPool = getJedisPool();
        RedisService redisService = new RedisService();
        redisService.setJedisPool(jedisPool);
        redisService.setServerName("RedisQueue");
    }
}
