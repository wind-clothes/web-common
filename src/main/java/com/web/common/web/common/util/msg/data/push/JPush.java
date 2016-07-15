package com.web.common.web.common.util.msg.data.push;

import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;

import cn.jpush.api.JPushClient;
import cn.jpush.api.common.DeviceType;
import cn.jpush.api.common.resp.APIConnectionException;
import cn.jpush.api.common.resp.APIRequestException;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.Message;
import cn.jpush.api.push.model.Options;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.PushPayload.Builder;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.notification.AndroidNotification;
import cn.jpush.api.push.model.notification.IosNotification;
import cn.jpush.api.push.model.notification.Notification;
import cn.jpush.api.push.model.notification.WinphoneNotification;
import cn.jpush.api.utils.StringUtils;

/**
 * @author: chengweixiong@uworks.cc
 * @date: 2015年12月13日 上午11:35:12
 */
public class JPush {
    private static Logger logger = LoggerFactory.getLogger(JPushClient.class);

    @Resource JPushClient jPushClient;

    public void push(AbstractAppPushModel pushModel) throws RuntimeException {
        PushResult pushResult = null;
        try {
            PushPayload pushPayload = buildPushPayload(pushModel);
            pushResult = jPushClient.sendPush(pushPayload);
        } catch (APIConnectionException e) {
            logger.error("Send push failed, connection error", e);
        } catch (APIRequestException e) {
            logger.error("Send push failed, illegal request", e);
            logger.error("HTTP Status: " + e.getStatus());
            logger.error("Error Code: " + e.getErrorCode());
            logger.error("Error Message: " + e.getErrorMessage());
        }
        if (pushResult != null) {
            logger.info("Send push success, message id = {}, send number = {}", pushResult.msg_id,
                pushResult.sendno);
        }
    }

    private PushPayload buildPushPayload(AbstractAppPushModel pushModel) {
        Builder builder = PushPayload.newBuilder();
        builder.setPlatform(buildPlatform(pushModel));
        builder.setAudience(buildAudience(pushModel));
        builder.setOptions(buildeOptions(pushModel));
        builder.setMessage(buildMessage(pushModel));
        builder.setNotification(buildNotification(pushModel));
        return builder.build();
    }

    private Platform buildPlatform(AbstractAppPushModel pushModel) {
        Platform platform = pushModel.getPlatform();
        if (platform == null) {
            return Platform.all();
        }
        return platform;
    }

    private Audience buildAudience(AbstractAppPushModel pushModel) {
        Audience audience = pushModel.getAudienceType();
        if (audience == null) {
            return Audience.all();
        }
        return audience;
    }

    /**
     * True 表示推送生产环境，False 表示要推送开发环境；
     * 如果不指定则为推送生产环境。JPush 官方 API LIbrary (SDK) 默认设置为推送 “开发环境”。
     *
     * @param pushModel
     * @return Options
     */
    private Options buildeOptions(AbstractAppPushModel pushModel) {
        Options options = pushModel.getOptions();
        // 如果是生产环境的话，需要配置以下
        if (options == null) {
            return Options.newBuilder().setApnsProduction(true).build();
        } else {
            options.setApnsProduction(true);
        }
        return options;
    }

    private Message buildMessage(AbstractAppPushModel pushModel) {
        if (!(pushModel instanceof AppPushMessage)) {
            return null;
        }
        AppPushMessage pushMessage = (AppPushMessage) pushModel;
        String message = pushMessage.getMessage();
        Message.Builder builder = Message.newBuilder();
        if (!StringUtils.isEmpty(message)) {
            builder.setMsgContent(message);
        }
        Map<String, String> extras = pushMessage.getExtra();
        if (extras != null && !extras.isEmpty()) {
            builder.addExtras(extras);
        }
        return builder.build();
    }

    private Notification buildNotification(AbstractAppPushModel pushModel) {
        if (!(pushModel instanceof AppPushNotification)) {
            return null;
        }
        AppPushNotification pushNotification = (AppPushNotification) pushModel;
        Platform platform = pushNotification.getPlatform();
        boolean android = false;
        boolean ios = false;
        boolean winphone = false;
        if (platform == null || platform.isAll()) {
            android = true;
            ios = true;
            winphone = true;
        } else {
            // 无奈只能去解析 JsonElement
            JsonElement json = platform.toJSON();
            JsonArray jsonArray = json.getAsJsonArray();
            if (jsonArray.contains(new JsonPrimitive(DeviceType.Android.value()))) {
                android = true;
            }
            if (jsonArray.contains(new JsonPrimitive(DeviceType.IOS.value()))) {
                ios = true;
            }
            if (jsonArray.contains(new JsonPrimitive(DeviceType.WinPhone.value()))) {
                winphone = true;
            }
        }
        Notification.Builder builder = Notification.newBuilder();
        String alert = pushNotification.getAlert();
        String title = pushNotification.getTitle();
        Map<String, String> extras = pushNotification.getExtra();
        if (android) {
            AndroidNotification.Builder notificationBuilder = AndroidNotification.newBuilder();
            notificationBuilder.setAlert(alert);
            notificationBuilder.setTitle(title);
            if (extras != null && !extras.isEmpty()) {
                notificationBuilder.addExtras(extras);
            }
            builder.addPlatformNotification(notificationBuilder.build());
        }
        if (ios) {
            IosNotification.Builder notificationBuilder = IosNotification.newBuilder();
            notificationBuilder.setAlert(alert);
            if (extras != null && !extras.isEmpty()) {
                notificationBuilder.addExtras(extras);
            }
            builder.addPlatformNotification(notificationBuilder.build());
        }
        if (winphone) {
            WinphoneNotification.Builder notificationBuilder = WinphoneNotification.newBuilder();
            notificationBuilder.setAlert(alert);
            if (extras != null && !extras.isEmpty()) {
                notificationBuilder.addExtras(extras);
            }
            builder.addPlatformNotification(notificationBuilder.build());
        }
        return builder.build();
    }

}
