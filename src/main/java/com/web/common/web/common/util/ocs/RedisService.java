package com.web.common.web.common.util.ocs;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.TimeoutException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPubSub;

public class RedisService {

    static final Logger logger = LoggerFactory.getLogger(RedisService.class);

    private static JedisPool jedisPool;

    public static final String NOT_FOUND = "nil";

    private String serverName;

    private boolean ALIVE = true;

    private volatile boolean EXCEPTION_FALG = false;


    public class MonitorThread extends Thread {

        public void run() {
            int sleepTime = 30000;
            int baseSleepTime = 1000;
            logger.info("redis 服务启动...");
            while (true) {
                try {
                    // 30秒执行监听
                    // 连续做3次连接获取
                    int errorTimes = 0;
                    for (int i = 0; i < 3; i++) {
                        try {
                            Jedis jedis = jedisPool.getResource();
                            if (jedis == null) {
                                errorTimes++;
                                continue;
                            }
                            releaseJedisInstance(jedis);
                            break;
                        } catch (Exception e) {
                            errorTimes++;
                            continue;
                        }
                    }
                    if (errorTimes == 3) {// 3次全部出错，表示服务器出现问题
                        ALIVE = false;
                        logger.error("redis[" + serverName + "] 服务器连接不上！ ！ ！");
                        // 修改休眠时间为5秒，尽快恢复服务
                        sleepTime = 5000;
                    } else {
                        if (ALIVE == false) {
                            ALIVE = true;
                            // 修改休眠时间为30秒，尽快恢复服务
                            sleepTime = 30000;
                            logger.info("redis[" + serverName + "] 服务器恢复正常！ ！ ！");
                        }
                        EXCEPTION_FALG = false;
                        Jedis jedis = jedisPool.getResource();
                        logger.info("redis[" + serverName + "] 当前记录数：" + jedis.dbSize());
                        releaseJedisInstance(jedis);
                    }
                } catch (Exception e) {
                }
            }
        }
    }

    public void setJedisPool(JedisPool jedisPool) {
        this.jedisPool = jedisPool;
        new MonitorThread().start();
    }

    public Jedis getJedis() throws TimeoutException {
        Jedis jedis = null;
        if (ALIVE) {// 当前状态为活跃才获取连接，否则直接返回null
            jedis = jedisPool.getResource();
        }
        return jedis;

    }

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    /**
     * 判断服务器是否活动
     *
     * @return
     */
    public boolean isServerAlive() {
        return ALIVE;
    }

    public void releaseJedisInstance(Jedis jedis) {
        if (jedis != null) {
            jedisPool.returnResource(jedis);
        }
    }

    /**
     * 得到指导key的值列表。此方法返回一个指定类型的数据列表。
     *
     * @param key   Key
     * @param clazz TypeReference， 指导返回的类型。
     * @return
     * @throws TimeoutException
     */
    public <T> T get(String key, TypeReference<T> clazz) throws TimeoutException {
        String json = null;
        Jedis jedis = null;
        try {
            jedis = getJedis();
            json = jedis.get(key);
        } catch (TimeoutException e) {
            throw e;
        } finally {
            if (jedis != null) {
                releaseJedisInstance(jedis);
            }
        }
        if (json == null) {
            return null;
        } else {
            return JSON.parseObject(json, clazz);
        }
    }

    public <T> T get(String key, Class<T> clazz) throws TimeoutException {
        String json = null;
        Jedis jedis = null;
        try {
            jedis = getJedis();
            json = jedis.get(key);
        } catch (TimeoutException e) {
            throw e;
        } finally {
            if (jedis != null) {
                releaseJedisInstance(jedis);
            }
        }
        if (json == null) {
            return null;
        } else {
            return JSON.parseObject(json, clazz);
        }
    }

    /**
     * Retrieve the values associated to the specified keys.
     * <p>
     * If some of the specified fields do not exist, nil values are returned. Non existing keys are
     * considered like empty hashes.
     * <p>
     * <b>Time complexity:</b> O(N) (with N being the number of fields)
     *
     * @param keys
     * @return Multi Bulk Reply specifically a list of all the values associated with the specified
     * keys, in the same order of the request. If fields is not exists, will return null.
     * @throws TimeoutException
     */
    public List<String> mget(final String... keys) throws TimeoutException {
        List<String> result = null;
        Jedis jedis = null;
        try {
            jedis = getJedis();
            result = jedis.mget(keys);
            if (result != null && !result.isEmpty()) {
                result.remove(NOT_FOUND);
            }
        } finally {
            releaseJedisInstance(jedis);
        }
        return result;
    }

    public void set(String key, Object o) throws TimeoutException {
        String s = JSON.toJSONString(o);
        Jedis jedis = null;
        try {
            jedis = getJedis();
            jedis.set(key, s);
        } catch (TimeoutException e) {
            throw e;
        } finally {
            releaseJedisInstance(jedis);
        }
    }

