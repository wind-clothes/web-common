package com.web.common.web.common.util.pay;

/**
 * 因各渠道的业务差异，有些特殊值需要过渡处理
 * 所以这里用于装载订单状态更新时的一些中间值，可根据需要补充；
 * @author apple
 *
 */
public class YilvPayUpdateBean {

	private String  orderNo;   //订单号
	private String  status;    //订单状态：NOTPAY, SUCCESS, FAIL, USERPAYING
	
	//查询流水单号，用于银联渠道查询订单或退款时用，保存在chargeRecord的query_id字段，另两个渠道可为空，
	//对于支付宝,用该字段保存支付宝交易号trade_no,在退款时使用
	private String  queryId;   
	
	private Integer IsServerSure; //服务器是否验证
	//以下可根据需要填充其他字段

	
	public Integer getIsServerSure() {
		return IsServerSure;
	}
	public void setIsServerSure(Integer isServerSure) {
		IsServerSure = isServerSure;
	}
	public String getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getQueryId() {
		return queryId;
	}
	public void setQueryId(String queryId) {
		this.queryId = queryId;
	}
	
	
}
