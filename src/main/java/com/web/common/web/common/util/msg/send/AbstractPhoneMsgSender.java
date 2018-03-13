package com.web.common.web.common.util.msg.send;

import java.io.IOException;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.web.common.web.common.util.HttpUtils;
import com.web.common.web.common.util.msg.data.FutureContext;
import com.web.common.web.common.util.msg.data.PhoneMsg;
import com.web.common.web.common.util.msg.enums.HttpMethod;
import com.web.common.web.common.util.msg.enums.PhoneMsgStatus;
import com.web.common.web.common.util.msg.send.result.PhoneMsgSendResult;

/**
 * <pre>
 * </pre>
 *
 * @author: xiongchengwei
 * @date: 2016年2月19日 下午2:36:34
 */
public abstract class AbstractPhoneMsgSender implements PhoneMsgSender {

    protected static Logger logger = LoggerFactory.getLogger(AbstractPhoneMsgSender.class);

    protected PhoneMsg phoneMsg;

    public PhoneMsg getPhoneMsg() {
        return phoneMsg;
    }

    public void setPhoneMsg(PhoneMsg phoneMsg) {
        this.phoneMsg = phoneMsg;
    }

    protected void validate(PhoneMsg phoneMsg) {
        if (StringUtils.isBlank(phoneMsg.getPhoneNo())) {
            throw new IllegalArgumentException("手机号不能为空！");
        }
        if (StringUtils.isBlank(phoneMsg.getContent())) {
            throw new IllegalArgumentException("短信内容不能为空！");
        }
    }

    protected PhoneMsgSendResult directBaseSend(PhoneMsg phoneMsg, String serviceUri) {
        validate(phoneMsg);
        return send(phoneMsg);
    }

    public abstract PhoneMsgSendResult send(PhoneMsg phoneMsg);

    protected String executeRequest(Map<String, String> params, String uri, String charset,
        String method) throws Exception {
        String resp = null;
        CloseableHttpClient httpClient = null;
        try {
            httpClient = HttpClients.createDefault();
            if (HttpMethod.GET.toString().equals(method)) {
                resp = HttpUtils.doGet(httpClient, RequestConfig.DEFAULT, params, uri, charset);
            } else {
                resp =
                    HttpUtils.doPost(httpClient, RequestConfig.DEFAULT, params, null, uri, charset);
            }
        } catch (Exception e) {
            throw new RuntimeException("Execute http request[" + uri + "] occurs error!", e);
        } finally {
            try {
                if (httpClient != null) {
                    httpClient.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return resp;
    }

    @Override public FutureContext call() throws Exception {
        if (logger.isDebugEnabled()) {
            logger.info("thread is call able");
        }
        FutureContext fc = new FutureContext();
        Integer sendResult = null;
        PhoneMsgSendResult result = send(phoneMsg);
        if (result.isSuccess()) {
            sendResult = PhoneMsgStatus.OK.getCode();
            fc.setExt(result.getMsg());
        } else {
            sendResult = PhoneMsgStatus.ERROR.getCode();
            fc.setErrorMsg(result.getMsg());
        }
        fc.setStatus(sendResult);
        return fc;
    }
}