    public void setAndExpire(String key, Object o, int expire) throws TimeoutException {
        String s = JSON.toJSONString(o);
        Jedis jedis = null;
        try {
            jedis = getJedis();
            jedis.set(key, s);
            jedis.expire(key, expire);
        } catch (TimeoutException e) {
            throw e;
        } finally {
            releaseJedisInstance(jedis);
        }
    }

    /**
     * Return the number of items in a hash.
     * <p>
     * <b>Time complexity:</b> O(1)
     *
     * @param key
     * @return The number of entries (fields) contained in the hash stored at key. If the specified
     * key does not exist, 0 is returned assuming an empty hash.
     */
    public Long hlen(String key) throws TimeoutException {
        Long len = 0L;
        Jedis jedis = null;
        try {
            jedis = getJedis();
            len = jedis.hlen(key);
        } finally {
            releaseJedisInstance(jedis);
        }
        return len;
    }

    public List<String> blpop(int timeout, String... key) throws TimeoutException {
        Jedis jedis = null;
        List<String> result = null;
        try {
            jedis = getJedis();
            result = jedis.blpop(timeout, key);
        } catch (TimeoutException e) {
            throw e;
        } finally {
            if (jedis != null) {
                releaseJedisInstance(jedis);
            }
        }
        return result;
    }

    /**
     * Set the specified hash field to the specified value.
     * <p>
     * If key does not exist, a new key holding a hash is created.
     * <p>
     * <b>Time complexity:</b> O(1)
     *
     * @param key
     * @param field
     * @param value
     * @return If the field already exists, and the HSET just produced an update of the value, 0 is
     * returned, otherwise if a new field is created 1 is returned.
     */
    public Long hset(String key, String field, String value) throws TimeoutException {
        Long result = 0L;
        Jedis jedis = null;
        try {
            jedis = getJedis();
            result = jedis.hset(key, field, value);
        } finally {
            releaseJedisInstance(jedis);
        }
        return result;
    }

    /**
     * Set the specified hash field to the specified value.
     * <p>
     * If key does not exist, a new key holding a hash is created.
     * <p>
     * <b>Time complexity:</b> O(1)
     *
     * @param key
     * @param field
     * @param o
     * @return If the field already exists, and the HSET just produced an update of the value, 0 is
     * returned, otherwise if a new field is created 1 is returned.
     */
    public Long hset(String key, String field, Object o) throws TimeoutException {
        String s = JSON.toJSONString(o);
        Long result = 0L;
        Jedis jedis = null;
        try {
            jedis = getJedis();
            result = jedis.hset(key, field, s);
        } finally {
            releaseJedisInstance(jedis);
        }
        return result;
    }

    /**
     * Set the specified hash field to the specified value.
     * <p>
     * Sets field in the hash stored at key to value, only if field does not yet exist. If key does
     * not exist, a new key holding a hash is created. If field already exists, this operation has no
     * effect.
     * <p>
     * <b>Time complexity:</b> O(1)
     *
     * @param key
     * @param field
     * @param o
     * @return If the field already exists, and the HSET just produced an update of the value, 0 is
     * returned, otherwise if a new field is created 1 is returned.
     */
    public Long hsetnx(String key, String field, Object o) throws TimeoutException {
        String s = JSON.toJSONString(o);
        Long result = 0L;
        Jedis jedis = null;
        try {
            jedis = getJedis();
            result = jedis.hsetnx(key, field, s);
        } finally {
            releaseJedisInstance(jedis);
        }
        return result;
    }

    /**
     * Remove the specified keys. If a given key does not exist no operation is performed for this
     * key. The command returns the number of keys removed. Time complexity: O(1)
     *
     * @param keys
     * @return Integer reply, specifically: an integer greater than 0 if one or more keys were removed
     * 0 if none of the specified key existed
     */
    public Long del(String... keys) throws TimeoutException {
        Long result = 0L;
        Jedis jedis = null;
        try {
            jedis = getJedis();
            result = jedis.del(keys);
        } finally {
            releaseJedisInstance(jedis);
        }
        return result;
    }

    /**
     * If key holds a hash, retrieve the value associated to the specified field.
     * <p>
     * If the field is not found or the key does not exist, a special 'nil' value is returned.
     * <p>
     * <b>Time complexity:</b> O(1)
     *
     * @param key
     * @param field
     * @return if not find, null is returned.
     */
    public String hget(String key, String field) throws TimeoutException {
        String result = null;
        Jedis jedis = null;
        try {
            jedis = getJedis();
            String str = jedis.hget(key, field);
            if (!NOT_FOUND.equals(str)) {
                result = str;
            }
        } finally {
            releaseJedisInstance(jedis);
        }
        return result;
    }

    /**
     * If key holds a hash, retrieve the value associated to the specified field.
     * <p>
     * If the field is not found or the key does not exist, a special 'nil' value is returned.
     * <p>
     * <b>Time complexity:</b> O(1)
     *
     * @param key
     * @param field
     * @return if not find, null is returned.
     */

