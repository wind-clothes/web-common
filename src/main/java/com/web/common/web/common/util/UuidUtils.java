package com.web.common.web.common.util;

import java.util.UUID;

/**
 * @author: chengweixiong@uworks.cc
 * @date: 2016年5月27日 下午2:10:19
 */
public class UuidUtils {

  private UuidUtils() {};

  /**
   * 
   * @param length
   * @return String
   */
  public static String getUUID(int length) {
    String uuid = UUID.randomUUID().toString();
    uuid = uuid.replace("-", "");
    if (uuid.length() > length) {
      uuid = uuid.substring(uuid.length() - length, uuid.length());
    }
    return uuid;
  }
}
