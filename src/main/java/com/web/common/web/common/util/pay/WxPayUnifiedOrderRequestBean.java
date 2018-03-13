package com.web.common.web.common.util.pay;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * <pre>
 * 微信支付-统一下单请求bean
 * 参数说明详见：https://pay.weixin.qq.com/wiki/doc/api/app.php?chapter=9_1#
 * </pre>
 * 
 * @author jzsong@uworks.cc
 */
@XmlRootElement(name = "xml")
public class WxPayUnifiedOrderRequestBean {

  /**
   * <pre>
   * 字段名：公众账号ID，必填，String(32)
   * 描述：微信分配的公众账号ID（企业号corpid即为此appId）
   * 示例值：wxd678efh567hg6787
   * </pre>
   */
  private String appid;
  /**
   * <pre>
   * 字段名：商户号，必填，String(32)
   * 描述：微信支付分配的商户号
   * 示例值：1230000109
   * </pre>
   */
  private String mch_id;
  /**
   * <pre>
   * 字段名：随机字符串，必填，String(32)
   * 描述：随机字符串，不长于32位
   * 示例值：5K8264ILTKCH16CQ2502SI8ZNMTM67VS
   * </pre>
   */
  private String nonce_str;
  /**
   * <pre>
   * 字段名：签名，必填，String(32)
   * 描述：签名，不长于32位
   * 示例值：C380BEC2BFD727A4B6845133519F3AD6
   * </pre>
   */
  private String sign;
  /**
   * <pre>
   * 字段名：设备号，非必填，String(32)
   * 描述：终端设备号(门店号或收银设备ID)，注意：PC网页或公众号内支付请传"WEB"
   * 示例值：013467007045764
   * </pre>
   */
  private String device_info;
  /**
   * <pre>
   * 字段名：商品描述，必填，String(32)
   * 描述：商品或支付单简要描述
   * 示例值：Ipad mini  16G  白色
   * </pre>
   */
  private String body;
  /**
   * <pre>
   * 字段名：商品详情，非必填，String(8192)
   * 描述：商品名称明细列表
   * 示例值：Ipad mini  16G  白色
   * </pre>
   */
  private String detail;
  /**
   * <pre>
   * 字段名：附加数据，非必填，String(127)
   * 描述：附加数据，在查询API和支付通知中原样返回，该字段主要用于商户携带订单的自定义数据
   * 示例值：深圳分店
   * </pre>
   */
  private String attach;
  /**
   * <pre>
   * 字段名：商户订单号，必填，String(32)
   * 描述：商户系统内部的订单号,32个字符内、可包含字母
   * 示例值：20150806125346
   * </pre>
   */
  private String out_trade_no;
  /**
   * <pre>
   * 字段名：货币类型，非必填，String(16)
   * 描述：符合ISO 4217标准的三位字母代码，默认人民币：CNY
   * 示例值：CNY
   * </pre>
   */
  private String fee_type = "CNY";
  /**
   * <pre>
   * 字段名：总金额，必填，int
   * 描述：订单总金额，单位为分
   * 示例值：888
   * </pre>
   */
  private int total_fee;
  /**
   * <pre>
   * 字段名：终端IP，必填，String(16)
   * 描述：APP和网页支付提交用户端ip，Native支付填调用微信支付API的机器IP
   * 示例值：123.12.12.123
   * </pre>
   */
  private String spbill_create_ip;
  /**
   * <pre>
   * 字段名：交易起始时间，非必填，String(14)
   * 描述：订单生成时间，格式为yyyyMMddHHmmss，如2009年12月25日9点10分10秒表示为20091225091010
   * 示例值：20091225091010
   * </pre>
   */
  private String time_start;
  /**
   * <pre>
   * 字段名：交易结束时间，非必填，String(14)
   * 描述：订单失效时间，格式为yyyyMMddHHmmss，如2009年12月27日9点10分10秒表示为20091227091010，注意：最短失效时间间隔必须大于5分钟
   * 示例值：20091227091010
   * </pre>
   */
  private String time_expire;
  /**
   * <pre>
   * 字段名：商品标记，非必填，String(32)
   * 描述：商品标记，代金券或立减优惠功能的参数
   * 示例值：WXG
   * </pre>
   */
  private String goods_tag;
  /**
   * <pre>
   * 字段名：通知地址，必填，String(256)
   * 描述：接收微信支付异步通知回调地址
   * 示例值：http://www.weixin.qq.com/wxpay/pay.php
   * </pre>
   */
  private String notify_url;
  /**
   * <pre>
   * 字段名：交易类型，必填，String(16)
   * 描述：统一下单接口，JSAPI--公众号支付、NATIVE--原生扫码支付、APP--app支付
   * MICROPAY--刷卡支付，刷卡支付有单独的支付接口，不调用统一下单接口
   * 示例值：JSAPI
   * </pre>
   */
  private String trade_type;
  /**
   * <pre>
   * 字段名：商品ID，非必填，String(32)
   * 描述：trade_type=NATIVE，此参数必传。此id为二维码中包含的商品ID，商户自行定义
   * 示例值：12235413214070356458058
   * </pre>
   */
  private String product_id;
  /**
   * <pre>
   * 字段名：用户标识，非必填，String(128)
   * 描述：trade_type=JSAPI，此参数必传，用户在商户appid下的唯一标识
   * 示例值：oUpF8uMuAJO_M2pxb1Q9zNjWeS6o
   * </pre>
   */
  private String openid;
  /**
   * <pre>
   * 字段名：指定支付方式，非必填，String(32)
   * 描述：no_credit--指定不能使用信用卡支付
   * 示例值：no_credit
   * </pre>
   */
  private String limit_pay;

