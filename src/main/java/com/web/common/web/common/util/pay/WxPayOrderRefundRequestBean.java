package com.web.common.web.common.util.pay;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * 微信支付退款申请bean
 * 
 */
@XmlRootElement(name = "xml")
public class WxPayOrderRefundRequestBean {
	
	private String appid;
	private String mch_id;
	private String nonce_str;
	private String sign;
	private String transaction_id;   //微信生产的订单号，在支付通知中有返回；和商户订单号二选一
	private String out_trade_no;     //商户订单号，和微信订单号二选一
	private String out_refund_no;    //商户系统内部的退款单号，商户系统内部唯一，同一退款单号多次请求只退一笔
	private Integer total_fee;       //订单总金额
	private Integer refund_fee;      //退款金额
	private String op_user_id;       //操作员帐号, 默认为商户号
	
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
	public String getOut_refund_no() {
		return out_refund_no;
	}
	public void setOut_refund_no(String out_refund_no) {
		this.out_refund_no = out_refund_no;
	}
	public Integer getTotal_fee() {
		return total_fee;
	}
	public void setTotal_fee(Integer total_fee) {
		this.total_fee = total_fee;
	}
	public Integer getRefund_fee() {
		return refund_fee;
	}
	public void setRefund_fee(Integer refund_fee) {
		this.refund_fee = refund_fee;
	}
	public String getOp_user_id() {
		return op_user_id;
	}
	public void setOp_user_id(String op_user_id) {
		this.op_user_id = op_user_id;
	}
	
	

}
