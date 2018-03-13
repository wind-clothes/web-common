package com.web.common.web.common.util.pay;

public class AlipayTradeQueryRequest {

	private String   service; //		接口名称	String	接口名称，固定值。	不可空	mobile.securitypay.pay
	private String   partner; //		合作者身份ID	String(16)	签约的支付宝账号对应的支付宝唯一用户号。以2088开头的16位纯数字组成。	不可空	2088101568358171
	private String   input_charset; //		参数编码字符集	String	商户网站使用的编码格式，固定为utf-8。	不可空	utf-8
	private String   sign_type; //		签名方式	String	签名类型，目前仅支持RSA。	不可空	RSA
	private String   sign; //		签名	String	请参见签名。	不可空	lBBK%2F0w5LOajrMrji7DUgEqNjIhQbidR13GovA5r3TgIbNqv231yC1NksLdw%2Ba3JnfHXoXuet6XNNHtn7VE%2BeCoRO1O%2BR1KugLrQEZMtG5jmJI
	private String   notify_url; //		服务器异步通知页面路径	String(200)	支付宝服务器主动通知商户网站里指定的页面http路径。	不可空	http://notify.msp.hk/notify.htm
	private String   app_id; //		客户端号	String	标识客户端。	可空	external
	private String   appenv; //		客户端来源	String	标识客户端来源。参数值内容约定如下：appenv=”system=客户端平台名^version=业务系统版本”	可空	appenv=”system=android^version=3.0.1.2”
	private String   out_trade_no; //		商户网站唯一订单号	String(64)	支付宝合作商户网站唯一订单号。	不可空	0819145412-6177
	private String   subject; //		商品名称	String(128)	商品的标题/交易标题/订单标题/订单关键字等。该参数最长为128个汉字。	不可空	测试
	private String   payment_type; //		支付类型	String(4)	支付类型。默认值为：1（商品购买）。	不可空	1
	private String   seller_id; //		卖家支付宝账号	String(16)	卖家支付宝账号（邮箱或手机号码格式）或其对应的支付宝唯一用户号（以2088开头的纯16位数字）。	不可空	xxx@alipay.com
	private String   total_fee; //		总金额	Number	该笔订单的资金总额，单位为RMB-Yuan。取值范围为[0.01，100000000.00]，精确到小数点后两位。	不可空	0.01
	private String   body; //		商品详情	String(512)	对一笔交易的具体描述信息。如果是多种商品，请将商品描述字符串累加传给body。	不可空	测试测试
	
	
}
