package com.web.common.web.common.util.pay;

/**
 * 微信支付响应基础数据
 * 
 * @author jzsong@uworks.cc
 */
public class WxPayResponseBaseBean {

  /**
   * <pre>
   * 字段名：签名，必填，String(32)
   * 描述：微信返回的签名
   * 示例值：C380BEC2BFD727A4B6845133519F3AD6
   * </pre>
   */
  private String sign;

  public String getSign() {
    return sign;
  }

  public void setSign(String sign) {
    this.sign = sign;
  }

}
