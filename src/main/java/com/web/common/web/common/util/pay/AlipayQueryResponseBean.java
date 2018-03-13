package com.web.common.web.common.util.pay;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "alipay")
public class AlipayQueryResponseBean {

  private String request;
  private AlipayQueryResponse response;
  private String is_success;
  private String sign;
  private String sign_type;
  private String error;

  public String getRequest() {
    return request;
  }

  public void setRequest(String request) {
    this.request = request;
  }

  public AlipayQueryResponse getResponse() {
    return response;
  }

  public void setResponse(AlipayQueryResponse response) {
    this.response = response;
  }

  public String getError() {
    return error;
  }

  public void setError(String error) {
    this.error = error;
  }

  public String getIs_success() {
    return is_success;
  }

  public void setIs_success(String is_success) {
    this.is_success = is_success;
  }

  public String getSign() {
    return sign;
  }

  public void setSign(String sign) {
    this.sign = sign;
  }

  public String getSign_type() {
    return sign_type;
  }

  public void setSign_type(String sign_type) {
    this.sign_type = sign_type;
  }
}
