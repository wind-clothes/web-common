package com.web.common.web.common.util.pay;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.net.ssl.SSLContext;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import com.web.common.web.common.util.UuidUtils;
import com.web.common.web.common.util.xml.XmlUtils;


public class WxPayService {

  @Resource
  private WeixinConfig weixinConfig;

  public static final Logger logger = Logger.getLogger(WxPayService.class.getName());

  // 微信分配的公众账号id
  // private static final String APPID = "wxa060767eac74f5fe";

  // 微信分配的接入密钥
  // public static final String APPSECRET = "cc3dbb09cf70e8aaba6bea323329b61b";

  // 微信支付分配的商户号
  // private static final String MCHID = "1318990901";

  // 注册时分配的财付通商户号 partnerId 即macid
  // private static final String PARTNERID = "1318990901";

  // key设置路径：微信商户平台(pay.weixin.qq.com)-->账户设置-->API安全-->密钥设置
  // public static final String API_KEY = "yeonerkhygfedm5l6y8d14tdw54nv638";

  // 生成预支付订单路径
  // private static final String UNIFIEORDER_URL = "https://api.mch.weixin.qq.com/pay/unifiedorder";

  // 回调路径
  // private static final String NOTIFY_URL =
  // "http://yeoner-appserver-dev.obaymax.com/mall/wxPayNotify";

  // 主动查询路径
  // private static final String QUERY_URL = "https://api.mch.weixin.qq.com/pay/orderquery";

  // 退款申请路径
  private static final String REFUND_URL = "https://api.mch.weixin.qq.com/secapi/pay/refund";

  // 证书路径
  // private static String CERT_PATH = "";

  // 证书密码
  // private static char[] CERT_PASSWORD = "".toCharArray();

  /**
   * 连接超时时间，默认10秒
   */
  private static int socketTimeout = 10000;
  /**
   * 传输超时时间，默认30秒
   */
  private static int connectTimeout = 30000;

  /**
   * 统一下单接口返回正常的prepay_id，再按签名规范重新生成签名后，将数据传输给APP。 拿到prepayid后需要再一次加密 生成带签名的客户端支付信息
   * 参与签名的字段名为appId，partnerId，prepayId，nonceStr，timeStamp，package。注意：package的值格式为Sign=WXPay
   * 
   * @param param
   * @return
   * @throws Exception
   */
  public Map<String, Object> genPrepayMap(PayParamBean param) throws Exception {
    String prepayId = genUnifiedorder(param);

    HashMap<String, Object> map2 = new HashMap<String, Object>();
    map2.put("prepayid", prepayId);
    map2.put("appid", weixinConfig.getAppid());
    map2.put("noncestr", UuidUtils.getUUID(32));
    map2.put("timestamp", String.valueOf(System.currentTimeMillis() / 1000));
    map2.put("package", "Sign=WXPay");
    map2.put("partnerid", weixinConfig.getPartnerid());

    // map2.put("mch_id",PARTNERID);
    // map2.put("body",param.getBody());
    // map2.put("notify_url", NOTIFY_URL);
    // map2.put("out_trade_no",param.getOrder_no());
    // map2.put("spbill_create_id",param.getClient_ip());
    // map2.put("total_fee",param.getAmount());
    // map2.put("trade_type","APP");
    map2.put("sign", Signature.getSignByMap(map2, "key=" + weixinConfig.getApi_key()).toUpperCase());

    return map2;
  }

  /**
   * 生成订单预付单 获取prepayId
   * 
   * @param paramBean
   * @return
   * @throws Exception
   */
  public String genUnifiedorder(PayParamBean paramBean) throws Exception {
    // 以下必须参数 生成预付单请求对象
    WxPayUnifiedOrderRequestBean unifiedOrderBean = new WxPayUnifiedOrderRequestBean();
    unifiedOrderBean.setAppid(weixinConfig.getAppid());
    unifiedOrderBean.setMch_id(weixinConfig.getMchid());
    unifiedOrderBean.setNonce_str(UuidUtils.getUUID(32));
    unifiedOrderBean.setOut_trade_no(paramBean.getOrder_no());
    unifiedOrderBean.setBody("订单");
    unifiedOrderBean.setTotal_fee(paramBean.getAmount());
    unifiedOrderBean.setSpbill_create_ip("");
    unifiedOrderBean.setTrade_type("APP");
    unifiedOrderBean.setNotify_url(weixinConfig.getNotify_url());
    // 获取签名
    String sign =
        Signature.getSign(unifiedOrderBean, "key=" + weixinConfig.getApi_key()).toUpperCase();
    logger.info("genUnifiedorder 生成预支付订单的签名：" + sign);
    unifiedOrderBean.setSign(sign);

    // 转换成xml格式
    String param = XmlUtils.toXml(unifiedOrderBean);
    logger.info("genUnifiedorder 生成预支付订单的参数：" + param);

    // 请求微信统一下单app获取结果
    String responseXml = postXml(weixinConfig.getUnifieorder_url(), param.toString());
    logger.info("genUnifiedorder 生成预支付订单的返回结果：" + responseXml);

    // 将xml结果转换为对象并获取prepayId
    WxPayUnifiedOrderResponseBean bean =
        XmlUtils.toObject(responseXml, WxPayUnifiedOrderResponseBean.class);
    String prepayId = "";
    if (bean.getReturn_code().equals("SUCCESS")) {
      if (bean.getResult_code().equals("SUCCESS")) {
        prepayId = bean.getPrepay_id();
        logger.info("取到的微信prepayid: " + prepayId);
      } else {
        logger.info("取微信prepayid失败，错误码:" + bean.getErr_code() + "; 错误描述" + bean.getErr_code_des());
      }
    } else {
      logger.info("取微信prepayid失败，错误码:" + bean.getReturn_msg());
    }

    return prepayId;
  }

