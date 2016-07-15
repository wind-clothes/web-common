package com.web.common.web.common.util.pay;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * 微信支付通知响应bean
 * 
 * @author jzsong@uworks.cc
 */
@XmlRootElement(name = "xml")
public class WxPayNotifyResponseBean extends WxPayResponseBaseBean {

  /**
   * <pre>
   * 字段名：返回状态码，必填，String(16)
   * 描述：SUCCESS/FAIL，此字段是通信标识，非交易标识，交易是否成功需要查看result_code来判断
   * 示例值：SUCCESS
   * </pre>
   */
  private String return_code;
  /**
   * <pre>
   * 字段名：返回信息，非必填，String(128)
   * 描述：返回信息，如非空，为错误原因，例如：签名失败、参数格式校验错误
   * 示例值：签名失败
   * </pre>
   */
  private String return_msg;

  // 以下字段在return_code为SUCCESS的时候有返回
  /**
   * <pre>
   * 字段名：公众账号ID，必填，String(32)
   * 描述：调用接口提交的公众账号ID
   * 示例值：wxd678efh567hg6787
   * </pre>
   */
  private String appid;
  /**
   * <pre>
   * 字段名：商户号，必填，String(32)
   * 描述：调用接口提交的商户号
   * 示例值：1230000109
   * </pre>
   */
  private String mch_id;
  /**
   * <pre>
   * 字段名：随机字符串，必填，String(32)
   * 描述：微信返回的随机字符串
   * 示例值：5K8264ILTKCH16CQ2502SI8ZNMTM67VS
   * </pre>
   */
  private String nonce_str;
  /**
   * <pre>
   * 字段名：设备号，非必填，String(32)
   * 描述：调用接口提交的终端设备号
   * 示例值：013467007045764
   * </pre>
   */
  private String device_info;
  /**
   * <pre>
   * 字段名：业务结果，必填，String(16)
   * 描述：SUCCESS/FAIL
   * 示例值：SUCCESS
   * </pre>
   */
  private String result_code;
  /**
   * <pre>
   * 字段名：错误代码，非必填，String(32)
   * 描述：
   * 示例值：SYSTEMERROR
   * </pre>
   */
  private String err_code;
  /**
   * <pre>
   * 字段名：错误代码描述，非必填，String(128)
   * 描述：错误返回的信息描述
   * 示例值：系统错误
   * </pre>
   */
  private String err_code_des;
  /**
   * <pre>
   * 字段名：用户标识，必填，String(128)
   * 描述：用户在商户appid下的唯一标识
   * 示例值：wxd930ea5d5a258f4f
   * </pre>
   */
  private String openid;
  /**
   * <pre>
   * 字段名：是否关注公众账号，必填，String(1)
   * 描述：用户是否关注公众账号，Y-关注，N-未关注，仅在公众账号类型支付有效
   * 示例值：Y
   * </pre>
   */
  private String is_subscribe;
  /**
   * <pre>
   * 字段名：交易类型，必填，String(16)
   * 描述：调用接口提交的交易类型
   * 示例值：JSAPI
   * </pre>
   */
  private String trade_type;
  /**
   * <pre>
   * 字段名：付款银行，必填，String(16)
   * 描述：银行类型，采用字符串类型的银行标识
   * 示例值：CMC
   * </pre>
   */
  private String bank_type;
  /**
   * <pre>
   * 字段名：总金额，必填，int
   * 描述：订单总金额，单位为分
   * 示例值：100
   * </pre>
   */
  private int total_fee;
  /**
   * <pre>
   * 字段名：货币种类，非必填，String(8)
   * 描述：货币类型，符合ISO4217标准的三位字母代码，默认人民币：CNY
   * 示例值：CNY
   * </pre>
   */
  private String fee_type;
  /**
   * <pre>
   * 字段名：现金支付金额，必填，int
   * 描述：现金支付金额订单现金支付金额
   * 示例值：100
   * </pre>
   */
  private int cash_fee;
  /**
   * <pre>
   * 字段名：现金支付货币类型，非必填，String(8)
   * 描述：现金支付货币类型，符合ISO4217标准的三位字母代码，默认人民币：CNY
   * 示例值：CNY
   * </pre>
   */
  private String cash_fee_type;
  
