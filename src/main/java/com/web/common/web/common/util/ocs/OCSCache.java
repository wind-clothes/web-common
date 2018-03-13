package com.web.common.web.common.util.ocs;

/**
 * 使用OCS的Cache，由其父类集成基本的Cache能力，自己实现具体的业务逻辑相关的缓存.
 * <p>
 * <pre>
 * 目前系统支持的是缓存用户时是通过ID标识的
 * 缓存验证码是通过手机号
 * </pre>
 *
 * @ClassName: OCSCache
 * @author: chengweixiong@uworks.cc
 * @date:2015年10月21日 下午5:42:46
 */
public class OCSCache extends AbstractCache {


    /**
     * <pre>
     * 将用户缓存到OCS
     *
     * <pre>
     *
     * @param key
     * @param user
     */

    /**
     * @param key
     * @return
     */

    /**
     * 将验证码放入缓存服务器
     *
     * @Title: setCacheVerificationcode @return String
     */

    /**
     * 获得验证码并重置过期时间
     *
     * @Title: getCacheVerificationcode @param bzPrefix @return String
     */

    /**
     * 删除缓存中得用户信息
     *
     * @param request
     */

    /**
     * 删除缓存中的验证码
     *
     * @param request
     */

}