  /**
   * 查询订单支付状态
   * 
   * @param orderNo
   * @return
   * @throws Exception
   */
  public YilvPayUpdateBean wxPayQuery(String orderNo) throws Exception {
    logger.info("开始微信支付主动查询，订单号：" + orderNo);
    WxPayOrderQueryRequestBean queryRequstBean = new WxPayOrderQueryRequestBean();
    queryRequstBean.setAppid(weixinConfig.getAppid());
    queryRequstBean.setMch_id(weixinConfig.getMchid());
    queryRequstBean.setOut_trade_no(orderNo);
    queryRequstBean.setNonce_str(UuidUtils.getUUID(32));
    queryRequstBean.setSign(Signature.getSign(queryRequstBean, "key=" + weixinConfig.getApi_key())
        .toUpperCase());

    String param = XmlUtils.toXml(queryRequstBean);
    logger.info("微信支付查询订单参数：" + param);

    // 返回请求结果
    String responseXml = postXml(weixinConfig.getQuery_url(), param.toString());
    // 将返回结果转为对象
    WxPayOrderQueryResponseBean queryResponseBean =
        XmlUtils.toObject(responseXml, WxPayOrderQueryResponseBean.class);
    String trade_state = null;
    if (queryResponseBean.getReturn_code().equals("SUCCESS")) {
      if (queryResponseBean.getResult_code().equals("SUCCESS")) {
        trade_state = queryResponseBean.getTrade_state();
        logger.info("微信支付查询订单结果：订单号：" + orderNo + "; 状态：" + trade_state);
      } else {
        logger.info("取微信支付查询订单失，订单号：" + orderNo + "; 错误码:" + queryResponseBean.getErr_code()
            + "; 错误描述" + queryResponseBean.getErr_code_des());
      }
    } else {
      logger.info("取微信支付查询订单失败，订单号：" + orderNo + "; 错误码:" + queryResponseBean.getReturn_msg());
    }

    // 这里归一化处理，将REFUND，CLOSED，PAYERROR都认为是失败
    if (!trade_state.equals("SUCCESS") && !trade_state.equals("USERPAING")
        && !trade_state.equals("NOTPAY")) {
      trade_state = "FAIL";
    }

    YilvPayUpdateBean bean = new YilvPayUpdateBean();
    bean.setStatus(trade_state);
    return bean;
  }

  /**
   * 微信退款申请接口
   * 
   * @param orderNo
   * @param amount
   * @return refund_id : 微信退款单号
   * @throws Exception
   */
  public String wxPayRefund(String orderNo, Integer amount) throws Exception {
    logger.info("开始微信支付退款申请，订单号：" + orderNo + "; 金额：" + amount);
    WxPayOrderRefundRequestBean refundRequstBean = new WxPayOrderRefundRequestBean();
    refundRequstBean.setAppid(weixinConfig.getAppid());
    refundRequstBean.setMch_id(weixinConfig.getMchid());
    refundRequstBean.setOut_trade_no(orderNo);
    refundRequstBean.setNonce_str(UuidUtils.getUUID(32));
    // 生成一个随机的退款单号
    String out_refund_no = UuidUtils.getUUID(32);
    refundRequstBean.setOut_refund_no(out_refund_no);
    refundRequstBean.setTotal_fee(amount);
    refundRequstBean.setRefund_fee(amount);
    refundRequstBean.setOp_user_id(weixinConfig.getMchid());

    refundRequstBean.setSign(Signature
        .getSign(refundRequstBean, "key=" + weixinConfig.getApi_key()).toUpperCase());

    String param = XmlUtils.toXml(refundRequstBean);
    logger.info("微信退款申请参数：" + param);

    // 返回请求结果
    String responseXml = postXml(REFUND_URL, param.toString());
    String refund_id = null;
    // 将返回结果转为对象
    WxPayOrderRefundResponseBean refundResponseBean =
        XmlUtils.toObject(responseXml, WxPayOrderRefundResponseBean.class);
    if (refundResponseBean.getReturn_code().equals("SUCCESS")) {
      if (refundResponseBean.getResult_code().equals("SUCCESS")) {
        refund_id = refundResponseBean.getRefund_id();
        logger.info("微信退款申请成功：订单号：" + orderNo + "; 微信退款单号：" + refund_id);
      } else {
        logger.info("微信退款申请失败，订单号：" + orderNo + "; 错误码:" + refundResponseBean.getErr_code()
            + "; 错误描述" + refundResponseBean.getErr_code_des());
      }
    } else {
      logger.info("微信退款申请失败，订单号：" + orderNo + "; 错误码:" + refundResponseBean.getReturn_msg());
    }

    return refund_id;
  }

