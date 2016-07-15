package com.web.common.web.common.util.pay;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;

import com.web.common.web.common.util.MD5Util;
import com.web.common.web.common.util.xml.XmlUtils;

public class AlipayService {

  public static final Logger logger = Logger.getLogger(AlipayService.class.getName());

  @Resource
  private AliConfig aliConfig;

  private static final String REFUND_NOTIFY_URL = "";
  private static final String REFUND_URL = "";

  private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

  /**
   * 支付前预处理 注意：支付宝的货币单位是元 做业务时要进行处理
   * 
   * @return
   */
  public Map<String, String> genPrepayMap(PayParamBean param) {
    Map<String, String> map = new HashMap<String, String>();
    map.put("trade_no", param.getOrder_no());
    map.put("partner", aliConfig.getPartner());
    map.put("seller_id", aliConfig.getPartner());
    map.put("out_trade_no", param.getOrder_no());
    map.put("notify_url", aliConfig.getNotify_url());
    map.put("subject", param.getSubject());
    map.put("body", param.getBody());
    map.put("total_fee", param.getTotal_fee() + "");
    map.put("service", "mobile.securitypay.pay");
    map.put("payment_type", 1 + "");
    map.put("_input_charset", aliConfig.getCharset());
    map.put("it_b_pay", "30m");
    return map;
  }

  public String getPayUrlString(PayParamBean param) throws Exception {
    Map<String, String> payinfoMap = genPrepayMap(param);
    String signContent = AlipaySignature.getSignContent(payinfoMap);// 得到签名内容
    String sign =
        AlipaySignature.rsaSign(signContent, aliConfig.getPrivate_key(), aliConfig.getCharset());
    sign = URLEncoder.encode(sign, aliConfig.getCharset());
    String sign_type = aliConfig.getSign_type();
    return signContent + "&sign=" + sign + "&sign_type=" + sign_type;
  }

  /**
   * 订单查询
   * 
   * @param orderNo
   * @param amount
   * @return
   * @throws JsonParseException
   * @throws JsonMappingException
   * @throws IOException
   */
  public YilvPayUpdateBean aliPayQuery(String orderNo, Integer amount) throws IOException {
    logger.info("开始调用支付宝查询接口，订单号：" + orderNo);
    Map<String, String> map = new HashMap<String, String>();
    // 加入各类参数，签名，远程请求，处理返回值
    map.put("service", "single_trade_query");
    map.put("partner", aliConfig.getPartner());
    map.put("_input_charset", aliConfig.getCharset());
    map.put("out_trade_no", orderNo);
    // 生成签名
    // 获取待签名字符串
    String preSignStr = createLinkString(map);
    System.out.println("待签名字符串 : " + preSignStr);
    // String sign = RSA.sign(preSignStr, AlipayConfig.private_key, CHARSET);

    preSignStr += aliConfig.getKey();
    String sign = MD5Util.MD5(preSignStr);
    logger.info("调用支付宝查询接口时生成了签名：" + sign);
    map.put("sign_type", "MD5");
    map.put("sign", sign);

    preSignStr += "&sign=" + sign + "&sign_type=MD5";
    String postUrl = aliConfig.getQuery_url();
    System.out.println(postUrl + preSignStr);
    // TODO
    String result = "";//HttpUtils.doGet(postUrl, map, aliConfig.getCharset());
    logger.info("支付宝订单查询返回内容：" + result);

    YilvPayUpdateBean bean = new YilvPayUpdateBean();
    String status = null;
    // 对result进行处理 生成bean
    AlipayQueryResponseBean queryBean = XmlUtils.toObject(result, AlipayQueryResponseBean.class);
    if (queryBean.getIs_success().equals("T")) {
      AlipayQueryResponse reb = queryBean.getResponse();
      AlipayQueryResponseTrade trade = reb.getTrade();
      String trade_status = trade.getTrade_status();
      String trade_no = trade.getTrade_no();
      bean.setQueryId(trade_no);
      logger.info("支付宝订单" + orderNo + "查询状态：" + trade_status);

      if (trade_status.equals("TRADE_FINISHED")) {
        status = "SUCCESS";
      } else if (trade_status.equals("TRADE_SUCCESS")) {
        status = "SUCCESS";
      } else if (trade_status.equals("WAIT_BUYER_PAY")) {
        status = "USERPAING";
      } else if (trade_status.equals("TRADE_CLOSED")) {
        status = "FAIL";
      } else {
        status = "USERPAING"; // 如果出现其他情况 先返回支付中状态 待进一步验证
      }
    } else {
      logger.info("支付宝订单" + orderNo + "查询出现问题，问题代码：" + queryBean.getError());
      status = "USERPAING";
    }

    bean.setStatus(status);
    return bean;
  }

