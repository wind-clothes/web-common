package com.web.common.web.common.util.pay;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;

/**
 * 阿里支付配置
 * @Date 2016年4月1日,下午10:57:01
 * @version v1.0
 */
public class AliConfig {

    private Logger logger = LoggerFactory.getLogger(AliConfig.class);

    /** 商户id */
    private String partner = "2088022511879472";

    /** 付款后回调url */
    private String notify_url = "http://yeoner-appserver-dev.obaymax.com/pay/aliPayNotify";

    /** 付款查询url */
    private String query_url = "https://mapi.alipay.com/gateway.do?";

    /** 安全校验key  */
    private String key = "gqk2b4xngtl61jsggg23na4af8jdk28y";

    /** 安全校验url  */
    private String https_verify_url = "https://mapi.alipay.com/gateway.do?service=notify_verify&";

    /** 商户的私钥  */
    private String private_key = "MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBAL/IYBdOEIILmN01RCLaZyy9CbAe4HNo3oh77PioC3ZZz7QePVhNwNLqmCPpMoYjniAr9nzOGfRPiikrVJprdEEPht+x+ivJSLPIGKGBLE1BC/margsm/nWEyNvnJZx43ij+OwyjsbyXwEtS+gWWXxgOhtnTuTBJpfb1mgATOtytAgMBAAECgYB1gzUF8qqA1NRU7xeR95tyuV5fUXwcm2yH2s+ISYfEfCGd702Ndw8o7eBNWM3Bjb+NXQLK1IrovXgd49QSTrdLOFdmO79OR5K+THX9Oyigj5urKGZquIA/WqpvH52yKHwVNEKumljCaDVzHrkrLQJpRCeH4LChsP5NScNNqouTQQJBAP1puVWqjXWAaTuihplKSjoMbufhPoXvwOcNFviwBEIzJAIw5M1bbPVU2nnq0Ge6aESpbOghpBZqPcNlwpfAWX0CQQDBvZXTfoGaNoc0jhBxieYO1ONn4+1Oy4BeDH2WumHNTQW8DgNqtMIInIuQdzdV94CUBe6PeGWnLNEiPMLCM3bxAkB6hFPjc/rCVVwLRe93SzCJ0hsIpqC41opX6r8xasAUVW9SU9s64bnS/rxF913c2IWytIr/y8vuPl0wDBHBlnBtAkA/UaHlBGSBLS8VKxUOtn38/PDxpFmLthDXLAfjujh6q6CjLIVLW5ZK0DcA2SW5r/FS73lfZH78mzYilW8ln0JRAkAxTx1gh2YV8WcaXzTwaFO7ebJGTWDnG4ITwoDYLoPudqxBZRlxfteFqIISk+IhDo0Abm0j3TM3cMBbEBrWsc9e";

    /** 支付宝的公钥，无需修改该值  */
    private String public_key = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCnxj/9qwVfgoUh/y2W89L6BkRAFljhNhgPdyPuBV64bfQNN1PjbCzkIM6qRdKBoLPXmKKMiFYnkd6rAoprih3/PrQEB/VsW8OoM8fxn67UDYuyBTqA23MML9q1+ilIZwBC2AQ2UBVOrFXfFl75p6/B5KsiNG9zpgmLCUYuLkxpLQIDAQAB";

    //private String pid = partner;
    //private String seller_id = partner;
    /** 输入编码 */
    private String input_charset = "utf-8";
    
    /** 签名算法  */
    private String sign_type = "RSA";
    
    /** 编码  */
    private String charset = "utf-8";

    public String getPartner() {
        return partner;
    }

    public void setPartner(String partner) {
        this.partner = partner;
    }

    public String getInput_charset() {
        return input_charset;
    }

    public void setInput_charset(String input_charset) {
        this.input_charset = input_charset;
    }

    public String getSign_type() {
        return sign_type;
    }

    public void setSign_type(String sign_type) {
        this.sign_type = sign_type;
    }

    public String getNotify_url() {
        return notify_url;
    }

    public void setNotify_url(String notify_url) {
        this.notify_url = notify_url;
    }

    public String getQuery_url() {
        return query_url;
    }

    public void setQuery_url(String query_url) {
        this.query_url = query_url;
    }

    public String getCharset() {
        return charset;
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getHttps_verify_url() {
        return https_verify_url;
    }

    public void setHttps_verify_url(String https_verify_url) {
        this.https_verify_url = https_verify_url;
    }

    public String getPrivate_key() {
        return private_key;
    }

    public void setPrivate_key(String private_key) {
        this.private_key = private_key;
    }

    @PostConstruct
    public void init() {
        logger.debug(JSON.toJSONString(this));
    }

    public String getPublic_key() {
        return public_key;
    }

    public void setPublic_key(String public_key) {
        this.public_key = public_key;
    }
}