    public <T> T hget(String key, String field, Class<T> clazz) throws TimeoutException {
        String result = null;
        Jedis jedis = null;
        try {
            jedis = getJedis();
            String str = jedis.hget(key, field);
            if (!NOT_FOUND.equals(str)) {
                result = str;
            }
        } finally {
            releaseJedisInstance(jedis);
        }

        if (result == null) {
            return null;
        } else {
            return JSON.parseObject(result, clazz);
        }
    }

    /**
     * If key holds a hash, retrieve the value associated to the specified field.
     * <p>
     * If the field is not found or the key does not exist, a special 'nil' value is returned.
     * <p>
     * <b>Time complexity:</b> O(1)
     *
     * @param key
     * @param field
     * @return if not find, null is returned.
     */

    public <T> T hget(String key, String field, TypeReference<T> tr) throws TimeoutException {
        String result = null;
        Jedis jedis = null;
        try {
            jedis = getJedis();
            String str = jedis.hget(key, field);
            if (!NOT_FOUND.equals(str)) {
                result = str;
            }
        } finally {
            releaseJedisInstance(jedis);
        }

        if (result == null) {
            return null;
        } else {
            return JSON.parseObject(result, tr);
        }
    }

    /**
     * Set the respective fields to the respective values. HMSET replaces old values with new values.
     * <p>
     * If key does not exist, a new key holding a hash is created.
     * <p>
     * <b>Time complexity:</b> O(N) (with N being the number of fields)
     *
     * @param key
     * @param hash
     * @return Always OK because HMSET can't fail
     */
    public String hmset(String key, Map<String, String> hash) throws TimeoutException {
        String result = null;
        Jedis jedis = null;
        try {
            jedis = getJedis();
            result = jedis.hmset(key, hash);
        } finally {
            releaseJedisInstance(jedis);
        }
        return result;
    }

    /**
     * Retrieve the values associated to the specified fields.
     * <p>
     * If some of the specified fields do not exist, nil values are returned. Non existing keys are
     * considered like empty hashes.
     * <p>
     * <b>Time complexity:</b> O(N) (with N being the number of fields)
     *
     * @param key
     * @param fields
     * @return Multi Bulk Reply specifically a list of all the values associated with the specified
     * fields, in the same order of the request. If fields is not exists, will return null.
     * @throws TimeoutException
     */
    public List<String> hmget(final String key, final String... fields) throws TimeoutException {
        List<String> result = null;
        Jedis jedis = null;
        try {
            jedis = getJedis();
            result = jedis.hmget(key, fields);
            if (result != null && !result.isEmpty()) {
                result.remove(NOT_FOUND);
            }
        } finally {
            releaseJedisInstance(jedis);
        }
        return result;
    }

    public <T> List<T> hmget(final String key, final Class<T> clazz, final String... fields)
        throws TimeoutException {
        List<String> jsons = null;
        List<T> results = null;
        Jedis jedis = null;

        try {
            jedis = getJedis();

            if (fields.length > 0) {
                results = new ArrayList<T>();
            }

            jsons = jedis.hmget(key, fields);
            if (jsons != null && !jsons.isEmpty()) {
                jsons.remove(NOT_FOUND);
            }

            for (String json : jsons) {
                if (json != null) {
                    results.add(JSON.parseObject(json, clazz));
                }
            }
        } catch (TimeoutException e) {
            throw e;
        } finally {
            if (jedis != null) {
                releaseJedisInstance(jedis);
            }
        }

        return results;
    }

    /**
     * Return all the fields and associated values in a hash.
     * <p>
     * <b>Time complexity:</b> O(N), where N is the total number of entries
     *
     * @param key
     * @return All the fields and values contained into a hash.
     */
    public Map<String, String> hgetAll(String key) throws TimeoutException {
        Map<String, String> result = new HashMap<String, String>();
        Jedis jedis = null;
        try {
            jedis = getJedis();
            result = jedis.hgetAll(key);
        } finally {
            releaseJedisInstance(jedis);
        }
        return result;
    }

    public Boolean hexists(String key, String field) throws TimeoutException {
        Boolean result = false;
        Jedis jedis = null;
        try {
            jedis = getJedis();
            result = jedis.hexists(key, field);
        } finally {
            releaseJedisInstance(jedis);
        }
        return result;
    }

    /**
     * Add the string value to the head (LPUSH) or tail (RPUSH) of the list stored at key. If the key
     * does not exist an empty list is created just before the append operation. If the key exists but
     * is not a List an error is returned.
     * <p>
     * Time complexity: O(1)
     *
     * @param key
     * @param string
     * @return Integer reply, specifically, the number of elements inside the list after the push
     * operation.
     * @see Jedis#lpush(String, String)
     */
    public Long rpush(String key, String string) throws TimeoutException {
        Long result = 0L;
        Jedis jedis = null;
        try {
            jedis = getJedis();
            result = jedis.rpush(key, string);
        } finally {
            releaseJedisInstance(jedis);
        }
        return result;
    }