  // 查询退款接口 待补充，用零钱支付的退款20分钟内到账，银行卡支付的退款3个工作日后重新查询退款状态。

  /**
   * 检验API返回的数据里面的签名是否合法，避免数据在传输的过程中被第三方篡改
   * 
   * @param responseXml API返回的XML数据字符串
   * @return clazz，根据responseXml，转成对象的class
   */
  // public static boolean checkIsSignValidFromResponseString(String responseXml,
  // Class<? extends WxPayResponseBaseBean> clazz) {
  // WxPayResponseBaseBean bean = WxPayUtil.toObject(responseXml, clazz);
  //
  // String sign = bean.getSign();
  // if (sign == null || sign == "") {
  // logger.info("API返回的数据签名数据不存在，有可能被第三方篡改!!!");
  // return false;
  // }
  // // 清掉返回数据对象里面的Sign数据（不能把这个数据也加进去进行签名），然后用签名算法进行签名
  // bean.setSign(null);
  // // 将API返回的数据根据用签名算法进行计算新的签名，用来跟API返回的签名进行比较
  // String signForAPIResponse = Signature.getSign(bean);
  // if (!signForAPIResponse.equals(sign)) {
  // // 签名验不过，表示这个API返回的数据有可能已经被篡改了
  // logger.info("API返回的数据签名验证不通过，有可能被第三方篡改!!!");
  // return false;
  // }
  // logger.info("恭喜，API返回的数据签名验证通过!!!");
  // return true;
  // }

  /**
   * post请求xml数据
   * 
   * @param url
   * @param postDataXml
   * @return
   */
  public static String postXml(String url, String postDataXml) throws Exception {
    String result = null;
    logger.info("微信支付请求数据：" + postDataXml);
    HttpPost httpPost = new HttpPost(url);
    StringEntity postEntity = new StringEntity(postDataXml, "UTF-8");
    httpPost.addHeader("Content-Type", "text/xml");
    httpPost.setEntity(postEntity);
    // 设置请求器的配置
    httpPost.setConfig(getRequestConfig());
    try {
      HttpResponse response = getHttpClient().execute(httpPost);
      HttpEntity entity = response.getEntity();
      result = EntityUtils.toString(entity, "UTF-8");
    } catch (Exception e) {
      throw new RuntimeException(e);
    } finally {
      httpPost.abort();
    }
    logger.info("微信支付响应数据：" + result);
    return result;
  }

  // 读取证书 并返回一个连接
  private static CloseableHttpClient getHttpClient() throws Exception {
    // KeyStore keyStore = KeyStore.getInstance("PKCS12");
    // 加载本地的证书进行https加密传输

    // String path = checkFilePath() + certPath;
    // System.out.println("certpath: " + path);

    // FileInputStream instream = new FileInputStream(new File(certPath));
    // try {
    // keyStore.load(instream, certpassword);// 设置证书密码
    // } catch (Exception e) {
    // throw new RuntimeException(e);
    // } finally {
    // instream.close();
    // }
    SSLContext sslcontext = SSLContext.getInstance("SSL");
    sslcontext.init(null, null, new java.security.SecureRandom());
    SSLConnectionSocketFactory sslsf =
        new SSLConnectionSocketFactory(sslcontext, new String[] {"TLSv1"}, null,
            SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
    CloseableHttpClient httpClient = HttpClients.custom().setSSLSocketFactory(sslsf).build();
    return httpClient;
  }

  private static RequestConfig getRequestConfig() {
    return RequestConfig.custom().setSocketTimeout(socketTimeout).setConnectTimeout(connectTimeout)
        .build();
  }

}