  /**
   * 请求退款
   * 
   * @param orderNo
   * @return
   * @throws IOException
   * @throws JsonMappingException
   * @throws JsonParseException
   */
  public String aliPayRefund(String orderNo, String tradeNo, Integer amount) throws IOException {
    Map<String, String> map = new HashMap<String, String>();
    // 加入各类参数，签名，远程请求，处理返回值
    map.put("service", "refund_fastpay_by_platform_pwd");
    map.put("partner", aliConfig.getPartner());
    map.put("_input_charset", aliConfig.getCharset());
    map.put("notify_url", REFUND_NOTIFY_URL);
    map.put("seller_user_id", aliConfig.getPartner());
    map.put("refund_date", sdf.format(System.currentTimeMillis()));

    // 退款批次号
    String batch_no = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
    map.put("batch_no", batch_no);
    map.put("batch_num", "1");

    // 格式为：原付款支付宝交易号^退款总金额^退款理由
    String detailData = tradeNo + "^" + (amount * 1f / 100) + "^" + "订单退款";
    // String detailData = "2015101600001000980063060626^5.00^协商退款";
    map.put("detail_data", detailData);

    logger.info("支付宝退款申请参数内容：" + map.toString());

    // 生成签名
    // 获取待签名字符串
    String preSignStr = createLinkString(map);
    // logger.info("支付宝退款申请待签名字符串 : " + preSignStr);

    preSignStr += aliConfig.getKey();
    String sign = MD5Util.MD5(preSignStr);
    // logger.info("调用支付宝退款申请接口时生成了签名：" + sign);
    preSignStr += "&sign=" + sign + "&sign_type=MD5";

    map.put("sign_type", "MD5");
    map.put("sign", sign);

    String postUrl = REFUND_URL;
    String params = createEncodeLinkString(map);
    // System.out.println(postUrl + params);
    // 这里不需要访问支付宝接口，直接返回链接地址给后台人员操作即可，否则返回的是支付页面的html代码流。
    // String result = WebUtils.doGet(postUrl, map, CHARSET);
    // logger.info("支付宝订单查询返回内容：" + result);

    // 返回一个请求地址
    logger.info("调用支付宝退款申请接口时生成了地址：" + postUrl + params);
    return postUrl + params;
  }

  /**
   * 验证消息是否是支付宝发出的合法消息
   * 
   * @param params 通知返回来的参数数组
   * @return 验证结果
   */
  public boolean verify(Map<String, String> params) {

    // 判断responsetTxt是否为true，isSign是否为true
    // responsetTxt的结果不是true，与服务器设置问题、合作身份者ID、notify_id一分钟失效有关
    // isSign不是true，与安全校验码、请求时的参数格式（如：带自定义参数等）、编码格式有关
    String responseTxt = "true";
    if (params.get("notify_id") != null) {
      String notify_id = params.get("notify_id");
      responseTxt = verifyResponse(notify_id);
    }
    String sign = "";
    if (params.get("sign") != null) {
      sign = params.get("sign");
    }
    boolean isSign = getSignVeryfy(params, sign);

    if (isSign && responseTxt.equals("true")) {
      return true;
    } else {
      return false;
    }
  }