    /**
     * Add the string value to the head (LPUSH) or tail (RPUSH) of the list stored at key. If the key
     * does not exist an empty list is created just before the append operation. If the key exists but
     * is not a List an error is returned.
     * <p>
     * Time complexity: O(1)
     *
     * @param key
     * @param string
     * @return Integer reply, specifically, the number of elements inside the list after the push
     * operation.
     * @see Jedis#rpush(String, String)
     */
    public Long lpush(String key, String string) throws TimeoutException {
        Long result = 0L;
        Jedis jedis = null;
        try {
            jedis = getJedis();
            result = jedis.lpush(key, string);
        } finally {
            releaseJedisInstance(jedis);
        }
        return result;
    }

    /**
     * Add the Object to the head (LPUSH) or tail (RPUSH) of the list stored at key. If the key does
     * not exist an empty list is created just before the append operation. If the key exists but is
     * not a List an error is returned.
     * <p>
     * Time complexity: O(1)
     *
     * @param key
     * @param 0   Object
     * @return Integer reply, specifically, the number of elements inside the list after the push
     * operation.
     * @see Jedis#rpush(String, String)
     */
    public Long lpush(String key, Object o) throws TimeoutException {
        Long result = 0L;
        Jedis jedis = null;
        try {
            jedis = getJedis();
            String string = JSON.toJSONString(o);

            result = jedis.lpush(key, string);
        } finally {
            releaseJedisInstance(jedis);
        }
        return result;
    }

    /**
     * Add the Object list to the head (LPUSH) or tail (RPUSH) of the list stored at key. If the key
     * does not exist an empty list is created just before the append operation. If the key exists but
     * is not a List an error is returned.
     * <p>
     * Time complexity: O(1)
     *
     * @param key
     * @param oList list of object
     * @see Jedis#rpush(String, String)
     */
    public <T> void lpush(String key, List<T> oList) throws TimeoutException {
        Jedis jedis = null;
        try {
            jedis = getJedis();
            for (T t : oList) {
                String string = JSON.toJSONString(t);

                jedis.lpush(key, string);
            }
        } finally {
            releaseJedisInstance(jedis);
        }
    }

    /**
     * Return the length of the list stored at the specified key. If the key does not exist zero is
     * returned (the same behaviour as for empty lists). If the value stored at key is not a list an
     * error is returned.
     * <p>
     * Time complexity: O(1)
     *
     * @param key
     * @return The length of the list.
     */
    public Long llen(String key) throws TimeoutException {
        Long result = 0L;
        Jedis jedis = null;
        try {
            jedis = getJedis();
            result = jedis.llen(key);
        } finally {
            releaseJedisInstance(jedis);
        }
        return result;
    }

    /**
     * Return the specified elements of the list stored at the specified key. Start and end are
     * zero-based indexes. 0 is the first element of the list (the list head), 1 the next element and
     * so on.
     * <p>
     * For example LRANGE foobar 0 2 will return the first three elements of the list.
     * <p>
     * start and end can also be negative numbers indicating offsets from the end of the list. For
     * example -1 is the last element of the list, -2 the penultimate element and so on.
     * <p>
     * <b>Consistency with range functions in various programming languages</b>
     * <p>
     * Note that if you have a list of numbers from 0 to 100, LRANGE 0 10 will return 11 elements,
     * that is, rightmost item is included. This may or may not be consistent with behavior of
     * range-related functions in your programming language of choice (think Ruby's Range.new,
     * Array#slice or Python's range() function).
     * <p>
     * LRANGE behavior is consistent with one of Tcl.
     * <p>
     * <b>Out-of-range indexes</b>
     * <p>
     * Indexes out of range will not produce an error: if start is over the end of the list, or start
     * > end, an empty list is returned. If end is over the end of the list Redis will threat it just
     * like the last element of the list.
     * <p>
     * Time complexity: O(start+n) (with n being the length of the range and start being the start
     * offset)
     *
     * @param key
     * @param start
     * @param end
     * @return Multi bulk reply, specifically a list of elements in the specified range.
     */
    public List<String> lrange(String key, int start, int end) throws TimeoutException {
        List<String> result = new ArrayList<String>();
        Jedis jedis = null;
        try {
            jedis = getJedis();
            result = jedis.lrange(key, start, end);
        } finally {
            releaseJedisInstance(jedis);
        }
        return result;
    }

    public <T> List<T> lrange(String key, Class<T> clazz, int start, int end)
        throws TimeoutException {
        List<T> result = new ArrayList<T>();
        Jedis jedis = null;
        try {
            jedis = getJedis();
            List<String> strings = jedis.lrange(key, start, end);

            if (strings != null && !strings.isEmpty()) {
                for (final String string : strings) {
                    result.add(JSON.parseObject(string, clazz));
                }
            }
        } finally {
            releaseJedisInstance(jedis);
        }
        return result;
    }

