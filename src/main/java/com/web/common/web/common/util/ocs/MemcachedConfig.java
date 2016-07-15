package com.web.common.web.common.util.ocs;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;

public class MemcachedConfig {

    private static Logger logger = LoggerFactory.getLogger(MemcachedConfig.class);

    private String servers;
    private String username;
    private String password;
    private boolean needAuth;

    public String getServers() {
        return servers;
    }

    public void setServers(String servers) {
        this.servers = servers;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isNeedAuth() {
        return needAuth;
    }

    public void setNeedAuth(boolean needAuth) {
        this.needAuth = needAuth;
    }

    @PostConstruct public void init() {
        logger.debug(JSON.toJSONString(this));
    }
}