  /**
   * 根据反馈回来的信息，生成签名结果
   * 
   * @param Params 通知返回来的参数数组
   * @param sign 比对的签名结果
   * @return 生成的签名结果
   */
  private boolean getSignVeryfy(Map<String, String> Params, String sign) {
    // 过滤空值、sign与sign_type参数
    Map<String, String> sParaNew = paraFilter(Params);
    // 获取待签名字符串
    String preSignStr = createLinkString(sParaNew);
    // 获得签名验证结果
    boolean isSign = false;

    try {
      isSign =
          AlipaySignature.rsaCheckContent(preSignStr, sign, aliConfig.getPublic_key(),
              aliConfig.getCharset());
    } catch (Exception ex) {
      ex.printStackTrace();
    }

    return isSign;
  }

  /**
   * 获取远程服务器ATN结果,验证返回URL
   * 
   * @param notify_id 通知校验ID
   * @return 服务器ATN结果 验证结果集： invalid命令参数不对 出现这个错误，请检测返回处理中partner和key是否为空 true 返回正确信息 false
   *         请检查防火墙或者是服务器阻止端口问题以及验证时间是否超过一分钟
   */
  public String verifyResponse(String notify_id) {
    // 获取远程服务器ATN结果，验证是否是支付宝服务器发来的请求
    String partner = aliConfig.getPartner();
    String veryfy_url =
        aliConfig.getHttps_verify_url() + "partner=" + partner + "&notify_id=" + notify_id;

    return checkUrl(veryfy_url);
  }

  /**
   * 获取远程服务器ATN结果
   * 
   * @param urlvalue 指定URL路径地址
   * @return 服务器ATN结果 验证结果集： invalid命令参数不对 出现这个错误，请检测返回处理中partner和key是否为空 true 返回正确信息 false
   *         请检查防火墙或者是服务器阻止端口问题以及验证时间是否超过一分钟
   */
  private String checkUrl(String urlvalue) {
    String inputLine = "";

    try {
      URL url = new URL(urlvalue);
      HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
      BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
      inputLine = in.readLine().toString();
    } catch (Exception e) {
      e.printStackTrace();
      inputLine = "";
    }

    return inputLine;
  }

  /**
   * 除去数组中的空值和签名参数
   * 
   * @param sArray 签名参数组
   * @return 去掉空值与签名参数后的新签名参数组
   */
  public static Map<String, String> paraFilter(Map<String, String> sArray) {

    Map<String, String> result = new HashMap<String, String>();

    if (sArray == null || sArray.size() <= 0) {
      return result;
    }

    for (String key : sArray.keySet()) {
      String value = sArray.get(key);
      if (value == null || value.equals("") || key.equalsIgnoreCase("sign")
          || key.equalsIgnoreCase("sign_type")) {
        continue;
      }
      result.put(key, value);
    }

    return result;
  }

  /**
   * 把数组所有元素排序，并按照“参数=参数值”的模式用“&”字符拼接成字符串
   * 
   * @param params 需要排序并参与字符拼接的参数组
   * @return 拼接后字符串
   */
  public static String createLinkString(Map<String, String> params) {

    List<String> keys = new ArrayList<String>(params.keySet());
    Collections.sort(keys);

    String prestr = "";

    for (int i = 0; i < keys.size(); i++) {
      String key = keys.get(i);
      String value = params.get(key);

      if (i == keys.size() - 1) {// 拼接时，不包括最后一个&字符
        prestr = prestr + key + "=" + value;
      } else {
        prestr = prestr + key + "=" + value + "&";
      }
    }

    return prestr;
  }

  /**
   * 把数组所有元素排序，并按照“参数=参数值”的模式用“&”字符拼接成字符串,同时进行url编码处理
   * 
   * @param params 需要排序并参与字符拼接的参数组
   * @return 拼接后字符串
   * @throws UnsupportedEncodingException
   */
  public String createEncodeLinkString(Map<String, String> params)
      throws UnsupportedEncodingException {

    List<String> keys = new ArrayList<String>(params.keySet());

    String prestr = "";

    for (int i = 0; i < keys.size(); i++) {
      String key = keys.get(i);
      String value = params.get(key);
      String encodeValue = URLEncoder.encode(value, aliConfig.getCharset());;

      if (i == keys.size() - 1) {// 拼接时，不包括最后一个&字符
        prestr = prestr + key + "=" + encodeValue;
      } else {
        prestr = prestr + key + "=" + encodeValue + "&";
      }
    }

    return prestr;
  }


}
