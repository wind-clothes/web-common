package com.web.common.web.common.util.pay;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * 微信支付-统一下单响应
 * 
 * @author jzsong@uworks.cc
 */
@XmlRootElement(name = "xml")
public class WxPayUnifiedOrderResponseBean extends WxPayResponseBaseBean {

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

  // 以下字段在return_code 和result_code都为SUCCESS的时候有返回
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
   * 字段名：预支付交易会话标识，必填，String(16)
   * 描述：微信生成的预支付回话标识，用于后续接口调用中使用，该值有效期为2小时
   * 示例值：JSAPI
   * </pre>
   */
  private String prepay_id;
  /**
   * <pre>
   * 字段名：二维码链接，非必填，String(64)
   * 描述：trade_type为NATIVE是有返回，可将该参数值生成二维码展示出来进行扫码支付
   * 示例值：URl：weixin：//wxpay/s/An4baqw
   * </pre>
   */
  private String code_url;

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

  public String getTrade_type() {
    return trade_type;
  }

  public void setTrade_type(String trade_type) {
    this.trade_type = trade_type;
  }

  public String getPrepay_id() {
    return prepay_id;
  }

  public void setPrepay_id(String prepay_id) {
    this.prepay_id = prepay_id;
  }

  public String getCode_url() {
    return code_url;
  }

  public void setCode_url(String code_url) {
    this.code_url = code_url;
  }

}