    /**
     * Set a timeout on the specified key. After the timeout the key will be automatically deleted by
     * the server. A key with an associated timeout is said to be volatile in Redis terminology.
     * <p>
     * Voltile keys are stored on disk like the other keys, the timeout is persistent too like all the
     * other aspects of the dataset. Saving a dataset containing expires and stopping the server does
     * not stop the flow of time as Redis stores on disk the time when the key will no longer be
     * available as Unix time, and not the remaining seconds.
     * <p>
     * Since Redis 2.1.3 you can update the value of the timeout of a key already having an expire
     * set. It is also possible to undo the expire at all turning the key into a normal key using the
     * {@link #persist(String) PERSIST} command.
     * <p>
     * Time complexity: O(1)
     *
     * @param key
     * @param seconds
     * @return Integer reply, specifically: 1: the timeout was set. 0: the timeout was not set since
     * the key already has an associated timeout (this may happen only in Redis versions <
     * 2.1.3, Redis >= 2.1.3 will happily update the timeout), or the key does not exist.
     * @see <ahref="http://code.google.com/p/redis/wiki/ExpireCommand">ExpireCommand</a>
     */
    public Long expire(String key, int seconds) throws TimeoutException {
        Long result = 0L;
        Jedis jedis = null;
        try {
            jedis = getJedis();
            result = jedis.expire(key, seconds);
        } finally {
            releaseJedisInstance(jedis);
        }
        return result;
    }

    /**
     * Test if the specified key exists. The command returns "0" if the key exists, otherwise "1" is
     * returned. Note that even keys set with an empty string as value will return "1". Time
     * complexity: O(1)
     *
     * @param key
     * @return Boolean reply
     */
    public Boolean exists(String key) throws TimeoutException {
        Boolean result = Boolean.FALSE;
        Jedis jedis = null;
        try {
            jedis = getJedis();
            result = jedis.exists(key);
        } finally {
            releaseJedisInstance(jedis);
        }
        return result;
    }

    /**
     * Remove the specified field from an hash stored at key.
     * <p>
     * <b>Time complexity:</b> O(1)
     *
     * @param key
     * @param field
     * @return If the field was present in the hash it is deleted and 1 is returned, otherwise 0 is
     * returned and no operation is performed.
     */
    public Long hdel(String key, String field) throws TimeoutException {
        Long result = 0L;
        Jedis jedis = null;
        try {
            jedis = getJedis();
            result = jedis.hdel(key, field);
        } finally {
            releaseJedisInstance(jedis);
        }
        return result;
    }

    /**
     * Returns all the keys matching the glob-style pattern as space separated strings. For example if
     * you have in the database the keys "foo" and "foobar" the command "KEYS foo*" will return
     * "foo foobar".
     * <p>
     * Note that while the time complexity for this operation is O(n) the constant times are pretty
     * low. For example Redis running on an entry level laptop can scan a 1 million keys database in
     * 40 milliseconds. <b>Still it's better to consider this one of the slow commands that may ruin
     * the DB performance if not used with care.</b>
     * <p>
     * In other words this command is intended only for debugging and special operations like creating
     * a script to change the DB schema. Don't use it in your normal code. Use Redis Sets in order to
     * group together a subset of objects.
     * <p>
     * Glob style patterns examples:
     * <ul>
     * <li>h?llo will match hello hallo hhllo
     * <li>h*llo will match hllo heeeello
     * <li>h[ae]llo will match hello and hallo, but not hillo
     * </ul>
     * <p>
     * Use \ to escape special chars if you want to match them verbatim.
     * <p>
     * Time complexity: O(n) (with n being the number of keys in the DB, and assuming keys and pattern
     * of limited length)
     *
     * @param pattern
     * @return Multi bulk reply
     */
    public List<String> keys(String pattern) throws TimeoutException {
        List<String> result = new ArrayList<String>();
        Set<String> set = new HashSet<String>();
        Jedis jedis = null;
        try {
            jedis = getJedis();
            set = jedis.keys(pattern);
            if (set != null && !set.isEmpty()) {
                final Iterator<String> ite = set.iterator();
                while (ite.hasNext()) {
                    result.add((String) ite.next());
                }
            }
        } finally {
            releaseJedisInstance(jedis);
        }
        return result;
    }

    /**
     * Get the value of the specified key. If the key does not exist the special value 'nil' is
     * returned. If the value stored at key is not a string an error is returned because GET can only
     * handle string values.
     * <p>
     * Time complexity: O(1)
     *
     * @param key
     * @return if not find, null id returned.
     */
    public String get(String key) throws TimeoutException {
        String result = null;
        Jedis jedis = null;
        try {
            jedis = getJedis();
            String str = jedis.get(key);
            if (!NOT_FOUND.equals(str)) {
                result = str;
            }
        } finally {
            releaseJedisInstance(jedis);
        }
        return result;
    }