  public WxPayUnifiedOrderRequestBean() {

  }

  public String getAppid() {
    return appid;
  }

  public void setAppid(String appid) {
    this.appid = appid;
  }

  public String getMch_id() {
    return mch_id;
  }

  public void setMch_id(String mch_id) {
    this.mch_id = mch_id;
  }

  public String getNonce_str() {
    return nonce_str;
  }

  public void setNonce_str(String nonce_str) {
    this.nonce_str = nonce_str;
  }

  public String getSign() {
    return sign;
  }

  public void setSign(String sign) {
    this.sign = sign;
  }

  public String getDevice_info() {
    return device_info;
  }

  public void setDevice_info(String device_info) {
    this.device_info = device_info;
  }

  public String getBody() {
    return body;
  }

  public void setBody(String body) {
    this.body = body;
  }

  public String getDetail() {
    return detail;
  }

  public void setDetail(String detail) {
    this.detail = detail;
  }

  public String getAttach() {
    return attach;
  }

  public void setAttach(String attach) {
    this.attach = attach;
  }

  public String getOut_trade_no() {
    return out_trade_no;
  }

  public void setOut_trade_no(String out_trade_no) {
    this.out_trade_no = out_trade_no;
  }

  public String getFee_type() {
    return fee_type;
  }

  public void setFee_type(String fee_type) {
    this.fee_type = fee_type;
  }

  public int getTotal_fee() {
    return total_fee;
  }

  public void setTotal_fee(int total_fee) {
    this.total_fee = total_fee;
  }

  public String getSpbill_create_ip() {
    return spbill_create_ip;
  }

  public void setSpbill_create_ip(String spbill_create_ip) {
    this.spbill_create_ip = spbill_create_ip;
  }

  public String getTime_start() {
    return time_start;
  }

  public void setTime_start(String time_start) {
    this.time_start = time_start;
  }

  public String getTime_expire() {
    return time_expire;
  }

  public void setTime_expire(String time_expire) {
    this.time_expire = time_expire;
  }

  public String getGoods_tag() {
    return goods_tag;
  }

  public void setGoods_tag(String goods_tag) {
    this.goods_tag = goods_tag;
  }

  public String getNotify_url() {
    return notify_url;
  }

  public void setNotify_url(String notify_url) {
    this.notify_url = notify_url;
  }

  public String getTrade_type() {
    return trade_type;
  }

  public void setTrade_type(String trade_type) {
    this.trade_type = trade_type;
  }

  public String getProduct_id() {
    return product_id;
  }

  public void setProduct_id(String product_id) {
    this.product_id = product_id;
  }

  public String getOpenid() {
    return openid;
  }

  public void setOpenid(String openid) {
    this.openid = openid;
  }

  public String getLimit_pay() {
    return limit_pay;
  }

  public void setLimit_pay(String limit_pay) {
    this.limit_pay = limit_pay;
  }

}
