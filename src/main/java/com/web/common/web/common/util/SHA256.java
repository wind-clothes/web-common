package com.web.common.web.common.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.codec.digest.DigestUtils;

import com.google.common.hash.Hashing;

public class SHA256 {

  public static String hashWithJavaMessageDigest(String message) throws NoSuchAlgorithmException {
    MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
    byte[] bytes = messageDigest.digest(message.getBytes(StandardCharsets.UTF_8));
    return bytesToHex(bytes);
  }

  public static String HashWithGuava(final String originalString) {
    final String sha256hex =
        Hashing.sha256().hashString(originalString, StandardCharsets.UTF_8).toString();
    return sha256hex;
  }

  public static String HashWithApacheCommons(final String originalString) {
    final String sha256hex = DigestUtils.sha256Hex(originalString);
    return sha256hex;
  }

  private static String bytesToHex(byte[] hash) {
    StringBuffer hexString = new StringBuffer();
    for (int i = 0; i < hash.length; i++) {
      String hex = Integer.toHexString(0xff & hash[i]);
      if (hex.length() == 1)
        hexString.append('0');
      hexString.append(hex);
    }
    return hexString.toString();
  }

}