    /**
     * Get the value of the specified key. If the key does not exist the special value 'nil' is
     * returned. If the value stored at key is not a string an error is returned because GET can only
     * handle string values.
     * <p>
     * Time complexity: O(1)
     *
     * @param key
     * @return Bulk reply
     */
    public String set(String key, String value) throws TimeoutException {
        String result = "";
        Jedis jedis = null;
        try {
            jedis = getJedis();
            result = jedis.set(key, value);
        } finally {
            releaseJedisInstance(jedis);
        }
        return result;
    }

    public <T> void rpush(String key, List<T> oList) throws TimeoutException {
        Jedis jedis = null;
        try {
            jedis = getJedis();
            for (Object o : oList) {
                String s = JSON.toJSONString(o);
                jedis.rpush(key, s);
            }
        } catch (TimeoutException e) {
            throw e;
        } finally {
            releaseJedisInstance(jedis);
        }
    }

    public <T> T lrange(String key, int start, int end, TypeReference<T> clazz)
        throws TimeoutException {
        List<String> jsonList = null;
        Jedis jedis = null;
        try {
            jedis = getJedis();
            jsonList = jedis.lrange(key, start, end);
        } catch (TimeoutException e) {
            throw e;
        } finally {
            if (jedis != null) {
                releaseJedisInstance(jedis);
            }
        }
        if (jsonList == null || jsonList.isEmpty()) {
            return null;
        } else {
            return JSON.parseObject(jsonList.toString(), clazz);
        }

    }

    public <T> List<T> lpop(String key, Class<T> clazz, int size) throws TimeoutException {
        String json = null;
        Jedis jedis = null;
        int count = 0;
        List<T> results = null;

        try {
            jedis = getJedis();
            long exists = jedis.llen(key);
            count = (int) (exists > size ? size : exists);

            if (count > 0) {
                results = new ArrayList<T>();
            }

            for (int i = 0; i < count; i++) {
                json = jedis.lpop(key);

                if (json != null) {
                    results.add(JSON.parseObject(json, clazz));
                }
            }
        } catch (TimeoutException e) {
            throw e;
        } finally {
            if (jedis != null) {
                releaseJedisInstance(jedis);
            }
        }

        return results;
    }

    public String lpop(String key) throws TimeoutException {
        Jedis jedis = null;
        try {
            jedis = getJedis();
            return jedis.lpop(key);
        } catch (TimeoutException e) {
            throw e;
        } finally {
            if (jedis != null) {
                releaseJedisInstance(jedis);
            }
        }
    }

    public String rpop(String key) throws TimeoutException {
        Jedis jedis = null;
        try {
            jedis = getJedis();
            return jedis.rpop(key);
        } catch (TimeoutException e) {
            throw e;
        } finally {
            if (jedis != null) {
                releaseJedisInstance(jedis);
            }
        }
    }

    public Long lrem(String key, int count, String value) throws TimeoutException {
        Jedis jedis = null;
        try {
            jedis = getJedis();
            return jedis.lrem(key, count, value);
        } catch (TimeoutException e) {
            throw e;
        } finally {
            if (jedis != null) {
                releaseJedisInstance(jedis);
            }
        }
    }

    public String lindex(String key, int index) throws TimeoutException {
        Jedis jedis = null;
        try {
            jedis = getJedis();
            return jedis.lindex(key, index);
        } catch (TimeoutException e) {
            throw e;
        } finally {
            if (jedis != null) {
                releaseJedisInstance(jedis);
            }
        }
    }

    public String lset(String key, int index, String value) throws TimeoutException {
        Jedis jedis = null;
        try {
            jedis = getJedis();
            return jedis.lset(key, index, value);
        } catch (TimeoutException e) {
            throw e;
        } finally {
            if (jedis != null) {
                releaseJedisInstance(jedis);
            }
        }
    }

    public void sadd(String key, String member) throws TimeoutException {
        Jedis jedis = null;
        try {
            jedis = getJedis();
            jedis.sadd(key, member);
        } catch (TimeoutException e) {
            throw e;
        } finally {
            if (jedis != null) {
                releaseJedisInstance(jedis);
            }
        }
    }

    public void saddAll(String key, Set<String> members) throws TimeoutException {
        Jedis jedis = null;
        try {
            jedis = getJedis();
            for (final String member : members) {
                jedis.sadd(key, member);
            }
        } catch (TimeoutException e) {
            throw e;
        } finally {
            if (jedis != null) {
                releaseJedisInstance(jedis);
            }
        }
    }

    public Set<String> smembers(String key) throws TimeoutException {
        Jedis jedis = null;
        try {
            jedis = getJedis();
            return jedis.smembers(key);
        } catch (TimeoutException e) {
            throw e;
        } finally {
            if (jedis != null) {
                releaseJedisInstance(jedis);
            }
        }
    }