  /**
   * <pre>
   * 字段名：微信支付订单号，必填，String(32)  
   * 描述：微信支付订单号
   * 示例值：1217752501201407033233368018
   * </pre>
   */
  private String transaction_id;
  /**
   * <pre>
   * 字段名：商户订单号，必填，String(32)
   * 描述：商户系统的订单号，与请求一致
   * 示例值：20150806125346
   * </pre>
   */
  private String out_trade_no;
  /**
   * <pre>
   * 字段名：附加数据，非必填，String(127)
   * 描述：商家数据包，原样返回
   * 示例值：深圳分店
   * </pre>
   */
  private String attach;
  /**
   * <pre>
   * 字段名：支付完成时间，非必填，String(14)
   * 描述：支付完成时间，格式为yyyyMMddHHmmss，如2009年12月25日9点10分10秒表示为20091225091010
   * 示例值：20141030133525
   * </pre>
   */
  private String time_end;

  public String getReturn_code() {
    return return_code;
  }

  public void setReturn_code(String return_code) {
    this.return_code = return_code;
  }

  public String getReturn_msg() {
    return return_msg;
  }

  public void setReturn_msg(String return_msg) {
    this.return_msg = return_msg;
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

  public String getDevice_info() {
    return device_info;
  }

  public void setDevice_info(String device_info) {
    this.device_info = device_info;
  }

  public String getResult_code() {
    return result_code;
  }

  public void setResult_code(String result_code) {
    this.result_code = result_code;
  }

  public String getErr_code() {
    return err_code;
  }

  public void setErr_code(String err_code) {
    this.err_code = err_code;
  }

  public String getErr_code_des() {
    return err_code_des;
  }

  public void setErr_code_des(String err_code_des) {
    this.err_code_des = err_code_des;
  }

  public String getOpenid() {
    return openid;
  }

  public void setOpenid(String openid) {
    this.openid = openid;
  }

  public String getIs_subscribe() {
    return is_subscribe;
  }

  public void setIs_subscribe(String is_subscribe) {
    this.is_subscribe = is_subscribe;
  }

  public String getTrade_type() {
    return trade_type;
  }

  public void setTrade_type(String trade_type) {
    this.trade_type = trade_type;
  }

  public String getBank_type() {
    return bank_type;
  }

  public void setBank_type(String bank_type) {
    this.bank_type = bank_type;
  }

  public int getTotal_fee() {
    return total_fee;
  }

  public void setTotal_fee(int total_fee) {
    this.total_fee = total_fee;
  }

  public String getFee_type() {
    return fee_type;
  }

  public void setFee_type(String fee_type) {
    this.fee_type = fee_type;
  }

  public int getCash_fee() {
    return cash_fee;
  }

  public void setCash_fee(int cash_fee) {
    this.cash_fee = cash_fee;
  }

  public String getCash_fee_type() {
    return cash_fee_type;
  }

  public void setCash_fee_type(String cash_fee_type) {
    this.cash_fee_type = cash_fee_type;
  }


  public String getTransaction_id() {
    return transaction_id;
  }

  public void setTransaction_id(String transaction_id) {
    this.transaction_id = transaction_id;
  }

  public String getOut_trade_no() {
    return out_trade_no;
  }

  public void setOut_trade_no(String out_trade_no) {
    this.out_trade_no = out_trade_no;
  }

  public String getAttach() {
    return attach;
  }

  public void setAttach(String attach) {
    this.attach = attach;
  }

  public String getTime_end() {
    return time_end;
  }

  public void setTime_end(String time_end) {
    this.time_end = time_end;
  }
}
