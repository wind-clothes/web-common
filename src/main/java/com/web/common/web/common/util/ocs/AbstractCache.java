package com.web.common.web.common.util.ocs;

import java.io.IOException;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import net.spy.memcached.AddrUtil;
import net.spy.memcached.ConnectionFactoryBuilder;
import net.spy.memcached.MemcachedClient;
import net.spy.memcached.ConnectionFactoryBuilder.Protocol;
import net.spy.memcached.auth.AuthDescriptor;
import net.spy.memcached.auth.PlainCallbackHandler;

/**
 * 提供最基本的Cache逻辑.
 */
public class AbstractCache {

    private static Logger logger = LoggerFactory.getLogger(AbstractCache.class);

    /**
     * 需要注入
     */
    @Resource private MemcachedClient memcachedClient;
    @Resource private MemcachedConfig memcachedConfig;

    public MemcachedClient memcachedClient() throws IOException {
        MemcachedClient memcachedClient = null;
        if (memcachedConfig.isNeedAuth()) {
            AuthDescriptor ad = new AuthDescriptor(new String[] {"PLAIN"},
                new PlainCallbackHandler(memcachedConfig.getUsername(),
                    memcachedConfig.getPassword()));
            memcachedClient = new MemcachedClient(
                new ConnectionFactoryBuilder().setProtocol(Protocol.BINARY).setAuthDescriptor(ad)
                    .build(), AddrUtil.getAddresses(memcachedConfig.getServers()));
        } else {
            memcachedClient = new MemcachedClient(
                new ConnectionFactoryBuilder().setProtocol(Protocol.BINARY).build(),
                AddrUtil.getAddresses(memcachedConfig.getServers()));
        }
        return memcachedClient;
    }

    /**
     * <pre>
     * 设置缓存
     * 若value==null，则清除key的缓存
     * </pre>
     *
     * @param key
     * @param value
     * @param exp   过期秒数
     */
    public void set(String key, Object value, int exp) {
        try {
            if (value == null) {
                delete(key);
            } else {
                memcachedClient.set(key, exp, value);
            }
        } catch (Exception e) {
            logger.info("缓存数据异常：" + key, e);
        }
    }

    /**
     * 获得缓存并重置过期时间.
     *
     * @param key
     * @param exp 单位是秒
     * @return
     */
    public Object getAndTouch(String key, int exp) {
        try {
            Object value = memcachedClient.get(key);
            if (value != null) {
                memcachedClient.set(key, exp, value);
                return value;
            }
        } catch (Exception e) {
            logger.info("无法从缓存中取出数据, key = " + key, e);
        }
        return null;
    }

    /**
     * 获得缓存，但不会重置过期时间(适用于权限、等经常变动的数据).
     *
     * @param key
     * @return
     */
    public Object get(String key) {
        try {
            Object value = memcachedClient.get(key);
            return value;
        } catch (Exception e) {
            logger.info("获取缓存数据异常:" + key, e);
        }
        return null;
    }

    /**
     * 删除某个缓存.
     *
     * @param key
     */
    public void delete(String key) {
        try {
            memcachedClient.delete(key);
        } catch (Exception e) {
            logger.info("删除缓存数据异常：" + key, e);
        }
    }

}