    public Long srem(String key, String member) throws TimeoutException {
        Jedis jedis = null;
        try {
            jedis = getJedis();
            return jedis.srem(key, member);
        } catch (TimeoutException e) {
            throw e;
        } finally {
            if (jedis != null) {
                releaseJedisInstance(jedis);
            }
        }
    }

    public Boolean sismember(String key, String member) throws TimeoutException {
        Jedis jedis = null;
        try {
            jedis = getJedis();
            return jedis.sismember(key, member);
        } catch (TimeoutException e) {
            throw e;
        } finally {
            if (jedis != null) {
                releaseJedisInstance(jedis);
            }
        }
    }

    public Long scard(String key) throws TimeoutException {
        Jedis jedis = null;
        try {
            jedis = getJedis();
            return jedis.scard(key);
        } catch (TimeoutException e) {
            throw e;
        } finally {
            if (jedis != null) {
                releaseJedisInstance(jedis);
            }
        }
    }

    public Long ttl(String key) throws TimeoutException {
        Jedis jedis = null;
        try {
            jedis = getJedis();
            return jedis.ttl(key);
        } catch (TimeoutException e) {
            throw e;
        } finally {
            if (jedis != null) {
                releaseJedisInstance(jedis);
            }
        }
    }

    public Long publish(String channel, String message) throws TimeoutException {
        Jedis jedis = null;
        try {
            jedis = getJedis();
            return jedis.publish(channel, message);
        } catch (TimeoutException e) {
            throw e;
        } finally {
            if (jedis != null) {
                releaseJedisInstance(jedis);
            }
        }
    }

    public void subscribe(JedisPubSub jedisPubSub, String... channels) throws TimeoutException {
        Jedis jedis = null;
        try {
            jedis = getJedis();
            jedis.subscribe(jedisPubSub, channels);
        } catch (TimeoutException e) {
            throw e;
        } finally {
            if (jedis != null) {
                releaseJedisInstance(jedis);
            }
        }
    }

    public Long incr(String key) throws TimeoutException {
        Jedis jedis = null;
        try {
            jedis = getJedis();
            return jedis.incr(key);
        } catch (TimeoutException e) {
            throw e;
        } finally {
            if (jedis != null) {
                releaseJedisInstance(jedis);
            }
        }
    }

    public Long incrBy(String key, long integer) throws TimeoutException {
        Jedis jedis = null;
        try {
            jedis = getJedis();
            return jedis.incrBy(key, integer);
        } catch (TimeoutException e) {
            throw e;
        } finally {
            if (jedis != null) {
                releaseJedisInstance(jedis);
            }
        }
    }

    public Long hincrBy(String key, String field, long value) throws TimeoutException {
        Jedis jedis = null;
        try {
            jedis = getJedis();
            return jedis.hincrBy(key, field, value);
        } catch (TimeoutException e) {
            throw e;
        } finally {
            if (jedis != null) {
                releaseJedisInstance(jedis);
            }
        }
    }

    public Long decr(String key) throws TimeoutException {
        Jedis jedis = null;
        try {
            jedis = getJedis();
            return jedis.decr(key);
        } catch (TimeoutException e) {
            throw e;
        } finally {
            if (jedis != null) {
                releaseJedisInstance(jedis);
            }
        }
    }

    public Long zcard(String key) throws TimeoutException {
        Jedis jedis = null;
        try {
            jedis = getJedis();
            return jedis.zcard(key);
        } catch (TimeoutException e) {
            throw e;
        } finally {
            if (jedis != null) {
                releaseJedisInstance(jedis);
            }
        }
    }

    public Set<String> zrange(String key, int start, int end) throws TimeoutException {
        Jedis jedis = null;
        try {
            jedis = getJedis();
            return jedis.zrange(key, start, end);
        } catch (TimeoutException e) {
            throw e;
        } finally {
            if (jedis != null) {
                releaseJedisInstance(jedis);
            }
        }
    }

    public <T> Set<T> zrange(String key, Class<T> clazz, int start, int end)
        throws TimeoutException {
        Set<T> result = new TreeSet<T>();
        Jedis jedis = null;
        Iterator<String> it = null;
        try {
            jedis = getJedis();
            Set<String> zset = jedis.zrange(key, start, end);

            if (zset != null && !zset.isEmpty()) {
                it = zset.iterator();
                while (it.hasNext()) {
                    result.add(JSON.parseObject(it.next(), clazz));
                }
            }
        } finally {
            releaseJedisInstance(jedis);
        }
        return result;
    }

    public Set<String> zrevrange(String key, int start, int end) throws TimeoutException {
        Jedis jedis = null;
        try {
            jedis = getJedis();
            return jedis.zrevrange(key, start, end);
        } catch (TimeoutException e) {
            throw e;
        } finally {
            if (jedis != null) {
                releaseJedisInstance(jedis);
            }
        }
    }

