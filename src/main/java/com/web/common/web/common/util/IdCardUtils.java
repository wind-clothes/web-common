package com.web.common.web.common.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Hashtable;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

/**
 * <pre>
 * 身份证前6位【ABCDEF】为行政区划数字代码（简称数字码）说明（参考《GB/T 2260-2007 中华人民共和国行政区划代码》）：
 * 该数字码的编制原则和结构分析，它采用三层六位层次码结构，按层次分别表示我国各省（自治区，直辖市，特别行政区）、
 * 市（地区，自治州，盟）、县（自治县、县级市、旗、自治旗、市辖区、林区、特区）。
 *    数字码码位结构从左至右的含义是：
 *    第一层为AB两位代码表示省、自治区、直辖市、特别行政区；
 *    第二层为CD两位代码表示市、地区、自治州、盟、直辖市所辖市辖区、县汇总码、省（自治区）直辖县级行政区划汇总码，其中：
 *    ——01~20、51~70表示市，01、02还用于表示直辖市所辖市辖区、县汇总码；
 *    ——21~50表示地区、自治州、盟；
 *    ——90表示省（自治区）直辖县级行政区划汇总码。
 *    第三层为EF两位表示县、自治县、县级市、旗、自治旗、市辖区、林区、特区，其中：
 *    ——01~20表示市辖区、地区（自治州、盟）辖县级市、市辖特区以及省（自治区）直辖县级行政区划中的县级市，01通常表示辖区汇总码；
 *    ——21~80表示县、自治县、旗、自治旗、林区、地区辖特区；
 *    ——81~99表示省（自治区）辖县级市。
 * </pre>
 *
 * @author: chengweixiong@uworks.cc
 * @date: 2015年10月29日 下午4:35:45
 */
public class IdCardUtils {

    public static String idCardAuth(String idCard) {
        if (StringUtils.isBlank(idCard)) {
            return "身份证不能为空";
        }
        idCard = idCard.toUpperCase();
        String errorInfo = null;// 记录错误信息
        String[] ValCodeArr = {"1", "0", "X", "9", "8", "7", "6", "5", "4", "3", "2"};
        String[] Wi =
            {"7", "9", "10", "5", "8", "4", "2", "1", "6", "3", "7", "9", "10", "5", "8", "4", "2"};
        // ================ 号码的长度 15位或18位 ================
        if (idCard.length() != 15 && idCard.length() != 18) {
            errorInfo = "身份证号码长度应该为15位或18位。";
            return errorInfo;
        }

        String temp = "";
        // ================ 数字 除最后以为都为数字 ================
        if (idCard.length() == 18) {
            temp = idCard.substring(0, 17);
        } else if (idCard.length() == 15) {
            temp = idCard.substring(0, 6) + "19" + idCard.substring(6, 15);
        }
        if (!NumberUtils.isNumber(temp)) {
            errorInfo = "身份证15位号码都应为数字 ; 18位号码除最后一位外，都应为数字。";
            return errorInfo;
        }

        // ================ 地区码时候有效 ================
        Hashtable<String, String> areaCode = GetAreaCode();
        if (areaCode.get(temp.substring(0, 2)) == null) {
            errorInfo = "身份证地区编码错误。";
            return errorInfo;
        }

        // ================ 出生年月是否有效 ================
        String strYear = temp.substring(6, 10);// 年份
        String strMonth = temp.substring(10, 12);// 月份
        String strDay = temp.substring(12, 14);// 月份
        try {
            GregorianCalendar gc = new GregorianCalendar();
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            if ((gc.get(Calendar.YEAR) - Integer.parseInt(strYear)) > 150 ||
                (gc.getTime().getTime() - format.parse(strYear + "-" + strMonth + "-" + strDay)
                    .getTime()) < 0) {
                errorInfo = "身份证生日不在有效范围。";
                return errorInfo;
            }
            if (Integer.parseInt(strMonth) > 12 || Integer.parseInt(strMonth) == 0) {
                errorInfo = "身份证月份无效";
                return errorInfo;
            }
            if (Integer.parseInt(strDay) > 31 || Integer.parseInt(strDay) == 0) {
                errorInfo = "身份证日期无效";
                return errorInfo;
            }
        } catch (Exception e) {
            errorInfo = "身份证日期验证失败";
            return errorInfo;
        }

        // ================ 判断最后一位的值 ================
        int TotalmulAiWi = 0;
        for (int i = 0; i < 17; i++) {
            TotalmulAiWi = TotalmulAiWi + Integer.parseInt(String.valueOf(temp.charAt(i))) * Integer
                .parseInt(Wi[i]);
        }
        int modValue = TotalmulAiWi % 11;
        String strVerifyCode = ValCodeArr[modValue];
        temp = temp + strVerifyCode;

        if (idCard.length() == 18) {
            if (temp.equals(idCard) == false) {
                errorInfo = "身份证无效，不是合法的身份证号码";
                return errorInfo;
            }
        }
        return errorInfo;
    }

    public static Boolean isIdCard(String idCard) {
        String result = idCardAuth(idCard);
        if (result != null) {
            return false;
        }
        return true;
    }

    /**
     * . 功能：设置地区编码
     *
     * @return Hashtable 对象
     */
    private static Hashtable<String, String> GetAreaCode() {
        Hashtable<String, String> hashtable = new Hashtable<String, String>();
        hashtable.put("11", "北京");
        hashtable.put("12", "天津");
        hashtable.put("13", "河北");
        hashtable.put("14", "山西");
        hashtable.put("15", "内蒙古");
        hashtable.put("21", "辽宁");
        hashtable.put("22", "吉林");
        hashtable.put("23", "黑龙江");
        hashtable.put("31", "上海");
        hashtable.put("32", "江苏");
        hashtable.put("33", "浙江");
        hashtable.put("34", "安徽");
        hashtable.put("35", "福建");
        hashtable.put("36", "江西");
        hashtable.put("37", "山东");
        hashtable.put("41", "河南");
        hashtable.put("42", "湖北");
        hashtable.put("43", "湖南");
        hashtable.put("44", "广东");
        hashtable.put("45", "广西");
        hashtable.put("46", "海南");
        hashtable.put("50", "重庆");
        hashtable.put("51", "四川");
        hashtable.put("52", "贵州");
        hashtable.put("53", "云南");
        hashtable.put("54", "西藏");
        hashtable.put("61", "陕西");
        hashtable.put("62", "甘肃");
        hashtable.put("63", "青海");
        hashtable.put("64", "宁夏");
        hashtable.put("65", "新疆");
        hashtable.put("71", "台湾");
        hashtable.put("81", "香港");
        hashtable.put("82", "澳门");
        hashtable.put("91", "国外");
        return hashtable;
    }

}
