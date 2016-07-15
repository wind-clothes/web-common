package com.web.common.web.common.util.pay;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

import com.web.common.web.common.util.MD5Util;

/**
 * Created by Cyan on 2015/8/20.
 */
public class Signature {

  /**
   * 签名算法
   * @param o   签名对象 按字典拼接成 key＝value& 字符串，
   * @param salt  约定的附加key
   * @return
   */
  public static String getSign(Object o,String salt) {
    ArrayList<String> list = new ArrayList<String>();
    Class<?> cls = o.getClass();
    Field[] fields = cls.getDeclaredFields();
    for (Field f : fields) {
      f.setAccessible(true);
      Object value = null;
      try {
        value = f.get(o);
      } catch (Exception e) {
        e.printStackTrace();
      }
      if (value != null && value != "") {
        list.add(f.getName() + "=" + value + "&");
      }
    }
    int size = list.size();
    String[] arrayToSort = list.toArray(new String[size]);
    Arrays.sort(arrayToSort, String.CASE_INSENSITIVE_ORDER);
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < size; i++) {
      sb.append(arrayToSort[i]);
    }
    String result = sb.toString();
    result += salt;
    try {
        
        result = MD5Util.MD5(result);
    } catch (Exception ex) {
        ex.printStackTrace();
    }
    return result;
  }

  /**
   * 功能同上
   * @param map
   * @param salt
   * @return
   */
  public static String getSignByMap(Map<String, Object> map,String salt) {
    ArrayList<String> list = new ArrayList<String>();
    for (Map.Entry<String, Object> entry : map.entrySet()) {
      if (entry.getValue() != "") {
        list.add(entry.getKey() + "=" + entry.getValue() + "&");
      }
    }
    int size = list.size();
    String[] arrayToSort = list.toArray(new String[size]);
    Arrays.sort(arrayToSort, String.CASE_INSENSITIVE_ORDER);
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < size; i++) {
      sb.append(arrayToSort[i]);
    }
    String result = sb.toString();
    result += salt;
    try {
        result = MD5Util.MD5(result);
    } catch (Exception ex) {
        ex.printStackTrace();
    }
    return result;
  }

}