    public <T> Set<T> zrevrange(String key, Class<T> clazz, int start, int end)
        throws TimeoutException {
        Set<T> result = new TreeSet<T>();
        Jedis jedis = null;
        Iterator<String> it = null;
        String json = null;
        try {
            jedis = getJedis();
            Set<String> zset = jedis.zrevrange(key, start, end);

            if (zset != null && !zset.isEmpty()) {
                it = zset.iterator();
                while (it.hasNext()) {
                    json = it.next();
                    result.add(JSON.parseObject(json, clazz));
                }
            }
        } finally {
            releaseJedisInstance(jedis);
        }
        return result;
    }

    public Set<String> zrangeByScore(String key, Long min, Long max) throws TimeoutException {
        Jedis jedis = null;
        try {
            jedis = getJedis();
            return jedis.zrangeByScore(key, min, max);
        } catch (TimeoutException e) {
            throw e;
        } finally {
            if (jedis != null) {
                releaseJedisInstance(jedis);
            }
        }
    }

    public void zrem(String key, String member) throws TimeoutException {
        Jedis jedis = null;
        try {
            jedis = getJedis();
            jedis.zrem(key, member);
        } catch (TimeoutException e) {
            throw e;
        } finally {
            if (jedis != null) {
                releaseJedisInstance(jedis);
            }
        }
    }

    public void zadd(String key, Long score, String member) throws TimeoutException {
        Jedis jedis = null;
        try {
            jedis = getJedis();
            jedis.zadd(key, score, member);
        } catch (TimeoutException e) {
            throw e;
        } finally {
            if (jedis != null) {
                releaseJedisInstance(jedis);
            }
        }
    }

    public void zincrBy(String key, Long score, String member) throws TimeoutException {
        Jedis jedis = null;
        try {
            jedis = getJedis();
            jedis.zincrby(key, score, member);
        } catch (TimeoutException e) {
            throw e;
        } finally {
            if (jedis != null) {
                releaseJedisInstance(jedis);
            }
        }
    }

    public void zincrBy(String key, Long score, Object o) throws TimeoutException {
        Jedis jedis = null;
        String member = JSON.toJSONString(o);
        try {
            jedis = getJedis();
            jedis.zincrby(key, score, member);
        } catch (TimeoutException e) {
            throw e;
        } finally {
            if (jedis != null) {
                releaseJedisInstance(jedis);
            }
        }
    }

    public void zremrangeByRank(String key, int start, int end) throws TimeoutException {
        Jedis jedis = null;
        try {
            jedis = getJedis();
            jedis.zremrangeByRank(key, start, end);
        } catch (TimeoutException e) {
            throw e;
        } finally {
            if (jedis != null) {
                releaseJedisInstance(jedis);
            }
        }
    }

    public void zremrangeByScore(String key, Long start, Long end) throws TimeoutException {
        Jedis jedis = null;
        try {
            jedis = getJedis();
            jedis.zremrangeByScore(key, start, end);
        } catch (TimeoutException e) {
            throw e;
        } finally {
            if (jedis != null) {
                releaseJedisInstance(jedis);
            }
        }
    }

    public int zscore(String key, Object o) throws TimeoutException {
        Jedis jedis = null;
        String member = JSON.toJSONString(o);
        try {
            jedis = getJedis();
            Double score = jedis.zscore(key, member);
            if (score != null) {
                return score.intValue();
            } else {
                return 0;
            }
        } catch (TimeoutException e) {
            throw e;
        } finally {
            if (jedis != null) {
                releaseJedisInstance(jedis);
            }
        }
    }

    /**
     * map数据序列化转换
     *
     * @param data
     * @return
     */
    public static Map<byte[], byte[]> serializeMap(Map<Object, Object> data) {
        Map<byte[], byte[]> result = new HashMap<byte[], byte[]>();
        try {
            Set<Object> keys = data.keySet();
            if (keys != null && keys.size() > 0) {
                for (Object key : keys) {
                    result.put(serializable(key), serializable(data.get(key)));
                }
            }
        } catch (Exception e) {
        }
        return result;
    }

    /**
     * 序列化处理
     *
     * @param obj
     * @return
     */
    public static byte[] serializable(Object obj) {
        if (obj == null) {
            return null;
        }
        ObjectOutputStream oos = null;
        ByteArrayOutputStream baos = null;
        try {
            // 序列化
            baos = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(baos);
            oos.writeObject(obj);
            byte[] bytes = baos.toByteArray();
            return bytes;
        } catch (Exception e) {
        }
        return null;
    }

    /**
     * 反序列化处理
     *
     * @param bytes
     * @return
     */
    public static Object unserialize(byte[] bytes) {
        if (bytes == null) {
            return null;
        }
        ByteArrayInputStream bais = null;
        try {
            // 反序列化
            bais = new ByteArrayInputStream(bytes);
            ObjectInputStream ois = new ObjectInputStream(bais);
            return ois.readObject();
        } catch (Exception e) {
        }
        return null;
    }
}
